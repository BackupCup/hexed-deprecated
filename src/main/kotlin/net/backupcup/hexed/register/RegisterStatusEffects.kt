package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.statusEffects.AflameStatusEffect
import net.backupcup.hexed.statusEffects.EtherealStatusEffect
import net.backupcup.hexed.statusEffects.IrradiatedStatusEffect
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RegisterStatusEffects {
    val IRRADIATED: StatusEffect = IrradiatedStatusEffect(StatusEffectCategory.BENEFICIAL, 0xcddf6c)
    val AFLAME: StatusEffect = AflameStatusEffect(StatusEffectCategory.BENEFICIAL, 0x7a3045)
    val ETHEREAL: StatusEffect = EtherealStatusEffect(StatusEffectCategory.BENEFICIAL, 0x0b8a8f)

    fun registerStatusEffects() {
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "irradiated"), IRRADIATED)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "aflame"), AFLAME)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "ethereal"), ETHEREAL)
    }
}