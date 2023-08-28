package studio.archetype.cardinal.config.proxies;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import studio.archetype.cardinal.utils.codec.CodecDispatcherEnum;
import studio.archetype.cardinal.utils.codec.Codecs;

@AllArgsConstructor
public enum Proxy implements CodecDispatcherEnum<Proxy.Data> {
    BUNGEE("bungee", BungeeData.CODEC),
    VELOCITY("velocity", VelocityData.CODEC),
    NONE("none", NoneData.CODEC);

    public static final Codec<Proxy> CODEC = Codecs.enumOf(Proxy.class);

    private final String serializedName;
    private final Codec<? extends Data> codec;

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    @Override
    public Codec<? extends Data> getCodec() {
        return codec;
    }

    public interface Data {
        Codec<Data> CODEC = Proxy.CODEC.dispatch(Data::getType, Proxy::getCodec);
        Proxy getType();

        void enable();
    }
}
