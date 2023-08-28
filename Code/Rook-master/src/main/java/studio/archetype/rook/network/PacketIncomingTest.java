package studio.archetype.rook.network;

import studio.archetype.rook.Entrypoint;

import java.nio.ByteBuffer;

public class PacketIncomingTest implements Packet.Incoming {

    @Override
    public int getPacketId() { return 0x00; }

    @Override
    public void decode(ByteBuffer buffer) { }

    @Override
    public void handle() {
        Entrypoint.info("Received test packet!");
    }
}
