package studio.archetype.firefight.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.archetype.firefight.client.mixin_hook.MinecraftClientMixinHook;

@Mixin(MinecraftClient.class)
@Environment(EnvType.CLIENT)
public class MinecraftClientMixin {
    // The mixins below were adapted from the TooManyEvents mod; brace for massive URL.
    // https://github.com/fabric-community/TooManyEvents/blob/master/src/main/java/io/github/fabriccommunity/events/mixin/client/MixinMinecraftClient.java
    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;doAttack()Z"
        ),
        method = "handleInputEvents",
        cancellable = true
    )
    private void doAttack(CallbackInfo ci) {
        MinecraftClientMixinHook.doAttack(ci);
    }

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"
        ),
        method = "doAttack"
    )
    private void redirectSwingHand(ClientPlayerEntity player, Hand hand) {
        MinecraftClientMixinHook.redirectSwingHand(player, hand);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V"))
    public void afterSplashScreen(CallbackInfo ci) {
        MinecraftClientMixinHook.afterSplashScreen(ci);
    }
}
