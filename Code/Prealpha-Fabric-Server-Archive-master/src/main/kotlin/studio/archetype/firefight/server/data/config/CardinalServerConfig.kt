package studio.archetype.firefight.server.data.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader

data class CardinalServerConfig(
    val mongoUri: String,
    val dbName: String,

    val matchMode: Boolean,
) {
    companion object {
        lateinit var instance: CardinalServerConfig

        fun load() {
            instance = Json.decodeFromString(
                FabricLoader
                    .getInstance()
                    .configDir.resolve("config.json")
                    .toFile()
                    .readText()
            )
        }
    }
}
