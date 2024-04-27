package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.item.labrys.BishopLabrysItem
import net.backupcup.hexed.util.TaintedItem
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.SwordItem
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

    val BISHOP_LABRYS: Item = register(
        BishopLabrysItem(ToolMaterials.NETHERITE, 5f, -3.2f, "bishop_labrys", FabricItemSettings()
            .rarity(Rarity.EPIC))
    )

//    val MILKYFUR_SICKLE: Item = register(
//        SwordItem(ToolMaterials.NETHERITE, 1, -2f, "???", FabricItemSettings()
//            .rarity(Rarity.EPIC))
//    )
//
//    val YIRMIRI_BAT: Item = register(
//        SwordItem(ToolMaterials.NETHERITE, 2, -2.4f, "???", FabricItemSettings()
//            .rarity(Rarity.EPIC))
//    )

    fun registerTaintItems() {
        itemList.forEach { item -> Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, (item as TaintedItem<String>).itemId), item) }
    }
}