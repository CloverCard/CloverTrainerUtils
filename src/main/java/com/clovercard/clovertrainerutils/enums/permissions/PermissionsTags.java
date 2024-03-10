package com.clovercard.clovertrainerutils.enums.permissions;

public enum PermissionsTags {
    PERMISSIONS_TAG("cloverperms");
    private String key;
    private PermissionsTags(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
