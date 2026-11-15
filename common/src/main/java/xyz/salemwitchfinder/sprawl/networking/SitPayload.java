package xyz.salemwitchfinder.sprawl.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import xyz.salemwitchfinder.sprawl.Constants;


public record SitPayload(boolean sitting) implements CustomPacketPayload {
    public static final Type<SitPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sit"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SitPayload> CODEC = CustomPacketPayload.codec(SitPayload::write, SitPayload::new);

    private SitPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(this.sitting);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
