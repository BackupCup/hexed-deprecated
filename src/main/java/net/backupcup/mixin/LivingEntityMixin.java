package net.backupcup.mixin;

import net.backupcup.hexed.register.RegisterStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow @Nullable public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float etherealDamageModifier(float amount) {
        if(this.hasStatusEffect(RegisterStatusEffects.INSTANCE.getETHEREAL())) {
            return amount + (amount * ((float) 1 /4 * (this.getStatusEffect(RegisterStatusEffects.INSTANCE.getETHEREAL())).getAmplifier() + 1));
        }
        return amount;
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void irradiatedHealCancel(float amount, CallbackInfo callbackInfo){
        if(this.hasStatusEffect(RegisterStatusEffects.INSTANCE.getIRRADIATED())) {
            callbackInfo.cancel();
        }
    }
}
