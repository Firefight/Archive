package studio.archetype.firefight.client.ui

import net.minecraft.client.MinecraftClient
import studio.archetype.firefight.client.ClientEnvironment

object FirefightUi {
    fun render() {
        val displayWidth: Int = MinecraftClient.getInstance().window.width
        val displayHeight: Int = MinecraftClient.getInstance().window.height
        ClientEnvironment.uiCore.update(displayWidth.toFloat(), displayHeight.toFloat(), 1f)
        ClientEnvironment.uiCore.render()
    }
}