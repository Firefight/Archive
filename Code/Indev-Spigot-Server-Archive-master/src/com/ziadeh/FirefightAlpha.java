package com.ziadeh;

import com.ziadeh.commands.SetSpawnCommand;
import com.ziadeh.commands.ShopCommand;
import com.ziadeh.config.Configuration;
import com.ziadeh.listeners.PlayerDeath;
import com.ziadeh.listeners.PlayerRespawn;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Brennan on 11/1/2019.
 */

public class FirefightAlpha extends JavaPlugin {

    private static FirefightAlpha instance;

    private Configuration configuration;

    private static Economy economy;

    public void onEnable() {

        // Ensure Vault economy is setup properly.
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

        instance = this;

        configuration = new Configuration();

        registerCommands();

        registerListeners();
    }

    /**
     * Registers commands.
     */
    public void registerCommands() {
        new SetSpawnCommand("sp", "sp.*");
        new ShopCommand("shop", "shop.access");
    }

    /**
     * Registers listeners.
     */
    public void registerListeners() {
        new PlayerRespawn();
        new PlayerDeath();
    }

    /**
     * Sets up vault economy.
     *
     * @return whether or not setup was successful.
     */
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    /**
     * Registers the specified listener.
     *
     * @param listener The listener being registered.
     */
    public void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    /**
     * Unregisters the specified listener.
     *
     * @param listener The listener being unregistered.
     */
    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Gets instance of the main class.
     *
     * @return instance of main class.
     */
    public static FirefightAlpha getInstance() {
        return instance;
    }

    /**
     * Gets the configuration management class.
     *
     * @return the configuration.
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Gets Vault economy.
     *
     * @return economy.
     */
    public static Economy getEconomy() {
        return economy;
    }
}
