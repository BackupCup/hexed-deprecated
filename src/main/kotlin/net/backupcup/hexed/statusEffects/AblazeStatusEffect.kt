package net.backupcup.hexed.statusEffects

import net.backupcup.hexed.register.RegisterDamageTypes
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.effect.StatusEffectCategory

class AblazeStatusEffect(category: StatusEffectCategory?, color: Int) : AbstractHexStatusEffect(category, color) {
    override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean {
        val i: Int = 40 shr amplifier
        if (i > 0) {
            return duration % i == 0
        }
        return true
    }

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        entity.damage(RegisterDamageTypes.of(entity.world, RegisterDamageTypes.ABLAZE_DAMAGE), (1 * (amplifier + 1)).toFloat())
    }

    override fun onApplied(entity: LivingEntity, attributes: AttributeContainer, amplifier: Int) {
        entity.damage(RegisterDamageTypes.of(entity.world, RegisterDamageTypes.ABLAZE_DAMAGE), (1 * (amplifier + 1)).toFloat())
    }
}