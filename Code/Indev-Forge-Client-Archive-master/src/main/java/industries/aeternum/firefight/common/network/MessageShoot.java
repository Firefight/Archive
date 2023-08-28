package industries.aeternum.firefight.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageShoot extends AbstractMessage<MessageShoot>
{
    @Override
    public void receive() throws MessageException
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, "Pew-pew!");
    }
}
