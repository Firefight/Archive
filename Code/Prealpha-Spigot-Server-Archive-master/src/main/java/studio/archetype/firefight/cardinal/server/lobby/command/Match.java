package studio.archetype.firefight.cardinal.server.lobby.command;

import com.hashicorp.nomad.javasdk.NomadException;
import studio.archetype.firefight.cardinal.server.lobby.matchmaking.MatchmakingManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Match implements CommandExecutor {
    public static final String internalError =
            ChatColor.RED + "There was an internal error running this command. Please contact a server administrator.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                String subcommand = args[0];
                switch (subcommand) {
                    case "join":
                        try {
                            MatchmakingManager.get().joinPlayerToRandomQueue(player);
                            player.chat(ChatColor.GOLD + "You have joined matchmaking!");
                        } catch (NomadException | IOException e) {
                            e.printStackTrace();
                            player.sendMessage(internalError);
                        }
                        break;
                    case "leave":
                        MatchmakingManager.get().removePlayerFromMatchmaking(player);
                        player.chat(ChatColor.GOLD + "You have left matchmaking!");
                        break;
                    default:
                        return false;
                }
            } else return false;
        } else sender.sendMessage(ChatColor.RED + "You must be in game to run this command.");

        return true;
    }
}
