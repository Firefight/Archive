package studio.archetype.firefight.cardinal.server.rtx;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

@FunctionalInterface
public interface RayResponse {
    void onRayResult(Entity entity, Block block, BlockFace face, Vector position);
}
