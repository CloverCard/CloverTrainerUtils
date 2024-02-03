package com.clovercard.clovertrainerutils.commands;

import com.clovercard.clovertrainerutils.enums.RequestTags;
import com.clovercard.clovertrainerutils.listeners.PlayerCommandsTickListener;
import com.clovercard.clovertrainerutils.objects.requests.InteractRequest;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
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
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int clear(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.RESET_TRAINER, null, 600));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int about(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        src.sendSuccess(() -> Component.translatable("ctu.about"), true);
        return 0;
    }
    private int addCheckpoints(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_CHECKPOINTS, args, 600));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int addBeginningCheckpoint(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_CHECKPOINT_BEGINNING, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int addFinalCheckpoint(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_CHECKPOINT_END, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int clearCheckpoint(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_CHECKPOINTS, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int listCheckpoints(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_CHECKPOINT, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }

    private int addCondDrops(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_COND_DROP, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int addCondDropsPreset(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_COND_DROP_PRESET, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int removeCondDrops(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_COND_DROP, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int clearCondDrops(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_COND_DROP, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int listCondDrops(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_COND_DROP, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }

    private int addStartCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_START_COMMAND, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int addForfeitCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_FORFEIT_COMMAND, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int addPlayerWinsCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_PLAYER_WIN_COMMAND, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int addPlayerLosesCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_PLAYER_LOSS_COMMAND, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }

    private int addInteractCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_INTERACT_COMMAND, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }

    private int removeStartCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_START_COMMAND, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int removeForfeitCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_FORFEIT_COMMAND, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int removePlayerWinsCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_PLAYER_WIN_COMMAND, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int removePlayerLosesCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_PLAYER_LOSS_COMMAND, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }

    private int removeInteractCommand(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_INTERACT_COMMAND, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }

    private int clearStartCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_START_COMMANDS, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int clearForfeitCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_FORFEIT_COMMANDS, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int clearPlayerWinsCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_PLAYER_WIN_COMMANDS, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int clearPlayerLosesCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_PLAYER_LOSS_COMMANDS, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int clearInteractCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_INTERACT_COMMANDS, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }

    private int listStartCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_START_COMMANDS, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int listForfeitCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_FORFEIT_COMMANDS, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int listPlayerWinsCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_PLAYER_WIN_COMMANDS, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int listPlayerLosesCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_LOSS_COMMANDS, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }

    private int listInteractCommand(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_INTERACT_COMMANDS, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }

    private int addShuffleTeam(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.ADD_SHUFFLE, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int removeShuffleTeam(CommandSourceStack src, String args) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.REMOVE_SHUFFLE, args, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int clearShuffleTeam(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.CLEAR_SHUFFLE, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
    private int listShuffleTeam(CommandSourceStack src) {
        if(!(src.getEntity() instanceof ServerPlayer)) return 1;
        ServerPlayer player = (ServerPlayer) src.getEntity();
        PlayerCommandsTickListener.pendingRequests.put(player.getUUID(), new InteractRequest(RequestTags.LIST_SHUFFLE, null, 300));
        src.sendSuccess(() -> Component.translatable("ctu.click.prompt"), true);
        return 0;
    }
}
