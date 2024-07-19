package com.clovercard.clovertrainerutils.listeners;

import com.clovercard.clovertrainerutils.dialogue.DialogueMaker;
import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.pixelmonmod.pixelmon.api.events.NPCChatEvent;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.entities.npcs.NPCFisherman;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class ChatListener {
    @SubscribeEvent
    public void onChat(NPCChatEvent event) {
        if(event.npc instanceof NPCFisherman) return;
        if(!(event.player instanceof ServerPlayerEntity)) return;
        if(!(event.npc instanceof NPCChatting)) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        NPCChatting npc = (NPCChatting) event.npc;
        if(npc.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) {
            ArrayList<String> text = npc.getChat(player.getLanguage());
            event.setCanceled(true);
            DialogueMaker.setChatterToDialogue((ServerPlayerEntity) event.player, text);
        }
    }
}
