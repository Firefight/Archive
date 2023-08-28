package studio.archetype.firefight.client.mixin_hook

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.util.Hand
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import studio.archetype.firefight.client.ClientEnvironment
import studio.archetype.firefight.common.weapon.WeaponItem

@Environment(EnvType.CLIENT)
object MinecraftClientMixinHook {
    private var shouldSwingHand = true

    @JvmStatic
    fun doAttack(ci: CallbackInfo) {
        //        ActionResult result = ClientPlayerInteractionEvents.ATTACK_KEY_PRESS.invoker().onAttackKeyPress(player, crosshairTarget);
//        shouldSwing = result != ActionResult.FAIL;
//        if (result != ActionResult.PASS) {
//            ci.cancel();
//        }
        val player = MinecraftClient.getInstance().player ?: return
        if (player.mainHandStack.item is WeaponItem) {
            val weaponItem = player.mainHandStack.item as WeaponItem
            shouldSwingHand = false
            weaponItem.shoot(player)
            ci.cancel()
        }
    }

    @JvmStatic
    fun redirectSwingHand(player: ClientPlayerEntity, hand: Hand) {
        if (shouldSwingHand) player.swingHand(hand)
    }

    @JvmStatic
    fun afterSplashScreen(ci: CallbackInfo) {
        ClientEnvironment.createUiCore()
    }
}