package industries.aeternum.firefight.common.network;

import industries.aeternum.firefight.Firefight;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class FFNetworkHandler
{
    public static SimpleNetworkWrapper wrapper;

    public static void register()
    {
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Firefight.MODID);

        registerMessage(MessageShoot.class, 0, Side.SERVER);
    }

    public static <T extends IMessage & IMessageHandler<T, IMessage>> void registerMessage(Class<T> msg, int id, Side side)
    {
        wrapper.registerMessage(msg, msg, id, side);
    }
}
