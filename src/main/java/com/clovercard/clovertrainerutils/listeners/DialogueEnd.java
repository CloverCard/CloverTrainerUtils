package com.clovercard.clovertrainerutils.listeners;

import com.clovercard.clovertrainerutils.objects.requests.BattleCommand;
import com.pixelmonmod.pixelmon.api.events.dialogue.DialogueEndedEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Util;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class DialogueEnd {
    @SubscribeEvent
    public void onDialogueEnds(DialogueEndedEvent event) {
        if(event.player.getPersistentData().contains("ctutilsplcmds")) {
            if(event.player.getPersistentData().get("ctutilsplcmds") instanceof ListNBT) {
                ServerPlayerEntity player = event.player;
                ListNBT nbtList = (ListNBT) player.getPersistentData().get("ctutilsplcmds");
                if(nbtList == null) return;
                for (INBT nbt : nbtList) {
                    if (!(nbt instanceof StringNBT)) continue;
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
                    player.getPersistentData().remove("ctutilsplcmds");
                }
            }
        }
    }
}
