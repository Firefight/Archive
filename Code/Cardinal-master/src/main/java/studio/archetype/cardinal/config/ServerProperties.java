package studio.archetype.cardinal.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minestom.server.coordinate.Pos;
import studio.archetype.cardinal.config.proxies.NoneData;
import studio.archetype.cardinal.config.proxies.Proxy;
import studio.archetype.cardinal.utils.codec.Codecs;
import studio.archetype.cardinal.Entrypoint;

import java.io.File;
import java.net.InetSocketAddress;

public record ServerProperties(
        String serverBrand,
        File defaultWorld,
        Pos spawnPosition,
        InetSocketAddress address,
        Proxy.Data proxyType) {

    public static final File FILE = new File(Entrypoint.CONFIG_DIR, "server.json");

    public static final ServerProperties DEFAULT = new ServerProperties(
            "firefight",
            new File(Entrypoint.WORLDS_DIR, "world"),
            new Pos(0, 64, 0),
            new InetSocketAddress("0.0.0.0", 25565),
            new NoneData(true));

    public static final Codec<ServerProperties> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.optionalFieldOf("serverBrand", DEFAULT.serverBrand).forGetter(ServerProperties::serverBrand),
            Codecs.relativePath(Entrypoint.WORLDS_DIR, true, true).optionalFieldOf("defaultWorld", DEFAULT.defaultWorld).forGetter(ServerProperties::defaultWorld),
            Codecs.POS_ROT.optionalFieldOf("spawnPos", DEFAULT.spawnPosition).forGetter(ServerProperties::spawnPosition),
            Codecs.IP_ADDRESS.fieldOf("ipAddress").forGetter(ServerProperties::address),
            Proxy.Data.CODEC.fieldOf("proxy").forGetter(ServerProperties::proxyType)
    ).apply(i, ServerProperties::new));
}
