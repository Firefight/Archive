package studio.archetype.rook.config.proxies;

import com.mojang.serialization.Codec;
import net.minestom.server.extras.velocity.VelocityProxy;

public record VelocityData(String secret) implements Proxy.Data {

    public Proxy getType() { return Proxy.VELOCITY; }

    public static final Codec<VelocityData> CODEC = Codec.STRING.xmap(VelocityData::new, VelocityData::secret).fieldOf("secret").codec();

    @Override
    public void enable() {
        VelocityProxy.enable(secret);
    }
}
