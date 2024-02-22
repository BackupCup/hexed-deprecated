package net.backupcup.hexed.util

import net.backupcup.hexed.enchantments.AbstractHex
import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

object HexData {

    fun getHexByName(name: String): Enchantment? {
        for (enchantment: Enchantment in Registries.ENCHANTMENT) {
            if (enchantment.translationKey == name)
                return enchantment
        }
        return null
    }

    fun getHexDescription(name: String): String {
        return name.plus(".desc")
    }

    fun getHexTexture(name: String): Identifier? {
        val enchantment = getHexByName(name)
        if (enchantment != null) {
            if (enchantment is AbstractHex) {
                return enchantment.texturepath
            }
        }
        return null
    }
}
