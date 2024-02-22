package net.backupcup.hexed.enchantments

import net.backupcup.hexed.register.RegisterStatusEffects
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
                    entityMultiplyingEffect(target, RegisterStatusEffects.VINDICTIVE, 60, 20)
                }
            } else {
                entityMultiplyingEffect(target, RegisterStatusEffects.VINDICTIVE, 60, 20)
            }
        }
        if (!hasFullRobes(user)) {
            entityMultiplyingEffect(user, RegisterStatusEffects.SMOULDERING, 60, 20)
        }
    }
}