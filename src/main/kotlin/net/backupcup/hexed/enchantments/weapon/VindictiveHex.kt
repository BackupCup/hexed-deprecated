package net.backupcup.hexed.enchantments.weapon

import net.backupcup.hexed.enchantments.AbstractHex
import net.backupcup.hexed.register.RegisterStatusEffects
import net.backupcup.hexed.util.HexHelper
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier

class VindictiveHex(
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
    override fun onTargetDamaged(user: LivingEntity, target: Entity, level: Int) {
        if (target is LivingEntity) {
            if (target.hasStatusEffect(RegisterStatusEffects.VINDICTIVE)) {
                if (target.getStatusEffect(RegisterStatusEffects.VINDICTIVE)?.amplifier!! < 10) {
                    HexHelper.entityMultiplyingEffect(target, RegisterStatusEffects.VINDICTIVE, 60, 20)
                }
            } else {
                HexHelper.entityMultiplyingEffect(target, RegisterStatusEffects.VINDICTIVE, 60, 20)
            }
        }
        if (!HexHelper.hasFullRobes(user)) {
            HexHelper.entityMultiplyingEffect(user, RegisterStatusEffects.SMOULDERING, 60, 20)
        }
    }
}