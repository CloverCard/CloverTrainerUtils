package com.clovercard.clovertrainerutils.listeners;

import com.clovercard.clovertrainerutils.objects.requests.BattleCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BattleCommandsTickQueue {
    private static Queue<BattleCommand> commandQueue = new LinkedList<>();
    private static LinkedList<BattleCommand> delayHolder = new LinkedList<>();
    public static void addToHolder(BattleCommand cmdData) {
        delayHolder.addLast(cmdData);
    }
    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        if(event.phase.equals(TickEvent.Phase.START)) return;
        //Run commands as soon as they are added
        while(!commandQueue.isEmpty()) {
            BattleCommand cmdData = commandQueue.poll();
            String cmd = cmdData.getCommand();
            ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(cmdData.getUser());
            if(player == null) ServerLifecycleHooks.getCurrentServer().getCommands().performPrefixedCommand(ServerLifecycleHooks.getCurrentServer().createCommandSourceStack(), cmd);
            else ServerLifecycleHooks.getCurrentServer().getCommands().performPrefixedCommand(player.createCommandSourceStack(), cmd);
        }
        //Handle delayed commands
        if(delayHolder.isEmpty()) return;
        ArrayList<BattleCommand> toRemove = new ArrayList<>();
        for(BattleCommand cmd: delayHolder) {
            int delay = cmd.getDelay();
            cmd.setDelay(delay - 1);
            if(delay <= 0) {
                toRemove.add(cmd);
            }
        }
        toRemove.forEach(removed -> {
            commandQueue.add(removed);
            delayHolder.remove(removed);
        });
    }
}
