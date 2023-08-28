package studio.archetype.firefight.cardinal.server.match.service;

import studio.archetype.firefight.cardinal.common.util.NetUtils;
import studio.archetype.firefight.cardinal.common.util.Service;
import studio.archetype.firefight.cardinal.server.CardinalConfig;
import studio.archetype.firefight.cardinal.server.data.MatchResult;
import studio.archetype.firefight.cardinal.server.data.Weapon;
import studio.archetype.firefight.cardinal.server.match.MatchConfig;
import studio.archetype.firefight.cardinal.server.match.net.NetworkManager;
import studio.archetype.firefight.cardinal.server.match.state.MatchState;
import studio.archetype.firefight.cardinal.server.match.stats.MatchStats;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import studio.archetype.firefight.net.packets.clientbound.AuthenticationSuccessPacket;
import studio.archetype.firefight.net.packets.clientbound.GameTimeChangePacket;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MatchService implements Service {
    public final static String CHANNEL = "firefight:login";

    private int playersOnline = 0;
    @Getter
    private boolean matchStarted = false;

    @Getter
    private final MatchState matchState = new MatchState();
    @Getter
    private final MatchStats matchStats = new MatchStats();

    private Instant matchStart = null;

    @Getter
    private NetworkManager networkManager;

    private static MatchService instance;
    public static MatchService get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            networkManager = new NetworkManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        networkManager.stop();
    }

    // https://youtu.be/j7gKwxRe7MQ

    @EventHandler
    public void on(PlayerJoinEvent event) throws IOException {
        playersOnline++;
        UUID code = UUID.randomUUID();
        networkManager.getAuthCodes().put(code, event.getPlayer());
        String ip = NetUtils.getIp();
        int port = CardinalConfig.get().getPlayerNetworkingPort();
        event.getPlayer().spigot().sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText("FIREFIGHT_SYSTEM_CONNECT " + code + " " + ip + " " + port));

        ScheduledExecutorService checkExecutor = Executors.newScheduledThreadPool(1);
        checkExecutor.schedule(() -> {
            boolean playing = networkManager.getConnectionForPlayer(event.getPlayer()).isPlaying();
            Bukkit.getLogger().info(event.getPlayer().getDisplayName() + " playing: " + playing);

            if (!playing)
                event.getPlayer().kickPlayer("Failed to authenticate with the socket in timeout");

        }, 350L, TimeUnit.MILLISECONDS);

        if (playersOnline == 6 && !matchStarted) {
            startMatch();
        }
    }

    @EventHandler
    public void on(PlayerItemHeldEvent event) {
        matchState.getPlayerStates().get(event.getPlayer())
                .setHeldWeapon(Weapon.held(event.getPlayer()));
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        playersOnline--;
        networkManager.disconnectPlayer(event.getPlayer());
    }

    public void broadcastMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(serverPlayer -> serverPlayer.sendMessage(message));
    }

    public void startMatch() {
        matchStarted = true;
        matchStart = Instant.now();

        ScheduledExecutorService countdownScheduler = Executors.newScheduledThreadPool(1);
        Runnable countdownRunnable = new Runnable() {
            static final HashMap<Integer, Runnable> actions = new HashMap<>();
            static {
                actions.put(30, () -> {
                    MatchService.get().broadcastMessage("30 seconds left");
                });
            }

            private int lengthSeconds = MatchConfig.get().getMatchLength() * 60;

            @Override
            public void run() {
                networkManager.getConnections().values().forEach(conn -> {
                    try {
                        conn.send(new GameTimeChangePacket(lengthSeconds));
                    } catch (IOException ignored) {}
                });

                if (actions.containsKey(lengthSeconds)) actions.get(lengthSeconds).run();

                if (lengthSeconds == 0) {
                    countdownScheduler.shutdown();
                    endMatch();
                }

                lengthSeconds--;
            }
        };
        countdownScheduler.scheduleAtFixedRate(countdownRunnable, 0, 1, TimeUnit.SECONDS);
    }

    public void endMatch() {
        Instant matchEnd = Instant.now();

        Duration duration = Duration.between(matchStart, matchEnd);
        ArrayList<MatchResult.PlayerResult> playerResults = new ArrayList<>();
        matchStats.getPlayerStatsMap().values().forEach((playerStats) -> playerResults.add(playerStats.compileResults()));

        MatchResult matchResult = new MatchResult(matchStart, duration, playerResults);
        matchResult.save();

        // todo: display stats

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final Runnable countdownRunnable = new Runnable() {
            int countdown = 30;

            @Override
            public void run() {
                if (countdown == 30) broadcastMessage("Returning to lobby in 30 seconds...");
                else if (countdown < 10) {
                    broadcastMessage("" + countdown);

                    if (countdown == 0) {
                        scheduler.shutdown();

                        Bukkit.getOnlinePlayers().forEach(serverPlayer -> {
                            serverPlayer.kickPlayer("Match ended");
                        });
                    }
                }

                countdown--;
            }
        };
        scheduler.scheduleAtFixedRate(countdownRunnable, 0, 1, TimeUnit.SECONDS);
    }
}
