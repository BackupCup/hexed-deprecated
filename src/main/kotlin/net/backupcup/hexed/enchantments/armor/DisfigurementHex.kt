package net.backupcup.hexed.enchantments.armor

import net.backupcup.hexed.Hexed
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
import org.apache.commons.codec.binary.Hex

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
        val boxRadius: Int = Hexed.getConfig()?.disfigurementHex?.boxRadius ?: 3

        val box = Box(
            user.x-boxRadius, user.y-boxRadius, user.z-boxRadius,
            user.x+boxRadius, user.y+boxRadius, user.z+boxRadius)
        val affectedEntities = user.world.getNonSpectatingEntities(
            LivingEntity::class.java, box)

        for (livingEntity in affectedEntities) {
            if (HexHelper.hasFullRobes(user) && livingEntity == user) { continue }
            livingEntity.addStatusEffect(
                StatusEffectInstance(
                    StatusEffects.WEAKNESS,
                    Hexed.getConfig()?.disfigurementHex?.weaknessDuration ?: 100, Hexed.getConfig()?.disfigurementHex?.weaknessAmplifier ?: 0,
                    true, true, true
                ))
            livingEntity.addStatusEffect(
                StatusEffectInstance(
                    StatusEffects.HUNGER,
                    Hexed.getConfig()?.disfigurementHex?.hungerDuration ?: 100, Hexed.getConfig()?.disfigurementHex?.hungerAmplifier ?: 0,
                    true, true, true
                ))
        }

        super.onUserDamaged(user, attacker, level)
    }
}