package com.clovercard.clovertrainerutils.helpers.permissions;

import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.clovercard.clovertrainerutils.enums.permissions.PermissionsTags;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;

import java.util.Set;

public class PermissionsHelper {
    public static String addPermissionsToTrainer(NPCTrainer trainer, String ... args) {
        //Check if initialized and args length is correct
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(args.length != 1) return "An incorrect amount of arguments was provided.";
        //Add checkpoints for check point and next batle
        if(args[0] != null) {
            if(!main.contains(PermissionsTags.PERMISSIONS_TAG.getKey())) main.put(PermissionsTags.PERMISSIONS_TAG.getKey(), new ListNBT());
            ListNBT list = main.getList(PermissionsTags.PERMISSIONS_TAG.getKey(), Constants.NBT.TAG_STRING);
            list.add(StringNBT.valueOf(args[0]));
            return "Successfully added the permission tag to limit access to this trainer.";
        }
        else return "No valid tags were provided!";
    }
    public static String removePermissionFromTrainer(NPCTrainer trainer, String ... args) {
        //Check if initialized and args length is correct
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(args.length != 1) return "An incorrect amount of arguments was provided.";
        StringBuilder resBuilder = new StringBuilder();
        if(main.contains(PermissionsTags.PERMISSIONS_TAG.getKey())) {
            ListNBT listNBT = main.getList(PermissionsTags.PERMISSIONS_TAG.getKey(), Constants.NBT.TAG_STRING);
            if(listNBT != null) {
                int index = -1;
                int counter = 0;
                for(INBT inbt: listNBT) {
                    if(inbt.getAsString().equals(args[0])) index = counter;
                    else counter += 1;
                }
                if(index != -1) listNBT.remove(index);
                resBuilder.append("Removed the " + args[0] + " permission from the selected trainer!");
            }
        }
        String res = resBuilder.toString();
        if(res.length() < 1) res = "Could not find the permission tag: " + args[0] + "!";
        return res;
    }
    public static String clearPermissionFromTrainer(NPCTrainer trainer) {
        //Check if initialized and args length is correct
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        StringBuilder resBuilder = new StringBuilder();
        if(main.contains(PermissionsTags.PERMISSIONS_TAG.getKey())) {
            main.remove(PermissionsTags.PERMISSIONS_TAG.getKey());
        }
        String res = resBuilder.toString();
        if(res.length() < 1) res = "There were no permission tags to clear!";
        return res;
    }
    public static String listPermissionsFromTrainer(NPCTrainer trainer) {
        //Check if initialized and args length is correct
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        StringBuilder resBuilder = new StringBuilder();
        if(main.contains(PermissionsTags.PERMISSIONS_TAG.getKey())) {
            resBuilder.append("PERMISSIONS LIST");
            ListNBT listNBT = main.getList(PermissionsTags.PERMISSIONS_TAG.getKey(), Constants.NBT.TAG_STRING);
            if(listNBT != null) {
                for(INBT inbt: listNBT) {
                    resBuilder.append("\n" + inbt.getAsString());
                }
            }
        }
        String res = resBuilder.toString();
        if(res.length() < 1) res = "There were no permissions to list!";
        return res;
    }
    public static boolean playerHasAccess(ServerPlayerEntity player, NPCTrainer trainer) {
        //Check if a player has the permissions required to battle
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return true;
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(!main.contains(PermissionsTags.PERMISSIONS_TAG.getKey())) return true;
        ListNBT listNBT = main.getList(PermissionsTags.PERMISSIONS_TAG.getKey(), Constants.NBT.TAG_STRING);
        if(listNBT == null) return true;
        Set<String> tags = player.getTags();
        if(tags == null) return false;
        for(INBT inbt: listNBT) {
            if(!tags.contains(inbt.getAsString())) {
                return false;
            }
        }
        return true;
    }
    public static boolean hasPermissions(NPCTrainer trainer) {
        //Check if a trainer has a permission tag
        if(!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return false;
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        return main.contains(PermissionsTags.PERMISSIONS_TAG.getKey());
    }
}
