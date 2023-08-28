package studio.archetype.firefight.cardinal.server.match.stats;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MatchStats {
    @Getter
    private final HashMap<Player, PlayerStats> playerStatsMap = new HashMap<>();

    public MatchStats() {
        for (Player p : Bukkit.getOnlinePlayers()) playerStatsMap.put(p, new PlayerStats(p));
    }
}
