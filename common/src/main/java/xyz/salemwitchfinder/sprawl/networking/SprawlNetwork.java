package xyz.salemwitchfinder.sprawl.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;

public final class SprawlNetwork {
    private SprawlNetwork() {}

    public static void sendSitToServer(boolean sitting) {
        send(new SitPayload(sitting));
    }

    public static void sendCrawlToServer(boolean crawling) {
        send(new CrawlPayload(crawling));
    }

    private static void send(net.minecraft.network.protocol.common.custom.CustomPacketPayload payload) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getConnection() == null) {
            return;
        }
        mc.getConnection().send(new ServerboundCustomPayloadPacket(payload));
    }
}
