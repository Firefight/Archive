package studio.archetype.firefight.cardinal.common.util;

import studio.archetype.firefight.cardinal.server.Cardinal;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public interface Service extends Listener {
    void onEnable();
    void onDisable();

    default void startService() {
        onEnable();
        Bukkit.getPluginManager().registerEvents(this, Cardinal.instance());
        info("Online & Active");
    }

    default void stopService() {
        HandlerList.unregisterAll(this);
        onDisable();
        info("Service Shutdown");
    }

    default void info(String msg) {
        Cardinal.info("[" + getClass().getSimpleName() + "]: " + msg);
    }

    default void warn(String msg) {
        Cardinal.warn("[" + getClass().getSimpleName() + "]: " + msg);
    }

    default void error(String msg) {
        Cardinal.error("[" + getClass().getSimpleName() + "]: " + msg);
    }
}
