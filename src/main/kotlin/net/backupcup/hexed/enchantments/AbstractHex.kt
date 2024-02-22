package net.backupcup.hexed.enchantments

import net.backupcup.hexed.register.RegisterTags
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.util.Identifier

abstract class AbstractHex(
    weight: Rarity?,
    target: EnchantmentTarget?,
    slotTypes: Array<out EquipmentSlot>?,
    val texturepath: Identifier
) : Enchantment(
    weight,
    target,
    slotTypes
) {
    override fun canAccept(other: Enchantment?): Boolean {
        if (isHex(other)) {
            return false
        }
        return super.canAccept(other)
    }

    override fun isTreasure(): Boolean {
        return true
    }

    override fun isCursed(): Boolean {
        return true
    }

    override fun isAvailableForEnchantedBookOffer(): Boolean {
        return false
    }

    override fun isAvailableForRandomSelection(): Boolean {
        return false
    }

    override fun getMaxLevel(): Int {
        return 1
    }

    private fun isHex(enchantment: Enchantment?): Boolean {
        return (enchantment is AbstractHex)
    }

    fun hasFullRobes(entity: LivingEntity): Boolean {
        entity.armorItems.forEach { piece ->
            if (!piece.isIn(RegisterTags.CALAMITOUS_ARMOR)) return false
        }
        return true
    }

    fun entityMultiplyingEffect(user: LivingEntity, effect: StatusEffect, duration: Int, decayLength: Int) {
        if (user.hasStatusEffect(effect)) {
            val effectAmplifier = user.getStatusEffect(effect)?.amplifier?.plus(1)

            for (i in 0..effectAmplifier!!) {
                user.addStatusEffect(
                    StatusEffectInstance(
                        effect,
                        duration + (effectAmplifier - i) * decayLength, i,
                        true, false, true
                    )
                )
            }
        } else {
            user.addStatusEffect(
                StatusEffectInstance(
                    effect,
                    duration, 0,
                    true, false, true
                )
            )
        }
    }
}