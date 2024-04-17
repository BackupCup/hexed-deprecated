package net.backupcup.hexed.listener

import io.netty.buffer.Unpooled
import net.backupcup.hexed.packets.HexNetworkingConstants.AGGRAVATE_UPDATE_PACKET
import net.backupcup.hexed.packets.HexNetworkingConstants.OVERCLOCK_UPDATE_PACKET
import net.backupcup.hexed.packets.HexNetworkingConstants.PHASED_UPDATE_PACKET
import net.backupcup.hexed.packets.HexNetworkingConstants.PROVISION_UPDATE_PACKET
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity

object ServerListener {
    fun registerServerListeners() {
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(ServerLifecycleEvents.SyncDataPackContents { player: ServerPlayerEntity?, joined: Boolean ->
            if (joined) {
                val overclockBuf = PacketByteBuf(Unpooled.buffer())
                overclockBuf.writeInt(0)
                ServerPlayNetworking.send(player, OVERCLOCK_UPDATE_PACKET, overclockBuf)

                val provisionBuf = PacketByteBuf(Unpooled.buffer())
                provisionBuf.writeBoolean(false)
                provisionBuf.writeInt(0)
                ServerPlayNetworking.send(player, PROVISION_UPDATE_PACKET, provisionBuf)

                val aggravateBuf = PacketByteBuf(Unpooled.buffer())
                aggravateBuf.writeInt(0)
                ServerPlayNetworking.send(player, AGGRAVATE_UPDATE_PACKET, aggravateBuf)

                val phasedBuf = PacketByteBuf(Unpooled.buffer())
                phasedBuf.writeInt(0)
                ServerPlayNetworking.send(player, PHASED_UPDATE_PACKET, phasedBuf)
            }
        })
    }
}