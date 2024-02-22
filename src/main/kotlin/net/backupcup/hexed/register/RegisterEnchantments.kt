package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.enchantments.*
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RegisterEnchantments {
    //WEAPON
    val AFLAME_HEX = AflameHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/aflame.png"))
    val PERSECUTED_HEX = PersecutedHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/persecuted.png"))
    val EPHEMERAL_HEX = EphemeralHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/ephemeral.png"))
    val VINDICTIVE_HEX = VindictiveHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/vindictive.png"))
    val TRAITOROUS_HEX = TraitorousHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/traitorous.png"))

    //ARMOR
    val DISPLACED_HEX = DisplacedHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR, arrayOf(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/displaced.png"))
    val AQUATIQUE_HEX = AquatiqueHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR, arrayOf(EquipmentSlot.FEET),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/aquatique.png"))
    val BLOODTHIRSTY_HEX = BloodthirstyHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR, arrayOf(EquipmentSlot.CHEST),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/bloodthirsty.png"))

    //BOW
    //TODO

    //CROSSBOW
    //TODO

    //TRIDENT
    //TODO

    //DIGGER
    //TODO

    fun registerHexes() {
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "aflame"), AFLAME_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "persecuted"), PERSECUTED_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "ephemeral"), EPHEMERAL_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "vindictive"), VINDICTIVE_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "traitorous"), TRAITOROUS_HEX)

        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "displaced"), DISPLACED_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "aquatique"), AQUATIQUE_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "bloodthirsty"), BLOODTHIRSTY_HEX)

        /*
        # - dummy hex, no translations, no logic
        ??? - still to get the idea

        feet:
            # Aquatique - increases swim speed, but increases fall damage
            ??? - mobility feet
        legs:
            Prowess - Each hit has a 50% to be dodged. All damage received is multiplied by 1.5
            ??? - mobility legs
        chest:
            # Bloodthirsty - Increases max hp by 4 hearts. All regular healing effects are disabled. Killing enemies now restores a portion of health.
            Disfigurement - Every living entity in a certain area (including the wearer) is constantly Hungered and Weakened.
        head:
            Metamorphosis - Any healing that exceeds max health is turned into saturation. Natural regeneration is turned off.
            Divine - Any chest that didn't generate loot already, will contain more loot. Each opening of a chest will result in 5 seconds of Darkness

        crossbow:
            FIREWORK RAPID LAUNCHER
        */
    }
}