package studio.archetype.firefight.cardinal.server.rtx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;

// Using the metric system
// Distance in Meters
// Time in Seconds
// Speed in Meters/Sec
// Mass in Grams
// Drag in Grams of force

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectileConfiguration {
    private static final double BLOCK_LENGTH = 1;
    private static final double BLOCK_MASS = 10_000;

    @Builder.Default
    private double radius = 0.1;

    @Builder.Default
    private double maxDistance = 650;

    @Builder.Default
    private double drag = 0.1;

    @Builder.Default
    private double gravitationalFactor = 0;

    @Builder.Default
    private double muzzleVelocity = 350;

    @Builder.Default
    private double projectileMass = 7;

    public void shoot(Location origin, RayResponse response)
    {
        Ray.cast(this, origin, response);
    }

    public boolean isInstant()
    {
        return muzzleVelocity <= 0;
    }

    public boolean isRay()
    {
        return gravitationalFactor <= 0;
    }

    public boolean isHitscan()
    {
        return isRay() && isInstant();
    }

    public double getDragForce(double speed)
    {
        return (drag / projectileMass) * speed;
    }
}
