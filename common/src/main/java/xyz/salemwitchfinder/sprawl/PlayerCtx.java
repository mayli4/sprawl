package xyz.salemwitchfinder.sprawl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import xyz.salemwitchfinder.sprawl.sit.SitState;

import java.util.UUID;

/** Snapshot of a players state. */
public record PlayerCtx(
        UUID playerId,
        String name,
        Pose pose,
        boolean sitting,
        boolean onGround,
        boolean passenger,
        boolean sleeping,
        boolean swimming,
        boolean fallFlying,
        boolean autoSpinAttack,
        boolean sprinting,
        boolean spectator,
        boolean inWater,
        boolean hurt,
        boolean dead,
        Hitbox size
) {
    public record Hitbox(float width, float height, float eyeHeight) {
        public static final Codec<Hitbox> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("width").forGetter(Hitbox::width),
                Codec.FLOAT.fieldOf("height").forGetter(Hitbox::height),
                Codec.FLOAT.fieldOf("eye_height").forGetter(Hitbox::eyeHeight)
        ).apply(instance, Hitbox::new));
    }

    public static final Codec<PlayerCtx> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.xmap(UUID::fromString, UUID::toString).fieldOf("player_id").forGetter(PlayerCtx::playerId),
            Codec.STRING.fieldOf("name").forGetter(PlayerCtx::name),
            Codec.INT.xmap(Pose.BY_ID::apply, Pose::id).fieldOf("pose").forGetter(PlayerCtx::pose),
            Codec.BOOL.fieldOf("sitting").forGetter(PlayerCtx::sitting),
            Codec.BOOL.fieldOf("on_ground").forGetter(PlayerCtx::onGround),
            Codec.BOOL.fieldOf("passenger").forGetter(PlayerCtx::passenger),
            Codec.BOOL.fieldOf("sleeping").forGetter(PlayerCtx::sleeping),
            Codec.BOOL.fieldOf("swimming").forGetter(PlayerCtx::swimming),
            Codec.BOOL.fieldOf("fall_flying").forGetter(PlayerCtx::fallFlying),
            Codec.BOOL.fieldOf("auto_spin_attack").forGetter(PlayerCtx::autoSpinAttack),
            Codec.BOOL.fieldOf("sprinting").forGetter(PlayerCtx::sprinting),
            Codec.BOOL.fieldOf("spectator").forGetter(PlayerCtx::spectator),
            Codec.BOOL.fieldOf("in_water").forGetter(PlayerCtx::inWater),
            Codec.BOOL.fieldOf("hurt").forGetter(PlayerCtx::hurt),
            Codec.BOOL.fieldOf("dead").forGetter(PlayerCtx::dead),
            Hitbox.CODEC.fieldOf("size").forGetter(PlayerCtx::size)
    ).apply(instance, PlayerCtx::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerCtx> STREAM_CODEC = StreamCodec.of(
            (buf, ctx) -> ctx.write(buf),
            PlayerCtx::read);

    public static PlayerCtx of(Player player) {
        UUID id = player.getUUID();
        return new PlayerCtx(
                id,
                player.getName().getString(),
                player.getPose(),
                SitState.isSitting(id),
                player.onGround(),
                player.isPassenger(),
                player.isSleeping(),
                player.isSwimming(),
                player.isFallFlying(),
                player.isAutoSpinAttack(),
                player.isSprinting(),
                player.isSpectator(),
                player.isInWater(),
                player.hurtTime > 0,
                player.isDeadOrDying(),
                new Hitbox(player.getBbWidth(), player.getBbHeight(), player.getEyeHeight()));
    }

    public boolean canSit() {
        if (dead) return false;
        if (sleeping) return false;
        if (passenger) return false;
        if (fallFlying) return false;
        if (swimming) return false;
        if (autoSpinAttack) return false;
        if (inWater && !onGround) return false;
        if (!onGround) return false;
        if (pose == Pose.SLEEPING) return false;

        return pose != Pose.DYING;
    }

    public boolean canStaySitting() {
        if (dead) return false;
        if (sleeping) return false;
        if (passenger) return false;
        if (fallFlying) return false;
        if (swimming) return false;
        if (autoSpinAttack) return false;
        if (spectator) return false;

        return !hurt;
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(this.playerId);
        buf.writeUtf(this.name);
        buf.writeEnum(this.pose);
        buf.writeBoolean(this.sitting);
        buf.writeBoolean(this.onGround);
        buf.writeBoolean(this.passenger);
        buf.writeBoolean(this.sleeping);
        buf.writeBoolean(this.swimming);
        buf.writeBoolean(this.fallFlying);
        buf.writeBoolean(this.autoSpinAttack);
        buf.writeBoolean(this.sprinting);
        buf.writeBoolean(this.spectator);
        buf.writeBoolean(this.inWater);
        buf.writeBoolean(this.hurt);
        buf.writeBoolean(this.dead);
        buf.writeFloat(this.size.width());
        buf.writeFloat(this.size.height());
        buf.writeFloat(this.size.eyeHeight());
    }

    private static PlayerCtx read(RegistryFriendlyByteBuf buf) {
        return new PlayerCtx(
                buf.readUUID(),
                buf.readUtf(),
                buf.readEnum(Pose.class),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                new Hitbox(buf.readFloat(), buf.readFloat(), buf.readFloat()));
    }
}
