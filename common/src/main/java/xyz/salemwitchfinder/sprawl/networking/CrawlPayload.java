package xyz.salemwitchfinder.sprawl.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import xyz.salemwitchfinder.sprawl.Constants;

public record CrawlPayload(boolean crawling) implements CustomPacketPayload {
    public static final Type<CrawlPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "crawl"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CrawlPayload> CODEC = CustomPacketPayload.codec(CrawlPayload::write, CrawlPayload::new);

    private CrawlPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(this.crawling);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
