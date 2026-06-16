package com.qwertyx.pigrent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PigRentCommand implements CommandExecutor {
    private final PigRentPlugin plugin;

    public PigRentCommand(PigRentPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        openRentalMenu(player);
        return true;
    }

    private void openRentalMenu(Player player) {
        player.sendMessage("§6===== §ePigRent Меню §6=====");
        player.sendMessage("§e/rent short §7- 30 сек за 1000$");
        player.sendMessage("§e/rent medium §7- 1 мин за 2000$");
        player.sendMessage("§e/rent long §7- 5 мин за 5000$");
    }
}