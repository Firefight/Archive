package studio.archetype.rook;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.lan.OpenToLAN;
import net.minestom.server.instance.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.archetype.rook.config.ServerProperties;
import studio.archetype.rook.utils.ReflectionUtils;
import studio.archetype.rook.instances.WorldManager;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.function.Consumer;

public abstract class Entrypoint {

    public static final Logger LOGGER = LoggerFactory.getLogger("Cardinal");

    public static final File CONFIG_DIR = new File(Entrypoint.workingDir(), "config");
    public static final File RESOURCE_DIR = new File(Entrypoint.workingDir(), "resources");
    public static final File WORLDS_DIR = new File(RESOURCE_DIR, "worlds");

    public static ServerProperties properties;

    public static MinecraftServer server;
    public static WorldManager worlds;
    public static GlobalEventHandler globalHandlers;

    public static File workingDir() {
        return FileSystems.getDefault().getPath(".").toFile();
    }

    protected Entrypoint() {
        properties = parseProperties();

        server = MinecraftServer.init();
        worlds = new WorldManager();
        globalHandlers = MinecraftServer.getGlobalEventHandler();

        MinecraftServer.setBrandName(properties.serverBrand());

        properties.proxyType().enable();

        if(properties.defaultWorld().exists())
            worlds.createInstance(properties.defaultWorld(), WorldManager.DEFAULT_INSTANCE);
        else
            worlds.createInstance(unit -> unit.modifier().fillHeight(0,64, Block.STONE), WorldManager.DEFAULT_INSTANCE);


        event(PlayerLoginEvent.class, e -> {
            e.setSpawningInstance(worlds.getDefaultInstance());
            e.getPlayer().setRespawnPoint(properties.spawnPosition());
        });

        onInitialize();
        startServer();
    }

    protected abstract void onInitialize();

    protected void startServer() {
        server.start(properties.address());
        if(properties.address().getHostString().equals("0.0.0.0"))
            OpenToLAN.open();
    }

    protected <T extends Event> EventNode<Event> event(Class<T> eventType, Consumer<T> handler) {
        return globalHandlers.addListener(eventType, handler);
    }

    private ServerProperties parseProperties() {
        if(!ServerProperties.FILE.exists() || ServerProperties.FILE.isDirectory()) {
            LOGGER.error("Server config \"server.json\" not found, falling back to default!");
            return ServerProperties.DEFAULT;
        }

        try(FileInputStream in = new FileInputStream(ServerProperties.FILE); InputStreamReader reader = new InputStreamReader(in)) {
            JsonElement json = JsonParser.parseReader(reader);
            DataResult<Pair<ServerProperties, JsonElement>> result = JsonOps.INSTANCE.withDecoder(ServerProperties.CODEC).apply(json);
            ServerProperties props = result.resultOrPartial(s -> {
                LOGGER.error("Error parsing \"server.json\":");
                LOGGER.error("\t" + s);
            }).orElseThrow(() -> new JsonParseException("Failed to parse server config, falling back to default!")).getFirst();
            return result.error().isEmpty() ? props : ReflectionUtils.merge(props, ServerProperties.DEFAULT);
        } catch(IOException | JsonParseException e) {
            logException(true, e, null);
            return ServerProperties.DEFAULT;
        }
    }

    public static void info(String format, Object... args) {
        LOGGER.info(String.format(format, args));
    }

    public static void error(String format, Object... args) {
        LOGGER.error(String.format(format, args));
    }

    public static void logException(boolean isSevere, Throwable e, String format, Object... args) {
        String eLine = String.format("%s%s%s", e.getClass().getSimpleName(), format != null ? "\t" : "", e.getMessage() != null ? ": " + e.getMessage() : "");
        if(isSevere) {
            if(format != null)
                error(String.format(format, args));
            error(eLine);
        } else {
            if(format != null)
                LOGGER.warn(String.format(format, args));
            LOGGER.warn(eLine);
        }

    }
}
