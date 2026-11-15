package xyz.salemwitchfinder.sprawl.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.salemwitchfinder.sprawl.networking.SitNetwork;
import xyz.salemwitchfinder.sprawl.sit.SitHandler;
import xyz.salemwitchfinder.sprawl.sit.SitKeys;
import xyz.salemwitchfinder.sprawl.sit.SitState;


@Mixin(Minecraft.class)
public abstract class MixinMinecraftSitTick {
    @Unique
    private static boolean sitKeyWasDown = false;

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

        boolean keyDown = SitKeys.SIT_KEY.isDown();
        boolean pressed = keyDown && !sitKeyWasDown;
        sitKeyWasDown = keyDown;

        if (mc.screen != null) {
            return;
        }

        if (pressed) {
            boolean currently = SitState.isSitting(mc.player.getUUID()) || mc.player.hasPose(Pose.SITTING);
            boolean iWannaSit = !currently;

            if (!iWannaSit || SitHandler.canSit(mc.player)) {
                SitState.setSitting(mc.player.getUUID(), iWannaSit);
                mc.player.setPose(iWannaSit ? Pose.SITTING : Pose.STANDING);
                mc.player.refreshDimensions();
                SitNetwork.sendSitToServer(iWannaSit);
            }
        }

        if (SitState.isSitting(mc.player.getUUID()) || mc.player.hasPose(Pose.SITTING)) {
            boolean movePressed = mc.options.keyUp.isDown()
                    || mc.options.keyDown.isDown()
                    || mc.options.keyLeft.isDown()
                    || mc.options.keyRight.isDown()
                    || mc.options.keyJump.isDown();

            boolean sneakPressed = mc.options.keyShift.isDown();
            if (movePressed || sneakPressed || mc.player.hurtTime > 0 || !SitHandler.canStaySitting(mc.player)) {

                boolean wasSitting = SitState.isSitting(mc.player.getUUID());
                SitState.setSitting(mc.player.getUUID(), false);
                if (mc.player.hasPose(Pose.SITTING)) {
                    mc.player.setPose(Pose.STANDING);
                    mc.player.refreshDimensions();
                }
                if (wasSitting) {
                    SitNetwork.sendSitToServer(false);
                }
            }
        }
    }
}
