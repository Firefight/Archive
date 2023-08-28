package studio.archetype.firefight.cardinal.proxy;

import com.google.gson.Gson;
import studio.archetype.firefight.cardinal.common.util.FileUtils;
import studio.archetype.firefight.cardinal.proxy.socket.ProxySocketSystem;
import studio.archetype.firefight.cardinal.server.CardinalConfig;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class CardinalBungee extends Plugin {
    private static CardinalBungee instance;
    private static CardinalConfig config;
    private ProxySocketSystem proxySocketSystem;

    @Override
    public void onEnable() {
        instance = this;
        try {
            proxySocketSystem = new ProxySocketSystem();
            proxySocketSystem.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File file(String path) {
        File f = new File(instance.getDataFolder(), path);
        f.getParentFile().mkdirs();
        return f;
    }

    public static CardinalConfig config() {
        if (config != null) return config;
        File file = file("config.json");
        info("Loading Config at " + file.getPath());

        if(file.exists()) {
            try {
                config = new Gson().fromJson(FileUtils.readAll(file), CardinalConfig.class);
            } catch(Throwable e) {
                config = new CardinalConfig();
                warn("Failed to load config at " + file.getPath() + ". Using default config.");
            }
        } else config = new CardinalConfig();

        try {
            FileUtils.writeAll(file, new Gson().toJson(config));
        } catch(Throwable e) {
            e.printStackTrace();
        }

        return config;
    }

    @Override
    public void onDisable() {
        proxySocketSystem.shutdown();
    }

    public static CardinalBungee get() {
        return instance;
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
