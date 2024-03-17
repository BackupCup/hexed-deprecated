package net.backupcup.hexed.statusEffects

import net.backupcup.hexed.register.RegisterDamageTypes
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.world.event.GameEvent

class AblazeStatusEffect(category: StatusEffectCategory?, color: Int) : AbstractHexStatusEffect(category, color) {
    override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean {
        val i: Int = 40 shr amplifier
        if (i > 0) {
            return duration % i == 0
        }
        return true
    }

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        applyAblazeDamage(entity)
    }

    override fun onApplied(entity: LivingEntity, attributes: AttributeContainer, amplifier: Int) {
        applyAblazeDamage(entity)
    }

    fun applyAblazeDamage(entity: LivingEntity) {
        entity.damage(RegisterDamageTypes.of(entity.world, RegisterDamageTypes.ABLAZE_DAMAGE), 2f)
    }
}