package studio.archetype.firefight.ordnance.client;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import studio.archetype.firefight.ordnance.extension.KeyBindingExt;

import java.util.Arrays;
import java.util.List;

public final class KeyBinds {

    private static final List<ClickBind> clickBinds = Lists.newArrayList();
    private static final List<HoldBind> holdBinds = Lists.newArrayList();

    static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(c -> {
            clickBinds.forEach(b -> {
                if(b.vanillaKeybind.wasPressed())
                    b.onPress.onTick(c, b.vanillaKeybind);
            });
            holdBinds.forEach(b -> b.held(c, b.vanillaKeyBind.isPressed()));
        });
    }

    public static KeyBinding registerPushKeyBinding(KeyBinding keyBind, KeyTick tick) {
        clickBinds.add(new ClickBind(KeyBindingHelper.registerKeyBinding(keyBind), tick));
        return keyBind;
    }

    public static KeyBinding registerHeldKeyBinding(KeyBinding keyBind, KeyTick onPress, KeyTick onRelease) {
        holdBinds.add(new HoldBind(KeyBindingHelper.registerKeyBinding(keyBind), onPress, onRelease));
        return keyBind;
    }

    public static void removeVanillaKeyBinds(KeyBinding... keyBinds) {
        List<KeyBinding> binds = Lists.newArrayList(Arrays.asList(MinecraftClient.getInstance().options.allKeys));
        List<KeyBinding> banishedOnes = Lists.newArrayList(Arrays.asList(keyBinds));
        banishedOnes.forEach(k -> ((KeyBindingExt)k).setEnabled(false));
        binds.removeAll(banishedOnes);
        clickBinds.forEach(b -> {
            binds.remove(b.vanillaKeybind);
            binds.add(b.vanillaKeybind);
        });
        holdBinds.forEach(b -> {
            binds.remove(b.vanillaKeyBind);
            binds.add(b.vanillaKeyBind);
        });
        MinecraftClient.getInstance().options.allKeys = binds.toArray(new KeyBinding[0]);
    }

    @FunctionalInterface
    public interface KeyTick { void onTick(MinecraftClient client, KeyBinding keybinding); }

    private record ClickBind(KeyBinding vanillaKeybind, KeyTick onPress) { }

    private static class HoldBind {
        private final KeyBinding vanillaKeyBind;
        private final KeyTick whileHeld, onRelease;

        private boolean isHeld = false;

        private HoldBind(KeyBinding vanillaKeyBind, KeyTick whileHeld, KeyTick onRelease) {
            this.vanillaKeyBind = vanillaKeyBind;
            this.whileHeld = whileHeld;
            this.onRelease = onRelease;
        }

        private void held(MinecraftClient client, boolean held) {
            if(!isHeld && held)
                isHeld = true;

            if(isHeld && !held) {
                isHeld = false;
                onRelease.onTick(client, vanillaKeyBind);
            }

            if(isHeld)
                whileHeld.onTick(client, vanillaKeyBind);
        }
    }
}
