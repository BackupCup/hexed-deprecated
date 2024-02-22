package net.backupcup.hexed.statusEffects

import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.effect.StatusEffectCategory

class EtherealStatusEffect(category: StatusEffectCategory?, color: Int, val modifier: Double) : AbstractHexStatusEffect(category, color) {
    override fun adjustModifierAmount(amplifier: Int, modifier: EntityAttributeModifier?): Double {
        return this.modifier * (amplifier + 1)
    }
}