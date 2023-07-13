package com.clovercard.clovertrainerutils.configs.battle;

import net.minecraft.nbt.ListNBT;

public class RewardInstance {
    private String type;
    private String data;
    private int prob;
    private int quantity;

    public RewardInstance(String type, String data, int prob, int quantity) {
        this.type = type;
        this.data = data;
        this.prob = prob;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public int getProb() {
        return prob;
    }

    public int getQuantity() {
        return quantity;
    }
}
