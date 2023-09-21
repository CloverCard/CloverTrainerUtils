package com.clovercard.clovertrainerutils.listeners;

import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.clovercard.clovertrainerutils.enums.battle.BattleCommandsTypes;
import com.clovercard.clovertrainerutils.helpers.GeneralHelper;
import com.clovercard.clovertrainerutils.helpers.battle.BattleCommandsHelper;
import com.clovercard.clovertrainerutils.objects.requests.InteractRequest;
import com.pixelmonmod.pixelmon.entities.pixelmon.StatueEntity;
import net.minecraft.network.chat.Component;
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
                        res = "This trainer has already been initialized!";
                    } else {
                        res = "Initialized trainer! It may now be customized!";
                        GeneralHelper.initUtilsNbt(npc);
                    }
                    player.sendSystemMessage(Component.literal(res));
                    break;
                case RESET_TRAINER:
                    if (npc.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) {
                        npc.getPersistentData().remove(TrainerUtilsTags.MAIN_TAG.getId());
                        res = "This trainer's data has been cleared!";
                    } else {
                        res = "This trainer has not been initialized, so there is no data to clear!";
                        GeneralHelper.initUtilsNbt(npc);
                    }
                    player.sendSystemMessage(Component.literal(res));
                    break;
                case ADD_INTERACT_COMMAND:
                    res = BattleCommandsHelper.addInteractCommand(npc, req.getSplitArgs());
                    player.sendSystemMessage(Component.literal(res));
                    break;
                case REMOVE_INTERACT_COMMAND:
                    res = BattleCommandsHelper.removeInteractCommand(npc, req.getSplitArgs());
                    player.sendSystemMessage(Component.literal(res));
                    break;
                case LIST_INTERACT_COMMANDS:
                    res = BattleCommandsHelper.listInteractCommands(npc);
                    player.sendSystemMessage(Component.literal(res));
                    break;
                case CLEAR_INTERACT_COMMANDS:
                    res = BattleCommandsHelper.clearInteractCommands(npc);
                    player.sendSystemMessage(Component.literal(res));
                    break;
                default:
                    player.sendSystemMessage(Component.literal("Statues may only have interaction commands!"));
            }
            PlayerCommandsTickListener.pendingRequests.remove(player.getUUID());
        }
    }
}
