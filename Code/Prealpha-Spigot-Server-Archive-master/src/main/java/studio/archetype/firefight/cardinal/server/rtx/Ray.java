package studio.archetype.firefight.cardinal.server.rtx;

import studio.archetype.firefight.cardinal.server.Cardinal;
import studio.archetype.firefight.cardinal.server.lobby.service.ProjectileService;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;

public class Ray {
    public static double JUMP_LAZY = 8;

    /**
     * Raytrace!
     * @param configuration the configuration of the projectile
     * @param source the source where it was shot (uses the locations direction)
     * @return
     */
    public static RayTraceResult cast(ProjectileConfiguration configuration, Location source, RayResponse response)
    {
        if(configuration.isHitscan())
        {
            RayTraceResult r = source.getWorld().rayTrace(source, source.getDirection(), 1024, FluidCollisionMode.NEVER, true, configuration.getRadius() * 2, (i) -> true);

            if(r != null)
            {
                response.onRayResult(r.getHitEntity(), r.getHitBlock(), r.getHitBlockFace(), r.getHitPosition());
            }

            else
            {
                response.onRayResult(null, null, null, null);
            }
        }

        else
        {
            Cardinal.service(ProjectileService.class).queue(new FutureProjectile(configuration, source, response));
        }

        return null;
    }
}
