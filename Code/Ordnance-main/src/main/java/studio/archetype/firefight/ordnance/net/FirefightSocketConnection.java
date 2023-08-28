package studio.archetype.firefight.ordnance.net;

import art.arcane.gsocks.GSocksClient;
import art.arcane.gsocks.Packet;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class FirefightSocketConnection {
    private final GSocksClient client;

    @Getter @Setter
    private ConnectionState connectionState = ConnectionState.LOGIN;

    public FirefightSocketConnection(GSocksClient client) {
        this.client = client;
    }

    public void sendPacket(Packet packet) {
        try {
            client.send(packet);
        } catch (IOException ignored) {}
    }

    public boolean isPlaying() {
        return this.connectionState == ConnectionState.PLAY;
    }
}
