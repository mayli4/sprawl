package xyz.salemwitchfinder.sprawl.mixin;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.salemwitchfinder.sprawl.common.CrawlHandler;
import xyz.salemwitchfinder.sprawl.common.CrawlState;

@Mixin(Player.class)
public abstract class MixinPlayerCrawl {
    @Inject(
            method = "updatePlayerPose",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sprawl$enforceCrawlingPose(
            CallbackInfo ci) {

        Player self = (Player) (Object) this;

        if (CrawlState.isCrawling(self.getUUID())) {
            if (CrawlHandler.canStayCrawling(self)) {
                self.setPose(Pose.SWIMMING);
                ci.cancel();
            } else {
                CrawlState.setCrawling(self.getUUID(), false);
            }
            return;
        }

        if (self.level().isClientSide && self.hasPose(Pose.SWIMMING)
                && !self.isInWater() && CrawlHandler.canStayCrawling(self)) {
            ci.cancel();
        }
    }
}
