package studio.archetype.firefight.ordnance.client.motiontracker.enums;

public enum RadarDistance {

    CENTER("center"),
    INNER("inner"),
    OUTER("outer");

    private final String key;

    RadarDistance(String key) {
        this.key = key;
    }

    public String getTextureKey() {
        return this.key;
    }
}
