package net.backupcup.hexed

import net.backupcup.hexed.altar.AccursedAltarRunesRenderer
import net.backupcup.hexed.altar.AccursedAltarScreen
import net.backupcup.hexed.packets.AltarNetworkingConstants
import net.backupcup.hexed.register.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.render.RenderLayer
import net.minecraft.network.PacketByteBuf

object HexedClient: ClientModInitializer {
    override fun onInitializeClient() {
        RegisterScreenHandlers.registerScreenHandlers()
        RegisterScreens.registerScreens()

        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.BRIMSTONE_CANDLE, RenderLayer.getCutout())
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.LICHLORE_CANDLE, RenderLayer.getCutout())
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.ACCURSED_ALTAR, RenderLayer.getCutout())

        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.SKELETON_SKULL_CANDLE, RenderLayer.getCutout())
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.WITHER_SKULL_CANDLE, RenderLayer.getCutout())
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.CALAMAIDAS_PLUSHIE, RenderLayer.getCutout())


        RegisterDecoCandles.candleTypes.forEach {(_, block) ->
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
        }

        BlockEntityRendererRegistry.register(RegisterBlockEntities.ACCURSED_ALTAR_BLOCK_ENTITY
        ) { AccursedAltarRunesRenderer() }

        ClientPlayNetworking.registerGlobalReceiver(
            AltarNetworkingConstants.AVAILABLE_HEX_PACKET,
            HexedClient::syncAltarScreenData
        )

        ClientPlayNetworking.registerGlobalReceiver(
            AltarNetworkingConstants.ACTIVE_ALTAR_PACKET,
            HexedClient::syncActiveScreenData
        )
    }

    fun syncAltarScreenData(client: MinecraftClient, handler: ClientPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender) {
        val altarScreen = MinecraftClient.getInstance().currentScreen as? AccursedAltarScreen ?: return

        val listLength = buf.readInt()
        val availableHexList: MutableList<String> = mutableListOf()
        for (i in 0..<listLength) {
            availableHexList.add(i, buf.readString())
        }
        val currentHex: String = buf.readString()

        altarScreen.updateScreenHexData(currentHex, availableHexList)
    }

    fun syncActiveScreenData(client: MinecraftClient, handler: ClientPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender) {
        val altarScreen = MinecraftClient.getInstance().currentScreen as? AccursedAltarScreen ?: return
        val isAltarActive = buf.readInt()

        altarScreen.updateScreenActiveData(isAltarActive)
    }
}