package net.backupcup.hexed.statusEffects

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory

class AflameStatusEffect(category: StatusEffectCategory?, color: Int) : StatusEffect(category, color) {
    override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean {
        return true
    }

    override fun applyUpdateEffect(entity: LivingEntity?, amplifier: Int) {
        entity?.setOnFireFor(1 + (1 shl amplifier))
    }
}