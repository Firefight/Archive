package studio.archetype.firefight.cardinal.server.rtx;

import studio.archetype.firefight.cardinal.server.CardinalConfig;
import lombok.Getter;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

@Getter
public class FutureProjectile {
    private final ProjectileConfiguration configuration;
    private final RayResponse response;
    private final Location position;
    private final Vector direction;
    private RayTraceResult result;
    private boolean done;
    private double speed;
    private double travel;

    public FutureProjectile(ProjectileConfiguration configuration, Location origin, RayResponse response)
    {
        this.configuration = configuration;
        this.position = origin;
        this.response = response;
        this.speed = configuration.getMuzzleVelocity();
        this.direction = origin.getDirection().clone().normalize();
        this.done = false;
        this.travel = 0;
        this.result = null;
    }

    public boolean isDone()
    {
        return done;
    }

    public void tick() {
        double rtsd = CardinalConfig.get().getProjectiles().getProjectileRTSegMeters();
        int jumps = (int) Math.round(Math.max(1, speed / rtsd));

        for(int i = 0; i < jumps; i++)
        {
            tickOne(rtsd, jumps);

            if(result != null && result.getHitEntity() != null || result.getHitBlock() != null)
            {
                done = true;
                response.onRayResult(result.getHitEntity(), result.getHitBlock(), result.getHitBlockFace(), result.getHitPosition());
                return;
            }

            if(travel > configuration.getMaxDistance())
            {
                done = true;
                response.onRayResult(null, null, null, position.toVector());
                return;
            }

            if(!isValid(position))
            {
                done = true;
                response.onRayResult(null, null, null, position.toVector());
                return;
            }
        }
    }

    private boolean isValid(Location position) {
        return position.getY() < position.getWorld().getMinHeight() || position.getY() > position.getWorld().getMaxHeight()
                || position.getWorld().isChunkLoaded(position.getBlockX() >> 4, position.getBlockZ() >> 4);
    }

    private void tickOne(double rtsd, double jumps)
    {
        if(configuration.getGravitationalFactor() != 0)
        {
            double gr = -((configuration.getGravitationalFactor() * 10D)/20D) / jumps;
            position.add(0, gr, 0);
            travel += gr;
        }

        if(configuration.getDrag() > 0)
        {
            speed -= Math.max(20, configuration.getDragForce(speed) / jumps);
        }

        travel += rtsd;
        result = position.getWorld().rayTrace(position, direction, rtsd, FluidCollisionMode.NEVER, true, configuration.getRadius() * 2, (i) -> true);
        position.add(direction.clone().multiply(rtsd));
    }
}
