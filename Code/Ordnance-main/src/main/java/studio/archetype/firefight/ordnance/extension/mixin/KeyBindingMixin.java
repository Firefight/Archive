package studio.archetype.firefight.ordnance.extension.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import studio.archetype.firefight.ordnance.extension.KeyBindingExt;

import java.util.Map;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements KeyBindingExt {

    @Shadow @Final private String category;
    @Shadow @Final private String translationKey;
    @Shadow @Final private static Map<String, Integer> CATEGORY_ORDER_MAP;
    @Unique private boolean enabled = true;

    public void setEnabled(boolean is) { this.enabled = is; }
    public boolean isEnabled() { return enabled; }

    @Inject(method = "wasPressed", at = @At("HEAD"), cancellable = true)
    private void disableKey(CallbackInfoReturnable<Boolean> ret) {
        if(!enabled)
            ret.setReturnValue(false);
    }

    /**
     * @author

    @Overwrite
    public int compareTo(KeyBinding keybind) {
        if (this.category.equals(keybind.getCategory()))
            return I18n.translate(this.translationKey).compareTo(I18n.translate(keybind.getTranslationKey()));
        else if(CATEGORY_ORDER_MAP.containsKey(this.category))
            return CATEGORY_ORDER_MAP.get(this.category).compareTo(CATEGORY_ORDER_MAP.get(keybind.getCategory()));
        else
            return 1;
    }     */
}
