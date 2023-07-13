package com.clovercard.clovertrainerutils.enums.checkpoints;

public enum CheckpointTags {
    CHECKPOINT_TAG("clovercheckpoint"),
    NEXT_BATTLE_TAG("clovernextcheckpoint");
    private String key;
    private CheckpointTags(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
