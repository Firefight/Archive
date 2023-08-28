package com.ziadeh.config;

import com.sun.corba.se.spi.orb.ORBData;
import com.ziadeh.FirefightAlpha;
import network.aeternum.ordnance.core.Ordnance;
import network.aeternum.ordnance.core.WeaponManager;
import network.aeternum.ordnance.weapons.Weapon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Brennan on 11/1/2019.
 */

public class Configuration {

    private FileConfiguration spawnConfig, pricesConfig;

    private File spawnFile, pricesFile;

    private static List<Location> spawnPoints;

    public static Map<String, Integer> gunPrices;

    static {
        spawnPoints = new ArrayList<>();
        gunPrices = new HashMap<>();
    }

    public Configuration() {
        TL.load();

        File dataFolder = FirefightAlpha.getInstance().getDataFolder();

        if(!dataFolder.exists())
            dataFolder.mkdirs();

        new Thread(() -> {
            loadSpawnConfig(); // load and cache spawn points...
            loadPricesConfig(); // load and cache gun prices...
        }).start();
    }

    /**
     * Loads or creates the prices configuration file. Then caches
     * the prices that are inside.
     */
    private void loadPricesConfig() {
        pricesFile = new File(FirefightAlpha.getInstance().getDataFolder(), "prices.yml");

        boolean addWeaponPrices = false;

        if(!pricesFile.exists()) {
            try {
                pricesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                addWeaponPrices = true;
            }
        }

        pricesConfig = YamlConfiguration.loadConfiguration(pricesFile);

        // Add default weapon prices...
        if(addWeaponPrices) {
            Ordnance.getWeapons().values()
                    .forEach(weapon -> pricesConfig.set(weapon.getName(), 100));

            try {
                pricesConfig.save(pricesFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load the weapon prices...
        pricesConfig.getConfigurationSection("").getKeys(false).forEach(key -> gunPrices.put(key, pricesConfig.getInt(key)));
    }

    /**
     * Loads or creates the spawns.yml configuration file. Then caches
     * the spawn points that are inside.
     */
    private void loadSpawnConfig() {
        spawnFile = new File(FirefightAlpha.getInstance().getDataFolder(), "spawns.yml");

        if(!spawnFile.exists()) {
            try {
                spawnFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);

        spawnConfig.getConfigurationSection("").getKeys(false).forEach(key -> {

            World world = Bukkit.getWorld(spawnConfig.getString(key + ".world"));

            double x = spawnConfig.getDouble(key + ".x");

            double y = spawnConfig.getDouble(key + ".y");

            double z = spawnConfig.getDouble(key + ".z");

            double pitch = spawnConfig.getDouble(key + ".pitch");

            double yaw = spawnConfig.getDouble(key + ".yaw");

            spawnPoints.add(new Location(world, x, y, z, (float) yaw, (float) pitch));
        });
    }

    /**
     *  Gets the spawn points that were retrieved from the configuration file.
     *
     * @return spawn points.
     */
    public static List<Location> getSpawnPoints() {
        return Collections.unmodifiableList(spawnPoints);
    }

    /**
     * Adds a spawn point to the spawn point list.
     * @param location the spawn location.
     */
    public static void addSpawnPoint(Location location) {
        spawnPoints.add(location);
    }

    /**
     * Gets the price of the specified gun.
     *
     * @param gun the name of the gun.
     * @return the amount of credits the gun costs.
     */
    public static int getPrice(String gun) {
        return gunPrices.get(gun);
    }

    /**
     * Gets a weapon by it's name.
     *
     * @param weaponName the name of the weapon.
     * @return the ItemStack of the weapon.
     */
    public static ItemStack getWeaponByName(String weaponName) {
        for(Weapon weapon : Ordnance.getWeapons().values()) {
            if(weapon.getName().equalsIgnoreCase(weaponName)) {
                return weapon.getItem();
            }
        }
        return null;
    }

    public FileConfiguration getSpawnConfig() {
        return spawnConfig;
    }

    public FileConfiguration getPricesConfig() {
        return pricesConfig;
    }

    public File getSpawnFile() {
        return spawnFile;
    }
}
