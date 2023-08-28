package studio.archetype.firefight

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import studio.archetype.firefight.client.ClientEnvironment
import studio.archetype.firefight.common.CommonEnvironment
import studio.archetype.firefight.server.ServerEnvironment

object Cardinal : ClientModInitializer, ModInitializer, DedicatedServerModInitializer {
    const val MOD_ID = "cardinal"

    fun id(name: String) = Identifier(MOD_ID, name)

    @Environment(EnvType.CLIENT)
    override fun onInitializeClient() {
        ClientEnvironment.init()
    }

    override fun onInitialize() {
        CommonEnvironment.init()
    }

    @Environment(EnvType.SERVER)
    override fun onInitializeServer() {
        ServerEnvironment.init()
    }
}
