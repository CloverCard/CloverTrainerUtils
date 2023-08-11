package com.clovercard.clovertrainerutils.listeners;

import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.clovercard.clovertrainerutils.enums.battle.BattleCommandsTypes;
import com.clovercard.clovertrainerutils.helpers.GeneralHelper;
import com.clovercard.clovertrainerutils.helpers.battle.BattleCommandsHelper;
import com.clovercard.clovertrainerutils.objects.requests.InteractRequest;
import com.pixelmonmod.pixelmon.entities.pixelmon.StatueEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StatueInteract {
    @SubscribeEvent
    public void onInteractStatue(PlayerInteractEvent.EntityInteract event){
        if(!(event.getTarget() instanceof StatueEntity)) return;
        if(event.getHand().equals(Hand.OFF_HAND)) return;
        PlayerEntity player = event.getPlayer();
        //Ensure only requests get through
        if (!PlayerCommandsTickListener.pendingRequests.containsKey(player.getUUID())) {
            BattleCommandsHelper.enqueueCommands(BattleCommandsTypes.INTERACT.getId(), (ServerPlayerEntity) player, event.getTarget());
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
                        res = "This trainer has already been initialized!";
                    } else {
                        res = "Initialized trainer! It may now be customized!";
                        GeneralHelper.initUtilsNbt(npc);
                    }
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case RESET_TRAINER:
                    if (npc.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) {
                        npc.getPersistentData().remove(TrainerUtilsTags.MAIN_TAG.getId());
                        res = "This trainer's data has been cleared!";
                    } else {
                        res = "This trainer has not been initialized, so there is no data to clear!";
                        GeneralHelper.initUtilsNbt(npc);
                    }
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case ADD_INTERACT_COMMAND:
                    res = BattleCommandsHelper.addInteractCommand(npc, req.getSplitArgs());
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case REMOVE_INTERACT_COMMAND:
                    res = BattleCommandsHelper.removeInteractCommand(npc, req.getSplitArgs());
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case LIST_INTERACT_COMMANDS:
                    res = BattleCommandsHelper.listInteractCommands(npc);
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case CLEAR_INTERACT_COMMANDS:
                    res = BattleCommandsHelper.clearInteractCommands(npc);
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                default:
                    player.sendMessage(new StringTextComponent("Statues may only have interaction commands!"), Util.NIL_UUID);
            }
            PlayerCommandsTickListener.pendingRequests.remove(player.getUUID());
        }
    }
}
