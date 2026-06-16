package com.qwertyx.pigrent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetParkCommand implements CommandExecutor {
    private final PigRentPlugin plugin;

    public SetParkCommand(PigRentPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (!player.hasPermission("pigrent.admin")) {
            player.sendMessage("§cНет прав!");
            return true;
        }

        if (args.length != 6) {
            player.sendMessage("§eИспользование: /setpark <x1> <y1> <z1> <x2> <y2> <z2>");
            return true;
        }

        try {
            int x1 = Integer.parseInt(args[0]);
            int y1 = Integer.parseInt(args[1]);
            int z1 = Integer.parseInt(args[2]);
            int x2 = Integer.parseInt(args[3]);
            int y2 = Integer.parseInt(args[4]);
            int z2 = Integer.parseInt(args[5]);

            plugin.getConfig().set("parking.x1", x1);
            plugin.getConfig().set("parking.y1", y1);
            plugin.getConfig().set("parking.z1", z1);
            plugin.getConfig().set("parking.x2", x2);
            plugin.getConfig().set("parking.y2", y2);
            plugin.getConfig().set("parking.z2", z2);
            plugin.saveConfig();

            player.sendMessage("§a✓ Зона парковки установлена!");
            player.sendMessage("§6(" + x1 + ", " + y1 + ", " + z1 + ") - (" + x2 + ", " + y2 + ", " + z2 + ")");

        } catch (NumberFormatException e) {
            player.sendMessage("§cОшибка! Координаты должны быть числами!");
        }

        return true;
    }
}