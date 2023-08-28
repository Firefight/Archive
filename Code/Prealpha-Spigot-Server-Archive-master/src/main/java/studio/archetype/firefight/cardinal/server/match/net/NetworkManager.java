package studio.archetype.firefight.cardinal.server.match.net;

import art.arcane.gsocks.GSocksClientBoundConnection;
import art.arcane.gsocks.GSocksServer;
import art.arcane.gsocks.Packet;
import org.bukkit.Bukkit;
import studio.archetype.firefight.cardinal.server.CardinalConfig;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class NetworkManager {
    private final GSocksServer server = GSocksServer.start(CardinalConfig.get().getPlayerNetworkingPort());

    @Getter
    private final HashMap<UUID, FirefightSocketConnection> connections = new HashMap<>();

    @Getter
    private final HashMap<UUID, Player> authCodes = new HashMap<>();

    public NetworkManager() throws IOException {
        server.setHandler((client, pkt) -> {
            if(pkt instanceof Packet p) {
                if (!connections.containsKey(client.getConnectionId())) {
                    FirefightSocketConnection connection = new FirefightSocketConnection(client);
                    connections.put(client.getConnectionId(), connection);
                }

                p.handleClientToServer(client); // Call the handle method
            }
        });
    }

    public FirefightSocketConnection getConnectionForClient(GSocksClientBoundConnection client) {
        return connections.get(client.getConnectionId());
    }

    public FirefightSocketConnection getConnectionForPlayer(Player player) {
        return connections
                .values()
                .stream()
                .filter(conn -> conn.getAuthPlayer().getUniqueId().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    public void disconnectPlayer(Player player, boolean kick) {
        if (player.isOnline() && kick) player.kickPlayer("Disconnected.");
        getConnectionForPlayer(player).disconnect();
    }

    public void disconnectPlayer(Player player) {
        disconnectPlayer(player, false);
    }

    public void disconnectAll() {
        connections.values().forEach(FirefightSocketConnection::disconnect);
    }

    public void stop() {
        disconnectAll();
    }

    public boolean checkAuth(UUID code, UUID playerId, GSocksClientBoundConnection client) {
        if (authCodes.containsKey(code)) {
            connections.get(client.getConnectionId()).setAuthPlayer(Bukkit.getPlayer(playerId));
            authCodes.remove(code);
            return true;
        }

        return false;
    }
}
