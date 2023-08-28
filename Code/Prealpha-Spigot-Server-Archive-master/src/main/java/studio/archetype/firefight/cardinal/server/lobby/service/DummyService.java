package studio.archetype.firefight.cardinal.server.lobby.service;

import studio.archetype.firefight.cardinal.common.util.Service;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DummyService implements Service {
    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void on(PlayerJoinEvent e)
    {
        // Only touch this service for this event, dont make unified event handlers
        // THIS IS AUTO MANAGED AND REGISTERED DONT REGISTER THIS LISTENER
    }

    @EventHandler
    public void on(PlayerQuitEvent e)
    {
        // ITS A GOOD IDEA TO OVERLOAD THE "ON" METHOD NAME FOR EVENTS. EASIER TO READ
    }
}
