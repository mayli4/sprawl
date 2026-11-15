package xyz.salemwitchfinder.sprawl.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;

public final class SitNetwork {
    private SitNetwork() {
    }

    public static void sendSitToServer(boolean sitting) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getConnection() == null) {
            return;
        }
        mc.getConnection().send(new ServerboundCustomPayloadPacket(new SitPayload(sitting)));
    }
}
