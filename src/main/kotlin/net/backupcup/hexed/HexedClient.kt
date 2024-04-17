package net.backupcup.hexed

import net.backupcup.hexed.altar.AccursedAltarRunesRenderer
import net.backupcup.hexed.enchantments.bow.AggravateHexUI
import net.backupcup.hexed.enchantments.bow.PhasedHexUI
import net.backupcup.hexed.enchantments.crossbow.OverclockHexUI
import net.backupcup.hexed.enchantments.crossbow.ProvisionHexUI
import net.backupcup.hexed.register.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

object HexedClient: ClientModInitializer {
    override fun onInitializeClient() {
        RegisterScreenHandlers.registerScreenHandlers()
        RegisterScreens.registerScreens()
        RegisterBlocks.registerBlockCutouts()
        RegisterEntities.registerEntityModels()
        RegisterPackets.registerClientPackets()

        ProvisionHexUI.registerClient()
        OverclockHexUI.registerClient()
        AggravateHexUI.registerClient()
        PhasedHexUI.registerClient()

        BlockEntityRendererRegistry.register(RegisterBlockEntities.ACCURSED_ALTAR_BLOCK_ENTITY
        ) { AccursedAltarRunesRenderer() }

        FluidRenderHandlerRegistry.INSTANCE.register(RegisterSlagBlocks.STILL_BLAZING_MAGMA, RegisterSlagBlocks.FLOW_BLAZING_MAGMA,
            SimpleFluidRenderHandler(
                Identifier(Hexed.MOD_ID, "block/blazing_magma_still"),
                Identifier(Hexed.MOD_ID, "block/blazing_magma_flow")
            ))

        ClientPlayNetworking.registerGlobalReceiver(
            Hexed.SYNC_CONFIG_PACKET
        ) { _: MinecraftClient?, _: ClientPlayNetworkHandler?, buf: PacketByteBuf?, _: PacketSender? ->
            if (buf != null) {
                Config.readFromServer(buf)?.let {
                    Hexed.setConfig(it)
                }
            }
        }
    }
}