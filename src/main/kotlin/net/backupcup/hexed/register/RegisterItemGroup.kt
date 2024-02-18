package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object RegisterItemGroup {

    fun registerItemGroup(): ItemGroup {
        return Registry.register(Registries.ITEM_GROUP, Identifier(Hexed.MOD_ID, "hexed_group"), FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.hexed.hexed_group"))
            .icon { ItemStack(RegisterItems.BRIMSTONE_CRYSTAL) }
            .entries { _, entries ->
                entries.add(ItemStack(RegisterBlocks.CALAMAIDAS_PLUSHIE))
                entries.add(ItemStack(RegisterItems.BRIMSTONE_CRYSTAL))
                entries.add(ItemStack(RegisterItems.CALAMITOUS_FABRIC))

                entries.add(ItemStack(RegisterArmor.CALAMITOUS_HELMET))
                entries.add(ItemStack(RegisterArmor.CALAMITOUS_CHESTPLATE))
                entries.add(ItemStack(RegisterArmor.CALAMITOUS_LEGGINGS))
                entries.add(ItemStack(RegisterArmor.CALAMITOUS_BOOTS))

                entries.add(ItemStack(RegisterBlocks.ACCURSED_ALTAR))

                entries.add(ItemStack(RegisterBlocks.SKELETON_SKULL_CANDLE))
                entries.add(ItemStack(RegisterBlocks.WITHER_SKULL_CANDLE))

                entries.add(ItemStack(RegisterBlocks.BRIMSTONE_CANDLE))
                entries.add(ItemStack(RegisterBlocks.LICHLORE_CANDLE))

                RegisterDecoCandles.candleTypes.forEach { (_, block, _) ->
                    entries.add(ItemStack(block))
                }
            }.build()
        )
    }
}