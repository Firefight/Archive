package studio.archetype.firefight.cardinal.server.match;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Map {
    // todo: fill in world names

    @SerializedName("small")
    SMALL(6, ""),

    @SerializedName("medium")
    MEDIUM(12, ""),

    @SerializedName("large")
    LARGE(20, ""),
    ;

    private final int maxPlayers;
    private final String worldName;
}
