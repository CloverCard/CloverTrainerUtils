package com.clovercard.clovertrainerutils.listeners;

import com.clovercard.clovertrainerutils.dialogue.DialogueMaker;
import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.pixelmonmod.pixelmon.api.events.NPCChatEvent;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class ChatListener {
    @SubscribeEvent
    public void onChat(NPCChatEvent event) {
        NPCChatting npc = (NPCChatting) event.npc;
        ArrayList<String> text = npc.getChat("en-us");
        if(npc.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) {
            event.setCanceled(true);
            DialogueMaker.setChatterToDialogue((ServerPlayer) event.player, text);
        }
    }
}
