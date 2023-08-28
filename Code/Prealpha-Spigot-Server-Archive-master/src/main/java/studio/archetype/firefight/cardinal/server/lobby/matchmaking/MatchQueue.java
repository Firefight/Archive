package studio.archetype.firefight.cardinal.server.lobby.matchmaking;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MatchQueue {
    private final ArrayList<Player> playersWaiting = new ArrayList<>();
    private final Match match;

    public MatchQueue(Match match) {
        this.match = match;
    }

    // returns a boolean indicating whether the player was actually added. if not we add them to a new queue later
    public boolean addPlayer(Player p) {
        if (playersWaiting.size() < match.maxPlayers) {
            playersWaiting.add(p);
            return true;
        } else {
            startMatch();
            return false;
        }
    }

    public void removePlayer(Player p) {
        playersWaiting.remove(p);
    }

    void startMatch() {
        playersWaiting.forEach(match::joinPlayerToGame);
    }
}
