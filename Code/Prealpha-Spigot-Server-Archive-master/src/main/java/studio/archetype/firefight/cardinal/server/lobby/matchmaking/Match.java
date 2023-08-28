package studio.archetype.firefight.cardinal.server.lobby.matchmaking;

import com.google.gson.Gson;
import com.hashicorp.nomad.javasdk.NomadException;
import studio.archetype.firefight.cardinal.common.util.MiscUtils;
import studio.archetype.firefight.cardinal.common.util.Nomad;
import studio.archetype.firefight.cardinal.proxy.socket.SocketMessage;
import studio.archetype.firefight.cardinal.proxy.socket.SocketMessageType;
import studio.archetype.firefight.cardinal.proxy.socket.message.MatchJoinData;
import studio.archetype.firefight.cardinal.server.CardinalConfig;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Match {
    private String matchId;
    private boolean ready = false;
    public int maxPlayers;
    private static final Gson gson = MiscUtils.buildGsonBungee();

    public Match(String mapJobId, int maxPlayers) throws NomadException, IOException {
        this.maxPlayers = maxPlayers;

        Nomad.get().startMatchAndWait(mapJobId, new Nomad.MatchParams(), (params) -> {
            this.matchId = params.getMatchId();
            this.ready = true;
        });
    }

    public void joinPlayerToGame(Player player) {
        // todo: wait for nomad

        if (!this.ready) throw new IllegalStateException("Tried to join player to match before match was ready!");

        try {
            Socket socket = new Socket(CardinalConfig.get().getInternalSocketAddress(), 27019);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            SocketMessage<MatchJoinData> socketMessage = new SocketMessage<>(
                    SocketMessageType.MATCH_JOIN,
                    new MatchJoinData(
                            matchId,
                            player.getUniqueId().toString()
                    )
            );

            out.println(gson.toJson(socketMessage));
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(studio.archetype.firefight.cardinal.server.lobby.command.Match.internalError);
        }
    }
}
