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
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.render.RenderLayer
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.DustParticleEffect
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import kotlin.random.Random

object HexedClient: ClientModInitializer {
    override fun onInitializeClient() {
        RegisterScreenHandlers.registerScreenHandlers()
        RegisterScreens.registerScreens()
        RegisterBlocks.registerBlockCutouts()
        RegisterEntities.registerEntityModels()
        RegisterPackets.registerPackets()

        BlockEntityRendererRegistry.register(RegisterBlockEntities.ACCURSED_ALTAR_BLOCK_ENTITY
        ) { AccursedAltarRunesRenderer() }

        FluidRenderHandlerRegistry.INSTANCE.register(RegisterSlagBlocks.STILL_BLAZING_MAGMA, RegisterSlagBlocks.FLOW_BLAZING_MAGMA,
            SimpleFluidRenderHandler(
                Identifier(Hexed.MOD_ID, "block/blazing_magma_still"),
                Identifier(Hexed.MOD_ID, "block/blazing_magma_flow")
            ))
    }
}