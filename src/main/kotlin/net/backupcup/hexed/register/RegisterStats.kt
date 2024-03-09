package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.stat.StatFormatter
import net.minecraft.stat.Stats
import net.minecraft.util.Identifier

object RegisterStats {
    val ACCURSED_ALTAR_OPENED: Identifier = Identifier(Hexed.MOD_ID, "accused_altar_opened")
    val ITEMS_HEXED: Identifier = Identifier(Hexed.MOD_ID, "items_hexed")

    fun registerStats() {
        Registry.register(Registries.CUSTOM_STAT, "accused_altar_opened", ACCURSED_ALTAR_OPENED)
        Stats.CUSTOM.getOrCreateStat(ACCURSED_ALTAR_OPENED, StatFormatter.DEFAULT)

        Registry.register(Registries.CUSTOM_STAT, "items_hexed", ITEMS_HEXED)
        Stats.CUSTOM.getOrCreateStat(ITEMS_HEXED, StatFormatter.DEFAULT)
    }
}