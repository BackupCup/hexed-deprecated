package net.backupcup.hexed.register

import net.backupcup.hexed.altar.AccursedAltarScreen
import net.backupcup.hexed.packets.AltarNetworkingConstants
import net.backupcup.hexed.packets.HexNetworkingConstants
import net.backupcup.hexed.util.HexRandom
import net.backupcup.hexed.util.PredicateInterface
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.DustParticleEffect
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d

object RegisterPackets {

    fun registerClientPackets() {
        ClientPlayNetworking.registerGlobalReceiver(
            AltarNetworkingConstants.AVAILABLE_HEX_PACKET,
            RegisterPackets::syncAltarScreenData
        )

        ClientPlayNetworking.registerGlobalReceiver(
            AltarNetworkingConstants.ACTIVE_ALTAR_PACKET,
            RegisterPackets::syncActiveScreenData
        )

        ClientPlayNetworking.registerGlobalReceiver(
            HexNetworkingConstants.BLOODTHIRSTY_PARTICLE_PACKET,
            RegisterPackets::createBloodthirstyParticles
        )

        ClientPlayNetworking.registerGlobalReceiver(
            HexNetworkingConstants.LINGER_PARTICLE_PACKET,
            RegisterPackets::createLingerParticles
        )
    }

    fun registerServerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(
            HexNetworkingConstants.PREDICATE_GETTER_PACKET,
            RegisterPackets::updatePlayerPullPredicate
        )
    }

    private fun updatePlayerPullPredicate(server: MinecraftServer, player: ServerPlayerEntity, handler: ServerPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender) {
        (player as PredicateInterface).setPredicate(buf.readFloat())
    }

    private fun syncAltarScreenData(client: MinecraftClient, handler: ClientPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender) {
        val altarScreen = MinecraftClient.getInstance().currentScreen as? AccursedAltarScreen ?: return

        val listLength = buf.readInt()
        val availableHexList: MutableList<String> = mutableListOf()
        for (i in 0..<listLength) {
            availableHexList.add(i, buf.readString())
        }
        val currentHex: String = buf.readString()

        altarScreen.updateScreenHexData(currentHex, availableHexList)
    }

    private fun syncActiveScreenData(client: MinecraftClient, handler: ClientPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender) {
        val altarScreen = MinecraftClient.getInstance().currentScreen as? AccursedAltarScreen ?: return
        val isAltarActive = buf.readInt()

        altarScreen.updateScreenActiveData(isAltarActive)
    }

    private fun createBloodthirstyParticles(client: MinecraftClient, handler: ClientPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender) {
        val particleAmount = buf.readInt()
        val targetPos = Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())

        for (i in 0..particleAmount) {
            client.particleManager.addParticle(
                DustParticleEffect(Vec3d.unpackRgb(10027008).toVector3f(), 1.5f),
                targetPos.x + randVec(), targetPos.y + 0.5 + randVec(), targetPos.z + randVec(),
                0.0, 0.5,
                0.0
            )
        }
    }

    private fun randVec(): Double {
        return HexRandom.nextDouble(-0.5, 0.5) * if (HexRandom.nextBoolean()) -1 else 1
    }

    private fun createLingerParticles(client: MinecraftClient, handler: ClientPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender) {
        val pos = Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())

        client.particleManager.addParticle(
            DustParticleEffect.DEFAULT,
            pos.x, pos.y, pos.z, 0.0, 0.5, 0.0
        )
    }
}