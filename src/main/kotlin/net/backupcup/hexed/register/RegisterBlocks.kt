package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.altar.AccursedAltar
import net.backupcup.hexed.block.*
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.client.render.RenderLayer
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

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "accursed_altar"), ACCURSED_ALTAR)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "accursed_altar"), BlockItem(ACCURSED_ALTAR, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "calamaidas_plushie"), CALAMAIDAS_PLUSHIE)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "calamaidas_plushie"), BlockItem(CALAMAIDAS_PLUSHIE, FabricItemSettings().rarity(Rarity.EPIC)))
        RegisterSlagBlocks.registerSlagBlocks()
    }

    fun registerBlockCutouts() {
        BlockRenderLayerMap.INSTANCE.putBlock(BRIMSTONE_CANDLE, RenderLayer.getCutout())
        BlockRenderLayerMap.INSTANCE.putBlock(LICHLORE_CANDLE, RenderLayer.getCutout())
        BlockRenderLayerMap.INSTANCE.putBlock(ACCURSED_ALTAR, RenderLayer.getCutout())

        BlockRenderLayerMap.INSTANCE.putBlock(CALAMAIDAS_PLUSHIE, RenderLayer.getCutout())

        BlockRenderLayerMap.INSTANCE.putBlock(RegisterSlagBlocks.BRIMSTONE_SLAG_PILLAR, RenderLayer.getCutout())
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterSlagBlocks.LAVENDIN_VERDURE, RenderLayer.getCutout())
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterSlagBlocks.LAVA_PISTIL, RenderLayer.getCutout())

        RegisterDecoCandles.candleTypes.forEach {(_, block) ->
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
        }
    }
}