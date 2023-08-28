package studio.archetype.rook.utils.codec;

import com.mojang.serialization.Codec;

public interface CodecDispatcherEnum<T> extends EnumCodec.Values {
    Codec<? extends T> getCodec();
}
