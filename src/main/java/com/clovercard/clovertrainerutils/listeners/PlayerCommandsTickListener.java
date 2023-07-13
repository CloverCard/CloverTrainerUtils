package com.clovercard.clovertrainerutils.listeners;

import com.clovercard.clovertrainerutils.objects.requests.InteractRequest;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerCommandsTickListener {
    public static HashMap<UUID, InteractRequest> pendingRequests = new HashMap<>();
    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        if(event.phase.equals(TickEvent.Phase.START)){
            return;
        }
        List<UUID> toRemove = new ArrayList<>();
        //Manages time before an edit request to a trainer expires
        pendingRequests.forEach((uuid, req) -> {
            int time = req.getTimeout() - 1;
            req.setTimeout(time);
            if(time <= 0) {
                toRemove.add(uuid);
                ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
                if(player != null) player.sendMessage(new StringTextComponent("You're request has expired!"), Util.NIL_UUID);
            }
        });
        toRemove.forEach(removed -> pendingRequests.remove(removed));
    }
}
