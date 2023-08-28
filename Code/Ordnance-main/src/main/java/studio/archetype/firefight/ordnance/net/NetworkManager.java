package studio.archetype.firefight.ordnance.net;

import art.arcane.gsocks.GSocksClient;
import art.arcane.gsocks.Packet;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import studio.archetype.firefight.net.packets.serverbound.AuthenticationPacket;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

public class NetworkManager {
    private GSocksClient client = null;

    @Getter
    private FirefightSocketConnection connection = null;

    public NetworkManager() {}

    public void login(UUID loginCode, String ip, int port) {
        Logger.getLogger("Firefight").info("fuck");
        if (connection == null) {
            Logger.getLogger("Firefight").info(ip + ":" + port);

            try {
                Logger.getLogger("Firefight").info("Connecting to Firefight Socket...");
                client = new GSocksClient(ip, port);

                client.setHandler((client, pkt) -> {
                    if(pkt instanceof Packet p) {
                        p.handleServerToClient(client); // Call the handle method
                    }
                });

                connection = new FirefightSocketConnection(client);
                connection.sendPacket(new AuthenticationPacket(loginCode, MinecraftClient.getInstance().player.getUuid()));
                Logger.getLogger("Firefight").info("Connected to Socket!");
            } catch(IOException e) {
                Logger.getLogger("Firefight").severe("Failed to connect to Firefight Socket!");
                e.printStackTrace();
            }
        }
    }
}
