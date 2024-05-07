package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.item.harvest.BlightedHarvestItem
import net.backupcup.hexed.item.labrys.BishopLabrysItem
import net.backupcup.hexed.item.basher.BlazingBasherItem
import net.backupcup.hexed.item.carnage.BalefulCarnageItem
import net.backupcup.hexed.util.TaintedItem
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ToolMaterials
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity

object RegisterTaintItems {
    private var itemList: MutableList<Item> = mutableListOf()

    fun register(item: Item): Item {
        itemList.add(item)
        return item
    }

    //DAIRY
    val BISHOP_LABRYS: Item = register( //SMILLY
        BishopLabrysItem(ToolMaterials.NETHERITE, 5f, -3.2f, "bishop_labrys", FabricItemSettings()
            .rarity(Rarity.EPIC)))

    val BLIGHTED_HARVEST: Item = register(
        BlightedHarvestItem(ToolMaterials.NETHERITE, 1, -2f, "blighted_harvest", FabricItemSettings()
            .rarity(Rarity.EPIC)))

    val BLAZING_BASHER: Item = register( //MIRI
        BlazingBasherItem(ToolMaterials.NETHERITE, 2, -2.4f, "blazing_basher", FabricItemSettings()
            .rarity(Rarity.EPIC)))

    //TIMEFALL
    val BALEFUL_CARNAGE: Item = register( //PHOENIX
        BalefulCarnageItem(ToolMaterials.NETHERITE, 3, -2.6f, "baleful_carnage", FabricItemSettings()
            .rarity(Rarity.EPIC)))

    fun registerTaintItems() {
        itemList.forEach { item -> Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, (item as TaintedItem<String>).itemId), item) }
    }
}