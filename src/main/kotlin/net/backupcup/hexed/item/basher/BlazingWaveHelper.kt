package net.backupcup.hexed.item.basher

import io.netty.buffer.Unpooled
import net.backupcup.hexed.packets.HexNetworkingConstants
import net.backupcup.hexed.register.RegisterDamageTypes
import net.backupcup.hexed.register.RegisterSounds
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.LivingEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.util.math.Box

object BlazingWaveHelper {
    fun createExplosion(target: LivingEntity, radius: Double): Unit {
        val explosionBox: Box = Box(target.blockPos).expand(radius)
        val entityList: List<LivingEntity> = target.entityWorld.getNonSpectatingEntities(LivingEntity::class.java, explosionBox)

        //get all players that need that packet
        val playerList: MutableCollection<ServerPlayerEntity> = PlayerLookup.tracking(target)

        //create the packet and write the center pos
        var buf = PacketByteBuf(Unpooled.buffer())
        buf.writeDouble(radius)
        buf.writeDouble(target.pos.x); buf.writeDouble(target.pos.y); buf.writeDouble(target.pos.z)

        var entityCount = 0

        //damage the enemies in the radius and also get the amount of enemies
        entityList.forEach { entity ->
            if (entity.distanceTo(target) <= radius) {
                entity.damage(RegisterDamageTypes.of(entity.entityWorld, RegisterDamageTypes.BLAZING_WAVE), radius.toFloat() - entity.distanceTo(target))
                entityCount += 1
            }
        }

        //pack the amount of enemies into the packet
        buf.writeInt(entityCount)

        //get the pos of all enemies in the range and pack them
        entityList.forEach { entity ->
            if (entity.distanceTo(target) <= radius) {
                buf.writeDouble(entity.pos.x); buf.writeDouble(entity.pos.y); buf.writeDouble(entity.pos.z)
            }
        }

        //send the final packet to every player that needs it
        playerList.forEach { serverPlayerEntity ->
            ServerPlayNetworking.send(serverPlayerEntity, HexNetworkingConstants.BASHER_PARTICLE_PACKET, buf)
        }

        //make a sound
        target.entityWorld.playSound(
            null, target.blockPos,
            RegisterSounds.ACCURSED_ALTAR_TAINT, SoundCategory.BLOCKS,
            0.25f, 2f)
    }
}