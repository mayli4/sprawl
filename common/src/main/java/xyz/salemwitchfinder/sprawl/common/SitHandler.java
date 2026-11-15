package xyz.salemwitchfinder.sprawl.common;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import xyz.salemwitchfinder.sprawl.PlayerCtx;

public final class SitHandler {
    public static final EntityDimensions SITTING_DIMENSIONS = EntityDimensions.scalable(0.6F, 1.2F).withEyeHeight(1.05F);

    public static final float SIT_MODEL_SHIFT_Y = 0.65F;

    private SitHandler() { }

    public static boolean canSit(Player player) {
        return PlayerCtx.of(player).canSit();
    }

    public static boolean canStaySitting(Player player) {
        return PlayerCtx.of(player).canStaySitting();
    }

    public static void setSitting(Player player, boolean sitting) {
        if (sitting) {
            if (!canSit(player)) {
                return;
            }
            SitState.setSitting(player.getUUID(), true);
            player.setPose(Pose.SITTING);
            player.refreshDimensions();
        } else {
            if (!SitState.isSitting(player.getUUID()) && !player.hasPose(Pose.SITTING)) {
                return;
            }
            SitState.setSitting(player.getUUID(), false);

            if (player.hasPose(Pose.SITTING)) {
                player.setPose(Pose.STANDING);
                player.refreshDimensions();
            }
        }
    }
}
