package xyz.salemwitchfinder.sprawl;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import xyz.salemwitchfinder.sprawl.sit.SitKeys;

public final class SprawlClient {
    private SprawlClient() {
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(SprawlClient::registerKeys);
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(SitKeys.SIT_KEY);
    }
}
