package studio.archetype.firefight.cardinal.proxy.socket.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchJoinData {
    private String matchId;
    private String playerId;
}
