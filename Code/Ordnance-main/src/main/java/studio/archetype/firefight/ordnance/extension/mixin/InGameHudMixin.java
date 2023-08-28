package studio.archetype.firefight.ordnance.extension.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.SpectatorHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import studio.archetype.firefight.ordnance.client.OrdnanceClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.archetype.firefight.ordnance.client.motiontracker.MotionTrackerManager;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V"))
    private void overwriteHotbarRender(InGameHud instance, float tickDelta, MatrixStack matrices) { } // Do nothing

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderExperienceBar(Lnet/minecraft/client/util/math/MatrixStack;I)V"))
    private void overwriteExpRender(InGameHud instance, MatrixStack matrices, int x) { } // Do nothing

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusBars(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    private void overwriteStatusRender(InGameHud instance, MatrixStack matrices) { } // Do nothing

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    private void overwriteEffectRender(InGameHud instance, MatrixStack matrices) { } // Do nothing

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/SpectatorHud;renderSpectatorMenu(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    private void overwriteSpectatorRender(SpectatorHud instance, MatrixStack matrices) { } // Do nothing

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHeldItemTooltip(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    private void overwriteTooltipRender(InGameHud instance, MatrixStack matrices) {}

//    @Inject(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerInventory;getMainHandStack()Lnet/minecraft/item/ItemStack;", shift = At.Shift.BEFORE))
//    private void tick(CallbackInfo info) {
//        if(MotionTrackerManager.INSTANCE.getSettings().isShowRadar())
//            MotionTrackerManager.INSTANCE.getWidget().tick();
//    }
//
//    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.AFTER))
//    private void render(MatrixStack stack, float delta, CallbackInfo info) {
//        if(MotionTrackerManager.INSTANCE.getSettings().isShowRadar())
//            MotionTrackerManager.INSTANCE.getWidget().render(stack);
//    }

    @Inject(method = "addChatMessage", at = @At(value = "HEAD"), cancellable = true)
    public void interceptSystemMessage(MessageType type, Text message, UUID sender, CallbackInfo ci) {
        if (type == MessageType.CHAT) {
            String messageString = message.asString();
            if (messageString.startsWith("FIREFIGHT_SYSTEM_CONNECT")) {
                List<String> parts = Arrays.asList(messageString.split(" "));

                UUID code = UUID.fromString(parts.get(1));
//                String ip = "131.153.7.186";
                String ip = "localhost";
                int port = 42069; // haha funny number

                OrdnanceClient.INSTANCE.getNetworkManager().login(code, ip, port);
                ci.cancel();
            }
        }
    }
}
