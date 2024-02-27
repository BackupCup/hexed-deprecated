package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.altar.AccursedAltar
import net.backupcup.hexed.block.*
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
import net.minecraft.util.Rarity

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

    val SKELETON_SKULL_CANDLE: Block = SkeletonSkullCandle(FabricBlockSettings.create()
        .strength(1f)
        .sounds(BlockSoundGroup.STONE)
        .mapColor(MapColor.GRAY)
        .nonOpaque()
        .luminance(SkeletonSkullCandle.STATE_TO_LUMINANCE)
        .pistonBehavior(PistonBehavior.DESTROY)
    )

    val WITHER_SKULL_CANDLE: Block = WitherSkullCandle(FabricBlockSettings.create()
        .strength(1f)
        .sounds(BlockSoundGroup.STONE)
        .mapColor(MapColor.GRAY)
        .nonOpaque()
        .luminance(WitherSkullCandle.STATE_TO_LUMINANCE)
        .pistonBehavior(PistonBehavior.DESTROY)
    )

    val CALAMAIDAS_PLUSHIE: Block = PlushieBlock(FabricBlockSettings.create()
        .strength(1f)
        .sounds(BlockSoundGroup.WOOL)
        .mapColor(MapColor.WHITE_GRAY)
        .nonOpaque()
        .pistonBehavior(PistonBehavior.NORMAL)
    )

    fun registerBlocks() {
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "brimstone_candle"), BRIMSTONE_CANDLE)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_candle"), BlockItem(BRIMSTONE_CANDLE, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "lichlore_candle"), LICHLORE_CANDLE)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "lichlore_candle"), BlockItem(LICHLORE_CANDLE, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "skeleton_skull_candle"), SKELETON_SKULL_CANDLE)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "skeleton_skull_candle"), BlockItem(SKELETON_SKULL_CANDLE, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "wither_skull_candle"), WITHER_SKULL_CANDLE)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "wither_skull_candle"), BlockItem(WITHER_SKULL_CANDLE, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "accursed_altar"), ACCURSED_ALTAR)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "accursed_altar"), BlockItem(ACCURSED_ALTAR, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "calamaidas_plushie"), CALAMAIDAS_PLUSHIE)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "calamaidas_plushie"), BlockItem(CALAMAIDAS_PLUSHIE, FabricItemSettings().rarity(Rarity.EPIC)))
        RegisterSlagBlocks.registerSlagBlocks()
    }
}