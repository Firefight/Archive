package com.ziadeh.config;

import com.ziadeh.FirefightAlpha;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.Map;

/**
 * Created by Brennan on 10/29/2019.
 */

public class TL {

    public static String ONLY_PLAYERS;
    public static String NO_PERMISSION;
    public static String SPAWN_POINT_CREATED;
    public static String SPAWN_POINT_FAILED;
    public static String SHOP_TITLE;
    public static String CONFIRMATION_TITLE;
    public static String NOT_ENOUGH_CREDITS;
    public static String PURCHASE_SUCCESSFUL;
    public static String CREDITS_EARNED;
    public static int CREDITS_PER_KILL;

    /**
     * Loads the messages
     */
    public static void load() {
        new Thread(() -> {
            FileConfiguration config = FirefightAlpha.getInstance().getConfig();

            ONLY_PLAYERS = translate(config.getString("only-players"));
            NO_PERMISSION = translate(config.getString("no-permission"));
            SPAWN_POINT_CREATED = translate(config.getString("spawn-point-created"));
            SPAWN_POINT_FAILED = translate(config.getString("spawn-point-failed"));
            SHOP_TITLE = translate(config.getString("shop-title"));
            CONFIRMATION_TITLE = translate(config.getString("confirmation-title"));
            NOT_ENOUGH_CREDITS = translate(config.getString("not-enough-credits"));
            PURCHASE_SUCCESSFUL = translate(config.getString("purchase-successful"));
            CREDITS_EARNED = translate(config.getString("credits-earned"));
            CREDITS_PER_KILL = config.getInt("credits-per-kill");

        }).start();
    }

    /**
     * Replaces a string (key) with the specified value.
     * Useful when retrieving messages with placeholder values.
     * Example: %player% -> player.getName();
     *
     * @param message the original message
     * @param replacements the replacement set
     * @return the new message
     */
    public static String get(String message, Map<String, String> replacements) {
        String afterReplacement = message;

        for(String key : replacements.keySet()) {
            afterReplacement = afterReplacement.replaceAll(key, replacements.get(key));
        }

        return afterReplacement;
    }

    /**
     * Translates alternate color codes in strings. ('&' -> '§')
     *
     * @param string the message being translated
     * @return the new message with translated color codes
     */
    public static String translate(String string) {
        return string.replaceAll("&", "§");
    }
}
