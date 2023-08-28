package studio.archetype.firefight.common.stats

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity
import studio.archetype.firefight.common.util.Service

/**
 * The only place this needs to run is the server. Clients will request their
 * stats later using a packet.
 */
object StatsService : Service {
    override fun init() {}

    @Environment(EnvType.CLIENT)
    override fun clientInit() {}

    @Environment(EnvType.SERVER)
    override fun serverInit() {
        // todo
    }

    @Environment(EnvType.SERVER)
    fun getPlayerStats(player: PlayerEntity) {
        // todo: get stats via mongo using player.uuid
    }
}