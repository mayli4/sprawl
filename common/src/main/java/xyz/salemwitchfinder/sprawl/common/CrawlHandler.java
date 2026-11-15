package xyz.salemwitchfinder.sprawl.common;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import xyz.salemwitchfinder.sprawl.PlayerCtx;

public final class CrawlHandler {
    private CrawlHandler() {}

    public static boolean canCrawl(Player player) {
        return PlayerCtx.of(player).canCrawl();
    }

    public static boolean canStayCrawling(Player player) {
        return PlayerCtx.of(player).canStayCrawling();
    }

    public static void setCrawling(Player player, boolean crawling) {
        if (crawling) {
            if (!canCrawl(player)) {
                return;
            }

            CrawlState.setCrawling(player.getUUID(), true);
            player.setPose(Pose.SWIMMING);
            player.refreshDimensions();
        } else {
            if (!CrawlState.isCrawling(player.getUUID()) && !player.hasPose(Pose.SWIMMING)) {
                return;
            }

            CrawlState.setCrawling(player.getUUID(), false);

            if (player.hasPose(Pose.SWIMMING) && !player.isInWater()) {
                player.setPose(Pose.STANDING);
                player.refreshDimensions();
            }
        }
    }
}
