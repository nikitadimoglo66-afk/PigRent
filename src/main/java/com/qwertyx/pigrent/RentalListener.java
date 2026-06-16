package com.qwertyx.pigrent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

public class RentalListener implements Listener {
    private final PigRentPlugin plugin;

    public RentalListener(PigRentPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        RentedPig rentedPig = plugin.getPigManager().getRentedPigs().get(player.getUniqueId());
        
        if (rentedPig != null) {
            rentedPig.getPig().remove();
            plugin.getPigManager().getRentedPigs().remove(player.getUniqueId());
        }
    }
}