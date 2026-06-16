package com.qwertyx.pigrent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RentCommand implements CommandExecutor {
    private final PigRentPlugin plugin;

    public RentCommand(PigRentPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§6===== §ePigRent Меню §6=====");
            player.sendMessage("§e/rent short §7- 30 сек за 1000$");
            player.sendMessage("§e/rent medium §7- 1 мин за 2000$");
            player.sendMessage("§e/rent long §7- 5 мин за 5000$");
            return true;
        }

        String type = args[0].toLowerCase();

        switch (type) {
            case "short":
                int durationShort = plugin.getConfig().getInt("rentals.short.duration");
                double priceShort = plugin.getConfig().getDouble("rentals.short.price");
                plugin.getPigManager().rentPig(player, durationShort, priceShort);
                break;
            case "medium":
                int durationMedium = plugin.getConfig().getInt("rentals.medium.duration");
                double priceMedium = plugin.getConfig().getDouble("rentals.medium.price");
                plugin.getPigManager().rentPig(player, durationMedium, priceMedium);
                break;
            case "long":
                int durationLong = plugin.getConfig().getInt("rentals.long.duration");
                double priceLong = plugin.getConfig().getDouble("rentals.long.price");
                plugin.getPigManager().rentPig(player, durationLong, priceLong);
                break;
            default:
                player.sendMessage("§cНеизвестный тип аренды!");
        }

        return true;
    }
}