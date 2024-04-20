package net.backupcup.mixin;

import net.backupcup.hexed.Hexed;
import net.backupcup.hexed.util.VolatilityInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends Entity implements VolatilityInterface {
    public PersistentProjectileEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique private boolean isVolatile = false;
    @Override
    public boolean getVolatility() { return this.isVolatile; }
    @Override
    public void setVolatility(boolean value) { this.isVolatile = value; }

    @Inject(method = "writeCustomDataToNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    private void hexed$writeCustomData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("isVolatile", this.isVolatile);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    private void hexed$readCustomData(NbtCompound nbt, CallbackInfo ci) {
        this.isVolatile = nbt.getBoolean("isVolatile");
    }

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;ceil(D)I", shift = At.Shift.AFTER))
    private void hexed$VolatilityExplode(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (this.isVolatile) {
            entityHitResult.getEntity().getEntityWorld().createExplosion(
                    null,
                    getX(), getY(), getZ(),
                    Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getVolatilityHex().getExplosionPower() / 2f : 2f,
                    World.ExplosionSourceType.NONE);

            remove(RemovalReason.DISCARDED);
        }
    }
}
