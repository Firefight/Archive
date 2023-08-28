package com.ziadeh.commands;

import com.ziadeh.FirefightAlpha;
import com.ziadeh.config.Configuration;
import com.ziadeh.config.TL;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Brennan on 11/1/2019.
 */

public class SetSpawnCommand extends FCommand {

    public SetSpawnCommand(String command, String permission) {
        super(command, permission);
    }

    @Override
    public void execute(Player player, String label, String[] args) {
        if(args.length == 0) {
            setSpawnPoint(player);
        }
    }

    private void setSpawnPoint(Player player) {
        new Thread(() -> {
            Location location = player.getLocation();

            FileConfiguration spawnConfig = FirefightAlpha.getInstance().getConfiguration().getSpawnConfig();

            File spawnFile = FirefightAlpha.getInstance().getConfiguration().getSpawnFile();

            int spawnPoint = 1;

            while(spawnConfig.contains("spawn" + spawnPoint))
                spawnPoint++;

            spawnConfig.set("spawn" + spawnPoint + ".world", location.getWorld().getName());

            spawnConfig.set("spawn" + spawnPoint + ".x", location.getX());

            spawnConfig.set("spawn" + spawnPoint + ".y", location.getY());

            spawnConfig.set("spawn" + spawnPoint + ".z", location.getZ());

            spawnConfig.set("spawn" + spawnPoint + ".pitch", location.getPitch());

            spawnConfig.set("spawn" + spawnPoint + ".yaw", location.getYaw());

            try {

                spawnConfig.save(spawnFile);

            } catch (IOException e) {

                player.sendMessage(TL.get(TL.SPAWN_POINT_FAILED, new HashMap<String, String>() {
                    {
                        put("%error%", e.getMessage());
                    }
                }));

            } finally {

                Configuration.addSpawnPoint(location);

                player.sendMessage(TL.SPAWN_POINT_CREATED);

            }
        }).start();
    }
}
