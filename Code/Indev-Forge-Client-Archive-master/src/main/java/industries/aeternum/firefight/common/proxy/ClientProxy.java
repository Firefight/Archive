package industries.aeternum.firefight.common.proxy;

import industries.aeternum.firefight.common.event.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy
{
    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void preInit()
    {
        super.preInit();
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
    }

    @Override
    public EntityPlayer getClient()
    {
        return mc.player;
    }

    @Override
    public void schedule(MessageContext ctx, Runnable runnable)
    {
        if (ctx.side.isClient())
        {
            mc.addScheduledTask(runnable);
        }
        else
        {
            super.schedule(ctx, runnable);
        }
    }
}
