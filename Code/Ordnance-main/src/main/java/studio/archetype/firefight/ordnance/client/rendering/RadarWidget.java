package studio.archetype.firefight.ordnance.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import studio.archetype.firefight.ordnance.client.OrdnanceClient;
import studio.archetype.firefight.ordnance.client.motiontracker.MotionTrackerManager;
import studio.archetype.firefight.ordnance.client.motiontracker.RadarContent;
import studio.archetype.firefight.ordnance.client.motiontracker.RadarSegment;
import studio.archetype.firefight.ordnance.client.motiontracker.enums.RadarDirection;
import studio.archetype.firefight.ordnance.client.motiontracker.enums.RadarDistance;
import studio.archetype.firefight.ordnance.client.motiontracker.enums.VerticalDiff;

public class RadarWidget {

    private static final Identifier TEX_PLAYER = OrdnanceClient.id("textures/arrow.png");
    private static final Identifier TEX_BASE = OrdnanceClient.id("textures/base.png");
    private static final Identifier TEX_BASE_SOFT = OrdnanceClient.id("textures/base_softer.png");

    private final RadarContent content;

    public RadarWidget() {
        this.content = new RadarContent();
    }

    public void tick() {
        content.update();
    }

    public void render(MatrixStack stack) {
        stack.push();

        stack.scale(.1F, .1F, 1);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        drawTexture(stack, MotionTrackerManager.INSTANCE.getSettings().isAltBase() ? TEX_BASE : TEX_BASE_SOFT);           // Draw the frame
        if(content.hasCenter())
            drawTexture(stack, getTexture(RadarDistance.CENTER, null, content.getYDiff())); // Draw the center circle
        for(RadarDirection dir : RadarDirection.values()) {                                         // Draw the directional segments
            RadarSegment segment = content.getSegment(dir);
            if(segment.isInner())
                drawTexture(stack, getTexture(RadarDistance.INNER, dir, segment.getYDiff()));       // Draw inner ring
            if(segment.isOuter())
                drawTexture(stack, getTexture(RadarDistance.OUTER, dir, null));                 // Draw the outer ring
        }
        drawTexture(stack, TEX_PLAYER);                                                             // Draw the player icon

        RenderSystem.disableBlend();

        stack.pop();
    }

    private void drawTexture(MatrixStack stack, Identifier id) {
        RenderSystem.setShaderTexture(0, id);
        DrawableHelper.drawTexture(stack, MotionTrackerManager.INSTANCE.getSettings().getOffsetX() * 10, MotionTrackerManager.INSTANCE.getSettings().getOffsetY() * 10, 0, 0, 1024, 1024, 1024, 1024);
    }

    private Identifier getTexture(RadarDistance distance, RadarDirection direction, VerticalDiff diff) {
        StringBuilder builder = new StringBuilder("textures/").append(distance.getTextureKey()).append("/");

        if(distance == RadarDistance.CENTER) {
            builder.append(distance.getTextureKey()).append("_").append(diff.getTextureKey()).append(".png");
            return OrdnanceClient.id(builder.toString());
        }

        if(distance == RadarDistance.INNER)
            builder.append(diff.getTextureKey()).append("/");
        builder.append(direction.getTextureKey()).append(".png");

        return OrdnanceClient.id(builder.toString());
    }
}
