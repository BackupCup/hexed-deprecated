package net.backupcup.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.backupcup.hexed.register.RegisterDamageTypes;
import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.register.RegisterStatusEffects;
import net.backupcup.hexed.util.HexHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Random;

@Mixin(value = LivingEntity.class, priority = 10)
public abstract class LivingEntityMixin extends Entity{

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract float getHealth();

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract float getMaxHealth();

    @Shadow @Nullable public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow public abstract void damageArmor(DamageSource source, float amount);

    @Shadow public abstract boolean damage(DamageSource source, float amount);

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

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void hexed$cancelHeal(float amount, CallbackInfo callbackInfo) {
        if(this.hasStatusEffect(RegisterStatusEffects.INSTANCE.getSMOULDERING()) ||
                this.hasStatusEffect(RegisterStatusEffects.INSTANCE.getTRAITOROUS()) ||
                HexHelper.INSTANCE.hasEnchantmentInSlot(getEquippedStack(EquipmentSlot.CHEST), RegisterEnchantments.INSTANCE.getBLOODTHIRSTY_HEX())) {

            if(HexHelper.INSTANCE.hasEnchantmentInSlot(getEquippedStack(EquipmentSlot.CHEST), RegisterEnchantments.INSTANCE.getBLOODTHIRSTY_HEX())
                    && HexHelper.INSTANCE.hasFullRobes(getArmorItems()) && getMaxHealth()/getHealth() > 1.3) return;
            callbackInfo.cancel();
        }
    }

    @ModifyArg(method = "handleFallDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
    private float hexed$fallDamageMultiplier(float amount) {
        if ((HexHelper.INSTANCE.hasEnchantmentInSlot(getEquippedStack(EquipmentSlot.FEET), RegisterEnchantments.INSTANCE.getAQUATIQUE_HEX()) ||
                HexHelper.INSTANCE.hasEnchantmentInSlot(getEquippedStack(EquipmentSlot.FEET), RegisterEnchantments.INSTANCE.getDYNAMIQUE_HEX())) &&
                !HexHelper.INSTANCE.hasFullRobes(getArmorItems())) { return amount * 1.5f; }
        return amount;
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float hexed$PersecutedDamage(float amount, DamageSource source) {
        if (source.getSource() instanceof LivingEntity) {
            if (HexHelper.INSTANCE.hasEnchantmentInSlot(((LivingEntity) source.getSource()).getMainHandStack(), RegisterEnchantments.INSTANCE.getPERSECUTED_HEX())) {
                return (amount <= 25f) ? (amount * 0.01f * getHealth()) : 25f;
            }
        }
        return amount;
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float hexed$EphemeralDischarge(float amount, DamageSource source) {
        if(source.getSource() instanceof LivingEntity) {
            if(HexHelper.INSTANCE.hasEnchantmentInSlot(((LivingEntity) source.getSource()).getMainHandStack(), RegisterEnchantments.INSTANCE.getEPHEMERAL_HEX())) {
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
        if (HexHelper.INSTANCE.hasEnchantmentInSlot(getEquippedStack(EquipmentSlot.FEET), RegisterEnchantments.INSTANCE.getAQUATIQUE_HEX())) {
            System.out.println(value);
            return (value + 1) * 2;
        }
        return value;
    }

    @ModifyExpressionValue(method = "travel",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isOnGround()Z", ordinal = 0))
    private boolean hexed$AquatiqueCap(boolean original) {
        return original && !HexHelper.INSTANCE.hasEnchantmentInSlot(getEquippedStack(EquipmentSlot.FEET), RegisterEnchantments.INSTANCE.getAQUATIQUE_HEX());
    }
    @Inject(method = "heal", at = @At("HEAD"))
    private void hexed$MetamorphosisFood(float amount, CallbackInfo ci) {
        if (getMaxHealth() < getHealth() + amount
            && HexHelper.INSTANCE.hasEnchantmentInSlot(getEquippedStack(EquipmentSlot.HEAD), RegisterEnchantments.INSTANCE.getMETAMORPHOSIS_HEX())
            && (Object)this instanceof PlayerEntity) {

            PlayerEntity player = (PlayerEntity) (Object)this;
            amount -= getMaxHealth() - getHealth();
            player.getHungerManager().add((int) amount, 0f);
        }
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float hexed$IroncladDamage(float amount, DamageSource source) {
        if (HexHelper.INSTANCE.hasEnchantmentInSlot(getEquippedStack(EquipmentSlot.LEGS), RegisterEnchantments.INSTANCE.getIRONCLAD_HEX())) {
            int duration = 200;
            if (HexHelper.INSTANCE.hasFullRobes(getArmorItems())) duration /= 2;

            if (getHealth() < getMaxHealth() && amount >= 1) {
                entityMultiplyingEffect(RegisterStatusEffects.INSTANCE.getIRONCLAD(), duration, 10);
            }
            return amount * 0.66f;
        }
        return amount;
    }

    @Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
    private void hexed$DynamiqueJump(CallbackInfoReturnable<Float> cir) {
        float DynamiqueJumpModifier = HexHelper.INSTANCE.hasEnchantmentInSlot(getEquippedStack(EquipmentSlot.FEET), RegisterEnchantments.INSTANCE.getDYNAMIQUE_HEX()) ? 0.15625f : 0f;
        cir.setReturnValue(cir.getReturnValue() + DynamiqueJumpModifier);
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float hexed$AvertingDamage(float amount, DamageSource source) {
        int avertingArmor = 0;
        for (ItemStack itemStack : getArmorItems()) {
            if(EnchantmentHelper.get(itemStack).containsKey(RegisterEnchantments.INSTANCE.getAVERTING_HEX())) avertingArmor += 1;
        }

        if((1-Math.exp(-avertingArmor)) * (1-amount/getMaxHealth()) > new Random().nextFloat()) {
            float newAmount = amount * (1 - 0.125f*avertingArmor);
            damageArmor(source, newAmount);
            return newAmount;
        }

        return amount;
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float hexed$FranticDamage(float amount, DamageSource source) {
        if (source.isOf(RegisterDamageTypes.INSTANCE.getFRANTIC_DAMAGE())) return amount;

        if (HexHelper.INSTANCE.hasEnchantmentInSlot(getEquippedStack(EquipmentSlot.LEGS), RegisterEnchantments.INSTANCE.getFRANTIC_HEX())) {
            entityMultiplyingEffect(RegisterStatusEffects.INSTANCE.getFRANTIC(), 100, 50);

            if (HexHelper.INSTANCE.hasFullRobes(getArmorItems())) return amount;

            damage(RegisterDamageTypes.INSTANCE.of(getWorld(), RegisterDamageTypes.INSTANCE.getFRANTIC_DAMAGE()),
                    amount *= 1 + (float) getStatusEffect(RegisterStatusEffects.INSTANCE.getFRANTIC()).getAmplifier() /2);
            return 0;
        }

        return amount;
    }
}
