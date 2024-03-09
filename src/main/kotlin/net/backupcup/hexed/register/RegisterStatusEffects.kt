package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.statusEffects.*
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RegisterStatusEffects {
    //Aflame Hex
    val AFLAME: StatusEffect = AflameStatusEffect(StatusEffectCategory.BENEFICIAL, 0x7a3045)
    val ABLAZE: StatusEffect = AblazeStatusEffect(StatusEffectCategory.BENEFICIAL, 0xae2334)

    //Persecuted Hex
    val ETHEREAL: StatusEffect = EtherealStatusEffect(StatusEffectCategory.BENEFICIAL, 0xe83b3b, -1.0)
        .addAttributeModifier(EntityAttributes.GENERIC_ARMOR, "0b99b923-903b-436a-b865-fdc3fe63aad7", 0.0, EntityAttributeModifier.Operation.ADDITION)

    //Ephemeral Hex
    val EXHAUSTION: StatusEffect = ExhaustionStatusEffect(StatusEffectCategory.BENEFICIAL, 0xe6904e)

    //Vindictive Hex
    val VINDICTIVE: StatusEffect = VindictiveStatusEffect(StatusEffectCategory.BENEFICIAL, 0xf4a58d)
    val SMOULDERING: StatusEffect = SmoulderingStatusEffect(StatusEffectCategory.BENEFICIAL, 0xcddf6c)

    //Traitorous Hex
    val TRAITOROUS: StatusEffect = TraitorousStatusEffect(StatusEffectCategory.BENEFICIAL, 0xe83b3b)

    //Ironclad Hex
    val IRONCLAD: StatusEffect = IroncladStatusEffect(StatusEffectCategory.BENEFICIAL, 0xfb6b1d, -2.0)
        .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, "f8164c46-1f2d-4286-a7e3-8617e355cd1e", 0.0, EntityAttributeModifier.Operation.ADDITION)

    //Frantic Hex
    val FRANTIC: StatusEffect = FranticStatusEffect(StatusEffectCategory.BENEFICIAL, 0x7a3045, 0.025)
        .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "0c496f0a-47bb-419a-954c-58aeb2d1708a", 0.0, EntityAttributeModifier.Operation.ADDITION)

    //Overburden Hex
    val OVERBURDEN: StatusEffect = OverburdenStatusEffect(StatusEffectCategory.BENEFICIAL, 0xe83b3b, -0.00390625)
        .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "91ebbc45-4c4c-4772-9b21-9a6bbbef6870", 0.0, EntityAttributeModifier.Operation.MULTIPLY_BASE)
    fun registerStatusEffects() {
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "aflame"), AFLAME)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "ablaze"), ABLAZE)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "ethereal"), ETHEREAL)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "exhaustion"), EXHAUSTION)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "vindictive"), VINDICTIVE)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "smouldering"), SMOULDERING)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "traitorous"), TRAITOROUS)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "ironclad"), IRONCLAD)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "frantic"), FRANTIC)
        Registry.register(Registries.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "overburden"), OVERBURDEN)
    }
}