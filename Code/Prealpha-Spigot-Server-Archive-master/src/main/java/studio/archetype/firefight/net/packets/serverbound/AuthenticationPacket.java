package studio.archetype.firefight.net.packets.serverbound;


import art.arcane.gsocks.GSocksClient;
import art.arcane.gsocks.GSocksClientBoundConnection;
import art.arcane.gsocks.Packet;
import org.bukkit.Bukkit;
import studio.archetype.firefight.cardinal.server.item.Loadout;
import studio.archetype.firefight.cardinal.server.match.net.FirefightSocketConnection;
import studio.archetype.firefight.cardinal.server.match.net.ConnectionState;
import studio.archetype.firefight.net.packets.clientbound.AuthenticationSuccessPacket;
import studio.archetype.firefight.cardinal.server.match.service.MatchService;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor
public class AuthenticationPacket implements Packet {
    private UUID code;
    private UUID playerId;

    @Override
    public void handleClientToServer(GSocksClientBoundConnection client) {
        boolean success = MatchService.get().getNetworkManager().checkAuth(code, playerId, client);

        FirefightSocketConnection connection = MatchService.get().getNetworkManager().getConnections().get(client.getConnectionId());
        if (success) try {
            connection.setConnectionState(ConnectionState.PLAY);

            Bukkit.getLogger().info(connection.getAuthPlayer().getDisplayName());
            client.send(new AuthenticationSuccessPacket());
            Loadout.testLoadout.apply(connection.getAuthPlayer());
            Bukkit.getLogger().info("fuck");
        } catch (IOException e) {
            e.printStackTrace();
            connection.getAuthPlayer().kickPlayer("An internal error has occurred, please contact a server administrator for help.");
            connection.disconnect();
        } else connection.disconnect();
    }

    @Override
    public void handleServerToClient(GSocksClient client) {
        throw new UnsupportedOperationException();
    }
}
