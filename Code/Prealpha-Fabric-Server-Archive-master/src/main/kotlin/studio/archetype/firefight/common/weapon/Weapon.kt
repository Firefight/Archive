package studio.archetype.firefight.common.weapon

import kotlinx.serialization.Serializable
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.item.Item
import studio.archetype.firefight.Cardinal
import studio.archetype.firefight.server.data.mongo.Database

@Serializable
data class Weapon(
    val display: String,
    val id: String,
    val throwable: Boolean,
    val range: Double,
    val baseDamage: Float,
) {
    fun getItem() =
        WeaponService.weaponItemRegistry.get(Cardinal.id(id))
            ?: throw IllegalStateException("getItem() failed: Weapon ID must correspond to a registered WeaponItem")

    fun getDamage(crit: Boolean = false): Float {
        if (crit) return baseDamage * 1.5f
        return baseDamage
    }

    companion object {
        const val collectionName = "weapons"

        @Environment(EnvType.SERVER)
        fun getCollection() = Database.coll<Weapon>(collectionName)
    }
}