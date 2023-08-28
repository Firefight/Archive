package studio.archetype.firefight.cardinal.proxy.socket;

import com.google.gson.Gson;
import studio.archetype.firefight.cardinal.common.util.MiscUtils;
import studio.archetype.firefight.cardinal.proxy.CardinalBungee;
import studio.archetype.firefight.cardinal.proxy.socket.message.MatchJoinData;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.UUID;

public class ProxySocketHandler extends Thread {
    private static final Gson gson = MiscUtils.buildGsonBungee();

    private final Socket clientSocket;
    private DataOutputStream out;
    private BufferedReader in;

    public ProxySocketHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            StringBuilder inputBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) inputBuilder.append(inputLine).append("\n");
            String input = inputBuilder.toString();

            SocketMessage<?> socketMessage = gson.fromJson(input, SocketMessage.class);
            handleMessage(socketMessage);

            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    private void handleMessage(SocketMessage<?> socketMessage) throws IOException {
        switch (socketMessage.getType()) {
            case MATCH_JOIN:
                MatchJoinData data = socketMessage.data();
                CardinalBungee.get()
                        .getProxy().getPlayer(UUID.fromString(data.getPlayerId()))
                        .connect(CardinalBungee.get().getProxy()
                                .getServerInfo("match_" + data.getMatchId())
                        );

                break;
            default:
                // ignore
        }
    }
}
