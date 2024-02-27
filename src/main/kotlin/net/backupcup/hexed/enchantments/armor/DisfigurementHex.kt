package net.backupcup.hexed.enchantments.armor

import net.backupcup.hexed.enchantments.AbstractHex
import net.backupcup.hexed.util.HexHelper
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box

class DisfigurementHex(
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
    override fun onUserDamaged(user: LivingEntity, attacker: Entity?, level: Int) {
        val box = Box(
            user.x-3, user.y-3, user.z-3,
            user.x+3, user.y+3, user.z+3)
        val affectedEntities = user.world.getNonSpectatingEntities(
            LivingEntity::class.java, box)

        for (livingEntity in affectedEntities) {
            if (HexHelper.hasFullRobes(user) && livingEntity == user) { continue }
            livingEntity.addStatusEffect(
                StatusEffectInstance(
                    StatusEffects.WEAKNESS,
                    100, 0,
                    true, true, true
                ))
            livingEntity.addStatusEffect(
                StatusEffectInstance(
                    StatusEffects.HUNGER,
                    100, 0,
                    true, true, true
                ))
        }

        super.onUserDamaged(user, attacker, level)
    }
}