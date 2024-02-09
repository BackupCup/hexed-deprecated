package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.enchantments.AflameHex
import net.backupcup.hexed.enchantments.EphemeralHex
import net.backupcup.hexed.enchantments.PersecutedHex
import net.backupcup.hexed.enchantments.VindictiveHex
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RegisterEnchantments {
    val AFLAME_HEX = AflameHex(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND))
    val PERSECUTED_HEX = PersecutedHex(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND))
    val EPHEMERAL_HEX = EphemeralHex(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND))
    val VINDICTIVE_HEX = VindictiveHex(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND))

    fun registerHexes() {
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "aflame"), AFLAME_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "persecuted"), PERSECUTED_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "ephemeral"), EPHEMERAL_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "vindictive"), VINDICTIVE_HEX)
    }
}