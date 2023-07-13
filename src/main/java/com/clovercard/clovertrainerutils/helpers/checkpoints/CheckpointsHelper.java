package com.clovercard.clovertrainerutils.helpers.checkpoints;

import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.clovercard.clovertrainerutils.enums.checkpoints.CheckpointTags;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.Set;

public class CheckpointsHelper {
    public static String addTagsToTrainer(NPCTrainer trainer, String ... args) {
        //Check if initialized and args length is correct
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(args.length != 2) return "An incorrect amount of arguments was provided.";
        //Add checkpoints for check point and next batle
        if(args[0] != null && args[1] != null) {
            main.putString(CheckpointTags.CHECKPOINT_TAG.getKey(), args[0]);
            main.putString(CheckpointTags.NEXT_BATTLE_TAG.getKey(), args[1]);
            return "Successfully added the checkpoint tag to limit access to this trainer and the checkpoint tag to give upon a player's victory.";
        }
        //Add checkpoint for only next battle
        else if (args[0] == null && args[1] != null) {
            if(args[1].split(" ").length != 1) return "An incorrect amount of arguments was provided.";
            main.putString(CheckpointTags.NEXT_BATTLE_TAG.getKey(), args[1]);
            if(main.contains(CheckpointTags.CHECKPOINT_TAG.getKey())) main.remove(CheckpointTags.CHECKPOINT_TAG.getKey());
            return "Successfully added the checkpoint tag to give upon a player's victory";
        }
        //Add checkpoint for only check point
        else if (args[0] != null) {
            if(args[0].split(" ").length != 1) return "An incorrect amount of arguments was provided.";
            main.putString(CheckpointTags.CHECKPOINT_TAG.getKey(), args[0]);
            if(main.contains(CheckpointTags.NEXT_BATTLE_TAG.getKey())) main.remove(CheckpointTags.NEXT_BATTLE_TAG.getKey());
            return "Successfully added the checkpoint tag to limit access to this trainer.";
        }
        else return "No valid tags were provided!";
    }
    public static String removeTagsFromTrainer(NPCTrainer trainer) {
        //Check if initialized and args length is correct
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        StringBuilder resBuilder = new StringBuilder();
        //Remove check point if exists
        if(main.contains(CheckpointTags.CHECKPOINT_TAG.getKey())) {
            main.remove(CheckpointTags.CHECKPOINT_TAG.getKey());
            resBuilder.append("Successfully removed the checkpoint that limits access to this trainer. ");
        }
        //Remove next battle if exists
        if(main.contains(CheckpointTags.NEXT_BATTLE_TAG.getKey())) {
            main.remove(CheckpointTags.NEXT_BATTLE_TAG.getKey());
            resBuilder.append("Successfully removed the checkpoint tag that's given upon a player's victory.");
        }
        String res = resBuilder.toString();
        if(res.length() < 1) res = "There were no checkpoint tags to remove!";
        return res;
    }
    public static String listTagsFromTrainer(NPCTrainer trainer) {
        //Check if initialized and args length is correct
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        StringBuilder resBuilder = new StringBuilder();
        //Get check point to display if exists
        if(main.contains(CheckpointTags.CHECKPOINT_TAG.getKey())) {
            String tag = main.getString(CheckpointTags.CHECKPOINT_TAG.getKey());
            resBuilder.append("TRAINER ACCESS TAG: [ " + tag + " ] ");
        }
        //Get next battle to display if exists
        if(main.contains(CheckpointTags.NEXT_BATTLE_TAG.getKey())) {
            String tag = main.getString(CheckpointTags.NEXT_BATTLE_TAG.getKey());
            resBuilder.append("TRAINER GIVEN TAG: [ " + tag + " ] ");
        }
        String res = resBuilder.toString();
        if(res.length() < 1) res = "There were no checkpoint tags to list!";
        return res;
    }
    public static boolean playerHasAccess(ServerPlayerEntity player, NPCTrainer trainer) {
        //Check if a player has the checkpoint flag required to battle
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return true;
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(!main.contains(CheckpointTags.CHECKPOINT_TAG.getKey())) return true;
        String value = main.getString(CheckpointTags.CHECKPOINT_TAG.getKey());
        if(value == null) return true;
        Set<String> tags = player.getTags();
        if(tags == null) return false;
        for(String tag: tags) {
            if(tag.equals(value)) return true;
        }
        return false;
    }
    public static boolean hasCheckpointTag(NPCTrainer trainer) {
        //Check if a trainer has a checkpoint tag
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return false;
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        return main.contains(CheckpointTags.CHECKPOINT_TAG.getKey());
    }
    public static boolean hasNextCheckpointTag(NPCTrainer trainer) {
        //Check if a trainer has a next battle tag
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return false;
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        return main.contains(CheckpointTags.NEXT_BATTLE_TAG.getKey());
    }
    public static void handlePlayerWin(ServerPlayerEntity player, NPCTrainer trainer) {
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return;
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(hasCheckpointTag(trainer)) {
            //Remove current checkpoint
            Set<String> tags = player.getTags();
            String checkpoint = main.getString(CheckpointTags.CHECKPOINT_TAG.getKey());
            if (tags == null) return;
            player.removeTag(checkpoint);
        }
        //Add next checkpoint if it exists
        if(hasNextCheckpointTag(trainer)) {
            player.addTag(main.getString(CheckpointTags.NEXT_BATTLE_TAG.getKey()));
        }
    }
    public static void handlePlayerLoss(ServerPlayerEntity player, NPCTrainer trainer) {
        if(!hasCheckpointTag(trainer)) return;

        //Remove current checkpoint
        Set<String> tags = player.getTags();
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        String checkpoint = main.getString(CheckpointTags.CHECKPOINT_TAG.getKey());
        if(tags == null) return;
        player.removeTag(checkpoint);
    }
}
