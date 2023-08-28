package studio.archetype.firefight.net.packets.serverbound;

import art.arcane.gsocks.GSocksClient;
import art.arcane.gsocks.GSocksClientBoundConnection;
import art.arcane.gsocks.Packet;

public class FirePacket implements Packet {
    @Override
    public void handleClientToServer(GSocksClientBoundConnection client) {} // Handled server-side

    @Override
    public void handleServerToClient(GSocksClient gSocksClient) {
        throw new UnsupportedOperationException();
    }
}
