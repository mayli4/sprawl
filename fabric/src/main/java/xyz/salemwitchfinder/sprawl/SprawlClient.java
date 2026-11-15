package xyz.salemwitchfinder.sprawl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import xyz.salemwitchfinder.sprawl.common.SprawlCommonKeys;

public class SprawlClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(SprawlCommonKeys.SIT_KEY);
        KeyBindingHelper.registerKeyBinding(SprawlCommonKeys.CRAWL_KEY);
    }
}
