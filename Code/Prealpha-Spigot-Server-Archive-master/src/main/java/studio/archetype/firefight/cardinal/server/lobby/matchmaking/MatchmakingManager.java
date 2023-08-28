package studio.archetype.firefight.cardinal.server.lobby.matchmaking;

import com.hashicorp.nomad.javasdk.NomadException;
import studio.archetype.firefight.cardinal.server.CardinalConfig;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MatchmakingManager {
    private static MatchmakingManager instance;
    private final ArrayList<MatchQueue> queues = new ArrayList<>();

    public void joinPlayerToRandomQueue(Player player) throws NomadException, IOException {
        int queueIndex = new Random().nextInt(queues.size());

        MatchQueue queue = queues.get(queueIndex);
        boolean success = queue.addPlayer(player);

        if (!success) { // if they weren't joined due to the queue being too full, let's fix that
            queues.remove(queueIndex);
            MatchQueue newQueue = new MatchQueue(new Match(CardinalConfig.get().getNomadMatchJobId(), 6));
            newQueue.addPlayer(player);
            queues.add(newQueue);
        }
    }

    public void removePlayerFromMatchmaking(Player player) {
        for (MatchQueue queue : queues) queue.removePlayer(player);
    }

    public static MatchmakingManager get() {
        if (instance != null) return instance;
        instance = new MatchmakingManager();
        return instance;
    }
}
