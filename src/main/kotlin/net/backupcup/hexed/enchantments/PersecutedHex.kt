package net.backupcup.hexed.enchantments

import net.backupcup.hexed.register.RegisterStatusEffects
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance

class PersecutedHex(
    weight: Rarity?,
    target: EnchantmentTarget?,
    slotTypes: Array<out EquipmentSlot>?
) : AbstractHex(
    weight,
    target,
    slotTypes
) {
    override fun onUserDamaged(user: LivingEntity, attacker: Entity, level: Int) {
        user.addStatusEffect(StatusEffectInstance(
            RegisterStatusEffects.ETHEREAL,
            100, 0,
            true, false, true
        ))
    }
}