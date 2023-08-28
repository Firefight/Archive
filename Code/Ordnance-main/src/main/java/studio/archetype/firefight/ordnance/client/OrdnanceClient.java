package studio.archetype.firefight.ordnance.client;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import studio.archetype.firefight.ordnance.client.motiontracker.MotionTrackerManager;
import studio.archetype.firefight.ordnance.config.ClientConfigManager;
import studio.archetype.firefight.ordnance.net.NetworkManager;
import studio.archetype.firefight.net.packets.serverbound.FirePacket;
import studio.archetype.firefight.ordnance.ui.OrdnanceUI;

@Environment(EnvType.CLIENT)
public class OrdnanceClient implements ClientModInitializer {
    public static final String MOD_ID = "ordnance";
    public static OrdnanceClient INSTANCE;

    @Getter
    private NetworkManager networkManager;

    public static Identifier id(String value) {
        return new Identifier(MOD_ID, value);
    }

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        ClientConfigManager.register();
        ClientLifecycleEvents.CLIENT_STARTED.register(c -> registerKeybinds());

        networkManager = new NetworkManager();
        MotionTrackerManager.init();

        OrdnanceUI.init();
    }

    private void registerKeybinds() {
        KeyBinds.registerPushKeyBinding(new KeyBinding("key.ordnance.fire", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_LEFT, "category.ordnance.match"), (c, k) -> {
            if(c.player == null)
                return;
            networkManager.getConnection().sendPacket(new FirePacket());
            c.player.sendMessage(new LiteralText("Pew?"), true);
        });

        KeyBinds.registerHeldKeyBinding(new KeyBinding("key.ordnance.zoom", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "category.ordnance.match"), (c, k) -> {
            if(c.player == null)
                return;
            ItemStack stack = c.player.getEquippedStack(EquipmentSlot.MAINHAND);
            if(c.options.getPerspective().isFirstPerson() && stack.hasNbt() && stack.getNbt().contains("Zoom") && k.isPressed())
                ZoomHandler.get().setZoom(stack.getNbt().getFloat("Zoom"));
            else
                ZoomHandler.get().stopZoom();
        }, (c, k) -> ZoomHandler.get().stopZoom());

        GameOptions opt = MinecraftClient.getInstance().options;
        KeyBinds.removeVanillaKeyBinds(opt.attackKey, opt.useKey, opt.pickItemKey,
                opt.inventoryKey, opt.dropKey, opt.swapHandsKey,
                opt.loadToolbarActivatorKey, opt.saveToolbarActivatorKey, opt.spectatorOutlinesKey, opt.advancementsKey);
        KeyBinds.removeVanillaKeyBinds(opt.hotbarKeys);

        KeyBinds.init();
    }
}
