package com.clovercard.clovertrainerutils.objects.requests;

import java.util.UUID;

public class BattleCommand {
    private int delay;
    private String command;
    private UUID user;

    public BattleCommand(int delay, String command, UUID user) {
        this.delay = delay;
        this.command = command;
        this.user = user;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }
}
