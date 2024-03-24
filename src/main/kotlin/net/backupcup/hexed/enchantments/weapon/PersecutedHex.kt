package net.backupcup.hexed.enchantments.weapon

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.enchantments.AbstractHex
import net.backupcup.hexed.register.RegisterStatusEffects
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.util.Identifier

class PersecutedHex(
    weight: Rarity?,
    target: EnchantmentTarget?,
    slotTypes: Array<out EquipmentSlot>?,
    texturepath: Identifier
) : AbstractHex(
    weight,
    target,
    slotTypes,
    texturepath
) {
    override fun onUserDamaged(user: LivingEntity, attacker: Entity, level: Int) {
        if (user.hasStatusEffect(RegisterStatusEffects.ETHEREAL)) {
            return
        }

        val armorPoints = user.armor
        for (i in 0..armorPoints) {
            user.addStatusEffect(
                StatusEffectInstance(
                    RegisterStatusEffects.ETHEREAL,
                    1 + (armorPoints - i) * (Hexed.getConfig()?.persecutedHex?.debuffDurationModifier ?: 10), i,
                    true, false, true
                )
            )
        }
    }
}