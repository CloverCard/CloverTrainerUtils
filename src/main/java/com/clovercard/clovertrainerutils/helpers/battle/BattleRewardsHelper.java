package com.clovercard.clovertrainerutils.helpers.battle;

import com.clovercard.clovertrainerutils.configs.battle.RewardInstance;
import com.clovercard.clovertrainerutils.configs.battle.RewardPresetConfig;
import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.clovercard.clovertrainerutils.enums.battle.BattleCommandsTypes;
import com.clovercard.clovertrainerutils.enums.battle.BattleRewardTags;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.api.util.helpers.ResourceLocationHelper;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleRewardsHelper {
    //Rolls and adds probability based rewards to a trainer.
    public static void setCondRewardsOnClone(NPCTrainer trainer) {
        //Check if main tag is on trainer
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return;
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());

        //Check if conditional winnings list is on trainer and is valid
        if (!main.contains(BattleRewardTags.COND_WINNINGS.getId())) return;
        if (!(main.get(BattleRewardTags.COND_WINNINGS.getId()) instanceof ListNBT)) {
            main.remove(BattleRewardTags.COND_WINNINGS.getId());
            main.put(BattleRewardTags.COND_WINNINGS.getId(), new ListNBT());
            return;
        }

        //Get List and ensure there is data read
        ListNBT nbtList = (ListNBT) main.get(BattleRewardTags.COND_WINNINGS.getId());
        if (nbtList == null) return;
        if (nbtList.isEmpty()) return;

        //Set up placeholders for new data
        List<ItemStack> winnings = new ArrayList<>();
        winnings.addAll(0, Arrays.asList(trainer.getWinnings()));

        if (!main.contains(BattleCommandsTypes.PLAYER_WINS.getId())) main.put(BattleCommandsTypes.PLAYER_WINS.getId(), new ListNBT());
        ListNBT winningCmds = main.getList(BattleCommandsTypes.PLAYER_WINS.getId(), Constants.NBT.TAG_STRING);

        //Sort through rewards
        for (INBT nbt : nbtList) {
            //Check if NBT is valid format
            if (!(nbt instanceof CompoundNBT)) continue;
            CompoundNBT rewardData = (CompoundNBT) nbt;
            if (!rewardData.contains("type")) continue;
            else if (!rewardData.contains("data")) continue;
            else if (!rewardData.contains("prob")) continue;
            else if (!rewardData.contains("quantity")) continue;
            else if (!(rewardData.get("type") instanceof StringNBT)) continue;
            else if (!(rewardData.get("data") instanceof StringNBT)) continue;
            else if (!(rewardData.get("prob") instanceof IntNBT)) continue;
            else if (!(rewardData.get("quantity") instanceof IntNBT)) continue;


            //Get NBT Data
            StringNBT type = (StringNBT) rewardData.get("type");
            StringNBT data = (StringNBT) rewardData.get("data");
            IntNBT prob = (IntNBT) rewardData.get("prob");
            IntNBT quantity = (IntNBT) rewardData.get("quantity");

            //Ensure NBT Data is not null
            if (type == null || data == null || prob == null || quantity == null) continue;

            //Roll if it is to be added
            if (!RandomHelper.getRandomChance(prob.getAsInt())) continue;

            //Add items
            if (type.getAsString().equals("item")) {
                IItemProvider optItem = ForgeRegistries.ITEMS.getValue(ResourceLocationHelper.of(data.getAsString()));
                if (optItem == null) continue;
                ItemStack item = new ItemStack(optItem, quantity.getAsInt());
                winnings.add(item);
            }
            //Add commands
            else if (type.getAsString().equals("cmd")) {
                for(int i = 0; i < quantity.getAsInt(); i++) {
                    winningCmds.add(data);
                }
            }
        }

        //Add commands to trainer data.
        main.put(BattleCommandsTypes.PLAYER_WINS.getId(), winningCmds);

        //Handle Itemstack Array Update
        ItemStack[] tempWinnings = new ItemStack[winnings.size()];
        tempWinnings = winnings.toArray(tempWinnings);
        trainer.updateDrops(tempWinnings);
    }
    public static String addSingleDrop(NPCTrainer trainer, String ... args) {
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(!main.contains(BattleRewardTags.COND_WINNINGS.getId())) main.put(BattleRewardTags.COND_WINNINGS.getId(), new ListNBT());
        ListNBT list = main.getList(BattleRewardTags.COND_WINNINGS.getId(), Constants.NBT.TAG_COMPOUND);
        if(args.length < 4) return "This command was not provided the needed number of arguments";
        String type;
        String data;
        Integer prob;
        Integer quantity;
        switch(args[0]) {
            case "item":
                type = "item";
                break;
            case "cmd":
                type = "cmd";
                break;
            default: return "Invalid type provided!";
        }
        prob = Integer.parseInt(args[1]);
        if(prob == null) return "This command did not provide an integer for probability.";
        quantity = Integer.parseInt(args[2]);
        if(quantity == null) return "This command did not provide an integer for quantity.";
        StringBuilder dataBuilder = new StringBuilder();
        for(int i = 3; i < args.length; i++) {
            dataBuilder.append(args[i]);
            if(i < (args.length - 1)) dataBuilder.append(" ");
        }
        data = dataBuilder.toString();
        CompoundNBT drop = new CompoundNBT();
        drop.putString("type", type);
        drop.putString("data", data);
        drop.putInt("prob", prob);
        drop.putInt("quantity", quantity);
        list.add(drop);
        return "Successfully added a new conditional drop to this trainer";
    }
    public static String removeSingleDrop(NPCTrainer trainer, String ... args) {
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(!main.contains(BattleRewardTags.COND_WINNINGS.getId())) main.put(BattleRewardTags.COND_WINNINGS.getId(), new ListNBT());
        ListNBT list = main.getList(BattleRewardTags.COND_WINNINGS.getId(), Constants.NBT.TAG_COMPOUND);
        if(args.length != 1) return "This command was not provided the needed number of arguments";
        Integer slot = Integer.parseInt(args[0]);
        if(slot == null) return "The argument provided needs to be an integer";
        slot -= 1;
        if(slot < 0) return "The slot provided must be 1 or higher!";
        if(slot >= list.size()) return "The slot provided is higher than the number of conditional drops on this trainer";
        list.remove(slot.intValue());
        return "Successfully removed the first matching drop matching the conditionals provided from this trainer";
    }
    public static String listConditionalDrops(NPCTrainer trainer) {
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(!main.contains(BattleRewardTags.COND_WINNINGS.getId())) main.put(BattleRewardTags.COND_WINNINGS.getId(), new ListNBT());
        ListNBT list = main.getList(BattleRewardTags.COND_WINNINGS.getId(), Constants.NBT.TAG_COMPOUND);
        StringBuilder builder = new StringBuilder();
        builder.append("=== Conditional Drops ===\n");
        int counter = 1;
        ArrayList<INBT> toRemove = new ArrayList<>();
        for(INBT nbt: list) {
            if(!(nbt instanceof CompoundNBT)) {
                toRemove.add(nbt);
                continue;
            }
            if(!((CompoundNBT) nbt).contains("type")) {
                toRemove.add(nbt);
                continue;
            }
            if(!((CompoundNBT) nbt).contains("data")) {
                toRemove.add(nbt);
                continue;
            }
            if(!((CompoundNBT) nbt).contains("prob")) {
                toRemove.add(nbt);
                continue;
            }
            if(!((CompoundNBT) nbt).contains("quantity")) {
                toRemove.add(nbt);
                continue;
            }
            builder.append(counter + ") " + ((CompoundNBT) nbt).getString("type") + " " + ((CompoundNBT) nbt).getString("data") + " " + ((CompoundNBT) nbt).getInt("prob") + " " + ((CompoundNBT) nbt).getInt("quantity") + "\n");
            counter++;
        }
        builder.append("=== Conditional Drops ===");
        toRemove.forEach(list::remove);
        return builder.toString();
    }
    public static String clearDrops(NPCTrainer trainer) {
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(!main.contains(BattleRewardTags.COND_WINNINGS.getId())) main.put(BattleRewardTags.COND_WINNINGS.getId(), new ListNBT());
        ListNBT list = main.getList(BattleRewardTags.COND_WINNINGS.getId(), Constants.NBT.TAG_COMPOUND);
        list.clear();
        return "Successfully cleared all conditional drops from this trainer.";
    }

    public static String addPresetDrops(NPCTrainer trainer, String ... args) {
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundNBT main = trainer.getPersistentData().getCompound(TrainerUtilsTags.MAIN_TAG.getId());
        if(!main.contains(BattleRewardTags.COND_WINNINGS.getId())) main.put(BattleRewardTags.COND_WINNINGS.getId(), new ListNBT());
        ListNBT list = main.getList(BattleRewardTags.COND_WINNINGS.getId(), Constants.NBT.TAG_COMPOUND);
        if(args.length != 1) return "This command was not provided the needed number of arguments";

        //Get preset if exists
        if(!RewardPresetConfig.DATA.getPresets().containsKey(args[0])) return "Cannot find provided preset!";
        List<RewardInstance> rewards = RewardPresetConfig.DATA.getPresets().get(args[0]);

        //Add preset rewards to trainer
        for(RewardInstance inst: rewards) {
            if(!inst.getType().equals("item") && !inst.getType().contains("cmd")) continue;
            if(inst.getProb() < 1 || inst.getProb() > 100) continue;
            if(inst.getQuantity() <= 0) continue;
            CompoundNBT drop = new CompoundNBT();
            drop.putString("type", inst.getType());
            drop.putString("data", inst.getData());
            drop.putInt("prob", inst.getProb());
            drop.putInt("quantity", inst.getQuantity());
            list.add(drop);
        }
        if(list.isEmpty()) return "No items were added from this preset. Double check to make sure the items inside are correct!";
        return "Successfully added preset to conditional drops!";
    }
}
