package studio.archetype.firefight.cardinal.server;

import studio.archetype.firefight.cardinal.server.data.Database;
import studio.archetype.firefight.cardinal.common.util.Service;
import studio.archetype.firefight.cardinal.server.match.service.MatchService;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import studio.archetype.firefight.cardinal.server.lobby.service.CommandService;
import studio.archetype.firefight.cardinal.server.lobby.service.DummyService;
import studio.archetype.firefight.cardinal.server.lobby.service.MatchmakingService;
import studio.archetype.firefight.cardinal.server.lobby.service.ProjectileService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Cardinal extends JavaPlugin {
    private static Cardinal instance;
    private Map<Class<? extends Service>, Service> services;

    public void onEnable() {
        instance = this;
        services = new HashMap<>();

        // Connect to MongoDB
        Database.connect();

        // Autostart services that need to wake up non-lazily.
        autostart(
                CommandService.class,
                DummyService.class,
                MatchmakingService.class,
                ProjectileService.class
        );

        if (CardinalConfig.get().isMatch()) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, MatchService.CHANNEL);
            service(MatchService.class);
        }

        // BEFORE YOU START ADDING REGISTRY SHIT HERE, STOP AND THINK ABOUT WHAT SERVICE IT SHOULD GO INTO INSTEAD.
    }

    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        services.values().forEach(Service::stopService);
    }

    @SafeVarargs
    private void autostart(Class<? extends Service>... services) {
        for(Class<? extends Service> i : services) service(i);
    }

    @SuppressWarnings("unchecked") // Go fuck yourself
    public static <T extends Service> T service(Class<T> serviceClass) {
        return (T) instance.services.computeIfAbsent(serviceClass, (k) -> {
            try {
                T c = serviceClass.getConstructor().newInstance();
                c.startService();
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public static Cardinal instance() { return instance; }

    public static NamespacedKey id(String key) {
        return new NamespacedKey(instance, key);
    }

    /**
     * Get a file in the plugin directory relative to, creating the parent directory if it doesnt exist
     * @param path the path...
     * @return the file...
     */
    public static File file(String path)
    {
        File f = new File(instance.getDataFolder(), path);
        f.getParentFile().mkdirs();
        return f;
    }

    public static File folder(String path)
    {
        File f = new File(instance.getDataFolder(), path);
        f.mkdirs();
        return f;
    }

    public static void info(String msg)
    {
        instance.getLogger().info(msg);
    }

    public static void warn(String msg)
    {
        instance.getLogger().warning(msg);
    }

    public static void error(String msg)
    {
        instance.getLogger().severe(msg);
    }
}
