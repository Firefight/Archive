package studio.archetype.firefight.cardinal.server.match.net;

public class UnknownPacketException extends Exception {
    public UnknownPacketException(int packetId, String state) {
        super(String.format("Unknown Packet with ID 0x%02x in state \"%s\"", packetId, state));
    }
}
