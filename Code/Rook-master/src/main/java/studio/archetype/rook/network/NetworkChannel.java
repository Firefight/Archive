package studio.archetype.rook.network;

import studio.archetype.rook.Entrypoint;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class NetworkChannel implements Runnable {

    private final Map<Integer, Class<Packet.Incoming>> incomingPackets;
    private final ByteBuffer headerBuffer = ByteBuffer.allocate(8);
    private final ByteBuffer packetBuffer = ByteBuffer.allocate(1024);
    private final ByteBuffer outBuffer = ByteBuffer.allocate(1024);

    protected final String host;
    protected final int port;

    private SocketChannel channel;
    private boolean listening = true;

    public NetworkChannel(String host, int port) {
        this.host = host;
        this.port = port;
        this.incomingPackets = registerIncomingPackets();
    }

    public abstract ConcurrentHashMap<Integer, Class<Packet.Incoming>> registerIncomingPackets();

    public abstract Socket createSocket() throws IOException;

    @Override
    public void run() {
        try(Socket targetSocket = createSocket()){
            targetSocket.setKeepAlive(true);
            channel = targetSocket.getChannel();
            mainLoop();
        } catch(IOException e) {
            Entrypoint.logException(true, e, "Unable to connect to remote socket!");
        }
    }

    private void mainLoop() throws IOException {
        while(channel.isConnectionPending()) { }
        Entrypoint.info("Socket connected to [%s:%d].", host, port);

        while(listening) {
            if(channel.read(headerBuffer) > 0) {
                int packetId = headerBuffer.getInt();
                headerBuffer.clear();
                channel.read(packetBuffer);
                if(!incomingPackets.containsKey(packetId)) {
                    Entrypoint.info("Received invalid packet with ID %d, ignoring.", packetId);
                } else {
                    Entrypoint.info("Received packet [%d]!", packetId);
                    handlePacket(packetId, packetBuffer);
                }
                packetBuffer.clear();
            }
        }
        channel.close();
        headerBuffer.clear();
        packetBuffer.clear();
    }

    public synchronized void sendPacket(Packet.Outgoing packet) {
        if(!listening) {
            Entrypoint.error("Tried to send packet on closed network channel!");
            return;
        }
        outBuffer.clear();
        packet.encode(outBuffer);
        int bufferSize = outBuffer.position();
        ByteBuffer out = ByteBuffer.allocate(bufferSize + 8);
        out.putInt(packet.getPacketId());
        out.putInt(bufferSize);
        out.put(outBuffer);
        try {
            channel.write(out);
        } catch(IOException e) {
            Entrypoint.logException(true, e, "Failed to send packet %d!", packet.getPacketId());
        }
        out.clear();
        outBuffer.clear();
    }

    public void closeChannel() {
        this.listening = false;
    }

    public void handlePacket(int packetId, ByteBuffer buffer) {
        try {
            Class<Packet.Incoming> clazz = incomingPackets.get(packetId);
            Packet.Incoming packet = clazz.getConstructor().newInstance();
            packet.decode(buffer);
            packet.handle();
        } catch(InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            Entrypoint.logException(true, e, "Failed to create instance for PackedID %d!", packetId);
        }
    }
}
