package com.clovercard.clovertrainerutils.commands;

import com.clovercard.clovertrainerutils.enums.RequestTags;
import com.clovercard.clovertrainerutils.listeners.PlayerCommandsTickListener;
import com.clovercard.clovertrainerutils.objects.requests.InteractRequest;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class TUtils {

    public TUtils(CommandDispatcher<CommandSourceStack> dispatcher) {
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
                                                .then(Commands.literal("interact")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> addInteractCommand(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
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
                                                .then(Commands.literal("interact")
                                                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                                                .executes(cmd -> removeInteractCommand(cmd.getSource(), StringArgumentType.getString(cmd, "args")))
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
                                                .then(Commands.literal("interact")
                                                        .executes(cmd -> clearInteractCommand(cmd.getSource()))
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
                                                .then(Commands.literal("interact")
                                                        .executes(cmd -> listInteractCommand(cmd.getSource()))
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
    private int init(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.INIT_TRAINER, null, 600));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int clear(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.RESET_TRAINER, null, 600));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int about(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        String msg = "====== CloverTrainerUtils ======\n\n" +
                "- Version 1.0.5 - Written By CloverCard\n\n" +
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
                "- /tutils commands add [start OR forfeit OR playerwins OR playerloses OR interact] [command]: Performs a command after the specified event happens.\n\n" +
                "- /tutils commands list [start OR forfeit OR playerwins OR playerloses OR interact]: Lists the commands that are performed after the specified event in order added.\n\n" +
                "- /tutils commands remove [start OR forfeit OR playerwins OR playerloses OR interact] [order_number]: Removes a command at the position of the order number from a trainer in the specified event.\n\n" +
                "- /tutils commands clear [start OR forfeit OR playerwins OR playerloses OR interact]: Clears all commands on a trainer for the specified event.\n\n" +
                "- /tutils shuffler add [team_id]: Adds a team defined in the config to potentially select at the start of a battle.\n\n" +
                "- /tutils shuffler remove [team_id]: Removes the team id provided from the ids a trainer can shuffle through.\n\n" +
                "- /tutils shuffler clear: Clears all the teams a trainer can shuffle through.\n\n" +
                "- /tutils shuffler list: Lists all the team ids the trainer can shuffle through.\n\n" +
                "====== COMMAND TAGS ======\n\n" +
                "- @PL: Get's the player's username.\n\n" +
                "- @PCMD: Makes the player run the command.\n\n" +
                "- @D:#: Delays the commands by the amount of seconds provided";

        player.sendSystemMessage(Component.literal(msg));
        return 0;
    }
    private int addCheckpoints(CommandSourceStack src, String args) {
        
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_CHECKPOINTS, args, 600));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int addBeginningCheckpoint(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_CHECKPOINT_BEGINNING, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int addFinalCheckpoint(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_CHECKPOINT_END, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int clearCheckpoint(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_CHECKPOINTS, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int listCheckpoints(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_CHECKPOINT, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }

    private int addCondDrops(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_COND_DROP, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int addCondDropsPreset(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_COND_DROP_PRESET, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int removeCondDrops(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_COND_DROP, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int clearCondDrops(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_COND_DROP, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int listCondDrops(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_COND_DROP, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }

    private int addStartCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_START_COMMAND, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int addForfeitCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_FORFEIT_COMMAND, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int addPlayerWinsCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_PLAYER_WIN_COMMAND, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int addPlayerLosesCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_PLAYER_LOSS_COMMAND, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }

    private int addInteractCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_INTERACT_COMMAND, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }

    private int removeStartCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_START_COMMAND, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int removeForfeitCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_FORFEIT_COMMAND, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int removePlayerWinsCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_PLAYER_WIN_COMMAND, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int removePlayerLosesCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_PLAYER_LOSS_COMMAND, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }

    private int removeInteractCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_INTERACT_COMMAND, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }

    private int clearStartCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_START_COMMANDS, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int clearForfeitCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_FORFEIT_COMMANDS, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int clearPlayerWinsCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_PLAYER_WIN_COMMANDS, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int clearPlayerLosesCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_PLAYER_LOSS_COMMANDS, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int clearInteractCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_INTERACT_COMMANDS, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }

    private int listStartCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_START_COMMANDS, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int listForfeitCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_FORFEIT_COMMANDS, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int listPlayerWinsCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_PLAYER_WIN_COMMANDS, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int listPlayerLosesCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_LOSS_COMMANDS, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }

    private int listInteractCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_INTERACT_COMMANDS, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }

    private int addShuffleTeam(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_SHUFFLE, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int removeShuffleTeam(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_SHUFFLE, args, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int clearShuffleTeam(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_SHUFFLE, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
    private int listShuffleTeam(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_SHUFFLE, null, 300));
        player.sendSystemMessage(Component.literal("[CloverTrainerUtils] All set! Right click a trainer to process your request!"));
        return 0;
    }
}
