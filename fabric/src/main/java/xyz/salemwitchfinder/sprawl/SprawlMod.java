package xyz.salemwitchfinder.sprawl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import xyz.salemwitchfinder.sprawl.sit.SitHandler;
import xyz.salemwitchfinder.sprawl.networking.SitPayload;

public class SprawlMod implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();

        PayloadTypeRegistry.playC2S().register(SitPayload.TYPE, SitPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SitPayload.TYPE, (payload, context) -> {
            SitHandler.setSitting(context.player(), payload.sitting());
        });
        net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.DISCONNECT.register(
                (handler, server) -> xyz.salemwitchfinder.sprawl.sit.SitState.clear(handler.player.getUUID()));
    }
}
