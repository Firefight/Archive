package studio.archetype.firefight.ordnance.client.motiontracker.enums;

import studio.archetype.firefight.ordnance.client.motiontracker.MotionTrackerManager;

public enum VerticalDiff {
    ABOVE("above"),
    LEVEL("level"),
    BELOW("below");

    private final String key;

    VerticalDiff(String key) {
        this.key = key;
    }

    public String getTextureKey() {
        return this.key;
    }

    public static VerticalDiff getDiff(double y) {
        if(y > MotionTrackerManager.INSTANCE.getSettings().getHeightThreshold())
            return ABOVE;
        else if(y < -MotionTrackerManager.INSTANCE.getSettings().getHeightThreshold())
            return BELOW;
        return LEVEL;
    }
}
