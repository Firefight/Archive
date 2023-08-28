package studio.archetype.firefight.common.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import studio.archetype.firefight.common.stats.StatsService
import studio.archetype.firefight.common.weapon.WeaponService

interface Service {
    fun init()

    @Environment(EnvType.CLIENT)
    fun clientInit()

    @Environment(EnvType.SERVER)
    fun serverInit()

    companion object {
        private val services = listOf(
            WeaponService,
            StatsService,
        )

        fun initServices() { services.forEach(Service::init) }

        @Environment(EnvType.CLIENT)
        fun initServicesClient() { services.forEach(Service::clientInit) }

        @Environment(EnvType.SERVER)
        fun initServicesServer() { services.forEach(Service::serverInit) }
    }
}