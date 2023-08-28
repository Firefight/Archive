package studio.archetype.firefight.client.mixin_hook

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.TranslatableText
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object TitleScreenMixinHook {
    @JvmStatic
    fun removeSinglePlayerButton(thisArg: Screen, y: Int, spacingY: Int, ci: CallbackInfo) {
        thisArg.addDrawableChild(ButtonWidget(
            thisArg.width / 2 - 100, y + spacingY, 200, 20, TranslatableText("menu.multiplayer")
        ) {
            MinecraftClient.getInstance().setScreen(
                MultiplayerScreen(thisArg)
            )
        }).active = true

        ci.cancel()
    }
}