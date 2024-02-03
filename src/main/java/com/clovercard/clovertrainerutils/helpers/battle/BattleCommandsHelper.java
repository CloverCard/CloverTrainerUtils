package com.clovercard.clovertrainerutils.helpers.battle;

import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.clovercard.clovertrainerutils.enums.battle.BattleCommandsTypes;
import com.clovercard.clovertrainerutils.listeners.BattleCommandsTickQueue;
import com.clovercard.clovertrainerutils.objects.requests.BattleCommand;
import com.pixelmonmod.pixelmon.entities.npcs.NPCEntity;

import net.minecraft.Util;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.jarjar.selection.util.Constants;

import java.util.UUID;

public class BattleCommandsHelper {
    //Puts BattleCommands into the BattleCommandsTickQueue
    public static void enqueueCommands(String tagId, ServerPlayer player, Entity trainer) {
        //Get the main tag nbt
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) return;
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());

        //Make sure the input tag is a command type
        if (!BattleCommandsTypes.contains(tagId)) return;

        //Make current data is in a valid format
        assert main != null;
        if (!main.contains(tagId)) main.put(tagId, new ListTag());
        if (!(main.get(tagId) instanceof ListTag)) main.put(tagId, new ListTag());
        ListTag nbtList = (ListTag) main.get(tagId);

        //Iterate though list, parse cmd, then put into the command queue.
        assert nbtList != null;
        for (Tag nbt : nbtList) {
            if (!(nbt instanceof StringTag)) continue;
            UUID uuid = Util.NIL_UUID;
            String cmd = nbt.getAsString();
            Integer delay = 0;
            StringBuilder cmdBuilder = new StringBuilder();
            String[] args = cmd.split(" ");
            //Handle placeholders
            for(int i = 0; i < args.length; i++) {
                if(args[i].toLowerCase().contains("@pl")) {
                    String res = args[i].toLowerCase().replaceAll("@pl", player.getName().getString());
                    cmdBuilder.append(res);
                    if(i < args.length-1) cmdBuilder.append(" ");
                }
                else if (args[i].toLowerCase().contains("@d")) {
                    String[] subArg = args[i].split(":");
                    if(subArg.length == 2) delay = Integer.parseInt(subArg[1]);
                }
                else if (args[i].toLowerCase().contains("@pcmd")) {
                    uuid = player.getUUID();
                }
                else {
                    cmdBuilder.append(args[i]);
                    if(i < args.length-1) cmdBuilder.append(" ");
                }
            }
            if(delay == null) delay = 0;
            cmd = cmdBuilder.toString();
            BattleCommand cmdData = new BattleCommand(delay*20, cmd, uuid);
            BattleCommandsTickQueue.addToHolder(cmdData);
        }
    }

    public static String addStartCommand(NPCEntity trainer, String... args) {
        //Stores a command in a ListTag to be run when a battle starts.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.START_BATTLE_COMMANDS.getId()))
            main.put(BattleCommandsTypes.START_BATTLE_COMMANDS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.START_BATTLE_COMMANDS.getId(), Tag.TAG_STRING);
        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            cmdBuilder.append(args[i]);
            if (i < args.length - 1) cmdBuilder.append(" ");
        }
        list.add(list.size(), StringTag.valueOf(cmdBuilder.toString()));
        return "Successfully added new command to trainer!";
    }

    public static String addPlayerWinsCommand(NPCEntity trainer, String... args) {
        //Stores a command in a ListTag to be run when a player wins.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.PLAYER_WINS.getId()))
            main.put(BattleCommandsTypes.PLAYER_WINS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.PLAYER_WINS.getId(), Tag.TAG_STRING);
        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            cmdBuilder.append(args[i]);
            if (i < args.length - 1) cmdBuilder.append(" ");
        }
        list.add(list.size(), StringTag.valueOf(cmdBuilder.toString()));
        return "Successfully added new command to trainer!";
    }

    public static String addPlayerLosesCommand(NPCEntity trainer, String... args) {
        //Stores a command in a ListTag to be run when a player loses.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.PLAYER_LOSS.getId()))
            main.put(BattleCommandsTypes.PLAYER_LOSS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.PLAYER_LOSS.getId(), Tag.TAG_STRING);
        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            cmdBuilder.append(args[i]);
            if (i < args.length - 1) cmdBuilder.append(" ");
        }
        list.add(list.size(), StringTag.valueOf(cmdBuilder.toString()));
        return "Successfully added new command to trainer!";
    }

    public static String addForfeitCommand(NPCEntity trainer, String... args) {
        //Stores a command in a ListTag to be run when a forfeit happens.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId()))
            main.put(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId(), Tag.TAG_STRING);
        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            cmdBuilder.append(args[i]);
            if (i < args.length - 1) cmdBuilder.append(" ");
        }
        list.add(list.size(), StringTag.valueOf(cmdBuilder.toString()));
        return "Successfully added new command to trainer!";
    }

    public static String addInteractCommand(Entity trainer, String... args) {
        //Stores a command in a ListTag to be run when a forfeit happens.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.INTERACT.getId()))
            main.put(BattleCommandsTypes.INTERACT.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.INTERACT.getId(), Tag.TAG_STRING);
        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            cmdBuilder.append(args[i]);
            if (i < args.length - 1) cmdBuilder.append(" ");
        }
        list.add(list.size(), StringTag.valueOf(cmdBuilder.toString()));
        return "Successfully added new command to trainer!";
    }

    public static String removeStartCommand(NPCEntity trainer, String... args) {
        //Removes a command from the ListTag managing battle starts.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        if(args.length != 1) return "Too many arguments provided";
        Integer slot = Integer.parseInt(args[0]);
        if(slot == null) return "Argument must be an integer!";
        slot -= 1;
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.START_BATTLE_COMMANDS.getId()))
            main.put(BattleCommandsTypes.START_BATTLE_COMMANDS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.START_BATTLE_COMMANDS.getId(), Tag.TAG_STRING);
        if(list.isEmpty()) return "This trainer does not have any commands to remove from this category";
        if(slot >= list.size()) return "Slot number provided is bigger than the size of this list!";
        if(slot < 0) return "Slot number must be 1 or higher!";
        list.remove(slot.intValue());
        return "Successfully removed command from trainer!";
    }
    public static String removeForfeitCommand(NPCEntity trainer, String... args) {
        //Removes a command from the ListTag managing forfeits.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        if(args.length != 1) return "Too many arguments provided";
        Integer slot = Integer.parseInt(args[0]);
        if(slot == null) return "Argument must be an integer!";
        slot -= 1;
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId()))
            main.put(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId(), Tag.TAG_STRING);
        if(list.isEmpty()) return "This trainer does not have any commands to remove from this category";
        if(slot >= list.size()) return "Slot number provided is bigger than the size of this list!";
        if(slot < 0) return "Slot number must be 1 or higher!";
        list.remove(slot.intValue());
        return "Successfully removed command from trainer!";
    }
    public static String removePlayerWinsCommand(NPCEntity trainer, String... args) {
        //Removes a command from the ListTag managing player wins.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        if(args.length != 1) return "Too many arguments provided";
        Integer slot = Integer.parseInt(args[0]);
        if(slot == null) return "Argument must be an integer!";
        slot -= 1;
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.PLAYER_WINS.getId()))
            main.put(BattleCommandsTypes.PLAYER_WINS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.PLAYER_WINS.getId(), Tag.TAG_STRING);
        if(list.isEmpty()) return "This trainer does not have any commands to remove from this category";
        if(slot >= list.size()) return "Slot number provided is bigger than the size of this list!";
        if(slot < 0) return "Slot number must be 1 or higher!";
        list.remove(slot.intValue());
        return "Successfully removed command from trainer!";
    }
    public static String removePlayerLosesCommand(NPCEntity trainer, String... args) {
        //Removes a command from the ListTag managing player losses.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        if(args.length != 1) return "Too many arguments provided";
        Integer slot = Integer.parseInt(args[0]);
        if(slot == null) return "Argument must be an integer!";
        slot -= 1;
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.PLAYER_LOSS.getId()))
            main.put(BattleCommandsTypes.PLAYER_LOSS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.PLAYER_LOSS.getId(), Tag.TAG_STRING);
        if(list.isEmpty()) return "This trainer does not have any commands to remove from this category";
        if(slot >= list.size()) return "Slot number provided is bigger than the size of this list!";
        if(slot < 0) return "Slot number must be 1 or higher!";
        list.remove(slot.intValue());
        return "Successfully removed command from trainer!";
    }

    public static String removeInteractCommand(Entity trainer, String... args) {
        //Removes a command from the ListTag managing player losses.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        if(args.length != 1) return "Too many arguments provided";
        Integer slot = Integer.parseInt(args[0]);
        if(slot == null) return "Argument must be an integer!";
        slot -= 1;
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.INTERACT.PLAYER_LOSS.getId()))
            main.put(BattleCommandsTypes.INTERACT.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.INTERACT.getId(), Tag.TAG_STRING);
        if(list.isEmpty()) return "This trainer does not have any commands to remove from this category";
        if(slot >= list.size()) return "Slot number provided is bigger than the size of this list!";
        if(slot < 0) return "Slot number must be 1 or higher!";
        list.remove(slot.intValue());
        return "Successfully removed command from trainer!";
    }

    public static String clearStartCommands(NPCEntity trainer) {
        //Clears the ListTag managing battle starts.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.START_BATTLE_COMMANDS.getId()))
            main.put(BattleCommandsTypes.START_BATTLE_COMMANDS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.START_BATTLE_COMMANDS.getId(), Tag.TAG_STRING);
        list.clear();
        return "Successfully cleared commands from trainer!";
    }
    public static String clearForfeitCommands(NPCEntity trainer) {
        //Clears the ListTag managing forfeits.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId()))
            main.put(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId(), Tag.TAG_STRING);
        list.clear();
        return "Successfully cleared commands from trainer!";
    }
    public static String clearPlayerWinCommands(NPCEntity trainer) {
        //Clears the ListTag managing player wins.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.PLAYER_WINS.getId()))
            main.put(BattleCommandsTypes.PLAYER_WINS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.PLAYER_WINS.getId(), Tag.TAG_STRING);
        list.clear();
        return "Successfully cleared commands from trainer!";
    }
    public static String clearPlayerLossCommands(NPCEntity trainer) {
        //Clears the ListTag managing player losses.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.PLAYER_LOSS.getId()))
            main.put(BattleCommandsTypes.PLAYER_LOSS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.PLAYER_LOSS.getId(), Tag.TAG_STRING);
        list.clear();
        return "Successfully cleared commands from trainer!";
    }

    public static String clearInteractCommands(Entity trainer) {
        //Clears the ListTag managing player losses.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.INTERACT.getId()))
            main.put(BattleCommandsTypes.INTERACT.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.INTERACT.getId(), Tag.TAG_STRING);
        list.clear();
        return "Successfully cleared commands from trainer!";
    }

    public static String listStartCommands(NPCEntity trainer) {
        //Displays the ListTag managing battle starts.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.START_BATTLE_COMMANDS.getId()))
            main.put(BattleCommandsTypes.START_BATTLE_COMMANDS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.START_BATTLE_COMMANDS.getId(), Tag.TAG_STRING);
        StringBuilder resBuilder = new StringBuilder();
        resBuilder.append("=== START COMMANDS ===\n");
        int counter = 1;
        for(Tag tag: list) {
            if(tag instanceof StringTag) {
                resBuilder.append(counter);
                resBuilder.append("). ");
                StringTag nbt = (StringTag) tag;
                resBuilder.append(nbt.getAsString());
                resBuilder.append("\n");
                counter++;
            }
        }
        resBuilder.append("=== START COMMANDS ===");
        return resBuilder.toString();
    }
    public static String listForfeitCommands(NPCEntity trainer) {
        //Displays the ListTag managing battle forfeits.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId()))
            main.put(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.FORFEIT_BATTLE_COMMANDS.getId(), Tag.TAG_STRING);
        StringBuilder resBuilder = new StringBuilder();
        resBuilder.append("=== FORFEIT COMMANDS ===\n");
        int counter = 1;
        for(Tag tag: list) {
            if(tag instanceof StringTag) {
                resBuilder.append(counter);
                resBuilder.append("). ");
                StringTag nbt = (StringTag) tag;
                resBuilder.append(nbt.getAsString());
                resBuilder.append("\n");
                counter++;
            }
        }
        resBuilder.append("=== FORFEIT COMMANDS ===");
        return resBuilder.toString();
    }
    public static String listPlayerWinsCommands(NPCEntity trainer) {
        //Displays the ListTag managing player wins.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.PLAYER_WINS.getId()))
            main.put(BattleCommandsTypes.PLAYER_WINS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.PLAYER_WINS.getId(), Tag.TAG_STRING);
        StringBuilder resBuilder = new StringBuilder();
        resBuilder.append("=== PLAYER WIN COMMANDS ===\n");
        int counter = 1;
        for(Tag tag: list) {
            if(tag instanceof StringTag) {
                resBuilder.append(counter);
                resBuilder.append("). ");
                StringTag nbt = (StringTag) tag;
                resBuilder.append(nbt.getAsString());
                resBuilder.append("\n");
                counter++;
            }
        }
        resBuilder.append("=== PLAYER WIN COMMANDS ===");
        return resBuilder.toString();
    }
    public static String listPlayerLossCommands(NPCEntity trainer) {
        //Displays the ListTag managing player losses.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.PLAYER_LOSS.getId()))
            main.put(BattleCommandsTypes.PLAYER_LOSS.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.PLAYER_LOSS.getId(), Tag.TAG_STRING);
        StringBuilder resBuilder = new StringBuilder();
        resBuilder.append("=== PLAYER LOSS COMMANDS ===\n");
        int counter = 1;
        for(Tag tag: list) {
            if(tag instanceof StringTag) {
                resBuilder.append(counter);
                resBuilder.append("). ");
                StringTag nbt = (StringTag) tag;
                resBuilder.append(nbt.getAsString());
                resBuilder.append("\n");
                counter++;
            }
        }
        resBuilder.append("=== PLAYER LOSS COMMANDS ===");
        return resBuilder.toString();
    }

    public static String listInteractCommands(Entity trainer) {
        //Displays the ListTag managing player losses.
        if (!trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId()))
            return "This trainer is not initialized! Use '/tutils init' in order to use this command!";
        CompoundTag main = (CompoundTag) trainer.getPersistentData().get(TrainerUtilsTags.MAIN_TAG.getId());
        if (!main.contains(BattleCommandsTypes.INTERACT.getId()))
            main.put(BattleCommandsTypes.INTERACT.getId(), new ListTag());
        ListTag list = main.getList(BattleCommandsTypes.INTERACT.getId(), Tag.TAG_STRING);
        StringBuilder resBuilder = new StringBuilder();
        resBuilder.append("=== INTERACT COMMANDS ===\n");
        int counter = 1;
        for(Tag tag: list) {
            if(tag instanceof StringTag) {
                resBuilder.append(counter);
                resBuilder.append("). ");
                StringTag nbt = (StringTag) tag;
                resBuilder.append(nbt.getAsString());
                resBuilder.append("\n");
                counter++;
            }
        }
        resBuilder.append("=== INTERACT COMMANDS ===");
        return resBuilder.toString();
    }
}
