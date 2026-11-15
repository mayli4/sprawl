package xyz.salemwitchfinder.sprawl.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.salemwitchfinder.sprawl.sit.SitHandler;
import xyz.salemwitchfinder.sprawl.sit.SitState;

@Mixin(Player.class)
public abstract class MixinPlayerSit {
    @Inject(
            method = "updatePlayerPose",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sprawl$enforceSittingPose(
            CallbackInfo ci) {
        Player self = (Player) (Object) this;

        if (SitState.isSitting(self.getUUID())) {
            if (SitHandler.canStaySitting(self)) {
                self.setPose(Pose.SITTING);
                ci.cancel();
            } else {
                SitState.setSitting(self.getUUID(), false);
            }
            return;
        }

        if (self.level().isClientSide && self.hasPose(Pose.SITTING)) {
            if (SitHandler.canStaySitting(self)) {
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "getDefaultDimensions",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sprawl$sittingDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (pose == Pose.SITTING) {
            cir.setReturnValue(SitHandler.SITTING_DIMENSIONS);
        }
    }
}
