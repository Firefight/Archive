package studio.archetype.firefight.net.packets.clientbound;

import art.arcane.gsocks.GSocksClient;
import art.arcane.gsocks.GSocksClientBoundConnection;
import art.arcane.gsocks.Packet;

public class AuthenticationSuccessPacket implements Packet {
    @Override
    public void handleClientToServer(GSocksClientBoundConnection client) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleServerToClient(GSocksClient client) {} // Handled client-side
}
