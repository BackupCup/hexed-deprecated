package net.backupcup.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.register.RegisterStatusEffects;
import net.backupcup.hexed.register.RegisterTags;
import net.backupcup.hexed.statusEffects.AbstractHexStatusEffect;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract float getHealth();

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float hexed$PersecutedDamage(float amount, DamageSource source) {
        if (source.getSource() instanceof LivingEntity) {
            if (hexed$hasEnchantmentInHands(RegisterEnchantments.INSTANCE.getPERSECUTED_HEX(), source)) {
                return (amount <= 25f) ? (amount * 0.01f * getHealth()) : 25f;
            }
        }
        return amount;
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float hexed$EphemeralDischarge(float amount, DamageSource source) {
        if(source.getSource() instanceof LivingEntity) {
            if(hexed$hasEnchantmentInHands(RegisterEnchantments.INSTANCE.getEPHEMERAL_HEX(), source)) {
                if (((LivingEntity) source.getSource()).hasStatusEffect(RegisterStatusEffects.INSTANCE.getEXHAUSTION())) {

                    float exhaustionModifier = Objects.requireNonNull(((LivingEntity) source.getSource()).getStatusEffect(RegisterStatusEffects.INSTANCE.getEXHAUSTION())).getAmplifier();
                    return Math.max(0, amount - exhaustionModifier);
                }
            }
        }
        return amount;
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void hexed$SmoulderingHeal(float amount, CallbackInfo callbackInfo) {
        if(this.hasStatusEffect(RegisterStatusEffects.INSTANCE.getSMOULDERING())) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void hexed$TraitorousHeal(float amount, CallbackInfo callbackInfo) {
        if(this.hasStatusEffect(RegisterStatusEffects.INSTANCE.getTRAITOROUS())) {
            callbackInfo.cancel();
        }
    }

    @WrapOperation(method = "clearStatusEffects", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;"))
    private Collection<StatusEffectInstance> hexed$skipHexedDebuffs(Map<?, StatusEffectInstance> map, Operation<Map<?, StatusEffectInstance>> original) {
        return map.values().stream().filter(effectInstance -> !(effectInstance.getEffectType() instanceof AbstractHexStatusEffect)).collect(Collectors.toList());
    }

    @Unique
    private boolean hexed$hasEnchantmentInHands(Object key, DamageSource source) {
        return EnchantmentHelper.get(((LivingEntity) Objects.requireNonNull(source.getSource())).getMainHandStack()).containsKey(key) ||
                EnchantmentHelper.get(((LivingEntity) Objects.requireNonNull(source.getSource())).getOffHandStack()).containsKey(key);
    }

    @Unique
    private boolean hexed$hasFullRobes(Iterable<ItemStack> armorStack) {
        for (ItemStack piece : armorStack) {
            if(!piece.isIn(RegisterTags.INSTANCE.getCALAMITOUS_ARMOR())) {
                return false;
            }
        }
        return true;
    }
}
