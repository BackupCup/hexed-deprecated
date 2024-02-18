package net.backupcup.hexed.packets

import net.backupcup.hexed.Hexed
import net.minecraft.util.Identifier

object AltarNetworkingConstants {
    val AVAILABLE_HEX_PACKET: Identifier = Identifier(Hexed.MOD_ID, "altar_packet")
    val ACTIVE_ALTAR_PACKET: Identifier = Identifier(Hexed.MOD_ID, "active_altar_packet")
}