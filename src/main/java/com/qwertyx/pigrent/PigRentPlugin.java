package com.qwertyx.pigrent;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PigRentPlugin extends JavaPlugin {
    private static Economy economy;
    private PigManager pigManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Setup Vault economy
        if (!setupEconomy()) {
            getLogger().severe("Vault not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        pigManager = new PigManager(this);

        // Register commands
        getCommand("pigrent").setExecutor(new PigRentCommand(this));
        getCommand("parkpig").setExecutor(new ParkCommand(this));
        getCommand("setpark").setExecutor(new SetParkCommand(this));
        getCommand("rent").setExecutor(new RentCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new RentalListener(this), this);

        getLogger().info("PigRent plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PigRent plugin disabled!");
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public PigManager getPigManager() {
        return pigManager;
    }
}