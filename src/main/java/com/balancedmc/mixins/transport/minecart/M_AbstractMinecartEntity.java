package com.balancedmc.mixins.transport.minecart;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AbstractMinecartEntity.class)
public abstract class M_AbstractMinecartEntity {

    /**
     * Minecarts now travel at 20m/s
     */
    @Inject(method = "getMaxSpeed()D", at = @At("RETURN"), cancellable = true)
    private void inject(CallbackInfoReturnable<Double> cir) {
        double speed = cir.getReturnValue();

        if ((AbstractMinecartEntity) (Object) this instanceof MinecartEntity cart) {
            List<Entity> passengers = cart.getPassengerList();
            if (passengers.size() > 0 && passengers.get(0) instanceof PlayerEntity) {
                speed *= 2.5;
            }
        }

        cir.setReturnValue(speed);
    }
}