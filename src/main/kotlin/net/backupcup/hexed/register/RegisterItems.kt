package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RegisterItems {
    val CALAMITOUS_FABRIC: Item = Item(FabricItemSettings())
    val BRIMSTONE_CRYSTAL: Item = Item(FabricItemSettings())

    fun registerItems() {
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "calamitous_fabric"), CALAMITOUS_FABRIC)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_crystal"), BRIMSTONE_CRYSTAL)
        RegisterRunes.registerRunes()
    }
}