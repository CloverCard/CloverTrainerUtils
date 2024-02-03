package com.clovercard.clovertrainerutils.dialogue;

import com.pixelmonmod.pixelmon.api.dialogue.Choice;
import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue.DialogueNextActionPacket;
import net.minecraft.server.level.ServerPlayer;


import java.util.ArrayList;
import java.util.Iterator;

public class DialogueMaker {
    public static Dialogue createDialogue(Iterator<String> text) {
        if(text.hasNext()) {
            Dialogue dialogue = new Dialogue.DialogueBuilder()
                    .setText(text.next())
                            .addChoice(
                                    new Choice.ChoiceBuilder()
                                            .setText("-->")
                                            .setHandle(a -> {
                                                if(text.hasNext()) a.reply(createDialogue(text));
                                                else a.setAction(DialogueNextActionPacket.DialogueGuiAction.CLOSE);
                                            })
                                            .build()
                            )
                    .build();
            return dialogue;
        }
        return null;
    }
    public static void setChatterToDialogue(ServerPlayer player, ArrayList<String> message) {
        Iterator<String> text = message.iterator();
        Dialogue dialogue = createDialogue(text);
        if(dialogue == null) return;
        ArrayList<Dialogue> dialogues = new ArrayList<>();
        dialogues.add(dialogue);
        Dialogue.setPlayerDialogueData(player, dialogues, true);
    }
}
