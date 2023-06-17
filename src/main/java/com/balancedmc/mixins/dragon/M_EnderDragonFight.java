package com.balancedmc.mixins.dragon;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonFight.class)
public abstract class M_EnderDragonFight {

    @Shadow @Final private ServerWorld world;
    @Shadow @Final private ServerBossBar bossBar;
    @Shadow private boolean dragonKilled;

    @Shadow public abstract int getAliveEndCrystals();

    /**
     * 0 = destroying end crystals<br>
     * 1 = spawn phantoms<br>
     * 2 = spawn illusioners
     */
    private int stage = 0;

    @Inject(
            method = "tick()V",
            at = @At("TAIL")
    )
    private void injected(CallbackInfo ci) {
        if (dragonKilled) {
            stage = 0;
        }
        else if (stage < 2 && bossBar.getPercent() != 0 && bossBar.getPercent() <= 0.5) {
            stage = 2;
            // spawn illusioners
            if (this.world.getEntitiesByClass(PhantomEntity.class, new Box(-50, 0, -50, 50, 255, 50), (PhantomEntity) -> true).isEmpty()) {
                for (int i = 0; i < 10; i++) {
                    double angle = Math.toRadians(i * 36);
                    int x = (int) (42 * Math.sin(angle));
                    int z = (int) (42 * Math.cos(angle));
                    int y = this.world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
                    IllusionerEntity entity = EntityType.ILLUSIONER.spawn(world, new BlockPos(x, y, z), SpawnReason.NATURAL);
                    entity.setPersistent();
                }
            }
        }
        else if (stage <= 1 && this.getAliveEndCrystals() == 0) {
            stage = 1;
            // spawn phantoms
            if (this.world.getEntitiesByClass(PhantomEntity.class, new Box(-50, 0, -50, 50, 255, 50), (PhantomEntity) -> true).size() < 20) {
                double angle = (Math.random() * 360);
                double dist = (Math.random() * 40);
                int x = (int) (dist * Math.sin(angle));
                int z = (int) (dist * Math.cos(angle));
                int y = this.world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
                EntityType.PHANTOM.spawn(world, new BlockPos(x, y, z), SpawnReason.NATURAL);
            }
        }
        // show stage on boss bar
        this.bossBar.setName(Text.of(this.bossBar.getName().getString().replaceAll(" - Stage \\d", "") + " - Stage " + (stage + 1)));
    }
}