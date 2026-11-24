package xyz.salemwitchfinder.sprawl.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.salemwitchfinder.sprawl.common.config.SprawlConfig;
import xyz.salemwitchfinder.sprawl.networking.SprawlNetwork;
import xyz.salemwitchfinder.sprawl.common.CrawlHandler;
import xyz.salemwitchfinder.sprawl.common.CrawlState;
import xyz.salemwitchfinder.sprawl.common.SitHandler;
import xyz.salemwitchfinder.sprawl.common.SprawlCommonKeys;
import xyz.salemwitchfinder.sprawl.common.SitState;


@Mixin(Minecraft.class)
public abstract class MixinMinecraftSitTick {
    @Unique
    private static boolean sitKeyWasDown = false;
    @Unique
    private static boolean crawlKeyWasDown = false;

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void sprawl$clientSitTick(
            CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;

        if (mc.player == null || mc.level == null) {
            return;
        }

        boolean keyDown = SprawlCommonKeys.SIT_KEY.isDown();
        boolean pressed = keyDown && !sitKeyWasDown;
        sitKeyWasDown = keyDown;

        boolean crawlDown = SprawlCommonKeys.CRAWL_KEY.isDown();
        boolean crawlPressed = crawlDown && !crawlKeyWasDown;
        crawlKeyWasDown = crawlDown;

        if (mc.screen != null) {
            return;
        }

        SprawlConfig config = SprawlConfig.get();

        if (config.sitToggle) {
            if (pressed) {
                boolean currently = SitState.isSitting(mc.player.getUUID()) || mc.player.hasPose(Pose.SITTING);
                boolean iWannaSit = !currently;

                if (!iWannaSit || SitHandler.canSit(mc.player)) {
                    SitState.setSitting(mc.player.getUUID(), iWannaSit);
                    mc.player.setPose(iWannaSit ? Pose.SITTING : Pose.STANDING);
                    mc.player.refreshDimensions();
                    SprawlNetwork.sendSitToServer(iWannaSit);
                }
            }
        } else if (SprawlCommonKeys.SIT_KEY.isDown()) {
            if (!SitState.isSitting(mc.player.getUUID()) && !mc.player.hasPose(Pose.SITTING)
                    && SitHandler.canSit(mc.player)) {
                SitState.setSitting(mc.player.getUUID(), true);
                mc.player.setPose(Pose.SITTING);
                mc.player.refreshDimensions();
                SprawlNetwork.sendSitToServer(true);
            }
        } else if (SitState.isSitting(mc.player.getUUID())) {
            SitState.setSitting(mc.player.getUUID(), false);
            if (mc.player.hasPose(Pose.SITTING)) {
                mc.player.setPose(Pose.STANDING);
                mc.player.refreshDimensions();
            }
            SprawlNetwork.sendSitToServer(false);
        }

        if (SitState.isSitting(mc.player.getUUID()) || mc.player.hasPose(Pose.SITTING)) {
            boolean movePressed = mc.options.keyUp.isDown()
                    || mc.options.keyDown.isDown()
                    || mc.options.keyLeft.isDown()
                    || mc.options.keyRight.isDown()
                    || mc.options.keyJump.isDown();

            boolean sneakPressed = mc.options.keyShift.isDown();
            boolean moveCancels = config.sitToggle && (movePressed || sneakPressed);
            if (moveCancels || mc.player.hurtTime > 0 || !SitHandler.canStaySitting(mc.player)) {

                boolean wasSitting = SitState.isSitting(mc.player.getUUID());
                SitState.setSitting(mc.player.getUUID(), false);
                if (mc.player.hasPose(Pose.SITTING)) {
                    mc.player.setPose(Pose.STANDING);
                    mc.player.refreshDimensions();
                }
                if (wasSitting) {
                    SprawlNetwork.sendSitToServer(false);
                }
            }
        }

        if (config.crawlToggle) {
            if (crawlPressed) {
                boolean crawling = CrawlState.isCrawling(mc.player.getUUID()) || (mc.player.hasPose(Pose.SWIMMING) && !mc.player.isInWater());
                boolean iWannaCrawl = !crawling;

                if (SitState.isSitting(mc.player.getUUID()) || mc.player.hasPose(Pose.SITTING)) {
                    boolean wasSitting = SitState.isSitting(mc.player.getUUID());
                    SitState.setSitting(mc.player.getUUID(), false);
                    if (mc.player.hasPose(Pose.SITTING)) {
                        mc.player.setPose(Pose.STANDING);
                        mc.player.refreshDimensions();
                    }
                    if (wasSitting) {
                        SprawlNetwork.sendSitToServer(false);
                    }
                }
                if (!iWannaCrawl || CrawlHandler.canCrawl(mc.player)) {
                    CrawlState.setCrawling(mc.player.getUUID(), iWannaCrawl);
                    if (iWannaCrawl) {
                        mc.player.setPose(Pose.SWIMMING);
                    } else if (mc.player.hasPose(Pose.SWIMMING) && !mc.player.isInWater()) {
                        mc.player.setPose(Pose.STANDING);
                    }
                    mc.player.refreshDimensions();
                    SprawlNetwork.sendCrawlToServer(iWannaCrawl);
                }
            }
        } else if (SprawlCommonKeys.CRAWL_KEY.isDown()) {
            if (SitState.isSitting(mc.player.getUUID()) || mc.player.hasPose(Pose.SITTING)) {
                boolean wasSitting = SitState.isSitting(mc.player.getUUID());
                SitState.setSitting(mc.player.getUUID(), false);
                if (mc.player.hasPose(Pose.SITTING)) {
                    mc.player.setPose(Pose.STANDING);
                    mc.player.refreshDimensions();
                }
                if (wasSitting) {
                    SprawlNetwork.sendSitToServer(false);
                }
            }
            if (!CrawlState.isCrawling(mc.player.getUUID()) && CrawlHandler.canCrawl(mc.player)) {
                CrawlState.setCrawling(mc.player.getUUID(), true);
                mc.player.setPose(Pose.SWIMMING);
                mc.player.refreshDimensions();
                SprawlNetwork.sendCrawlToServer(true);
            }
        } else if (CrawlState.isCrawling(mc.player.getUUID())) {
            CrawlState.setCrawling(mc.player.getUUID(), false);
            if (mc.player.hasPose(Pose.SWIMMING) && !mc.player.isInWater()) {
                mc.player.setPose(Pose.STANDING);
                mc.player.refreshDimensions();
            }
            SprawlNetwork.sendCrawlToServer(false);
        }
    }
}
