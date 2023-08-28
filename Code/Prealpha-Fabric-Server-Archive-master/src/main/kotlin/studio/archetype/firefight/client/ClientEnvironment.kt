package studio.archetype.firefight.client

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.prismclient.aether.ui.UICore
import studio.archetype.firefight.client.ui.DefaultRenderer
import studio.archetype.firefight.common.util.Service

@Environment(EnvType.CLIENT)
object ClientEnvironment {
    lateinit var uiCore: UICore

    fun init() {
        Service.initServicesClient()
    }

    fun createUiCore() {
        val displayWidth = MinecraftClient.getInstance().window.width
        val displayHeight = MinecraftClient.getInstance().window.height
        uiCore = UICore(DefaultRenderer)
        uiCore.update(displayWidth.toFloat(), displayHeight.toFloat(), 1f)
    }
}