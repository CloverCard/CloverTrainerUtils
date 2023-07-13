package com.clovercard.clovertrainerutils.enums;

public enum TrainerUtilsTags {
    MAIN_TAG("clovertrainerutils"),
    CLONE_TRAINER("clovertemptrainer");
    private String id;
    private TrainerUtilsTags(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
