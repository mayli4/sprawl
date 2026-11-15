package xyz.salemwitchfinder.sprawl;

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
    }

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
}
