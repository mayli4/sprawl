package xyz.salemwitchfinder.sprawl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import xyz.salemwitchfinder.sprawl.common.CrawlHandler;
import xyz.salemwitchfinder.sprawl.common.CrawlState;
import xyz.salemwitchfinder.sprawl.common.SitHandler;
import xyz.salemwitchfinder.sprawl.common.SitState;
import xyz.salemwitchfinder.sprawl.networking.CrawlPayload;
import xyz.salemwitchfinder.sprawl.networking.SitPayload;

public class SprawlMod implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();

        PayloadTypeRegistry.playC2S().register(SitPayload.TYPE, SitPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SitPayload.TYPE, (payload, context) -> {
            SitHandler.setSitting(context.player(), payload.sitting());
        });
        PayloadTypeRegistry.playC2S().register(CrawlPayload.TYPE, CrawlPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(CrawlPayload.TYPE, (payload, context) -> {
            CrawlHandler.setCrawling(context.player(), payload.crawling());
        });
        net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.DISCONNECT.register(
                (handler, server) -> {
                    SitState.clear(handler.player.getUUID());
                    CrawlState.clear(handler.player.getUUID());
                });
    }
}
