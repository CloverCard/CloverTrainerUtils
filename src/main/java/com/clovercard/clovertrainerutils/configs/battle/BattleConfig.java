package com.clovercard.clovertrainerutils.configs.battle;

import java.util.HashMap;
import java.util.List;

public class BattleConfig {
    public static BattleConfig battleConfig = new BattleConfig(new HashMap<String, List<RewardInstance>>());
    private HashMap<String, List<RewardInstance>> condRewardsList;
    private BattleConfig(HashMap<String, List<RewardInstance>> condRewardsList) {
        this.condRewardsList = new HashMap<>();
    }
    public HashMap<String, List<RewardInstance>> getRewardsList() {
        return this.condRewardsList;
    }
}
