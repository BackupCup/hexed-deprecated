package net.backupcup.hexed.statusEffects

import net.backupcup.hexed.register.RegisterDamageTypes
import net.backupcup.hexed.register.RegisterStatusEffects
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.effect.StatusEffectCategory

class VindictiveStatusEffect(category: StatusEffectCategory?, color: Int) : AbstractHexStatusEffect(category, color) {
    override fun onRemoved(entity: LivingEntity, attributes: AttributeContainer, amplifier: Int) {
        entity.damage(RegisterDamageTypes.of(entity.world, RegisterDamageTypes.VINDICTIVE_DAMAGE), (amplifier + 1).toFloat())
        if (entity.hasStatusEffect(RegisterStatusEffects.VINDICTIVE)) {
            entity.removeStatusEffect(RegisterStatusEffects.VINDICTIVE)
        }
    }
}