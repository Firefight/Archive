package studio.archetype.firefight.client.mixin_hook

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import studio.archetype.firefight.client.ui.FirefightUi

object InGameHudMixinHook {
    @JvmStatic
    fun afterHudRender(ci: CallbackInfo) {
        FirefightUi.render()
    }
}