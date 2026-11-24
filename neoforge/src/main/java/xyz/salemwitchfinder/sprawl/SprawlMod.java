package xyz.salemwitchfinder.sprawl;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import xyz.salemwitchfinder.sprawl.common.CrawlHandler;
import xyz.salemwitchfinder.sprawl.common.CrawlState;
import xyz.salemwitchfinder.sprawl.common.SitHandler;
import xyz.salemwitchfinder.sprawl.networking.CrawlPayload;
import xyz.salemwitchfinder.sprawl.networking.SitPayload;
import xyz.salemwitchfinder.sprawl.common.SitState;

@Mod(Constants.MOD_ID)
public class SprawlMod {

    public SprawlMod(IEventBus eventBus) {
        CommonClass.init();

        eventBus.addListener(this::registerPayloads);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            SprawlClient.register(eventBus);
            SprawlNeoForgeClient.init();
        }

        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedOutEvent e) -> {
            if (e.getEntity() != null) {
                SitState.clear(e.getEntity().getUUID());
                CrawlState.clear(e.getEntity().getUUID());
            }
        });
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToServer(
                SitPayload.TYPE,
                SitPayload.CODEC,
                (payload, ctx) -> SitHandler.setSitting(
                        ctx.player(), payload.sitting()));

        registrar.playToServer(
                CrawlPayload.TYPE,
                CrawlPayload.CODEC,
                (payload, ctx) -> CrawlHandler.setCrawling(
                        ctx.player(), payload.crawling()));
    }
}