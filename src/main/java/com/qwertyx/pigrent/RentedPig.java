package com.qwertyx.pigrent;

import org.bukkit.entity.Pig;

import java.util.UUID;

public class RentedPig {
    private final UUID playerId;
    private final Pig pig;
    private final int durationSeconds;
    private final double price;
    private final long rentStartTime;

    public RentedPig(UUID playerId, Pig pig, int durationSeconds, double price) {
        this.playerId = playerId;
        this.pig = pig;
        this.durationSeconds = durationSeconds;
        this.price = price;
        this.rentStartTime = System.currentTimeMillis();
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Pig getPig() {
        return pig;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public double getPrice() {
        return price;
    }

    public long getRentStartTime() {
        return rentStartTime;
    }

    public boolean isExpired() {
        long elapsedSeconds = (System.currentTimeMillis() - rentStartTime) / 1000;
        return elapsedSeconds >= durationSeconds;
    }
}