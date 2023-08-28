package studio.archetype.rook.network;

import net.minestom.server.utils.NamespaceID;

import java.nio.ByteBuffer;

public interface Packet {

    int getPacketId();

    interface Outgoing extends Packet {
        void encode(ByteBuffer buffer);
    }

    interface Incoming extends Packet {

        void decode(ByteBuffer buffer);

        void handle();

        default boolean enforceMainThread() { return true; }
    }
}
