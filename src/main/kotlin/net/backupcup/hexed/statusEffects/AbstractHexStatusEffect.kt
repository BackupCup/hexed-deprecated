package net.backupcup.hexed.statusEffects

import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory

abstract class AbstractHexStatusEffect(category: StatusEffectCategory?, color: Int) : StatusEffect(category, color) {
}