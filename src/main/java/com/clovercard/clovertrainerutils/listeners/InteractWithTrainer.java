package com.clovercard.clovertrainerutils.listeners;

import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.clovercard.clovertrainerutils.enums.battle.BattleCommandsTypes;
import com.clovercard.clovertrainerutils.helpers.GeneralHelper;
import com.clovercard.clovertrainerutils.helpers.battle.BattleCommandsHelper;
import com.clovercard.clovertrainerutils.helpers.battle.BattleRewardsHelper;
import com.clovercard.clovertrainerutils.helpers.checkpoints.CheckpointsHelper;
import com.clovercard.clovertrainerutils.helpers.shuffler.ShufflerHelper;
import com.clovercard.clovertrainerutils.objects.requests.InteractRequest;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.entities.npcs.NPCEntity;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InteractWithTrainer {
    @SubscribeEvent
    public void onInteract(NPCEvent.Interact event) {
        PlayerEntity player = event.player;
        //Ensure only requests get through
        if (!PlayerCommandsTickListener.pendingRequests.containsKey(event.player.getUUID())) {
            if(!(event.npc instanceof NPCTrainer)) {
                BattleCommandsHelper.enqueueCommands(BattleCommandsTypes.INTERACT.getId(), (ServerPlayerEntity) event.player, event.npc);
            }
            return;
        }
        else {
            NPCEntity npc = event.npc;
            NPCTrainer trainer = null;

            if (npc instanceof NPCTrainer) trainer = (NPCTrainer) npc;

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
                case ADD_SHUFFLE:
                    if (trainer != null) res = ShufflerHelper.addShuffleTeam(trainer, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case LIST_SHUFFLE:
                    if (trainer != null) res = ShufflerHelper.listShuffleTeams(trainer);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case CLEAR_SHUFFLE:
                    if (trainer != null) res = ShufflerHelper.clearShuffleTeams(trainer);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case REMOVE_SHUFFLE:
                    if (trainer != null) res = ShufflerHelper.removeShuffleTeam(trainer, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case ADD_CHECKPOINTS:
                    if (trainer != null) res = CheckpointsHelper.addTagsToTrainer(trainer, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case ADD_CHECKPOINT_BEGINNING:
                    if (trainer != null)
                        res = CheckpointsHelper.addTagsToTrainer(trainer, new String[]{null, req.getArgs()});
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case ADD_CHECKPOINT_END:
                    if (trainer != null)
                        res = CheckpointsHelper.addTagsToTrainer(trainer, new String[]{req.getArgs(), null});
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case LIST_CHECKPOINT:
                    if (trainer != null) res = CheckpointsHelper.listTagsFromTrainer(trainer);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case CLEAR_CHECKPOINTS:
                    if (trainer != null) res = CheckpointsHelper.removeTagsFromTrainer(trainer);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case ADD_COND_DROP:
                    if (trainer != null) res = BattleRewardsHelper.addSingleDrop(trainer, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case ADD_COND_DROP_PRESET:
                    if (trainer != null) res = BattleRewardsHelper.addPresetDrops(trainer, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case LIST_COND_DROP:
                    if (trainer != null) res = BattleRewardsHelper.listConditionalDrops(trainer);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case REMOVE_COND_DROP:
                    if (trainer != null) res = BattleRewardsHelper.removeSingleDrop(trainer, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case CLEAR_COND_DROP:
                    if (trainer != null) res = BattleRewardsHelper.clearDrops(trainer);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case ADD_START_COMMAND:
                    if (trainer != null) res = BattleCommandsHelper.addStartCommand(npc, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case ADD_FORFEIT_COMMAND:
                    if (trainer != null) res = BattleCommandsHelper.addForfeitCommand(npc, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case ADD_PLAYER_WIN_COMMAND:
                    if (trainer != null) res = BattleCommandsHelper.addPlayerWinsCommand(npc, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case ADD_PLAYER_LOSS_COMMAND:
                    if (trainer != null) res = BattleCommandsHelper.addPlayerLosesCommand(npc, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case ADD_INTERACT_COMMAND:
                    if(trainer == null) res = BattleCommandsHelper.addInteractCommand(npc, req.getSplitArgs());
                    else res = "This cannot work on a trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case REMOVE_START_COMMAND:
                    if (trainer != null) res = BattleCommandsHelper.removeStartCommand(npc, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case REMOVE_FORFEIT_COMMAND:
                    if (trainer != null) res = BattleCommandsHelper.removeForfeitCommand(npc, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case REMOVE_PLAYER_LOSS_COMMAND:
                    if (trainer != null) res = BattleCommandsHelper.removePlayerLosesCommand(npc, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case REMOVE_PLAYER_WIN_COMMAND:
                    if (trainer != null) res = BattleCommandsHelper.removePlayerWinsCommand(npc, req.getSplitArgs());
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case REMOVE_INTERACT_COMMAND:
                    if(trainer == null) res = BattleCommandsHelper.removeInteractCommand(npc, req.getSplitArgs());
                    else res = "This cannot work on a trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case LIST_START_COMMANDS:
                    if (trainer != null) res = BattleCommandsHelper.listStartCommands(npc);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case LIST_FORFEIT_COMMANDS:
                    if (trainer != null) res = BattleCommandsHelper.listForfeitCommands(npc);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case LIST_LOSS_COMMANDS:
                    if (trainer != null) res = BattleCommandsHelper.listPlayerLossCommands(npc);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case LIST_PLAYER_WIN_COMMANDS:
                    if (trainer != null) res = BattleCommandsHelper.listPlayerWinsCommands(npc);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case LIST_INTERACT_COMMANDS:
                    if(trainer == null) res = BattleCommandsHelper.listInteractCommands(npc);
                    else res = "This cannot work on a trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case CLEAR_FORFEIT_COMMANDS:
                    if (trainer != null) res = BattleCommandsHelper.clearForfeitCommands(npc);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case CLEAR_START_COMMANDS:
                    if (trainer != null) res = BattleCommandsHelper.clearStartCommands(npc);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case CLEAR_PLAYER_WIN_COMMANDS:
                    if (trainer != null) res = BattleCommandsHelper.clearPlayerWinCommands(npc);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case CLEAR_PLAYER_LOSS_COMMANDS:
                    if (trainer != null) res = BattleCommandsHelper.clearPlayerLossCommands(npc);
                    else res = "This cannot work on a non-trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
                case CLEAR_INTERACT_COMMANDS:
                    if(trainer == null) res = BattleCommandsHelper.clearInteractCommands(npc);
                    else res = "This cannot work on a trainer npc!";
                    player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                    break;
            }
            PlayerCommandsTickListener.pendingRequests.remove(player.getUUID());
        }
    }
}
