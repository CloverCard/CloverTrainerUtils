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
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class BattleListeners {
    @SubscribeEvent
    public void onBattleStarted(BattleStartedEvent.Pre event) {
        if (event.getBattleController().containsParticipantType(WildPixelmonParticipant.class)) return;
        //Get Player and Trainer if they exist
        ServerPlayerEntity player = null;
        NPCTrainer trainer = null;
        boolean trainerExists = false;
        for (BattleParticipant participant : event.getTeamOne()) {
            if (participant instanceof PlayerParticipant) {
                player = ((PlayerParticipant) participant).player;
            } else if (participant instanceof TrainerParticipant) {
                trainerExists = true;
                trainer = ((TrainerParticipant) participant).trainer;
            }
        }
        for (BattleParticipant participant : event.getTeamTwo()) {
            if (participant instanceof PlayerParticipant) {
                player = ((PlayerParticipant) participant).player;
            } else if (participant instanceof TrainerParticipant) {
                trainerExists = true;
                trainer = ((TrainerParticipant) participant).trainer;
            }
        }
        if (!trainerExists) return;
        if (player == null) return;

        //Handle encounter limits
        EnumEncounterMode encounterMode = trainer.getEncounterMode();
        if (encounterMode == EnumEncounterMode.OncePerMCDay) {
            trainer.playerEncounters.put(player.getUUID(), player.getLevel().getGameTime());
        } else if (encounterMode == EnumEncounterMode.OncePerDay) {
            trainer.playerEncounters.put(player.getUUID(), System.currentTimeMillis());
        }

        //Check if Data needed for sidemod is present
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return;
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());

        //Handle Checkpoints
        if (CheckpointsHelper.hasCheckpointTag(trainer)) {
            if (!CheckpointsHelper.playerHasAccess(player, trainer)) {
                player.sendMessage(new StringTextComponent("You do not have access to this trainer fight!"), Util.NIL_UUID);
                event.setCanceled(true);
                return;
            }
        }

        if (main.contains(TrainerUtilsTags.CLONE_TRAINER.getId())) {
            //Handle Commands
            BattleCommandsHelper.enqueueCommands(BattleCommandsTypes.START_BATTLE_COMMANDS.getId(), player, trainer);
        }
        else {
            //Create Clone Trainer
            event.setCanceled(true);
            NPCTrainer temp = new NPCTrainer(trainer.level);
            temp.setPersistenceRequired();
            temp.setPos(player.getX(), player.getY(), player.getZ());
            if(trainer.getWinnings() != null) temp.updateDrops(trainer.getWinnings());
            if(trainer.battleRules != null) temp.battleRules = trainer.battleRules;
            if(trainer.getMegaItem() != null) temp.setMegaItem(trainer.getMegaItem());
            temp.winMoney = trainer.winMoney;
            temp.winMessage = trainer.winMessage;
            temp.loseMessage = trainer.loseMessage;
            temp.greeting = trainer.greeting;
            temp.getPersistentData().put(TrainerUtilsTags.MAIN_TAG.getId(), main.copy());
            CompoundNBT tempMain = temp.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
            tempMain.putBoolean(TrainerUtilsTags.CLONE_TRAINER.getId(), true);
            if(tempMain.contains(BattleRewardTags.COND_WINNINGS.getId())) {
                BattleRewardsHelper.setCondRewardsOnClone(temp);
            }
            ArrayList<Pokemon> team = new ArrayList<>();
            //Handle Shuffler and Pokemon Cloning
            if (main.contains(ShufflerTags.SHUFFLING_TRAINER.getId())) {
                String teamId = ShufflerHelper.findRandomTeamId(trainer);
                if(teamId != null) team = ShufflerHelper.generateTeam(teamId);
            }
            if(team.isEmpty()) team = GeneralHelper.cloneTrainerStorage(trainer, temp);
            for(int i = 0; i < 6; i++) {
                temp.getPokemonStorage().set(i, null);
            }
            team.forEach(pokemon -> temp.getPokemonStorage().add(pokemon));

            if(!trainer.getBossTier().equals(BossTierRegistry.NOT_BOSS)) temp.setBossTier(trainer.getBossTier());

            temp.update(new SetTrainerData("Trainer", temp.greeting, temp.winMessage, temp.loseMessage, temp.winMoney, temp.getWinnings()));
            ShufflerHelper.startTrainerSingleBattle(player, temp);
        }
    }

    @SubscribeEvent
    public void onPlayerWin(BeatTrainerEvent event) {
        if(!event.trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return;
        //Run Winning Commands
        BattleCommandsHelper.enqueueCommands(BattleCommandsTypes.PLAYER_WINS.getId(), event.player, event.trainer);
        //Delete Clone
        CompoundNBT main = event.trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
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
}
