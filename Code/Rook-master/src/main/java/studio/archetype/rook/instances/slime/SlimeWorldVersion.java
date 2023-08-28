package studio.archetype.rook.instances.slime;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SlimeWorldVersion {
    INVALID((byte)0x00),
    V_1((byte)0x01),
    V_2((byte)0x02),
    V_3((byte)0x03),
    V_4((byte)0x04),
    V_5((byte)0x05),
    V_6((byte)0x06),
    V_7((byte)0x07);

    private final byte version;

    public static SlimeWorldVersion get(byte version) {
        return Arrays.stream(values()).filter(v -> v.getVersion() == version).findFirst().orElse(INVALID);
    }
}
