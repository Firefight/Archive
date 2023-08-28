package studio.archetype.firefight.cardinal.server.data;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerData {
    private UUID _id;

    public static final String COLLECTION = "players";

//    public ArrayList<MatchResult> getRecentMatches(int limit) {
//        return MatchResult.model.getRecentMatchesForPlayer(_id, limit);
//    }
}
