package net.backupcup.hexed.enchantments

import net.backupcup.hexed.register.RegisterStatusEffects
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.util.Identifier

class AflameHex(
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
        if(target is LivingEntity) {
            if (target.isAlive) {
                target.addStatusEffect(StatusEffectInstance(
                    RegisterStatusEffects.ABLAZE,
                    80, 1,
                    true, true, true
                ))
            }
            if (user.isAlive && !hasFullRobes(user)) {
                user.addStatusEffect(StatusEffectInstance(
                    RegisterStatusEffects.AFLAME,
                    40, 0,
                    true, true, true
                ))
            }
        }
    }
}