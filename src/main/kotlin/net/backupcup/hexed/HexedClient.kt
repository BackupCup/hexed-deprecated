package net.backupcup.hexed

import com.google.gson.annotations.Until
import net.backupcup.hexed.altar.AccursedAltarRunesRenderer
import net.backupcup.hexed.altar.AccursedAltarScreen
import net.backupcup.hexed.packets.AltarNetworkingConstants
import net.backupcup.hexed.packets.HexNetworkingConstants
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
import net.minecraft.particle.DustParticleEffect
import net.minecraft.util.math.Vec3d
import kotlin.random.Random

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

        BlockRenderLayerMap.INSTANCE.putBlock(RegisterSlagBlocks.BRIMSTONE_SLAG_PILLAR, RenderLayer.getCutout())


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

        ClientPlayNetworking.registerGlobalReceiver(
            HexNetworkingConstants.BLOODTHIRSTY_PARTICLE_PACKET,
            HexedClient::createBloodthirstyParticles
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

    fun createBloodthirstyParticles(client: MinecraftClient, handler: ClientPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender) {
        val random = buf.readInt()
        val targetPos = Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())

        for (i in 0..random) {
            client.particleManager.addParticle(
                DustParticleEffect(Vec3d.unpackRgb(10027008).toVector3f(), 1.5f),
                targetPos.x + randVec(), targetPos.y + 0.5 + randVec(), targetPos.z + randVec(),
                0.0, 0.5,
                0.0
            )
        }
    }

    private fun randVec(): Double {
        return Random.nextDouble(-.5, .5) * if (Random.nextInt(0, 1) == 0) -1 else 1
    }
}