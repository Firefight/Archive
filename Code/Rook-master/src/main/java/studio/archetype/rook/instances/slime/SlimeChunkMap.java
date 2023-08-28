package studio.archetype.rook.instances.slime;

import java.util.HashMap;

public class SlimeChunkMap {

    private final HashMap<Long, SlimeChunk> chunkMap = new HashMap<>();

    public void injectChunk(int x, int z, SlimeChunk chunk) {
        chunkMap.put(chunkMapKey(x, z), chunk);
    }

    private static long chunkMapKey(int x, int z) {
        return ((long)x << 32) | (z & 0xFFFFFFFFL);
    }
}
