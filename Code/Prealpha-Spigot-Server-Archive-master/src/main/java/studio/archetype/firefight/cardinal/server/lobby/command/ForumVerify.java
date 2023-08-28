package studio.archetype.firefight.cardinal.server.lobby.command;

import com.google.gson.Gson;
import studio.archetype.firefight.cardinal.common.util.MiscUtils;
import studio.archetype.firefight.cardinal.server.CardinalConfig;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ForumVerify implements CommandExecutor {
    @AllArgsConstructor
    private static class VerificationRequestBody {
        private String code;
        private String uuid;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                String code = args[0];
                UUID id = player.getUniqueId();

                VerificationRequestBody body = new VerificationRequestBody(code, id.toString());

                try {
                    URL url = new URL(CardinalConfig.get().getForumEndpoint());

                    URLConnection conn = url.openConnection();
                    HttpURLConnection http = (HttpURLConnection)conn;
                    http.setRequestMethod("POST");

                    Gson gson = MiscUtils.buildGson();
                    byte[] out = gson.toJson(body).getBytes(StandardCharsets.UTF_8);

                    http.setFixedLengthStreamingMode(out.length);
                    http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    http.connect();
                    try (OutputStream os = http.getOutputStream()) {
                        os.write(out);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    player.chat(ChatColor.RED + "Verification failed, service may be down. Please file a bug report.");
                }
            } else return false;
        } else sender.sendMessage(ChatColor.RED + "You must be in game to run this command.");
        return true;
    }
}
