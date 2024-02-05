package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.altar.AccursedAltar
import net.backupcup.hexed.block.BrimstoneCandle
import net.backupcup.hexed.block.LichloreCandle
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

object RegisterBlocks {
    val BRIMSTONE_CANDLE: Block = BrimstoneCandle(FabricBlockSettings.create()
        .strength(.1f)
        .sounds(BlockSoundGroup.CANDLE)
        .mapColor(MapColor.BRIGHT_RED)
        .nonOpaque()
        .luminance(BrimstoneCandle.STATE_TO_LUMINANCE)
        .pistonBehavior(PistonBehavior.DESTROY)
    )

    val LICHLORE_CANDLE: Block = LichloreCandle(FabricBlockSettings.create()
        .strength(.1f)
        .sounds(BlockSoundGroup.CANDLE)
        .mapColor(MapColor.DARK_GREEN)
        .nonOpaque()
        .luminance(LichloreCandle.STATE_TO_LUMINANCE)
        .pistonBehavior(PistonBehavior.DESTROY)
    )

    val ACCURSED_ALTAR: Block = AccursedAltar(FabricBlockSettings.create()
        .strength(3.5f)
        .sounds(BlockSoundGroup.ANCIENT_DEBRIS)
        .mapColor(MapColor.DARK_CRIMSON)
        .nonOpaque()
        .pistonBehavior(PistonBehavior.BLOCK))

    fun registerBlocks() {
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "brimstone_candle"), BRIMSTONE_CANDLE)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_candle"), BlockItem(BRIMSTONE_CANDLE, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "lichlore_candle"), LICHLORE_CANDLE)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "lichlore_candle"), BlockItem(LICHLORE_CANDLE, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "accursed_altar"), ACCURSED_ALTAR)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "accursed_altar"), BlockItem(ACCURSED_ALTAR, FabricItemSettings()))
    }
}