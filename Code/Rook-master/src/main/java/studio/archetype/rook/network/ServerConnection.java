package studio.archetype.rook.network;

import studio.archetype.rook.Entrypoint;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ServerConnection extends Thread {

    private final Map<UUID, NetworkChannel> serverConnections = new ConcurrentHashMap<>();

    private final InetSocketAddress address;

    private boolean listening = true;

    public ServerConnection(String host, int port) {
        this.address = new InetSocketAddress(host, port);
    }

    public void closeChannel() {
        this.listening = false;
    }

    @Override
    public void run() {
        try(Selector selector = Selector.open(); ServerSocketChannel channel = ServerSocketChannel.open()) {
            channel.bind(address);
            channel.configureBlocking(false);
            channel.register(selector, channel.validOps());
            while(listening) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    if(next.isAcceptable()) {
                        //TODO Add new connection
                    } else if(next.isReadable()) {
                        next.channel();
                        // TODO Data has arrived
                    }
                    iterator.remove();
                }
            }
        } catch(IOException e) {
            Entrypoint.logException(true, e, "An error occured while setting up a server connection!");
        }
    }
}
