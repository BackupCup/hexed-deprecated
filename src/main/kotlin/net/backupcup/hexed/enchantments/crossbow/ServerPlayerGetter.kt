package net.backupcup.hexed.enchantments.crossbow

import net.minecraft.server.network.ServerPlayerEntity
import java.util.UUID

interface ServerPlayerGetter {
    fun getServerPlayerEntity(uuid: UUID): ServerPlayerEntity
}