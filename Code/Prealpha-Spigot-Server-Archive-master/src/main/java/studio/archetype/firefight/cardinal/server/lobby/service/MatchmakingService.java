package studio.archetype.firefight.cardinal.server.lobby.service;

import studio.archetype.firefight.cardinal.common.util.Service;
import studio.archetype.firefight.cardinal.server.lobby.matchmaking.MatchmakingManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class MatchmakingService implements Service {
    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @EventHandler
    public void on(PlayerQuitEvent event) {
        MatchmakingManager.get().removePlayerFromMatchmaking(event.getPlayer());
    }
}

