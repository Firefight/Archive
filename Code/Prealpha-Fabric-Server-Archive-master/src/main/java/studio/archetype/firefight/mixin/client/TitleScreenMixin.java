package studio.archetype.firefight.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.archetype.firefight.client.mixin_hook.TitleScreenMixinHook;

@Mixin(TitleScreen.class)
@Environment(EnvType.CLIENT)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgetsNormal", at = @At("HEAD"), cancellable = true)
    public void removeSinglePlayerButton(int y, int spacingY, CallbackInfo ci) {
        TitleScreenMixinHook.removeSinglePlayerButton(this, y, spacingY, ci);
    }
}
