package com.qwertyx.pigrent;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class PigManager {
    private final PigRentPlugin plugin;
    private final Map<UUID, RentedPig> rentedPigs = new HashMap<>();
    private final Map<UUID, BukkitTask> warningTasks = new HashMap<>();

    public PigManager(PigRentPlugin plugin) {
        this.plugin = plugin;
    }

    public void rentPig(Player player, int durationSeconds, double price) {
        Economy economy = PigRentPlugin.getEconomy();

        // Check money
        if (!economy.has(player, price)) {
            player.sendMessage(plugin.getConfig().getString("messages.prefix") + 
                "§cУ тебя недостаточно денег! Нужно: $" + price);
            return;
        }

        // Withdraw money
        economy.withdrawPlayer(player, price);

        // Spawn pig
        Pig pig = player.getWorld().spawn(player.getLocation(), Pig.class);
        pig.setCustomName("§6[АРЕНДА] " + player.getName());
        pig.setCustomNameVisible(true);

        // Store rental info
        RentedPig rentedPig = new RentedPig(player.getUniqueId(), pig, durationSeconds, price);
        rentedPigs.put(player.getUniqueId(), rentedPig);

        player.sendMessage(plugin.getConfig().getString("messages.prefix") + 
            plugin.getConfig().getString("messages.pig-spawned"));

        // Start timer for warning
        startWarningTimer(player, durationSeconds);

        // Start timer for expiration
        startExpirationTimer(player, durationSeconds);
    }

    private void startWarningTimer(Player player, int durationSeconds) {
        int warningTime = plugin.getConfig().getInt("warning-time");
        int delayTicks = (durationSeconds - warningTime) * 20;

        if (delayTicks < 0) return;

        BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (rentedPigs.containsKey(player.getUniqueId())) {
                player.sendMessage(plugin.getConfig().getString("messages.prefix") + 
                    plugin.getConfig().getString("messages.warning")
                        .replace("%time%", String.valueOf(warningTime)));
            }
        }, delayTicks);

        warningTasks.put(player.getUniqueId(), task);
    }

    private void startExpirationTimer(Player player, int durationSeconds) {
        int delayTicks = durationSeconds * 20;

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            RentedPig rentedPig = rentedPigs.get(player.getUniqueId());
            if (rentedPig == null) return;

            // Check if pig is in parking zone
            if (isPigInParking(rentedPig.getPig())) {
                player.sendMessage(plugin.getConfig().getString("messages.prefix") + 
                    plugin.getConfig().getString("messages.pig-parked"));
                
                // Pay for the ride
                player.sendMessage(plugin.getConfig().getString("messages.prefix") + 
                    plugin.getConfig().getString("messages.parking-expired")
                        .replace("%price%", String.valueOf(rentedPig.getPrice())));
                
                // Remove pig
                rentedPig.getPig().remove();
                rentedPigs.remove(player.getUniqueId());
            } else {
                // Pig not parked, charge extra
                player.sendMessage(plugin.getConfig().getString("messages.prefix") + 
                    plugin.getConfig().getString("messages.not-in-parking"));
                
                rentedPig.getPig().remove();
                rentedPigs.remove(player.getUniqueId());
            }

            // Cancel warning task
            BukkitTask warningTask = warningTasks.remove(player.getUniqueId());
            if (warningTask != null) {
                warningTask.cancel();
            }
        }, delayTicks);
    }

    private boolean isPigInParking(Pig pig) {
        int x = pig.getLocation().getBlockX();
        int y = pig.getLocation().getBlockY();
        int z = pig.getLocation().getBlockZ();

        int x1 = plugin.getConfig().getInt("parking.x1");
        int y1 = plugin.getConfig().getInt("parking.y1");
        int z1 = plugin.getConfig().getInt("parking.z1");
        int x2 = plugin.getConfig().getInt("parking.x2");
        int y2 = plugin.getConfig().getInt("parking.y2");
        int z2 = plugin.getConfig().getInt("parking.z2");

        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    public Map<UUID, RentedPig> getRentedPigs() {
        return rentedPigs;
    }

    public void showParkingZone(Player player) {
        int x1 = plugin.getConfig().getInt("parking.x1");
        int y1 = plugin.getConfig().getInt("parking.y1");
        int z1 = plugin.getConfig().getInt("parking.z1");
        int x2 = plugin.getConfig().getInt("parking.x2");
        int y2 = plugin.getConfig().getInt("parking.y2");
        int z2 = plugin.getConfig().getInt("parking.z2");

        player.sendMessage(plugin.getConfig().getString("messages.prefix") + 
            "§eЗона парковки: §6(" + x1 + ", " + y1 + ", " + z1 + ") - (" + x2 + ", " + y2 + ", " + z2 + ")");
    }
}