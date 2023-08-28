package studio.archetype.firefight.cardinal.server.match.stats;

import lombok.Getter;
import org.bukkit.entity.Player;
import studio.archetype.firefight.cardinal.server.data.MatchResult;

@Getter
public class PlayerStats {
    private final Player player;
    public PlayerStats(Player player) {
        this.player = player;
    }

    private int kills = 0;
    private int deaths = 0;
    private int shotsFired = 0;
    private long damageDealt = 0;

    public void addKill() { kills++; }
    public void addDeath() { deaths++; }
    public void addShotFired() { shotsFired++; }
    public void incrementDamageDealt(int amount) { damageDealt += amount; }

    public MatchResult.PlayerResult compileResults() {
        return new MatchResult.PlayerResult(
            player.getUniqueId(),
            kills, deaths,
            shotsFired,
            damageDealt
        );
    }
}
