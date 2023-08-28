package industries.aeternum.firefight;

import industries.aeternum.firefight.common.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Firefight.MODID, name = Firefight.NAME, version = Firefight.VERSION)
public class Firefight
{
    public static final String NAME = "Firefight";
    public static final String MODID = "firefight";
    public static final String VERSION = "2019.2-1.3.1";

    @Mod.Instance(MODID)
    public static Firefight INSTANCE;

    @SidedProxy(clientSide = "industries.aeternum.firefight.common.proxy.ClientProxy", serverSide = "industries.aeternum.firefight.common.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }
}
