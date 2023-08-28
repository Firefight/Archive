package studio.archetype.firefight.server

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import studio.archetype.firefight.common.util.Service
import studio.archetype.firefight.server.data.config.CardinalServerConfig
import studio.archetype.firefight.server.data.mongo.Database

@Environment(EnvType.SERVER)
object ServerEnvironment {
    fun init() {
        CardinalServerConfig.load()
        Database.connect()
        Service.initServicesServer()
    }
}