package com.clovercard.clovertrainerutils.enums.shuffler;

public enum ShufflerTags {
    SHUFFLING_TRAINER("clovershufflingtrainer");
    private String id;
    private ShufflerTags(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
