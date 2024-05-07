package net.backupcup.hexed.packets

import net.backupcup.hexed.Hexed
import net.minecraft.util.Identifier

object HexNetworkingConstants {
    val BLOODTHIRSTY_PARTICLE_PACKET: Identifier = Identifier(Hexed.MOD_ID, "bloodthirsty_particle_packet")
    val LINGER_PARTICLE_PACKET: Identifier = Identifier(Hexed.MOD_ID, "linger_particle_packet")

    val PROVISION_UPDATE_PACKET: Identifier = Identifier(Hexed.MOD_ID, "provision_update_packet")
    val OVERCLOCK_UPDATE_PACKET: Identifier = Identifier(Hexed.MOD_ID, "overclock_update_packet")
    val AGGRAVATE_UPDATE_PACKET: Identifier = Identifier(Hexed.MOD_ID, "aggravate_update_packet")
    val PHASED_UPDATE_PACKET: Identifier = Identifier(Hexed.MOD_ID, "phased_update_packet")

    val BASHER_PARTICLE_PACKET: Identifier = Identifier(Hexed.MOD_ID, "basher_particle_packet")

    val PREDICATE_GETTER_PACKET: Identifier = Identifier(Hexed.MOD_ID, "predicate_get_packet")
}