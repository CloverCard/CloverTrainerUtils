package com.clovercard.clovertrainerutils.listeners;

import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.clovercard.clovertrainerutils.enums.battle.BattleCommandsTypes;
import com.clovercard.clovertrainerutils.enums.battle.BattleRewardTags;
import com.clovercard.clovertrainerutils.enums.shuffler.ShufflerTags;
import com.clovercard.clovertrainerutils.helpers.GeneralHelper;
import com.clovercard.clovertrainerutils.helpers.battle.BattleCommandsHelper;
import com.clovercard.clovertrainerutils.helpers.battle.BattleRewardsHelper;
import com.clovercard.clovertrainerutils.helpers.checkpoints.CheckpointsHelper;
import com.clovercard.clovertrainerutils.helpers.shuffler.ShufflerHelper;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.LostToTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTierRegistry;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.storage.TrainerPartyStorage;
import com.pixelmonmod.pixelmon.battles.api.rules.teamselection.TeamSelectionRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.*;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class BattleListeners {
    @SubscribeEvent
    public void onBattleStarted(BattleStartedEvent.Pre event) {
        if (event.getBattleController().containsParticipantType(WildPixelmonParticipant.class)) return;
        //Get Player and Trainer if they exist
        ServerPlayerEntity player = null;
        NPCTrainer trainer = null;
        List<Pokemon> selection = new ArrayList<>();
        boolean trainerExists = false;
        for (BattleParticipant participant : event.getTeamOne()) {
            if (participant instanceof PlayerParticipant) {
                player = ((PlayerParticipant) participant).player;
                List<Pokemon> pkms = new ArrayList<>();
                for(PixelmonWrapper pw: participant.allPokemon) {
                    if(pw != null) pkms.add(pw.pokemon);
                }
                selection = pkms;
            } else if (participant instanceof TrainerParticipant) {
                trainerExists = true;
                trainer = ((TrainerParticipant) participant).trainer;
            }
        }
        for (BattleParticipant participant : event.getTeamTwo()) {
            if (participant instanceof PlayerParticipant) {
                player = ((PlayerParticipant) participant).player;
                List<Pokemon> pkms = new ArrayList<>();
                for(PixelmonWrapper pw: participant.allPokemon) {
                    if(pw != null) pkms.add(pw.pokemon);
                }
                selection = pkms;
            } else if (participant instanceof TrainerParticipant) {
                trainerExists = true;
                trainer = ((TrainerParticipant) participant).trainer;
            }
        }
        if (!trainerExists) return;
        if (player == null) return;

        //Check if Data needed for sidemod is present
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return;
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());

        //Check for Checkpoints
        if (CheckpointsHelper.hasCheckpointTag(trainer)) {
            if (!CheckpointsHelper.playerHasAccess(player, trainer)) {
                player.sendMessage(new StringTextComponent("You do not have access to this trainer fight!"), Util.NIL_UUID);
                event.setCanceled(true);
                return;
            }
        }
        //Check if Clone
        else if (main.contains(TrainerUtilsTags.CLONE_TRAINER.getId())) {
            //Handle Commands
            BattleCommandsHelper.enqueueCommands(BattleCommandsTypes.START_BATTLE_COMMANDS.getId(), player, trainer);
        }
        //Make clone
        else {
            createClone(event, player, trainer, main, selection);
        }
    }

    @SubscribeEvent
    public void onPlayerWin(BeatTrainerEvent event) {
        if(!event.trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return;
        //Run Winning Commands
        BattleCommandsHelper.enqueueCommands(BattleCommandsTypes.PLAYER_WINS.getId(), event.player, event.trainer);
        //Delete Clone
        CompoundNBT main = event.trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        NPCTrainer clone = event.trainer;
        if(event.trainer.level instanceof ServerWorld && clone.getPersistentData().contains("clbase")) {
            handleEncounter(clone, event.player);
        }
        CheckpointsHelper.handlePlayerWin(event.player, event.trainer);
        if(main.contains(TrainerUtilsTags.CLONE_TRAINER.getId())) {
            event.trainer.remove();
        }
    }

    @SubscribeEvent
    public void onPlayerLoss(LostToTrainerEvent event) {
        if(!event.trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return;
        //Handle Forfeit Commands
        if(this.isForfeit(event.player, event.trainer)) {
            if(CheckpointsHelper.hasCheckpointTag(event.trainer) && !CheckpointsHelper.playerHasAccess(event.player, event.trainer)) return;
            BattleCommandsHelper.enqueueCommands(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId(), event.player, event.trainer);
        }
        //Handle Loss Commands
        else BattleCommandsHelper.enqueueCommands(BattleCommandsTypes.PLAYER_LOSS.getId(), event.player, event.trainer);
        //Delete Clone
        CompoundNBT main = event.trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        CheckpointsHelper.handlePlayerLoss(event.player, event.trainer);
        if(main.contains(TrainerUtilsTags.CLONE_TRAINER.getId())) {
            event.trainer.remove();
        }
    }

    private boolean isForfeit(ServerPlayerEntity player, NPCTrainer trainer) {
        //Evaluate if battle is a forfeit.
        PlayerPartyStorage pStorage = StorageProxy.getParty(player.getUUID());
        TrainerPartyStorage tStorage = trainer.getPokemonStorage();
        boolean playerHasPokemon = pStorage.countAblePokemon() > 0;
        boolean trainerHasPokemon = tStorage.countAblePokemon() > 0;
        if(playerHasPokemon && trainerHasPokemon) return true;
        return playerHasPokemon == trainerHasPokemon;
    }
    private void handleEncounter(NPCTrainer clone, ServerPlayerEntity player) {
        ServerWorld world = (ServerWorld) clone.level;
        if(!clone.getPersistentData().contains("clbase")) return;
        if(!clone.getPersistentData().contains("clenc")) return;
        String strUUID = clone.getPersistentData().getString("clbase");
        String encounterMode = clone.getPersistentData().getString("clenc");
        List<Entity> trainers = world.getEntities(clone.getType(), npc -> npc instanceof NPCTrainer);
        for (Entity tr: trainers) {
            if(strUUID.equals(tr.getStringUUID())) {
                NPCTrainer trainer = (NPCTrainer) tr;
                if (encounterMode.equals(EnumEncounterMode.OncePerMCDay.name())) {
                    trainer.playerEncounters.put(player.getUUID(), player.getLevel().getGameTime());
                } else if (encounterMode.equals(EnumEncounterMode.OncePerDay.name())) {
                    trainer.playerEncounters.put(player.getUUID(), System.currentTimeMillis());
                } else if (encounterMode.equals(EnumEncounterMode.OncePerPlayer.name())) {
                    trainer.playerEncounters.put(player.getUUID(), Long.MAX_VALUE);
                }
                break;
            }
        }
    }
    private void createClone(BattleStartedEvent.Pre event, ServerPlayerEntity player, NPCTrainer trainer, CompoundNBT main, List<Pokemon> selection) {
        //Create Clone Trainer
        event.setCanceled(true);
        NPCTrainer temp = new NPCTrainer(trainer.level);
        createCloneBase(temp, trainer, player);

        //Set Up NBT Data
        temp.getPersistentData().put(TrainerUtilsTags.MAIN_TAG.getId(), main.copy());
        CompoundNBT tempMain = temp.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        tempMain.putBoolean(TrainerUtilsTags.CLONE_TRAINER.getId(), true);

        //Add Conditional Reward Nbt data
        if(tempMain.contains(BattleRewardTags.COND_WINNINGS.getId())) {
            BattleRewardsHelper.setCondRewardsOnClone(temp);
        }

        //Handle Encounter Modes
        if(trainer.getEncounterMode().isTimedAccess() || trainer.getEncounterMode() == EnumEncounterMode.OncePerPlayer) {
            temp.getPersistentData().putString("clbase", trainer.getStringUUID());
            temp.getPersistentData().putString("clenc", trainer.getEncounterMode().name());
        }

        //Handle Shuffler and Pokemon Cloning
        ArrayList<Pokemon> team = new ArrayList<>();
        if (main.contains(ShufflerTags.SHUFFLING_TRAINER.getId())) {
            String teamId = ShufflerHelper.findRandomTeamId(trainer);
            if(teamId != null) team = ShufflerHelper.generateTeam(teamId);
        }
        if(team.isEmpty()) team = GeneralHelper.cloneTrainerStorage(trainer, temp);
        for(int i = 0; i < 6; i++) {
            temp.getPokemonStorage().set(i, null);
        }
        team.forEach(pokemon -> temp.getPokemonStorage().add(pokemon));

        //Spawn and Start Clone's Fight
        if(selection.isEmpty()) return;
        else {
            temp.setPos(player.getX(), player.getY(), player.getZ());
            player.getLevel().addFreshEntity(temp);
            temp.addEffect(new EffectInstance(Effects.INVISIBILITY, Integer.MAX_VALUE, 0, true, true));
            if(main.contains(ShufflerTags.SHUFFLING_TRAINER.getId())) TeamSelectionRegistry.builder().members(new Entity[]{temp, player}).showOpponentTeam().battleRules(temp.battleRules).start();
            else ShufflerHelper.startTrainerBattle(player, temp, selection);
        }
    }
    private void createCloneBase(NPCTrainer temp, NPCTrainer trainer, ServerPlayerEntity player) {
        temp.init(new BaseTrainer("TUtils Trainer"));
        temp.clearGreetings();
        temp.setPos(player.getX(), player.getY(), player.getZ());
        if(trainer.getWinnings() != null) temp.updateDrops(trainer.getWinnings());
        if(trainer.battleRules != null) temp.battleRules = trainer.battleRules;
        if(trainer.getMegaItem() != null) temp.setMegaItem(trainer.getMegaItem());
        temp.winMoney = trainer.winMoney;
        temp.winMessage = trainer.winMessage;
        temp.loseMessage = trainer.loseMessage;
        temp.greeting = trainer.greeting;
        temp.setOldGenMode(trainer.getOldGen());
        if(!trainer.getBossTier().equals(BossTierRegistry.NOT_BOSS)) temp.setBossTier(trainer.getBossTier());
        temp.update(new SetTrainerData("Trainer", temp.greeting, temp.winMessage, temp.loseMessage, temp.winMoney, temp.getWinnings()));
    }
}
