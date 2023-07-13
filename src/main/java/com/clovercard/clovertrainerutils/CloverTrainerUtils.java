package com.clovercard.clovertrainerutils;

import com.clovercard.clovertrainerutils.commands.TUtils;
import com.clovercard.clovertrainerutils.configs.battle.RewardPresetConfig;
import com.clovercard.clovertrainerutils.configs.shuffler.MemberConfig;
import com.clovercard.clovertrainerutils.configs.shuffler.TeamConfig;
import com.clovercard.clovertrainerutils.listeners.BattleCommandsTickQueue;
import com.clovercard.clovertrainerutils.listeners.BattleListeners;
import com.clovercard.clovertrainerutils.listeners.InteractWithTrainer;
import com.clovercard.clovertrainerutils.listeners.PlayerCommandsTickListener;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CloverTrainerUtils.MODID)
@Mod.EventBusSubscriber(modid = CloverTrainerUtils.MODID)
public class CloverTrainerUtils {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "clovertrainerutils";

    public CloverTrainerUtils() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new BattleCommandsTickQueue());
        Pixelmon.EVENT_BUS.register(new BattleListeners());
        Pixelmon.EVENT_BUS.register(new InteractWithTrainer());
        MinecraftForge.EVENT_BUS.register(new PlayerCommandsTickListener());
        MemberConfig.DATA.load();
        TeamConfig.DATA.load();
        RewardPresetConfig.DATA.load();
    }
    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        new TUtils(event.getDispatcher());
    }
}
