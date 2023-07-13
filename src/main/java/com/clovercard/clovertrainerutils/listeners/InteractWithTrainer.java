package com.clovercard.clovertrainerutils.listeners;

import com.clovercard.clovertrainerutils.enums.TrainerUtilsTags;
import com.clovercard.clovertrainerutils.helpers.GeneralHelper;
import com.clovercard.clovertrainerutils.helpers.battle.BattleCommandsHelper;
import com.clovercard.clovertrainerutils.helpers.battle.BattleRewardsHelper;
import com.clovercard.clovertrainerutils.helpers.checkpoints.CheckpointsHelper;
import com.clovercard.clovertrainerutils.helpers.shuffler.ShufflerHelper;
import com.clovercard.clovertrainerutils.objects.requests.InteractRequest;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InteractWithTrainer {
    @SubscribeEvent
    public void onInteract(NPCEvent.Interact event) {
        //Ensure only requests get through
        if(!event.type.equals(EnumNPCType.Trainer)) return;
        if(!PlayerCommandsTickListener.pendingRequests.containsKey(event.player.getUUID())) return;

        PlayerEntity player = event.player;
        NPCTrainer trainer = (NPCTrainer) event.npc;

        //Manage Request
        InteractRequest req = PlayerCommandsTickListener.pendingRequests.get(player.getUUID());
        String res;
        switch(req.getTag()){
            case INIT_TRAINER:
                if(trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) {
                    res = "This trainer has already been initialized!";
                }
                else {
                    res = "Initialized trainer! It may now be customized!";
                    GeneralHelper.initUtilsNbt(trainer);
                }
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case RESET_TRAINER:
                if(trainer.getPersistentData().contains(TrainerUtilsTags.MAIN_TAG.getId())) {
                    trainer.getPersistentData().remove(TrainerUtilsTags.MAIN_TAG.getId());
                    res = "This trainer's data has been cleared!";
                }
                else {
                    res = "This trainer has not been initialized, so there is no data to clear!";
                    GeneralHelper.initUtilsNbt(trainer);
                }
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case ADD_SHUFFLE:
                res = ShufflerHelper.addShuffleTeam(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case LIST_SHUFFLE:
                res = ShufflerHelper.listShuffleTeams(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case CLEAR_SHUFFLE:
                res = ShufflerHelper.clearShuffleTeams(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case REMOVE_SHUFFLE:
                res = ShufflerHelper.removeShuffleTeam(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case ADD_CHECKPOINTS:
                res = CheckpointsHelper.addTagsToTrainer(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case ADD_CHECKPOINT_BEGINNING:
                res = CheckpointsHelper.addTagsToTrainer(trainer, new String[] {null, req.getArgs()});
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case ADD_CHECKPOINT_END:
                res = CheckpointsHelper.addTagsToTrainer(trainer, new String[] {req.getArgs(), null});
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case LIST_CHECKPOINT:
                res = CheckpointsHelper.listTagsFromTrainer(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case CLEAR_CHECKPOINTS:
                res = CheckpointsHelper.removeTagsFromTrainer(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case ADD_COND_DROP:
                res = BattleRewardsHelper.addSingleDrop(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case ADD_COND_DROP_PRESET:
                res = BattleRewardsHelper.addPresetDrops(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case LIST_COND_DROP:
                res = BattleRewardsHelper.listConditionalDrops(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case REMOVE_COND_DROP:
                res = BattleRewardsHelper.removeSingleDrop(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case CLEAR_COND_DROP:
                res = BattleRewardsHelper.clearDrops(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case ADD_START_COMMAND:
                res = BattleCommandsHelper.addStartCommand(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case ADD_FORFEIT_COMMAND:
                res = BattleCommandsHelper.addForfeitCommand(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case ADD_PLAYER_WIN_COMMAND:
                res = BattleCommandsHelper.addPlayerWinsCommand(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case ADD_PLAYER_LOSS_COMMAND:
                res = BattleCommandsHelper.addPlayerLosesCommand(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case REMOVE_START_COMMAND:
                res = BattleCommandsHelper.removeStartCommand(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case REMOVE_FORFEIT_COMMAND:
                res = BattleCommandsHelper.removeForfeitCommand(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case REMOVE_PLAYER_LOSS_COMMAND:
                res = BattleCommandsHelper.removePlayerLosesCommand(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case REMOVE_PLAYER_WIN_COMMAND:
                res = BattleCommandsHelper.removePlayerWinsCommand(trainer, req.getSplitArgs());
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case LIST_START_COMMANDS:
                res = BattleCommandsHelper.listStartCommands(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case LIST_FORFEIT_COMMANDS:
                res = BattleCommandsHelper.listForfeitCommands(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case LIST_LOSS_COMMANDS:
                res = BattleCommandsHelper.listPlayerLossCommands(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case LIST_PLAYER_WIN_COMMANDS:
                res = BattleCommandsHelper.listPlayerWinsCommands(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case CLEAR_FORFEIT_COMMANDS:
                res = BattleCommandsHelper.clearForfeitCommands(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case CLEAR_START_COMMANDS:
                res = BattleCommandsHelper.clearStartCommands(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case CLEAR_PLAYER_WIN_COMMANDS:
                res = BattleCommandsHelper.clearPlayerWinCommands(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
            case CLEAR_PLAYER_LOSS_COMMANDS:
                res = BattleCommandsHelper.clearPlayerLossCommands(trainer);
                player.sendMessage(new StringTextComponent(res), Util.NIL_UUID);
                break;
        }
        PlayerCommandsTickListener.pendingRequests.remove(player.getUUID());
    }
}
