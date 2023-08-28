package com.ziadeh.listeners;

import com.ziadeh.FirefightAlpha;
import com.ziadeh.config.Configuration;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import java.util.List;
import java.util.Random;

/**
 * Created by Brennan on 11/3/2019.
 */

public class PlayerRespawn implements Listener {

    private static Random random;

    static {
        random = new Random();
    }

    public PlayerRespawn() {
        FirefightAlpha.getInstance().registerListener(this);
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent event) {
        List<Location> spawnPoints = Configuration.getSpawnPoints();

        if(!spawnPoints.isEmpty()) {
            event.setRespawnLocation(spawnPoints.get(random.nextInt(spawnPoints.size())));
        }
    }
}
