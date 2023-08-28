package studio.archetype.firefight.net.packets.serverbound;

import art.arcane.gsocks.GSocksClient;
import art.arcane.gsocks.GSocksClientBoundConnection;
import art.arcane.gsocks.Packet;
import studio.archetype.firefight.cardinal.server.data.Weapon;
import studio.archetype.firefight.cardinal.server.match.net.FirefightSocketConnection;
import studio.archetype.firefight.cardinal.server.match.service.MatchService;
import studio.archetype.firefight.cardinal.server.match.state.PlayerState;
import studio.archetype.firefight.cardinal.server.match.stats.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class FirePacket implements Packet {
    @Override
    public void handleClientToServer(GSocksClientBoundConnection client) {
        FirefightSocketConnection connection = MatchService.get().getNetworkManager().getConnectionForClient(client);

        Player player = connection.getAuthPlayer();
        PlayerState playerState = connection.getPlayerState();
        PlayerStats playerStats = connection.getPlayerStats();
        Weapon weapon = playerState.getHeldWeapon();

        if (weapon != null) {
            playerState.updateModel(true);

            weapon.shootForPlayer(player, (entity, block, face, position) -> {
                // If the match is started, shots do damage
                if (MatchService.get().isMatchStarted()) {
                    playerStats.addShotFired();
                    if (entity instanceof LivingEntity living && living instanceof Player opponent) {
                        PlayerState opponentState = MatchService.get().getMatchState().getPlayerStates().get(opponent);
                        PlayerStats opponentStats = MatchService.get().getMatchStats().getPlayerStatsMap().get(opponent);

                        if (opponentState != null) {
                            int damage = weapon.getBaseDamage(); // we can add modifiers later
                            playerStats.incrementDamageDealt(damage);

                            boolean opponentKilled = opponentState.damagePlayer(damage);
                            if (opponentKilled) {
                                opponentStats.addDeath();
                                playerStats.addKill();

                                // Broadcast a kill message
                                MatchService.get().broadcastMessage(ChatColor.WHITE + player.getName() + ChatColor.DARK_GRAY + " killed " + ChatColor.WHITE + opponent.getName());
                            }
                        }
                    }

                    if (block != null) block.breakNaturally();
                }

                // show particles irrelevant of whether shots can do damage
                if (position != null) player.getWorld().spawnParticle(
                        Particle.CRIT,
                        position.toLocation(player.getWorld()),
                        1
                );
            });
        }
    }

    @Override
    public void handleServerToClient(GSocksClient client) {
        throw new UnsupportedOperationException();
    }
}
