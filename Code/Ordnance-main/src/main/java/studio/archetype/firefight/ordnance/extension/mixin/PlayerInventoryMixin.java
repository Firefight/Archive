package studio.archetype.firefight.ordnance.extension.mixin;

import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Inject(method = "scrollInHotbar", at = @At("HEAD"), cancellable = true)
    private void disableHotbarScroll(double scrollAmount, CallbackInfo ci) { ci.cancel(); } // Disable
}
