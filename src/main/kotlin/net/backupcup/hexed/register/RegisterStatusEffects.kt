package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.statusEffects.AblazeStatusEffect
import net.backupcup.hexed.statusEffects.AflameStatusEffect
import net.backupcup.hexed.statusEffects.EtherealStatusEffect
import net.backupcup.hexed.statusEffects.IrradiatedStatusEffect
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RegisterStatusEffects {
    //Aflame Hex
    val AFLAME: StatusEffect = AflameStatusEffect(StatusEffectCategory.BENEFICIAL, 0x7a3045)
    val ABLAZE: StatusEffect = AblazeStatusEffect(StatusEffectCategory.BENEFICIAL, 0xae2334)


    //UNUSED
    val IRRADIATED: StatusEffect = IrradiatedStatusEffect(StatusEffectCategory.BENEFICIAL, 0xcddf6c)
    val ETHEREAL: StatusEffect = EtherealStatusEffect(StatusEffectCategory.BENEFICIAL, 0x0b8a8f)

    fun registerStatusEffects() {
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "aflame"), AFLAME)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "ablaze"), ABLAZE)

        //UNUSED
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "irradiated"), IRRADIATED)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "ethereal"), ETHEREAL)
    }
}