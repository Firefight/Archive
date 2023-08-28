package studio.archetype.firefight.ordnance.hooks;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.gui.ConfigScreenProvider;
import studio.archetype.firefight.ordnance.client.OrdnanceClient;
import studio.archetype.firefight.ordnance.config.OrdnanceConfig;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigScreenProvider<OrdnanceConfig> provider = (ConfigScreenProvider<OrdnanceConfig>) AutoConfig.getConfigScreen(OrdnanceConfig.class, parent);
            provider.setOptionFunction((gen, field) -> "config." + OrdnanceClient.MOD_ID + "." + field.getName());
            return provider.get();
        };
    }
}
