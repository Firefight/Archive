package studio.archetype.firefight.net.packets.serverbound;


import art.arcane.gsocks.GSocksClient;
import art.arcane.gsocks.GSocksClientBoundConnection;
import art.arcane.gsocks.Packet;
import lombok.AllArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
public class AuthenticationPacket implements Packet {
    private UUID code;
    private UUID playerId;

    @Override
    public void handleClientToServer(GSocksClientBoundConnection client) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleServerToClient(GSocksClient client) {} // Handled server-side
}
