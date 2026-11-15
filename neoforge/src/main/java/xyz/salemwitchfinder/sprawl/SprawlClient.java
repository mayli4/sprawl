package xyz.salemwitchfinder.sprawl;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import xyz.salemwitchfinder.sprawl.common.SprawlCommonKeys;

public final class SprawlClient {
    private SprawlClient() {
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(SprawlClient::registerKeys);
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(SprawlCommonKeys.SIT_KEY);
        event.register(SprawlCommonKeys.CRAWL_KEY);
    }
}
