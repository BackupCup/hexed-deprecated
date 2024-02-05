package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RegisterRunes {
    val OMEGA: Item = Item(FabricItemSettings())
    val FIRE: Item = Item(FabricItemSettings())
    val MAGNESIUM: Item = Item(FabricItemSettings())
    val MERCURY: Item = Item(FabricItemSettings())
    val MOON: Item = Item(FabricItemSettings())
    val SULFUR: Item = Item(FabricItemSettings())

    fun registerRunes() {
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "rune_omega"), OMEGA)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "rune_fire"), FIRE)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "rune_magnesium"), MAGNESIUM)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "rune_mercury"), MERCURY)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "rune_moon"), MOON)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "rune_sulfur"), SULFUR)
    }
}