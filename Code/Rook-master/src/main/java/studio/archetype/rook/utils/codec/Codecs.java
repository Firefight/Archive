package studio.archetype.rook.utils.codec;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minestom.server.coordinate.Pos;

import java.io.File;
import java.net.InetSocketAddress;

public final class Codecs {

    public static final Codec<Pos> POS = Codec.DOUBLE.listOf().xmap(
            l -> new Pos(l.get(0), l.get(1), l.get(2)),
            p -> ImmutableList.of(p.x(), p.y(), p.z()));
    public static final Codec<Pos> POS_ROT = Codec.DOUBLE.listOf().xmap(
            l -> new Pos(l.get(0), l.get(1), l.get(2), l.get(3).floatValue(), l.get(4).floatValue()),
            p -> ImmutableList.of(p.x(), p.y(), p.z(), (double)p.pitch(), (double)p.yaw()));

    public static final Codec<InetSocketAddress> IP_ADDRESS = Codec.pair(Codec.STRING.fieldOf("address").codec(), Codec.intRange(0, 99999).fieldOf("port").codec()).xmap(
            p -> new InetSocketAddress(p.getFirst(), p.getSecond()),
            a -> new Pair<>(a.getHostString(), a.getPort())
    );

    public static RelativePathCodec relativePath(File parent, boolean mustExist, boolean isDirectory) {
        return new RelativePathCodec(parent, mustExist, isDirectory);
    }

    public static <E extends Enum<E> & EnumCodec.Values> EnumCodec<E> enumOf(Class<E> clazz) {
        return new EnumCodec<>(clazz);
    }
}
