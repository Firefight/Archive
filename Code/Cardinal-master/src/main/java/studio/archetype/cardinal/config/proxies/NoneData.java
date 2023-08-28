package studio.archetype.cardinal.config.proxies;

import com.mojang.serialization.Codec;
import net.minestom.server.extras.MojangAuth;

public record NoneData(boolean mojangAuth) implements Proxy.Data {

    public Proxy getType() { return Proxy.NONE; }

    public static final Codec<NoneData> CODEC = Codec.BOOL.xmap(NoneData::new, NoneData::mojangAuth).optionalFieldOf("mojangAuth", new NoneData(true)).codec();

    @Override
    public void enable() {
        if(mojangAuth)
            MojangAuth.init();
    }
}