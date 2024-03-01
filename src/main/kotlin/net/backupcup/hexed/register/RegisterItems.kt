package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.item.TooltipItem
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

object RegisterItems {
    val CALAMITOUS_FABRIC: Item = TooltipItem(FabricItemSettings(),
        listOf(
            Text.translatable("tooltip.hexed.calamitous_fabric.line_1").formatted(Formatting.DARK_RED, Formatting.ITALIC, Formatting.BOLD),
            Text.translatable("tooltip.hexed.calamitous_fabric.line_2").formatted(Formatting.GRAY)
        ))
    val BRIMSTONE_CRYSTAL: Item = TooltipItem(FabricItemSettings(),
        listOf(
            Text.translatable("tooltip.hexed.brimstone_crystal.line_1").formatted(Formatting.DARK_RED, Formatting.ITALIC, Formatting.BOLD),
            Text.translatable("tooltip.hexed.brimstone_crystal.line_2").formatted(Formatting.GRAY),
            Text.translatable("tooltip.hexed.brimstone_crystal.line_3").formatted(Formatting.GRAY)
        ))

    fun registerItems() {
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "calamitous_fabric"), CALAMITOUS_FABRIC)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_crystal"), BRIMSTONE_CRYSTAL)
        RegisterRunes.registerRunes()
    }
}