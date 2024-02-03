package com.clovercard.clovertrainerutils.helpers;

import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.clovercard.clovertrainerutils.enums.battle.BattleCommandsTypes;
import com.clovercard.clovertrainerutils.enums.battle.BattleRewardTags;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.stats.Moveset;
import com.pixelmonmod.pixelmon.api.storage.TrainerPartyStorage;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.entities.npcs.NPCEntity;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.StatueEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;

public class GeneralHelper {
    public static void initUtilsNbt(StatueEntity trainer) {
        //Add needed NBT tags and structures to trainer
        trainer.getPersistentData().put(TrainerUtilsTags.MAIN_TAG.getId(), new CompoundTag());
        CompoundTag main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        main.put(BattleCommandsTypes.START_BATTLE_COMMANDS.getId(), new ListTag());
        main.put(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId(), new ListTag());
        main.put(BattleCommandsTypes.PLAYER_WINS.getId(), new ListTag());
        main.put(BattleCommandsTypes.PLAYER_LOSS.getId(), new ListTag());
        main.put(BattleCommandsTypes.INTERACT.getId(), new ListTag());
        main.put(BattleRewardTags.COND_WINNINGS.getId(), new ListTag());
    }

    public static void initUtilsNbt(NPCEntity trainer) {
        //Add needed NBT tags and structures to trainer
        trainer.getPersistentData().put(TrainerUtilsTags.MAIN_TAG.getId(), new CompoundTag());
        CompoundTag main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        main.put(BattleCommandsTypes.START_BATTLE_COMMANDS.getId(), new ListTag());
        main.put(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId(), new ListTag());
        main.put(BattleCommandsTypes.PLAYER_WINS.getId(), new ListTag());
        main.put(BattleCommandsTypes.PLAYER_LOSS.getId(), new ListTag());
        main.put(BattleCommandsTypes.INTERACT.getId(), new ListTag());
        main.put(BattleRewardTags.COND_WINNINGS.getId(), new ListTag());
    }

    public static ArrayList<Pokemon> cloneTrainerStorage(NPCTrainer trainer, NPCTrainer temp) {
        ArrayList<Pokemon> team = new ArrayList<>();
        TrainerPartyStorage oldStorage = trainer.getPokemonStorage();
        if(oldStorage == null) {
            return team;
        }
        //Copy the trainer's team
        for(Pokemon pkm: oldStorage.getAll()) {
            if(pkm != null) {
                Pokemon pkm2 = PokemonFactory.copy(pkm);
                pkm2.setForm(pkm.getForm());
                pkm2.setGender(pkm.getGender());
                pkm2.setLevel(pkm.getPokemonLevel());
                pkm2.setAbility(pkm.getAbility());
                pkm2.setNature(pkm.getNature());
                pkm2.setHeldItem(pkm.getHeldItem());
                pkm2.getIVs().fillFromArray(pkm.getIVs().getArray());
                pkm2.getEVs().fillFromArray(pkm.getEVs().getArray());
                Moveset moves = pkm2.getMoveset();
                int counter = 0;
                for(Attack move: pkm.getMoveset().attacks) {
                    if(move == null) moves.set(counter, null);
                    else moves.set(counter, move);
                    counter++;
                }
                team.add(pkm2);
            }
        }
        return team;
    }
}
