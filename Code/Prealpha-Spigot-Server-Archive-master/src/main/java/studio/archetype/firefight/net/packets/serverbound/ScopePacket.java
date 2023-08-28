package studio.archetype.firefight.net.packets.serverbound;

import art.arcane.gsocks.GSocksClient;
import art.arcane.gsocks.GSocksClientBoundConnection;
import art.arcane.gsocks.Packet;
import studio.archetype.firefight.cardinal.server.match.net.FirefightSocketConnection;
import studio.archetype.firefight.cardinal.server.match.service.MatchService;
import studio.archetype.firefight.cardinal.server.match.state.PlayerState;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScopePacket implements Packet {
    private boolean scoping;

    @Override
    public void handleClientToServer(GSocksClientBoundConnection client) {
        FirefightSocketConnection connection = MatchService.get().getNetworkManager().getConnectionForClient(client);

        PlayerState playerState = connection.getPlayerState();
        playerState.setScoping(scoping);
        playerState.updateModel();
    }

    @Override
    public void handleServerToClient(GSocksClient client) {
        throw new UnsupportedOperationException();
    }
}
