package net.backupcup.mixin;

import net.backupcup.hexed.register.RegisterEnchantments;
import net.backupcup.hexed.register.RegisterStatusEffects;
import net.backupcup.hexed.register.RegisterTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract float getHealth();

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float hexed$PersecutedDamage(float amount, DamageSource source) {
        if (source.getSource() instanceof LivingEntity) {
            if (hexed$hasEnchantmentInHands(RegisterEnchantments.INSTANCE.getPERSECUTED_HEX(), source)) {
                return (amount <= 25f) ? (amount * 0.01f * getHealth()) : 25f;
            }

            if (this.hasStatusEffect(RegisterStatusEffects.INSTANCE.getETHEREAL()) && !hexed$hasFullRobes(this.getArmorItems())) {
                return amount * (1f + this.getStatusEffect(RegisterStatusEffects.INSTANCE.getETHEREAL()).getDuration() / 10f);
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

    @Unique
    private boolean hexed$hasEnchantmentInHands(Object key, DamageSource source) {
        return EnchantmentHelper.get(((LivingEntity) Objects.requireNonNull(source.getSource())).getMainHandStack()).containsKey(key) ||
                EnchantmentHelper.get(((LivingEntity) source.getSource()).getOffHandStack()).containsKey(key);
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
