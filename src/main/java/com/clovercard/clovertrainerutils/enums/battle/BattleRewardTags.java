package com.clovercard.clovertrainerutils.enums.battle;

public enum BattleRewardTags {
    COND_WINNINGS("condwinnings");
    private String id;
    private BattleRewardTags(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
