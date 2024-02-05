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

    fun registerSounds() {
        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "accursed_altar_hex"), ACCURSED_ALTAR_HEX)
        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "accursed_altar_taint"), ACCURSED_ALTAR_TAINT)
        Registry.register(Registries.SOUND_EVENT, Identifier(Hexed.MOD_ID, "accursed_altar_activate"), ACCURSED_ALTAR_ACTIVATE)
    }
}