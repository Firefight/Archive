package studio.archetype.firefight.cardinal.server.match.state;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

@Getter
public class MatchState {
    private final HashMap<Player, PlayerState> playerStates = new HashMap<>();

    public MatchState() {
        for (Player p : Bukkit.getOnlinePlayers()) playerStates.put(p, new PlayerState(p));
    }
}
