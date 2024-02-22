package net.backupcup.hexed.enchantments

import net.backupcup.hexed.register.RegisterStatusEffects
import net.backupcup.hexed.util.HexHelper
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier
import kotlin.math.abs
import kotlin.math.atan2

class TraitorousHex(
    weight: Rarity?,
    target: EnchantmentTarget?,
    slotTypes: Array<out EquipmentSlot>?,
    texturepath: Identifier
) : AbstractHex(
    weight,
    target,
    slotTypes,
    texturepath) {
    override fun onTargetDamaged(user: LivingEntity, target: Entity, level: Int) {
        var targetYaw = target.yaw % 360
        if (targetYaw > 180) targetYaw -= 360

        var userYaw = user.yaw % 360
        if (userYaw > 180) userYaw -= 360

        if (abs(targetYaw - userYaw) <= 67.5 && target is LivingEntity && user is PlayerEntity) {
            target.addStatusEffect(
                StatusEffectInstance(
                    RegisterStatusEffects.TRAITOROUS,
                    80, 0,
                    true, true, true
                ))

            if (!hasFullRobes(user)) {
                if (HexHelper.getEnchantments(user.mainHandStack).contains(this)) user.itemCooldownManager.set(user.mainHandStack.item, 40)
                if (HexHelper.getEnchantments(user.offHandStack).contains(this)) user.itemCooldownManager.set(user.offHandStack.item, 40)
            }
        }

        super.onTargetDamaged(user, target, level)
    }
}