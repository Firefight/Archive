package studio.archetype.firefight.ordnance.client.motiontracker;

import lombok.Getter;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexConsumerProvider;
import studio.archetype.firefight.ordnance.client.rendering.RadarDebugRenderer;
import studio.archetype.firefight.ordnance.client.rendering.RadarWidget;

public class MotionTrackerManager {

    public static MotionTrackerManager INSTANCE;

    @Getter private final MotionTrackerSettings settings;
    @Getter private final RadarWidget widget;

    private final RadarDebugRenderer debugRenderer;

    public MotionTrackerManager() {
        this.settings = new MotionTrackerSettings();
        this.widget = new RadarWidget();
        this.debugRenderer = new RadarDebugRenderer();
        WorldRenderEvents.AFTER_ENTITIES.register(l ->  {
            if(settings.isEnableDebug() && settings.isShowRadar()) {
                VertexConsumerProvider.Immediate provider = VertexConsumerProvider.immediate(new BufferBuilder(256));
                debugRenderer.render(l.matrixStack(), l.tickDelta(), provider, l.camera());
            }
        });
    }

    public static void init() {
        INSTANCE = new MotionTrackerManager();
    }
}
