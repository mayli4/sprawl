package xyz.salemwitchfinder.sprawl;

import xyz.salemwitchfinder.sprawl.common.client.SprawlConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SprawlConfigScreen::build;
    }
}
