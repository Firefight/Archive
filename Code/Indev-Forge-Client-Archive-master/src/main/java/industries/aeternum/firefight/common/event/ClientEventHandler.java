package industries.aeternum.firefight.common.event;

import industries.aeternum.firefight.FFConstants;
import industries.aeternum.firefight.common.network.FFNetworkHandler;
import industries.aeternum.firefight.common.network.MessageShoot;
import industries.aeternum.firefight.util.FFHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public enum ClientEventHandler
{
    INSTANCE;

    private final Minecraft mc = Minecraft.getMinecraft();

    private int leftClickDelay;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END && !mc.isGamePaused() && mc.player != null)
        {
            if (FFHelper.isHoldingGun(mc.player))
            {
                if (leftClickDelay <= 0 && mc.gameSettings.keyBindAttack.isKeyDown())
                {
//                    mc.player.connection.sendPacket(new CPacketCustomPayload(FFConstants.CHANNEL_SHOOT, new PacketBuffer(Unpooled.buffer()).writeString("Pew!")));
                    FFNetworkHandler.wrapper.sendToServer(new MessageShoot());
                    leftClickDelay = FFHelper.getFireRate(mc.player.getHeldItemMainhand());
                }

                float f = mc.player.ticksExisted / 12F;
                float f1 = 0.05F;

                if (FFHelper.isZoomed(mc.player))
                {
                    f1 /= 10;
                }

                mc.player.rotationYaw += MathHelper.sin(f / 2) * f1;
                mc.player.rotationPitch += MathHelper.cos(f) * f1;
            }

            if (leftClickDelay > 0)
            {
                --leftClickDelay;
            }
        }
    }

    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent event)
    {
        if (FFHelper.isZoomed(event.getEntity()))
        {
            event.setNewfov(0);
        }
    }
}
