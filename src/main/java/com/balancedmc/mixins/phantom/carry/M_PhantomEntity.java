package com.balancedmc.mixins.phantom.carry;

import net.minecraft.entity.mob.PhantomEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Phantoms drop players after 5-10 seconds<br>
 * Players render lower when riding phantoms
 */
@Mixin(PhantomEntity.class)
public abstract class M_PhantomEntity {

    private int carryingTicks = -1;

    @Inject(
            method = "tick()V",
            at = @At("TAIL")
    )
    private void injected(CallbackInfo ci) {
        PhantomEntity phantom = (PhantomEntity)(Object)this;
        if (phantom.hasPassengers() && !phantom.getWorld().isClient) {
            if (carryingTicks == -1) {
                carryingTicks = (int) (Math.random() * 100) + 100;
            }
            else if (carryingTicks == 0) {
                phantom.removeAllPassengers();
                carryingTicks = -1;
            }
            else {
                carryingTicks--;
            }
        }
    }

    /**
     * @author HB0P
     * @reason Players render lower when riding phantoms
     */
    @Overwrite
    public double getMountedHeightOffset() {
        return -1.3;
    }
}
