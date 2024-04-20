package net.backupcup.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.backupcup.hexed.Hexed;
import net.backupcup.hexed.entity.blazingSkull.BlazingSkullEntity;
import net.backupcup.hexed.register.*;
import net.backupcup.hexed.util.HexHelper;
import net.backupcup.hexed.util.HexRandom;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(value = LivingEntity.class, priority = 10)
public abstract class LivingEntityMixin extends Entity{

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique private boolean isFlaring = false;

    @Unique private boolean hasSpawnedSkulls = false;

    @Unique private int avertingTicks = 0;

    @Shadow public abstract float getHealth();

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract float getMaxHealth();

    @Shadow @Nullable public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract void tickRiding();

    @Shadow public abstract boolean isUsingRiptide();

    @Shadow public abstract ItemStack getMainHandStack();

    @Shadow public abstract void writeCustomDataToNbt(NbtCompound nbt);

    @Shadow public abstract void readCustomDataFromNbt(NbtCompound nbt);

    @Unique
    private void entityMultiplyingEffect(StatusEffect effect, int duration, int decayLength) {
        if (hasStatusEffect(effect)) {
            int effectAmplifier = getStatusEffect(effect).getAmplifier() + 1;

            for (int i = 0; i <= effectAmplifier; i++) {
                addStatusEffect(new StatusEffectInstance(
                        effect,
                        duration + (effectAmplifier - i) * decayLength, i,
                        true, false, true
                ));
            }
        } else {
            addStatusEffect(new StatusEffectInstance(
                    effect,
                    duration, 0,
                    true, false, true
            ));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void hexed$LivingEntityWriteData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("isFlaring", this.isFlaring);
        nbt.putBoolean("hasSpawnedSkulls", this.hasSpawnedSkulls);
        nbt.putInt("avertingTicks", this.avertingTicks);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void hexed$LivingEntityReadData(NbtCompound nbt, CallbackInfo ci) {
        this.isFlaring = nbt.getBoolean("isFlaring");
        this.hasSpawnedSkulls = nbt.getBoolean("hasSpawnedSkulls");
        this.avertingTicks = nbt.getInt("avertingTicks");
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void hexed$cancelHeal(float amount, CallbackInfo callbackInfo) {
        if(this.hasStatusEffect(RegisterStatusEffects.INSTANCE.getSMOULDERING()) ||
                this.hasStatusEffect(RegisterStatusEffects.INSTANCE.getTRAITOROUS()) ||
                HexHelper.INSTANCE.stackHasEnchantment(getEquippedStack(EquipmentSlot.CHEST), RegisterEnchantments.INSTANCE.getBLOODTHIRSTY_HEX())) {

            if(HexHelper.INSTANCE.stackHasEnchantment(getEquippedStack(EquipmentSlot.CHEST), RegisterEnchantments.INSTANCE.getBLOODTHIRSTY_HEX())
                    && HexHelper.INSTANCE.hasFullRobes(getArmorItems()) && getMaxHealth()/getHealth() > 1.3) return;
            callbackInfo.cancel();
        }
    }

    @ModifyArg(method = "handleFallDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
    private float hexed$fallDamageMultiplier(float amount) {
        List<Enchantment> hexList = new ArrayList<>();
            hexList.add(RegisterEnchantments.INSTANCE.getAQUATIQUE_HEX());
            hexList.add(RegisterEnchantments.INSTANCE.getDYNAMIQUE_HEX());

        if (HexHelper.INSTANCE.stackHasEnchantment(getEquippedStack(EquipmentSlot.FEET), hexList) &&
                !HexHelper.INSTANCE.hasFullRobes(getArmorItems())) { return amount * 1.5f; }
        return amount;
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float hexed$PersecutedDamage(float amount, DamageSource source) {
        if (source.getSource() instanceof LivingEntity) {
            if (HexHelper.INSTANCE.stackHasEnchantment(((LivingEntity) source.getSource()).getMainHandStack(), RegisterEnchantments.INSTANCE.getPERSECUTED_HEX())) {
                float healthCap = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getPersecutedHex().getHealthCap() : 25f;
                return (amount <= healthCap) ? (amount * 0.01f * getHealth()) : healthCap;
            }
        }
        return amount;
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float hexed$EphemeralDischarge(float amount, DamageSource source) {
        if(source.getSource() instanceof LivingEntity) {
            if(HexHelper.INSTANCE.stackHasEnchantment(((LivingEntity) source.getSource()).getMainHandStack(), RegisterEnchantments.INSTANCE.getEPHEMERAL_HEX())) {
                if (((LivingEntity) source.getSource()).hasStatusEffect(RegisterStatusEffects.INSTANCE.getEXHAUSTION())) {

                    float exhaustionModifier = Objects.requireNonNull(((LivingEntity) source.getSource()).getStatusEffect(RegisterStatusEffects.INSTANCE.getEXHAUSTION())).getAmplifier();
                    return Math.max(0, amount - exhaustionModifier);
                }
            }
        }
        return amount;
    }

    @ModifyVariable(method = "travel",
        at = @At("STORE"), ordinal = 2)
    private float hexed$AquatiqueSpeed(float value) {
        if (HexHelper.INSTANCE.stackHasEnchantment(getEquippedStack(EquipmentSlot.FEET), RegisterEnchantments.INSTANCE.getAQUATIQUE_HEX())) {
            return (value + 1) * (Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getAquatiqueHex().getSpeedMultiplier() : 2);
        }
        return value;
    }

    @ModifyExpressionValue(method = "travel",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isOnGround()Z", ordinal = 0))
    private boolean hexed$AquatiqueCap(boolean original) {
        return original && !HexHelper.INSTANCE.stackHasEnchantment(getEquippedStack(EquipmentSlot.FEET), RegisterEnchantments.INSTANCE.getAQUATIQUE_HEX());
    }
    @Inject(method = "heal", at = @At("HEAD"))
    private void hexed$MetamorphosisFood(float amount, CallbackInfo ci) {
        if (getMaxHealth() < getHealth() + amount
            && HexHelper.INSTANCE.stackHasEnchantment(getEquippedStack(EquipmentSlot.HEAD), RegisterEnchantments.INSTANCE.getMETAMORPHOSIS_HEX())
            && (Object)this instanceof PlayerEntity) {

            PlayerEntity player = (PlayerEntity) (Object) this;
            amount -= getMaxHealth() - getHealth();
            player.getHungerManager().add(
                    (int) (amount * (Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getMetamorphosisHex().getFoodModifier() : 1)),
                    Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getMetamorphosisHex().getSaturationAmount() : 0f);
        }
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float hexed$IroncladDamage(float amount, DamageSource source) {
        if (HexHelper.INSTANCE.stackHasEnchantment(getEquippedStack(EquipmentSlot.LEGS), RegisterEnchantments.INSTANCE.getIRONCLAD_HEX())) {
            int duration = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getIroncladHex().getDebuffDuration() : 200;
            if (HexHelper.INSTANCE.hasFullRobes(getArmorItems()))
                duration /= Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getIroncladHex().getRobesDebuffModifier() : 2;

            if (getHealth() < getMaxHealth() && amount >= 1) {
                entityMultiplyingEffect(RegisterStatusEffects.INSTANCE.getIRONCLAD(),
                        duration,
                        Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getIroncladHex().getDebuffDecayLength() : 10);
            }
            return amount * (1f - (Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getIroncladHex().getDamageReductionAmount() : 0.33f));
        }
        return amount;
    }

    @Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
    private void hexed$DynamiqueJump(CallbackInfoReturnable<Float> cir) {
        float DynamiqueJumpModifier = HexHelper.INSTANCE.stackHasEnchantment(getEquippedStack(EquipmentSlot.FEET), RegisterEnchantments.INSTANCE.getDYNAMIQUE_HEX()) ?
                (Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getDynamiqueHex().getJumpModifier() : 0.15625f) : 0f;
        cir.setReturnValue(cir.getReturnValue() + DynamiqueJumpModifier);
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private DamageSource hexed$AvertingSource(DamageSource source) {
        int avertingArmor = 0;

        for (ItemStack itemStack : getArmorItems()) {
            if(HexHelper.INSTANCE.stackHasEnchantment(itemStack, RegisterEnchantments.INSTANCE.getAVERTING_HEX())) avertingArmor += 1;
        }

        if (this.avertingTicks > 0 && avertingArmor > 0) {
            source = RegisterDamageTypes.INSTANCE.of(getEntityWorld(), RegisterDamageTypes.INSTANCE.getABYSSAL_CRUSH());
        }

        return source;
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float hexed$AvertingDamage(float amount) {
        int avertingArmor = 0;

        for (ItemStack itemStack : getArmorItems()) {
            if(HexHelper.INSTANCE.stackHasEnchantment(itemStack, RegisterEnchantments.INSTANCE.getAVERTING_HEX())) avertingArmor += 1;
        }

        if(this.avertingTicks == 0 && avertingArmor * (Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getAvertingHex().getDamageReduction() : 0.125) > HexRandom.INSTANCE.nextFloat()) {
            amount = 0f;
            this.avertingTicks = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getAvertingHex().getAvertingCooldown() : 50;
            if ((LivingEntity)(Object) this instanceof PlayerEntity) {
                ((PlayerEntity) (Object) this).playSound(
                        RegisterSounds.INSTANCE.getACCURSED_ALTAR_TAINT(),
                        SoundCategory.PLAYERS, 0.25f, 2f
                );
            }
        }

        return amount;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;sendEquipmentChanges()V", shift = At.Shift.AFTER))
    private void hexed$AvertingTick(CallbackInfo ci) {
        if (this.avertingTicks > 0) { this.avertingTicks -= 1; }
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float hexed$FranticDamage(float amount, DamageSource source) {
        if (source.isOf(RegisterDamageTypes.INSTANCE.getFRANTIC_DAMAGE())) return amount;

        if (HexHelper.INSTANCE.stackHasEnchantment(getEquippedStack(EquipmentSlot.LEGS), RegisterEnchantments.INSTANCE.getFRANTIC_HEX())) {
            entityMultiplyingEffect(RegisterStatusEffects.INSTANCE.getFRANTIC(),
                    Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getFranticHex().getFranticDuration() : 100,
                    Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getFranticHex().getFranticDecayLength() : 50);

            if (HexHelper.INSTANCE.hasFullRobes(getArmorItems())) return amount;

            damage(RegisterDamageTypes.INSTANCE.of(getWorld(), RegisterDamageTypes.INSTANCE.getFRANTIC_DAMAGE()),
                    amount *= 1 + (float) getStatusEffect(RegisterStatusEffects.INSTANCE.getFRANTIC()).getAmplifier() /
                            (Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getFranticHex().getDamageModifier() : 2f));
            return 0;
        }

        return amount;
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void hexed$FlaringRiptide(CallbackInfo ci) {
        if (isUsingRiptide() && HexHelper.INSTANCE.stackHasEnchantment(getMainHandStack(), RegisterEnchantments.INSTANCE.getFLARING_HEX()) && !this.isFlaring) {
            this.isFlaring = true;
        }
        if(!isUsingRiptide() && this.isFlaring) this.isFlaring = false;

        if (this.isFlaring && getEntityWorld().getNonSpectatingEntities(FallingBlockEntity.class, new Box(getBlockPos()).expand(1)).isEmpty()) {
            FallingBlockEntity.spawnFromBlock(getWorld(), getBlockPos(), (
                            Hexed.INSTANCE.getConfig() != null ?
                                    Hexed.INSTANCE.getConfig().getFlaringHex().isSoulFire() ?
                                            Blocks.SOUL_FIRE.getDefaultState() :
                                            Blocks.FIRE.getDefaultState() :
                                    Blocks.FIRE.getDefaultState()
                    )
            );
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void hexed$SepultureRiptide(CallbackInfo ci) {
        if (isUsingRiptide() && HexHelper.INSTANCE.stackHasEnchantment(getMainHandStack(), RegisterEnchantments.INSTANCE.getSEPULTURE_HEX()) && !this.hasSpawnedSkulls) {
            this.hasSpawnedSkulls = true;

            for (int i = 0; i < 3; i++) {
                double angle = i * 2 * Math.PI / 3;
                Vec3d spawnPos = new Vec3d(getPos().getX() + 1 * Math.cos(angle), getPos().getY() + 0.5, getPos().getZ() + 1 * Math.sin(angle));
                Vec3d movementVec = new Vec3d((spawnPos.x - getPos().getX())/4, 0.333, (spawnPos.z - getPos().getZ())/4);

                LivingEntity entity = HexRandom.INSTANCE.nextDouble(0.0, 1.0) <=
                        (Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getSepultureHex().getAngerChance() : 0.333) ?
                        (LivingEntity) (Object) this : null;
                if(HexHelper.INSTANCE.hasFullRobes(getArmorItems()) && entity != null) entity = null;

                float explosionPower = Hexed.INSTANCE.getConfig() != null ? Hexed.INSTANCE.getConfig().getSepultureHex().getExplosionPower() : 1.5f;

                getWorld().spawnEntity(
                        new BlazingSkullEntity(
                                RegisterEntities.INSTANCE.getBLAZING_SKULL(), getWorld(),
                                spawnPos, movementVec, entity, explosionPower)
                );
            }

            getWorld().playSound(
                    null, getBlockPos(),
                    RegisterSounds.INSTANCE.getACCURSED_ALTAR_HEX(), SoundCategory.HOSTILE,
                    (float) HexRandom.INSTANCE.nextDouble(0.5, 1.0),
                    (float) HexRandom.INSTANCE.nextDouble(0.75, 1.25)
            );
        }
        if(!isUsingRiptide() && this.hasSpawnedSkulls) this.hasSpawnedSkulls = false;
    }
}
