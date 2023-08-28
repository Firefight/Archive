package studio.archetype.rook.config.proxies;

import com.mojang.serialization.Codec;
import net.minestom.server.extras.bungee.BungeeCordProxy;

public record BungeeData() implements Proxy.Data {

    public Proxy getType() { return Proxy.BUNGEE; }

    public static final Codec<BungeeData> CODEC = Codec.unit(new BungeeData());

    @Override
    public void enable() {
        BungeeCordProxy.enable();
    }
}
