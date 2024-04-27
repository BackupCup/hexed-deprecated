package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.block.PlushieBlock
import net.backupcup.hexed.util.TaintedItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object RegisterItemGroupMain {
    fun registerItemGroup(): ItemGroup {
        return Registry.register(Registries.ITEM_GROUP, Identifier(Hexed.MOD_ID, "hexed_group"), FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.hexed.hexed_group"))
            .icon { ItemStack(RegisterItems.BRIMSTONE_CRYSTAL) }
            .entries { _, entries ->
                entries.add(ItemStack(RegisterItems.BRIMSTONE_CRYSTAL))
                entries.add(ItemStack(RegisterItems.CALAMITOUS_FABRIC))

                entries.add(ItemStack(RegisterArmor.CALAMITOUS_HELMET))
                entries.add(ItemStack(RegisterArmor.CALAMITOUS_CHESTPLATE))
                entries.add(ItemStack(RegisterArmor.CALAMITOUS_LEGGINGS))
                entries.add(ItemStack(RegisterArmor.CALAMITOUS_BOOTS))

                entries.add(ItemStack(RegisterBlocks.ACCURSED_ALTAR))

                entries.add(ItemStack(RegisterBlocks.BRIMSTONE_CANDLE))
                entries.add(ItemStack(RegisterBlocks.LICHLORE_CANDLE))

                Registries.ITEM.forEach { item -> if (item is TaintedItem<*>) entries.add(ItemStack(item)) }
            }.build()
        )
    }
}

object RegisterItemGroupDeco {
    fun registerItemGroup(): ItemGroup {
        return Registry.register(Registries.ITEM_GROUP, Identifier(Hexed.MOD_ID, "hexed_deco_group"), FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.hexed.hexed_deco_group"))
            .icon { ItemStack(RegisterSlagBlocks.BRIMSTONE_SLAG_PILLAR) }
            .entries { _, entries ->
                entries.add(RegisterSlagBlocks.LAVENDIN_CINDER)
                entries.add(RegisterSlagBlocks.LAVENDIN_VERDURE)
                entries.add(RegisterSlagBlocks.LAVA_PISTIL)
                entries.add(RegisterSlagBlocks.BLAZING_MAGMA_BUCKET)

                entries.add(RegisterSlagBlocks.BRIMSTONE_SLAG)
                entries.add(RegisterSlagBlocks.BRIMSTONE_BRICKS_STAIRS)
                entries.add(RegisterSlagBlocks.BRIMSTONE_BRICKS_SLAB)
                entries.add(RegisterSlagBlocks.BRIMSTONE_BRICKS_WALL)

                entries.add(RegisterSlagBlocks.BRIMSTONE_BRICKS)
                entries.add(RegisterSlagBlocks.BRIMSTONE_SLAG_STAIRS)
                entries.add(RegisterSlagBlocks.BRIMSTONE_SLAG_SLAB)
                entries.add(RegisterSlagBlocks.BRIMSTONE_SLAG_WALL)

                entries.add(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG)
                entries.add(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_STAIRS)
                entries.add(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_SLAB)
                entries.add(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_WALL)

                entries.add(RegisterSlagBlocks.BRIMSTONE_SLAG_PILLAR)
                entries.add(RegisterSlagBlocks.CHISELED_BRIMSTONE_SLAG)

                RegisterDecoCandles.candleTypes.forEach { (_, block, _) ->
                    entries.add(ItemStack(block))
                }
            }.build()
        )
    }
}

object RegisterItemGroupPlushies {
    fun registerItemGroup(): ItemGroup {
        return Registry.register(Registries.ITEM_GROUP, Identifier(Hexed.MOD_ID, "hexed_plushie_group"), FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.hexed.hexed_plushie_group"))
            .icon { ItemStack(RegisterPlushies.CALAMAIDAS_PLUSHIE) }
            .entries { _, entries ->
                Registries.BLOCK.forEach { block ->
                    if (block is PlushieBlock) entries.add(ItemStack(block.asItem()))
                }
            }
            .build()
        )
    }
}

fun registerAllGroups() {
    RegisterItemGroupMain.registerItemGroup()
    RegisterItemGroupDeco.registerItemGroup()
    RegisterItemGroupPlushies.registerItemGroup()
}