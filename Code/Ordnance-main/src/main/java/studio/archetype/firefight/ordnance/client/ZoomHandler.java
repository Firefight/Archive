package studio.archetype.firefight.ordnance.client;

import io.github.ennuil.libzoomer.api.ZoomInstance;
import io.github.ennuil.libzoomer.api.ZoomRegistry;
import io.github.ennuil.libzoomer.api.modifiers.SpyglassMouseModifier;
import io.github.ennuil.libzoomer.api.transitions.SmoothTransitionMode;
import studio.archetype.firefight.net.packets.serverbound.ScopePacket;

public class ZoomHandler {

    private static ZoomHandler instance;

    private static final ZoomInstance smoothZoomSlowMod = new ZoomInstance(
            OrdnanceClient.id("zoom"), 1,
            new SmoothTransitionMode(), new SpyglassMouseModifier(),
            null);

    public static ZoomHandler get() {
        return instance == null ? (instance = new ZoomHandler()) : instance;
    }

    public void setZoom(float zoom) {
        if(smoothZoomSlowMod.getZoom())
            return;
        smoothZoomSlowMod.setZoomDivisor(zoom);
        smoothZoomSlowMod.setZoom(true);
       OrdnanceClient.INSTANCE.getNetworkManager().getConnection().sendPacket(new ScopePacket(true));
    }

    public void stopZoom() {
        if(!smoothZoomSlowMod.getZoom())
            return;
        smoothZoomSlowMod.setZoom(false);
        smoothZoomSlowMod.resetZoomDivisor();
        OrdnanceClient.INSTANCE.getNetworkManager().getConnection().sendPacket(new ScopePacket(false));
    }

    private ZoomHandler() {
        ZoomRegistry.registerInstance(smoothZoomSlowMod);
    }
}
