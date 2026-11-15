package xyz.salemwitchfinder.sprawl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import xyz.salemwitchfinder.sprawl.sit.SitKeys;

public class SprawlClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(SitKeys.SIT_KEY);
    }
}
