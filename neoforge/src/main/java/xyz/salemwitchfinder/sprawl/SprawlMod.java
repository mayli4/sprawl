package xyz.salemwitchfinder.sprawl;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import xyz.salemwitchfinder.sprawl.sit.SitHandler;
import xyz.salemwitchfinder.sprawl.networking.SitPayload;
import xyz.salemwitchfinder.sprawl.sit.SitState;

@Mod(Constants.MOD_ID)
public class SprawlMod {

    public SprawlMod(IEventBus eventBus) {
        CommonClass.init();

        eventBus.addListener(this::registerPayloads);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            SprawlClient.register(eventBus);
        }

        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedOutEvent e) -> {
            if (e.getEntity() != null) {
                SitState.clear(e.getEntity().getUUID());
            }
        });
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(
                SitPayload.TYPE,
                SitPayload.CODEC,
                (payload, ctx) -> SitHandler.setSitting(
                        ctx.player(), payload.sitting()));
    }
}