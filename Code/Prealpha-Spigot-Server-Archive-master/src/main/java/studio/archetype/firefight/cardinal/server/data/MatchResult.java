package studio.archetype.firefight.cardinal.server.data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.conversions.Bson;
import studio.archetype.firefight.cardinal.server.match.MatchConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {
    private UUID _id;
    private Instant timestamp;
    private Duration duration; // Millis
    private List<PlayerResult> playerResults;

    public MatchResult(Instant matchTimestamp, Duration matchDuration, List<PlayerResult> playerResults) {
        this(
                UUID.fromString(MatchConfig.get().getMatchId()),
                matchTimestamp, matchDuration, playerResults
        );
    }

    @AllArgsConstructor
    @Getter
    public static final class PlayerResult {
        public UUID playerId;
        public int kills;
        public int deaths;
        public int shotsFired;
        public long damageDealt;
    }

    public static final String COLLECTION = "match_results";

    public static MongoCollection<MatchResult> getCollection() {
        return Database.getDatabase().getCollection(COLLECTION, MatchResult.class);
    }

    public void save() {
        MongoCollection<MatchResult> collection = getCollection();
        Bson idFilter = eq("_id", _id);
        MatchResult document = collection.find(idFilter).first();
        if (document == null) collection.insertOne(this);
        else collection.replaceOne(idFilter, this);
    }
}
