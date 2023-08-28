package studio.archetype.firefight.common.weapon

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d

class WeaponItem(
    val weapon: Weapon
) : Item(
    FabricItemSettings()
        .maxCount(1) // todo: keep track of grenade count later
        .group(ItemGroup.COMBAT)
) {
    fun shoot(player: PlayerEntity) {

        val rangeBox = Box.from(player.pos).expand(weapon.range)
        val rangeMin = Vec3d(rangeBox.minX, rangeBox.minY, rangeBox.minZ)
        val rangeMax = Vec3d(rangeBox.maxX, rangeBox.maxY, rangeBox.maxZ)
        val entityHit = ProjectileUtil.raycast(player, rangeMin, rangeMax, rangeBox, {
            // todo: filter valid targets
            true
        }, 0.1) ?: return

        when (entityHit.type!!) { // never null so make kotlin think it's non nullable
            HitResult.Type.ENTITY -> {
                // todo: calculate head position for critical hits.
                entityHit.entity.damage(DamageSource.player(player), weapon.getDamage())
            }
            HitResult.Type.MISS -> {} // do nothing (for now)
            HitResult.Type.BLOCK -> {} // never happens
        }
    }
}