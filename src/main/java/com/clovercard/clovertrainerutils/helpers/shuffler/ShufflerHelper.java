package com.clovercard.clovertrainerutils.helpers.shuffler;

import com.clovercard.clovertrainerutils.configs.shuffler.MemberConfig;
import com.clovercard.clovertrainerutils.configs.shuffler.TeamConfig;
import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.clovercard.clovertrainerutils.enums.shuffler.ShufflerTags;
import com.pixelmonmod.pixelmon.api.battles.BattleType;
import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.Nature;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbilityRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.pokemon.stats.Moveset;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.ResourceLocationHelper;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRuleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShufflerHelper {
    public static ArrayList<Pokemon> generateTeam(String trainerTeam) {
        //Generate placeholder for new team
        ArrayList<Pokemon> pkmteam = new ArrayList<>();

        //Find a list of a trainer's teams if it exists.
        TeamConfig.PokemonTeam teamsId = TeamConfig.DATA.getTeams().get(trainerTeam);
        if(teamsId == null) return pkmteam;
        List<String> pkmIds = teamsId.getPokemonNames();
        if(pkmIds.isEmpty()) return pkmteam;

        //Get Loaded Pokemon
        HashMap<String, MemberConfig.PokemonMember> loadedPokemon = MemberConfig.DATA.getPokemon();
        if(loadedPokemon == null) return pkmteam;
        if(loadedPokemon.isEmpty()) return pkmteam;

        //Create the team
        for(String member: pkmIds) {
            MemberConfig.PokemonMember pkmData = loadedPokemon.get(member);
            if(pkmData == null) continue;
            if(PixelmonSpecies.get(pkmData.getSpecies()).isEmpty()) {
                continue;
            }
            Pokemon pkm = PokemonFactory.create(PixelmonSpecies.get(pkmData.getSpecies()).get().getValueUnsafe());
            pkm.setForm(pkmData.getForm());
            pkm.setGender(Gender.getGender(pkmData.getGender()));
            pkm.setAbility(AbilityRegistry.getAbility(pkmData.getAbility()));
            pkm.setNature(Nature.natureFromString(pkmData.getNature()));
            pkm.setHeldItem(new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocationHelper.of(pkmData.getHeldItem()))));
            pkm.getIVs().fillFromArray(pkmData.getIvs());
            pkm.getEVs().fillFromArray(pkmData.getEvs());
            pkm.setLevel(pkmData.getLevel());
            Moveset moves = pkm.getMoveset();
            int counter = 0;
            for(String move: pkmData.getMoveset()) {
                if(AttackRegistry.getAttackBase(move).isPresent()) {
                    moves.set(counter, new Attack(AttackRegistry.getAttackBase(move).get()));
                }
                else moves.set(counter, null);
                counter++;
            }
            pkmteam.add(pkm);
        }
        return pkmteam;
    }
    public static String findRandomTeamId(NPCTrainer trainer) {
        //Get team ids stored on a trainer
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return null;
        CompoundTag main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(!main.contains(ShufflerTags.SHUFFLING_TRAINER.getId())) return null;
        ListTag teamNBTList = (ListTag) main.get(ShufflerTags.SHUFFLING_TRAINER.getId());
        if(teamNBTList == null) return null;
        List<String> teamIds = new ArrayList<>();
        for(Tag tag: teamNBTList) {
            if(!(tag instanceof StringTag)) continue;
            StringTag nbt = (StringTag) tag;
            teamIds.add(nbt.getAsString());
        }
        if(teamIds.isEmpty()) return null;
        //Select a random one and return it
        int index = (int) Math.floor(Math.random()*teamIds.size());
        return teamIds.get(index);
    }
    public static void startTrainerBattle(ServerPlayer player, NPCTrainer trainer, List<Pokemon> selection) {
        if(player == null) return;
        //Register NPC and player for battle
        int controlled = 1;
        if(trainer.battleRules.getOrDefault(BattleRuleRegistry.BATTLE_TYPE) == BattleType.DOUBLE) controlled = 2;
        if(trainer.battleRules.getOrDefault(BattleRuleRegistry.BATTLE_TYPE) == BattleType.TRIPLE) controlled = 3;
        if(trainer.battleRules.getOrDefault(BattleRuleRegistry.BATTLE_TYPE) == BattleType.ROTATION) controlled = 3;
        if(trainer.battleRules.getOrDefault(BattleRuleRegistry.BATTLE_TYPE) == BattleType.HORDE) controlled = 5;

        PlayerParticipant pp;
        if(trainer.battleRules.isDefault()) {
            PlayerPartyStorage storage = StorageProxy.getParty(player).getNow(new PlayerPartyStorage(Util.NIL_UUID));
            Pokemon selected = null;
            if(!storage.uuid.equals(Util.NIL_UUID)) selected = storage.getSelectedPokemon();
            if(selected != null) pp = new PlayerParticipant(player, selected);
            else pp = new PlayerParticipant(player, selection, controlled);
        }
        else pp = new PlayerParticipant(player, selection, controlled);
        TrainerParticipant tp = new TrainerParticipant(trainer, player, controlled);
        if(trainer.canStartBattle(player, false)) BattleRegistry.startBattle(new BattleParticipant[] {pp}, new BattleParticipant[] {tp}, trainer.battleRules);
    }

    public static String addShuffleTeam(NPCTrainer trainer, String ... args) {
        //Check if trainer is initialized and arguments length is correct
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(args.length != 1) return "Invalid Argument Number! Expected 1 Argument";
        //Add team id to ListTag holding all team ids.
        String teamId = args[0];
        if(!main.contains(ShufflerTags.SHUFFLING_TRAINER.getId())) main.put(ShufflerTags.SHUFFLING_TRAINER.getId(), new ListTag());
        ListTag teamsList = main.getList(ShufflerTags.SHUFFLING_TRAINER.getId(), Tag.TAG_STRING);
        teamsList.add(StringTag.valueOf(teamId));
        return "Successfully added the team id, " + teamId + ", to this trainer's shuffle list";
    }

    public static String removeShuffleTeam(NPCTrainer trainer, String ... args) {
        //Check if trainer is initialized and arguments length is correct
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(args.length != 1) return "Invalid Argument Number! Expected 1 Argument";
        //Remove team id from ListTag holding all team ids
        String teamId = args[0];
        if(!main.contains(ShufflerTags.SHUFFLING_TRAINER.getId())) return "This trainer already has no shuffle teams";
        ListTag teamsList = main.getList(ShufflerTags.SHUFFLING_TRAINER.getId(), Tag.TAG_STRING);
        teamsList.remove(StringTag.valueOf(teamId));
        if(teamsList.size() == 0) main.remove(ShufflerTags.SHUFFLING_TRAINER.getId());
        return "Successfully remove the team id, " + teamId + ", from this trainer's shuffle list";
    }

    public static String listShuffleTeams(NPCTrainer trainer) {
        //Check if trainer is initialized and arguments length is correct
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        //Check if trainer has team ids listed
        if(!main.contains(ShufflerTags.SHUFFLING_TRAINER.getId())) return "This trainer has no shuffle teams";
        ListTag teamsList = main.getList(ShufflerTags.SHUFFLING_TRAINER.getId(), Tag.TAG_STRING);
        if(teamsList.isEmpty()) return "This trainer has no shuffle teams";
        //Get and display existing team ids
        StringBuilder res = new StringBuilder("Shuffle Teams: [ ");
        for(Tag tag: teamsList) {
            res.append(tag.getAsString()).append(" ");
        }
        res.append("]");
        return res.toString();
    }

    public static String clearShuffleTeams(NPCTrainer trainer) {
        //Check if trainer is initialized and arguments length is correct
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        //Check if trainer has team id listed and remove it if so
        if(!main.contains(ShufflerTags.SHUFFLING_TRAINER.getId())) return "This trainer already has no shuffle teams";
        main.remove(ShufflerTags.SHUFFLING_TRAINER.getId());
        return "Successfully cleared this trainer's shuffle list!";
    }
}
