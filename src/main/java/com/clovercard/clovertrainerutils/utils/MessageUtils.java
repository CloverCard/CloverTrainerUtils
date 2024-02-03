package com.clovercard.clovertrainerutils.utils;

import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class MessageUtils {
    public static String MOD_INFO = """
            ====== CloverTrainerUtils ======
            - Version 2.0.0 - Written By CloverCard
            ====== COMMANDS ======
            - /tutils init: Enables a trainer to cloned and customized.
            - /tutils clear: Clears all tutils data on a trainer.
            - /tutils checkpoints add [access_tag] [rewarded_tag]: Sets the tag required to access a trainer and the tag rewarded for beating the trainer.
            - /tutils checkpoints begin [rewarded_tag]: Sets the tag rewarded for beating the trainer and removes any tag required to access the trainer.
            - /tutils checkpoints finish [access_tag]: Sets the tag required to access a trainer and removes any tag rewarded for beating the trainer.
            - /tutils checkpoints list: Displays the current tags on a trainer in order.
            - /tutils checkpoints clear: Removes all tags on a trainer.
            - /tutils conddrops add [item OR cmd] [probability (1-100)] [quantity (1 or Greater)] [item id OR command]: Adds an item/cmd drop with a conditional drop rate to the trainer.
            - /tutils conddrops addpreset [preset_name]: Adds a list of conditional drops defined within the config.
            - /tutils conddrops list: Lists a trainer's conditional drops in the order added.
            - /tutils conddrops clear: Removes all of a trainer's conditional drops.
            - /tutils conddrops remove [order_number]: Removes a conditional drop currently in the order number's position.
            - /tutils commands add [start OR forfeit OR playerwins OR playerloses OR interact] [command]: Performs a command after the specified event happens.
            - /tutils commands list [start OR forfeit OR playerwins OR playerloses OR interact]: Lists the commands that are performed after the specified event in order added.
            - /tutils commands remove [start OR forfeit OR playerwins OR playerloses OR interact] [order_number]: Removes a command at the position of the order number from a trainer in the specified event.
            - /tutils commands clear [start OR forfeit OR playerwins OR playerloses OR interact]: Clears all commands on a trainer for the specified event.
            - /tutils shuffler add [team_id]: Adds a team defined in the config to potentially select at the start of a battle.
            - /tutils shuffler remove [team_id]: Removes the team id provided from the ids a trainer can shuffle through.
            - /tutils shuffler clear: Clears all the teams a trainer can shuffle through.
            - /tutils shuffler list: Lists all the team ids the trainer can shuffle through.
            ====== COMMAND TAGS ======
            - @PL: Get's the player's username.
            - @PCMD: Makes the player run the command.
            - @D:#: Delays the commands by the amount of seconds provided
            """;
    public static void sendTranslatableMessage(ServerPlayer player, String message) {
        MutableComponent text = Component.translatable(message);
        player.sendSystemMessage(text);
    }
    public static void sendTranslatableMessage(Player player, String message) {
        MutableComponent text = Component.translatable(message);
        player.sendSystemMessage(text);
    }
}
