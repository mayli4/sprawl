package xyz.salemwitchfinder.sprawl;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import xyz.salemwitchfinder.sprawl.common.CrawlState;
import xyz.salemwitchfinder.sprawl.common.SitState;

import java.util.UUID;

/** Snapshot of a players state. */
public record PlayerCtx(
        UUID playerId,
        String name,
        Pose pose,
        boolean sitting,
        boolean crawling,
        boolean onGround,
        boolean passenger,
        boolean sleeping,
        boolean swimming,
        boolean fallFlying,
        boolean flying,
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
                CrawlState.isCrawling(id),
                player.onGround(),
                player.isPassenger(),
                player.isSleeping(),
                player.isSwimming(),
                player.isFallFlying(),
                player.getAbilities().flying,
                player.isAutoSpinAttack(),
                player.isSprinting(),
                player.isSpectator(),
                player.isInWater(),
                player.hurtTime > 0,
                player.isDeadOrDying(),
                new Hitbox(player.getBbWidth(), player.getBbHeight(), player.getEyeHeight()));
    }

    public boolean canCrawl() {
        if (sitting) return false;
        if (dead) return false;
        if (sleeping) return false;
        if (passenger) return false;
        if (fallFlying) return false;
        if (flying) return false;
        if (swimming) return false;
        if (autoSpinAttack) return false;
        if (spectator) return false;
        if (inWater) return false;
        if (!onGround) return false;
        if (pose == Pose.SLEEPING) return false;

        return pose != Pose.DYING;
    }

    public boolean canStayCrawling() {
        if (dead) return false;
        if (sleeping) return false;
        if (passenger) return false;
        if (fallFlying) return false;
        if (flying) return false;
        if (autoSpinAttack) return false;
        if (spectator) return false;

        return true;
    }

    public boolean canSit() {
        if (crawling) return false;
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
