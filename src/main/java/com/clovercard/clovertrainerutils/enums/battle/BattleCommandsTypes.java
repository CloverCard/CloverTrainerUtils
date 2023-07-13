package com.clovercard.clovertrainerutils.enums.battle;

public enum BattleCommandsTypes {
    START_BATTLE_COMMANDS("cloverbattlestart"),
    FORFEIT_BATTLE_COMMANDS("cloverbattleforfeit"),
    PLAYER_WINS("cloverplayerwin"),
    PLAYER_LOSS("cloverplayerloss");
    private String id;
    private BattleCommandsTypes(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public static boolean contains(String id){
        for(BattleCommandsTypes type: values()) {
            if(type.id.equals(id)) return true;
        }
        return false;
    }
}
