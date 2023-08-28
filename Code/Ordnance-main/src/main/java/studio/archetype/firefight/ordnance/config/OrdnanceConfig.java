package studio.archetype.firefight.ordnance.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import studio.archetype.firefight.ordnance.client.OrdnanceClient;

@Config(name = OrdnanceClient.MOD_ID)
public class OrdnanceConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    Boolean bool = Boolean.FALSE;
}
