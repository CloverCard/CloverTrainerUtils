package com.clovercard.clovertrainerutils.commands;

import com.clovercard.clovertrainerutils.enums.RequestTags;
import com.clovercard.clovertrainerutils.listeners.PlayerCommandsTickListener;
import com.clovercard.clovertrainerutils.objects.requests.InteractRequest;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

public class TUtils {
    public TUtils(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("tutils")
                        .then(
                                Commands.literal("checkpoints")
                                        .then(
                                                Commands.literal("add")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> addCheckpoints(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                        )
                                        )
                                        .then(
                                                Commands.literal("begin")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> addBeginningCheckpoint(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                        )
                                        )
                                        .then(
                                                Commands.literal("finish")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> addFinalCheckpoint(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                        )
                                        )
                                        .then(Commands.literal("clear")
                                                .executes(cmd -> clearCheckpoint(cmd.getSource()))
                                        )
                                        .then(Commands.literal("list")
                                                .executes(cmd -> listCheckpoints(cmd.getSource()))
                                        )
                        )
                        .then(
                                Commands.literal("conddrops")
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("args", StringArgumentType.greedyString())
                                                        .executes(cmd -> addCondDrops(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                )
                                        )
                                        .then(Commands.literal("addpreset")
                                                .then(Commands.argument("args", StringArgumentType.greedyString())
                                                        .executes(cmd -> addCondDropsPreset(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.argument("args", StringArgumentType.greedyString())
                                                        .executes(cmd -> removeCondDrops(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                )
                                        )
                                        .then(Commands.literal("clear")
                                                .executes(cmd -> clearCondDrops(cmd.getSource()))
                                        )
                                        .then(Commands.literal("list")
                                                .executes(cmd -> listCondDrops(cmd.getSource()))
                                        )
                        )
                        .then(
                                Commands.literal("shuffler")
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("team", StringArgumentType.string())
                                                        .executes(cmd -> addShuffleTeam(cmd.getSource(), StringArgumentType.getString(cmd, "team")))
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.argument("team", StringArgumentType.string())
                                                        .executes(cmd -> removeShuffleTeam(cmd.getSource(), StringArgumentType.getString(cmd, "team")))
                                                )
                                        )
                                        .then(Commands.literal("clear")
                                                .executes(cmd -> clearShuffleTeam(cmd.getSource()))
                                        )
                                        .then(Commands.literal("list")
                                                .executes(cmd -> listShuffleTeam(cmd.getSource()))
                                        )
                        )
                        .then(
                                Commands.literal("commands")
                                        .then(Commands.literal("add")
                                                .then(Commands.literal("start")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> addStartCommand(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                        )
                                                )
                                                .then(Commands.literal("forfeit")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> addForfeitCommand(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                        )
                                                )
                                                .then(Commands.literal("playerwins")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> addPlayerWinsCommand(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                        )
                                                )
                                                .then(Commands.literal("playerloses")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> addPlayerLosesCommand(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                        )
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.literal("start")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> removeStartCommand(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                        )
                                                )
                                                .then(Commands.literal("forfeit")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> removeForfeitCommand(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                        )
                                                )
                                                .then(Commands.literal("playerwins")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> removePlayerWinsCommand(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                        )
                                                )
                                                .then(Commands.literal("playerloses")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> removePlayerLosesCommand(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
                                                        )
                                                )
                                        )
                                        .then(Commands.literal("clear")
                                                .then(Commands.literal("start")
                                                        .executes(cmd -> clearStartCommand(cmd.getSource()))
                                                )
                                                .then(Commands.literal("forfeit")
                                                        .executes(cmd -> clearForfeitCommand(cmd.getSource()))
                                                )
                                                .then(Commands.literal("playerwins")
                                                        .executes(cmd -> clearPlayerWinsCommand(cmd.getSource()))
                                                )
                                                .then(Commands.literal("playerloses")
                                                        .executes(cmd -> clearPlayerLosesCommand(cmd.getSource()))
                                                )
                                        )
                                        .then(Commands.literal("list")
                                                .then(Commands.literal("start")
                                                        .executes(cmd -> listStartCommand(cmd.getSource()))
                                                )
                                                .then(Commands.literal("forfeit")
                                                        .executes(cmd -> listForfeitCommand(cmd.getSource()))
                                                )
                                                .then(Commands.literal("playerwins")
                                                        .executes(cmd -> listPlayerWinsCommand(cmd.getSource()))
                                                )
                                                .then(Commands.literal("playerloses")
                                                        .executes(cmd -> listPlayerLosesCommand(cmd.getSource()))
                                                )
                                        )
                        )
                        .then(
                                Commands.literal("init").executes(cmd -> init(cmd.getSource()))
                        )
                        .then(
                                Commands.literal("clear").executes(cmd -> clear(cmd.getSource()))
                        )
                        .then(
                                Commands.literal("about").executes(cmd -> about(cmd.getSource()))
                        )
        );
    }
    private int init(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.INIT_TRAINER, null, 600));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int clear(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.RESET_TRAINER, null, 600));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int about(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        String msg = "====== CloverTrainerUtils ======\n\n" +
                "- Version 1.0.0 - Written By CloverCard\n\n" +
                "====== COMMANDS ======\n\n" +
                "- /tutils init: Enables a trainer to cloned and customized.\n\n" +
                "- /tutils clear: Clears all tutils data on a trainer.\n\n" +
                "- /tutils checkpoints add [access_tag] [rewarded_tag]: Sets the tag required to access a trainer and the tag rewarded for beating the trainer.\n\n" +
                "- /tutils checkpoints begin [rewarded_tag]: Sets the tag rewarded for beating the trainer and removes any tag required to access the trainer.\n\n" +
                "- /tutils checkpoints finish [access_tag]: Sets the tag required to access a trainer and removes any tag rewarded for beating the trainer.\n\n" +
                "- /tutils checkpoints list: Displays the current tags on a trainer in order.\n\n" +
                "- /tutils checkpoints clear: Removes all tags on a trainer.\n\n" +
                "- /tutils conddrops add [item OR cmd] [probability (1-100)] [quantity (1 or Greater)] [item id OR command]: Adds an item/cmd drop with a conditional drop rate to the trainer.\n\n" +
                "- /tutils conddrops addpreset [preset_name]: Adds a list of conditional drops defined within the config.\n\n" +
                "- /tutils conddrops list: Lists a trainer's conditional drops in the order added.\n\n" +
                "- /tutils conddrops clear: Removes all of a trainer's conditional drops.\n\n" +
                "- /tutils conddrops remove [order_number]: Removes a conditional drop currently in the order number's position.\n\n" +
                "- /tutils commands add [start OR forfeit OR playerwins OR playerloses] [command]: Performs a command after the specified event happens.\n\n" +
                "- /tutils commands list [start OR forfeit OR playerwins OR playerloses]: Lists the commands that are performed after the specified event in order added.\n\n" +
                "- /tutils commands remove [start OR forfeit OR playerwins OR playerloses] [order_number]: Removes a command at the position of the order number from a trainer in the specified event.\n\n" +
                "- /tutils commands clear [start OR forfeit OR playerwins OR playerloses]: Clears all commands on a trainer for the specified event.\n\n" +
                "- /tutils shuffler add [team_id]: Adds a team defined in the config to potentially select at the start of a battle.\n\n" +
                "- /tutils shuffler remove [team_id]: Removes the team id provided from the ids a trainer can shuffle through.\n\n" +
                "- /tutils shuffler clear: Clears all the teams a trainer can shuffle through.\n\n" +
                "- /tutils shuffler list: Lists all the team ids the trainer can shuffle through.\n\n" +
                "====== COMMAND TAGS ======\n\n" +
                "- @PL: Get's the player's username.\n\n" +
                "- @PCMD: Makes the player run the command.\n\n" +
                "- @D:#: Delays the commands by the amount of seconds provided";
        StringTextComponent msgObject = new StringTextComponent(msg);
        player.sendMessage(msgObject, Util.NIL_UUID);
        return 0;
    }
    private int addCheckpoints(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_CHECKPOINTS, args, 600));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int addBeginningCheckpoint(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_CHECKPOINT_BEGINNING, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int addFinalCheckpoint(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_CHECKPOINT_END, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int clearCheckpoint(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_CHECKPOINTS, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int listCheckpoints(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_CHECKPOINT, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }

    private int addCondDrops(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_COND_DROP, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int addCondDropsPreset(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_COND_DROP_PRESET, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int removeCondDrops(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_COND_DROP, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int clearCondDrops(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_COND_DROP, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int listCondDrops(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_COND_DROP, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }

    private int addStartCommand(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_START_COMMAND, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int addForfeitCommand(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_FORFEIT_COMMAND, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int addPlayerWinsCommand(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_PLAYER_WIN_COMMAND, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int addPlayerLosesCommand(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_PLAYER_LOSS_COMMAND, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }

    private int removeStartCommand(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_START_COMMAND, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int removeForfeitCommand(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_FORFEIT_COMMAND, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int removePlayerWinsCommand(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_PLAYER_WIN_COMMAND, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int removePlayerLosesCommand(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_PLAYER_LOSS_COMMAND, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }

    private int clearStartCommand(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_START_COMMANDS, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int clearForfeitCommand(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_FORFEIT_COMMANDS, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int clearPlayerWinsCommand(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_PLAYER_WIN_COMMANDS, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int clearPlayerLosesCommand(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_PLAYER_LOSS_COMMANDS, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }

    private int listStartCommand(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_START_COMMANDS, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int listForfeitCommand(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_FORFEIT_COMMANDS, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int listPlayerWinsCommand(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_PLAYER_WIN_COMMANDS, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int listPlayerLosesCommand(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_LOSS_COMMANDS, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }

    private int addShuffleTeam(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_SHUFFLE, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int removeShuffleTeam(CommandSource src, String args) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_SHUFFLE, args, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int clearShuffleTeam(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_SHUFFLE, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
    private int listShuffleTeam(CommandSource src) {
        if(!(src.getEntity() instanceof ServerPlayerEntity)) return 1;
        ServerPlayerEntity player = (ServerPlayerEntity) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_SHUFFLE, null, 300));
        player.sendMessage(new StringTextComponent("[CloverTrainerUtils] All set! Right click a trainer to process your request!"), Util.NIL_UUID);
        return 0;
    }
}
