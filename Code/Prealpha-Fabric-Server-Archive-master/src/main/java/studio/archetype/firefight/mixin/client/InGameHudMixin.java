package studio.archetype.firefight.mixin.client;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.SpectatorHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.archetype.firefight.client.mixin_hook.InGameHudMixinHook;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    /**
     * Render right before the F3 menu.
     * Weird artifacts occur when rendered before the sky
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    public void afterHudRender(CallbackInfo callbackInfo) {
        InGameHudMixinHook.afterHudRender(callbackInfo);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V"))
    private void overwriteHotbarRender(InGameHud instance, float tickDelta, MatrixStack matrices) {
    } // Do nothing

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderExperienceBar(Lnet/minecraft/client/util/math/MatrixStack;I)V"))
    private void overwriteExpRender(InGameHud instance, MatrixStack matrices, int x) {
    } // Do nothing

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusBars(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    private void overwriteStatusRender(InGameHud instance, MatrixStack matrices) {
    } // Do nothing

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/SpectatorHud;renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    private void overwriteSpectatorRender(SpectatorHud instance, MatrixStack matrices) {
    } // Do nothing

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHeldItemTooltip(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    private void overwriteTooltipRender(InGameHud instance, MatrixStack matrices) {
    }
}
