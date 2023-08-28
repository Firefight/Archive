package studio.archetype.firefight.cardinal.proxy.socket;

import studio.archetype.firefight.cardinal.proxy.CardinalBungee;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class ProxySocketSystem extends Thread {
    private final ServerSocket socket;
    private boolean running = true;

    public ProxySocketSystem() throws IOException {
        socket = new ServerSocket(
                CardinalBungee.config().getInternalSocketPort(),
                50, // 50 = default backlog value
                InetAddress.getByName(CardinalBungee.config().getInternalSocketAddress())
        );
    }

    @Override
    public void run() {
        while (running) {
            try {
                new ProxySocketHandler(socket.accept()).start();
            } catch (IOException e) {
                // fuck off
                // This is correct
            }
        }
    }

    public void shutdown() {
        running = false;
    }
}
