package net.backupcup.hexed.loot

import net.backupcup.hexed.register.RegisterItems
import net.fabricmc.fabric.api.loot.v2.LootTableEvents
import net.fabricmc.fabric.api.loot.v2.LootTableSource
import net.minecraft.loot.LootManager
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.LootTables
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier

object ModifyLootTables {
    private val bastionLoot = arrayOf(LootTables.BASTION_TREASURE_CHEST, LootTables.BASTION_BRIDGE_CHEST, LootTables.BASTION_OTHER_CHEST)
    fun modifyBastion() {
        LootTableEvents.MODIFY.register(LootTableEvents.Modify {
            resourceManager: ResourceManager?,
            lootManager: LootManager?,
            id: Identifier?, tableBuilder:
            LootTable.Builder,
            source: LootTableSource ->

            val crystalWeight = if (id == LootTables.BASTION_TREASURE_CHEST) 100 else 0
            val fabricWeight = when(id) {
                LootTables.BASTION_TREASURE_CHEST -> 40
                LootTables.BASTION_BRIDGE_CHEST -> 18
                LootTables.BASTION_OTHER_CHEST -> 12
                else -> 0
            }

            if (id != null) {
                if (source.isBuiltin && bastionLoot.contains(id)) {
                    val poolBuilder = LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(0.25f, 1f))
                        .with(ItemEntry.builder(RegisterItems.BRIMSTONE_CRYSTAL).weight(crystalWeight))
                        .with(ItemEntry.builder(RegisterItems.CALAMITOUS_FABRIC).weight(fabricWeight))
                    tableBuilder.pool(poolBuilder)
                }
            }
        })
    }

    fun registerLootModifiers() {
        modifyBastion()
    }
}