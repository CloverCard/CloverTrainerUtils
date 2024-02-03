package com.clovercard.clovertrainerutils.listeners;

import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.clovercard.clovertrainerutils.enums.battle.BattleCommandsTypes;
import com.clovercard.clovertrainerutils.helpers.GeneralHelper;
import com.clovercard.clovertrainerutils.helpers.battle.BattleCommandsHelper;
import com.clovercard.clovertrainerutils.objects.requests.InteractRequest;
import com.clovercard.clovertrainerutils.utils.MessageUtils;
import com.pixelmonmod.pixelmon.entities.pixelmon.StatueEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StatueInteract {
    @SubscribeEvent
    public void onInteractStatue(PlayerInteractEvent.EntityInteract event){
        if(!(event.getTarget() instanceof StatueEntity)) return;
        if(event.getHand().equals(InteractionHand.OFF_HAND)) return;
        Player player = event.getEntity();
        //Ensure only requests get through
        if (!PlayerCommandsTickListener.pendingRequests.containsKey(player.getUUID())) {
            BattleCommandsHelper.enqueueCommands(BattleCommandsTypes.INTERACT.getId(), (ServerPlayer) player, event.getTarget());
            return;
        }
        else {
            StatueEntity npc = (StatueEntity) event.getTarget();
            //Manage Request
            InteractRequest req = PlayerCommandsTickListener.pendingRequests.get(player.getUUID());
            String res;
            switch (req.getTag()) {
                case INIT_TRAINER:
                    if (npc.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) {
                        res = "ctu.init.error";
                    } else {
                        res = "ctu.init.success";
                        GeneralHelper.initUtilsNbt(npc);
                    }
                    MessageUtils.sendTranslatableMessage(player, res);
                    break;
                case RESET_TRAINER:
                    if (npc.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) {
                        npc.getPersistentData().remove(TrainerUtilsTags.MAIN_TAG.getId());
                        res = "ctu.clear.success";
                    } else {
                        res = "ctu.clear.error";
                        GeneralHelper.initUtilsNbt(npc);
                    }
                    MessageUtils.sendTranslatableMessage(player, res);
                    break;
                case ADD_INTERACT_COMMAND:
                    res = BattleCommandsHelper.addInteractCommand(npc, req.getSplitArgs());
                    MessageUtils.sendTranslatableMessage(player, res);
                    break;
                case REMOVE_INTERACT_COMMAND:
                    res = BattleCommandsHelper.removeInteractCommand(npc, req.getSplitArgs());
                    MessageUtils.sendTranslatableMessage(player, res);
                    break;
                case LIST_INTERACT_COMMANDS:
                    res = BattleCommandsHelper.listInteractCommands(npc);
                    MessageUtils.sendTranslatableMessage(player, res);
                    break;
                case CLEAR_INTERACT_COMMANDS:
                    res = BattleCommandsHelper.clearInteractCommands(npc);
                    MessageUtils.sendTranslatableMessage(player, res);
                    break;
                default:
                    MessageUtils.sendTranslatableMessage(player, "ctu.error.statue.noninteract");
            }
            PlayerCommandsTickListener.pendingRequests.remove(player.getUUID());
        }
    }
}
