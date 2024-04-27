package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier

object RegisterSounds {
    val ACCURSED_ALTAR_HEX: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "accursed_altar_hex"))
    val ACCURSED_ALTAR_TAINT: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "accursed_altar_taint"))
    val ACCURSED_ALTAR_ACTIVATE: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "accursed_altar_activate"))

    val BEEP_ALT: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "beep_alt"))

    val PROVISION_FAIL: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "provision_fail"))
    val PROVISION_CHARGE: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "provision_charge"))
    val PROVISION_IN_RANGE: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "provision_in_range"))

    val OVERCLOCK_TIER: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "overclock_tier"))
    val AGGRAVATE_TIER: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "aggravate_tier"))

    val PHASED_SHOT: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "phased_shot"))
    val PHASED_TIER: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "phased_tier"))

    val ELECTRIC_CHARGE: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "electric_charge"))
    val ELECTRIC_DISCHARGE: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "electric_discharge"))

    val TECH_LAUGH: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "tech_laugh"))

    val YIPPEE: SoundEvent = SoundEvent.of(Identifier(Hexed.MOD_ID, "yippee"))

    fun registerSounds() {
        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "accursed_altar_hex"), ACCURSED_ALTAR_HEX)
        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "accursed_altar_taint"), ACCURSED_ALTAR_TAINT)
        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "accursed_altar_activate"), ACCURSED_ALTAR_ACTIVATE)

        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "beep_alt"), BEEP_ALT)

        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "provision_fail"), PROVISION_FAIL)
        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "provision_charge"), PROVISION_CHARGE)

        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "provision_in_range"), PROVISION_IN_RANGE)
        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "overclock_tier"), OVERCLOCK_TIER)
        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "aggravate_tier"), AGGRAVATE_TIER)

        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "phased_shot"), PHASED_SHOT)
        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "phased_tier"), PHASED_TIER)

        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "electric_charge"), ELECTRIC_CHARGE)
        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "electric_discharge"), ELECTRIC_DISCHARGE)

        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "tech_laugh"), TECH_LAUGH)
        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "yippee"), YIPPEE)
    }
}