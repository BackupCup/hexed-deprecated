package net.backupcup.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.backupcup.hexed.Hexed;
import net.backupcup.hexed.util.ResentfulInterface;
import net.backupcup.hexed.util.VolatilityInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends Entity implements VolatilityInterface, ResentfulInterface {
    @Shadow private double damage;

    public PersistentProjectileEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique private boolean isVolatile = false;
    @Override
    public boolean getVolatility() { return this.isVolatile; }
    @Override
    public void setVolatility(boolean value) { this.isVolatile = value; }

    @Unique private Entity ownerEntity = null;
    @Override
    public void setOwner(Entity owner) { this.ownerEntity = owner; }

    @Inject(method = "writeCustomDataToNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    private void hexed$writeCustomData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("isVolatile", this.isVolatile);
        nbt.putUuid("resentfulOwner", this.ownerEntity.getUuid());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    private void hexed$readCustomData(NbtCompound nbt, CallbackInfo ci) {
        this.isVolatile = nbt.getBoolean("isVolatile");
        this.ownerEntity = getEntityWorld().getPlayerByUuid(nbt.getUuid("resentfulOwner"));
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

    @ModifyVariable(method = "onEntityHit", at = @At("STORE"), ordinal = 0)
    private int hexed$ResentfulDamage(int damage, @Local(ordinal = 0) Entity entity) {
        if (this.ownerEntity != null && entity != null) {
            double distance = entity.distanceTo(this.ownerEntity);

            double modifier;
            double increasePoint = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getResentfulHex().getIncreasePoint() : 16.0;
            double maxRange = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getResentfulHex().getMaxIncreaseRange() : 80.0;

            if (distance >= 0 && distance <= increasePoint) {
                modifier = 0.3 / increasePoint * distance + 0.7;
            } else {
                modifier = Math.min(2.0, 1 / (maxRange - increasePoint + 1) * (distance - increasePoint + 1) + 1);
            }

            return (int) (damage * modifier);
        }
        return damage;
    }
}
