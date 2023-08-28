package studio.archetype.rook.utils.codec;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import lombok.AllArgsConstructor;

import java.io.File;
import java.net.URI;
import java.util.Optional;

@AllArgsConstructor
public class RelativePathCodec implements PrimitiveCodec<File> {

    private final File parent;
    private final boolean mustExist, isDirectory;

    @Override
    public <T> DataResult<File> read(DynamicOps<T> ops, T input) {
        Optional<String> value = ops.getStringValue(input).result();
        if(value.isEmpty())
            return DataResult.error("Missing value for relative path codec?");
        File f = new File(parent, value.get());
        if(mustExist && !f.exists())
            return DataResult.error("Relative file does not exist!", f);
        if(isDirectory && !f.isDirectory())
            return DataResult.error("Found relative file, but it is not a directory!", f);
        return DataResult.success(f);
    }

    @Override
    public <T> T write(DynamicOps<T> ops, File value) {
        URI relative = value.toURI().relativize(parent.toURI());
        return ops.createString(relative.getPath());
    }
}
