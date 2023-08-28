package studio.archetype.firefight.net.packets.clientbound;

import art.arcane.gsocks.GSocksClient;
import art.arcane.gsocks.GSocksClientBoundConnection;
import art.arcane.gsocks.Packet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameTimeChangePacket implements Packet {
    private int timeInSeconds;
    
    @Override
    public void handleClientToServer(GSocksClientBoundConnection client) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleServerToClient(GSocksClient client) {
        int secondsAfterMinutes = timeInSeconds % 60;
        int minutes = (timeInSeconds - secondsAfterMinutes) / 60;
        // todo: set time in ui
    }
}