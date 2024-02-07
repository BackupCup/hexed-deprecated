package net.backupcup.hexed.statusEffects

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory

class AflameStatusEffect(category: StatusEffectCategory?, color: Int) : StatusEffect(category, color) {
    override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean {
        val i: Int = 40 shr amplifier
        if (i > 0) {
            return duration % i == 0
        }
        return true
    }

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        entity.setOnFireFor(2 + (1 shl amplifier))
    }

    override fun onApplied(entity: LivingEntity, attributes: AttributeContainer, amplifier: Int) {
        entity.setOnFireFor(2 + (1 shl amplifier))
    }
}