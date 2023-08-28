package studio.archetype.rook.instances.slime;

import com.github.luben.zstd.Zstd;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import studio.archetype.rook.math.Vec2i;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class SlimeWorld {

    public static final short MAGIC = (short)0xB10B;
    public static final byte VERSION = (byte)0x09;

    private SlimeWorldVersion worldVersion;
    private SlimeChunkMap chunkMap;
    //TODO Entities, Block Entities & Maps

    private void parse(File f) throws SlimeParseException {
        try(FileInputStream fileIn = new FileInputStream(f); DataInputStream data = new DataInputStream(fileIn)) {
            short magic = data.readShort();
            if(magic != MAGIC)
                throw new SlimeParseException("File \"%s\" has no matching magic value, it is likely not a Slime world. [%02X != %02X]", f.getName(), magic, MAGIC);
            byte version = data.readByte();
            if(version != VERSION)
                throw new SlimeParseException("Slime world is using version \"%02X\", but currently only %02X is supported.", version, VERSION);

            this.worldVersion = SlimeWorldVersion.get(data.readByte());
            if(worldVersion != SlimeWorldVersion.V_7)
                throw new SlimeParseException("Slime worlds for Minecraft versions older than 1.17 are not supported yet. [%s != %s]", worldVersion, SlimeWorldVersion.V_7);
            this.chunkMap = new SlimeChunkMap();
            parseChunkData(data);

        } catch(IOException ex) {

        }
    }

    private void parseChunkData(DataInputStream data) throws IOException {
        Vec2i minPos = new Vec2i(data.readShort(), data.readShort());
        int width = data.readUnsignedShort();
        int depth = data.readUnsignedShort();
        byte[] chunkMapBits = data.readNBytes((int)Math.ceil(width * depth / 8));
        List<Vec2i> decodedPositions = decodeChunkMap(minPos, width, depth, chunkMapBits);

        int compressedSize = data.readInt();
        int uncompressedSize = data.readInt();
        try(ByteArrayInputStream rawData = new ByteArrayInputStream(Zstd.decompress(data.readNBytes(compressedSize), uncompressedSize)); DataInputStream chunkData = new DataInputStream(rawData)){
            for(Vec2i pos : decodedPositions)
                decodeChunk(pos, chunkData);
        }
    }

    private List<Vec2i> decodeChunkMap(Vec2i min, int width, int depth, byte[] chunkBits) {
        List<Vec2i> chunkPositions = new ArrayList<>();
        for(int x = 0; x < width; x++) {
            for(int z = 0; z < depth; z++) {
                Vec2i gridPos = min.add(x, z);
                int bitIndex = x * 8 + z;
                if((chunkBits[bitIndex / 8] << (bitIndex % 8) & 1) == 1) {
                    // Chunk Data is present, we add the coordinates to assign it when parsing the actual chunk data.
                    chunkPositions.add(gridPos);
                } else {
                    // Chunk Data is not present,we simply inject an empty chunk.
                    chunkMap.injectChunk(gridPos.x(), gridPos.y(), SlimeChunk.createEmpty());
                }
            }
        }
        return chunkPositions;
    }

    private void decodeChunk(Vec2i pos, DataInputStream data) throws IOException{
        int hmSize = data.readInt();
        byte[] heightmap = data.readNBytes(hmSize);
        int biomeSize = data.readInt();
        byte[] biomeArray = data.readNBytes(biomeSize);

        int minY = data.readInt();
        int maxY = data.readInt();
        int sectionCount = data.readInt();

        List<SlimeChunk.Section> sections = new ArrayList<>(sectionCount);
        for(int i = 0; i < sectionCount; i++) {
            // Block Light
            if(data.readBoolean()) {
                // TODO BlockLight
            }

            int blockStateSize = data.readInt();
            byte[] blockStates = data.readNBytes(blockStateSize);
            int biomeStateSize = data.readInt();
            byte[] biomeStates = data.readNBytes(biomeStateSize);

            // Sky Light
            if(data.readBoolean()) {
                // TODO SkyLight
            }
        }
    }
}
