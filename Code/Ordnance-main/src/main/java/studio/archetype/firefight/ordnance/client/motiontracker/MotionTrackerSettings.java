package studio.archetype.firefight.ordnance.client.motiontracker;

import lombok.Data;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import java.util.List;

@Data
public class MotionTrackerSettings {

    private boolean showRadar, altBase, enableDebug;
    private int innerRingDistance, totalDistance,heightThreshold, totalHeight;
    private int offsetX, offsetY;
    private int attackRange;

    private List<EntityType<?>> entityPredicate;

    public MotionTrackerSettings() {
        loadDefaults();
    }

    public void loadDefaults() {
        this.showRadar = true;
        this.altBase = this.enableDebug = false;
        this.innerRingDistance = 6;
        this.totalDistance = 16;
        this.heightThreshold = 3;
        this.totalHeight = 8;
        this.offsetX = this.offsetY = 16;
    }

    public boolean isEntityValid(Entity e) {
        return entityPredicate.contains(e.getType());
    }
}
