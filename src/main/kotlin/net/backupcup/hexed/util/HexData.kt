package net.backupcup.hexed.util

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.register.RegisterEnchantments
import net.minecraft.enchantment.Enchantment
import net.minecraft.util.Identifier

object HexData {
    data class HexInfo(val texturepath: Identifier, val name: String, val description: String, val hexInstance: Enchantment)

    fun getHexInfo(name: String): HexInfo? {
        return hexList.find { it.name == name }
    }

    private val AFLAME: HexInfo = HexInfo(
        Identifier(Hexed.MOD_ID, "textures/gui/runes/aflame.png"),
        "enchantment.hexed.aflame",
        "enchantment.hexed.aflame.desc",
        RegisterEnchantments.AFLAME_HEX)

    private val EPHEMERAL: HexInfo = HexInfo(
        Identifier(Hexed.MOD_ID, "textures/gui/runes/ephemeral.png"),
        "enchantment.hexed.ephemeral",
        "enchantment.hexed.ephemeral.desc",
        RegisterEnchantments.EPHEMERAL_HEX)

    private val PERSECUTED: HexInfo = HexInfo(
        Identifier(Hexed.MOD_ID, "textures/gui/runes/persecuted.png"),
        "enchantment.hexed.persecuted",
        "enchantment.hexed.persecuted.desc",
        RegisterEnchantments.PERSECUTED_HEX)

    private val VINDICTIVE: HexInfo = HexInfo(
        Identifier(Hexed.MOD_ID, "textures/gui/runes/vindictive.png"),
        "enchantment.hexed.vindictive",
        "enchantment.hexed.vindictive.desc",
        RegisterEnchantments.VINDICTIVE_HEX)

    private val hexList: List<HexInfo> = listOf(
        AFLAME, EPHEMERAL, PERSECUTED, VINDICTIVE
    )
}
