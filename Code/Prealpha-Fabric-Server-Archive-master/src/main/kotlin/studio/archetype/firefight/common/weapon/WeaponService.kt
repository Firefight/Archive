package studio.archetype.firefight.common.weapon

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.SimpleRegistry
import studio.archetype.firefight.Cardinal
import studio.archetype.firefight.common.util.Service

object WeaponService: Service {
    lateinit var weaponRegistry: SimpleRegistry<Weapon>
    lateinit var weaponItemRegistry: SimpleRegistry<WeaponItem>

    override fun init() {
        weaponRegistry = FabricRegistryBuilder
            .createSimple(Weapon::class.java, Cardinal.id("weapon"))
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister()

        weaponItemRegistry = FabricRegistryBuilder
            .createSimple(WeaponItem::class.java, Cardinal.id("weapon_item"))
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister()
    }

    fun registerWeapon(weapon: Weapon, id: Identifier): Weapon {
        val registeredWeapon = Registry.register(weaponRegistry, id, weapon)
        registerWeaponItem(WeaponItem(registeredWeapon), id)
        return registeredWeapon
    }

    fun registerWeaponItem(weaponItem: WeaponItem, id: Identifier) =
        Registry.register(weaponItemRegistry, id, weaponItem)

    @Environment(EnvType.CLIENT)
    override fun clientInit() {}

    @Environment(EnvType.SERVER)
    override fun serverInit() {
        val weapons = Weapon.getCollection().find().toList()
        weapons.forEach { registerWeapon(it, Cardinal.id(it.id)) }
    }
}
