package studio.archetype.firefight.cardinal.server.lobby.service;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import studio.archetype.firefight.cardinal.common.util.Service;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatService implements Service {
    /** Pattern matching "nicer" legacy hex chat color codes - &#rrggbb */
    private static final Pattern NICER_HEX_COLOR_PATTERN = Pattern.compile("&#([0-9a-fA-F]{6})");

    private Chat vaultChat = null;

    private void refreshVault() {
        Chat vaultChat = Bukkit.getServer().getServicesManager().load(Chat.class);
        if (vaultChat != this.vaultChat) {
            Bukkit.getLogger().info("New Vault Chat implementation registered: " + (vaultChat == null ? "null" : vaultChat.getName()));
        }
        this.vaultChat = vaultChat;
    }

    private static final String NAME_PLACEHOLDER = "{name}";
    private static final String PREFIX_PLACEHOLDER = "{prefix}";
    private static final String SUFFIX_PLACEHOLDER = "{suffix}";

    // Format placeholder patterns
    private static final Pattern NAME_PLACEHOLDER_PATTERN = Pattern.compile(NAME_PLACEHOLDER, Pattern.LITERAL);
    private static final Pattern PREFIX_PLACEHOLDER_PATTERN = Pattern.compile(PREFIX_PLACEHOLDER, Pattern.LITERAL);
    private static final Pattern SUFFIX_PLACEHOLDER_PATTERN = Pattern.compile(SUFFIX_PLACEHOLDER, Pattern.LITERAL);

    /** The default format */
    private static final String FORMAT = PREFIX_PLACEHOLDER + " " + NAME_PLACEHOLDER + " " + SUFFIX_PLACEHOLDER + "//" + ChatColor.RESET + " %2$s";

    @Override
    public void onEnable() {
        refreshVault();
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onServiceChange(ServiceRegisterEvent e) {
        if (e.getProvider().getService() == Chat.class) {
            refreshVault();
        }
    }

    @EventHandler
    public void onServiceChange(ServiceUnregisterEvent e) {
        if (e.getProvider().getService() == Chat.class) {
            refreshVault();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatLow(AsyncPlayerChatEvent e) {
        // Set out format on the lowest priority - allow other plugins to override or add their own parts.
        e.setFormat(FORMAT);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatHigh(AsyncPlayerChatEvent e) {
        // Replace our placeholders on highest - just before
        String format = e.getFormat();

        if (this.vaultChat != null) {
            format = replaceAll(PREFIX_PLACEHOLDER_PATTERN, format, () -> colorize(this.vaultChat.getPlayerPrefix(e.getPlayer())));
            format = replaceAll(SUFFIX_PLACEHOLDER_PATTERN, format, () -> colorize(this.vaultChat.getPlayerSuffix(e.getPlayer())));
        }
        format = replaceAll(NAME_PLACEHOLDER_PATTERN, format, () -> e.getPlayer().getName());

        e.setFormat(format);
    }

    /**
     * Equivalent to {@link String#replace(CharSequence, CharSequence)}, but uses a
     * {@link Supplier} for the replacement.
     *
     * @param pattern the pattern for the replacement target
     * @param input the input string
     * @param replacement the replacement
     * @return the input string with the replacements applied
     */
    private static String replaceAll(Pattern pattern, String input, Supplier<String> replacement) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.replaceAll(Matcher.quoteReplacement(replacement.get()));
        }
        return input;
    }

    /**
     * Translates color codes in the given input string.
     *
     * @param string the string to "colorize"
     * @return the colorized string
     */
    private static String colorize(String string) {
        if (string == null) {
            return "null";
        }

        // Convert from the '&#rrggbb' hex color format to the '&x&r&r&g&g&b&b' one used by Bukkit.
        Matcher matcher = NICER_HEX_COLOR_PATTERN.matcher(string);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            StringBuilder replacement = new StringBuilder(14).append("&x");
            for (char character : matcher.group(1).toCharArray()) {
                replacement.append('&').append(character);
            }
            matcher.appendReplacement(sb, replacement.toString());
        }
        matcher.appendTail(sb);

        // Translate from '&' to '§' (section symbol)
        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }
}
