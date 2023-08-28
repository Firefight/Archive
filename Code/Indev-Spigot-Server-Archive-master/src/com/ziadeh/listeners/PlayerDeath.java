package com.ziadeh.listeners;

import com.ziadeh.FirefightAlpha;
import com.ziadeh.config.TL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

/**
 * Created by Brennan on 11/3/2019.
 */
public class PlayerDeath implements Listener {

    public PlayerDeath() {
        FirefightAlpha.getInstance().registerListener(this);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();

            FirefightAlpha.getEconomy().depositPlayer(killer, TL.CREDITS_PER_KILL);

            killer.sendMessage(TL.get(TL.CREDITS_EARNED, new HashMap<String, String>(){{
                put("%player%", event.getEntity().getName());
                put("%amount%", String.valueOf(TL.CREDITS_PER_KILL));
            }}));
        }
    }
}
