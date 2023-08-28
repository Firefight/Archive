package industries.aeternum.firefight.common.proxy;

import industries.aeternum.firefight.common.event.CommonEventHandler;
import industries.aeternum.firefight.common.network.FFNetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy
{
    public void preInit()
    {
        FFNetworkHandler.register();
        MinecraftForge.EVENT_BUS.register(CommonEventHandler.INSTANCE);
    }

    public void init()
    {
    }

    public void postInit()
    {
    }

    public EntityPlayer getClient()
    {
        return null;
    }

    public void schedule(MessageContext ctx, Runnable runnable)
    {
        WorldServer server = ctx.getServerHandler().player.getServerWorld();
        server.addScheduledTask(runnable);
    }
}
