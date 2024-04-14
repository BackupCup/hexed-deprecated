package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.block.PlushieBlock
import net.backupcup.hexed.item.TooltipItem
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.loot.v2.LootTableEvents
import net.minecraft.item.Item
import net.minecraft.item.MusicDiscItem
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTables
import net.minecraft.loot.condition.RandomChanceLootCondition
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity

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