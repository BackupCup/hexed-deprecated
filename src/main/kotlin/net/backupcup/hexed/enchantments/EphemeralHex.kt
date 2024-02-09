package net.backupcup.hexed.enchantments

import net.backupcup.hexed.register.RegisterStatusEffects
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityGroup
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance

class EphemeralHex(
    weight: Rarity?,
    target: EnchantmentTarget?,
    slotTypes: Array<out EquipmentSlot>?
) : AbstractHex(
    weight,
    target,
    slotTypes
) {
    override fun getAttackDamage(level: Int, group: EntityGroup?): Float {
        return 4f
    }

    override fun onTargetDamaged(user: LivingEntity, target: Entity, level: Int) {
        if (target is LivingEntity) {
            if(!hasFullRobes(user)) {
                if (user.hasStatusEffect(RegisterStatusEffects.EXHAUSTION)) {
                    val exhaustionAmplifier = user.getStatusEffect(RegisterStatusEffects.EXHAUSTION)?.amplifier?.plus(1)

                    for (i in 0..exhaustionAmplifier!!) {
                        user.addStatusEffect(StatusEffectInstance(
                            RegisterStatusEffects.EXHAUSTION,
                            150 + (exhaustionAmplifier - i) * 50, i,
                            true, false, true
                        ))
                    }
                } else {
                    user.addStatusEffect(StatusEffectInstance(
                        RegisterStatusEffects.EXHAUSTION,
                        30, 0
                    ))
                }
            }
        }
    }
}