package studio.archetype.firefight.ordnance.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class ClientConfigManager {
    public static OrdnanceConfig CONFIG;
    public static void register() {
        AutoConfig.register(OrdnanceConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(OrdnanceConfig.class).getConfig();
    }
}
