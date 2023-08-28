package studio.archetype.cardinal.world;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.utils.NamespaceID;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorldManager {

    public static final NamespaceID DEFAULT_INSTANCE = NamespaceID.from("cardinal", "default");

    private static final Map<NamespaceID, Instance> WORLDS = new ConcurrentHashMap<>();

    public InstanceManager instanceManager;

    public WorldManager() {
        this.instanceManager = MinecraftServer.getInstanceManager();
    }

    @SuppressWarnings("UnstableApiUsage")
    public Instance createInstance(File worldDir, NamespaceID id) {
        if(WORLDS.containsKey(id))
            return getInstance(id);
        Instance instance = instanceManager.createInstanceContainer(new AnvilLoader(worldDir.getPath()));
        WORLDS.put(id, instance);
        return instance;
    }

    public Instance createInstance(Generator generator, NamespaceID id) {
        if(WORLDS.containsKey(id))
            return getInstance(id);
        Instance instance = instanceManager.createInstanceContainer();
        instance.setGenerator(generator);
        WORLDS.put(id, instance);
        return instance;
    }

    public Instance getInstance(NamespaceID id) {
        return WORLDS.getOrDefault(id, null);
    }

    public Instance getDefaultInstance() {
        return getInstance(DEFAULT_INSTANCE);
    }
}
