package studio.archetype.firefight.cardinal.server.match.net;

import art.arcane.gsocks.GSocksClientBoundConnection;
import art.arcane.gsocks.Packet;
import studio.archetype.firefight.cardinal.server.match.service.MatchService;
import studio.archetype.firefight.cardinal.server.match.state.PlayerState;
import studio.archetype.firefight.cardinal.server.match.stats.PlayerStats;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.io.IOException;

public class FirefightSocketConnection {
    private final GSocksClientBoundConnection client;

    @Getter @Setter
    private Player authPlayer = null;

    @Getter @Setter
    private ConnectionState connectionState = ConnectionState.LOGIN;

    public FirefightSocketConnection(GSocksClientBoundConnection client) {
        this.client = client;
    }

    public PlayerState getPlayerState() {
        return MatchService.get().getMatchState().getPlayerStates().get(authPlayer);
    }

    public PlayerStats getPlayerStats() {
        return MatchService.get().getMatchStats().getPlayerStatsMap().get(authPlayer);
    }

    public void send(Packet p) throws IOException {
        client.send(p);
    }

    public boolean isPlaying() {
        return this.connectionState == ConnectionState.PLAY;
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (IOException ignored) {}
    }
}
