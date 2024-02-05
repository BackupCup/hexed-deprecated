package net.backupcup.hexed

import net.backupcup.hexed.altar.BrimstoneCrystalRenderer
import net.backupcup.hexed.register.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.minecraft.client.render.RenderLayer

object HexedClient: ClientModInitializer {
    override fun onInitializeClient() {
        RegisterScreenHandlers.registerScreenHandlers()
        RegisterScreens.registerScreens()

        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.BRIMSTONE_CANDLE, RenderLayer.getCutout())
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.LICHLORE_CANDLE, RenderLayer.getCutout())
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.ACCURSED_ALTAR, RenderLayer.getCutout())


        RegisterDecoCandles.candleTypes.forEach {(_, block) ->
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
        }


        BlockEntityRendererRegistry.register(RegisterBlockEntities.ACCURSED_ALTAR_BLOCK_ENTITY
        ) { BrimstoneCrystalRenderer() }
    }
}