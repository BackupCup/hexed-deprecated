package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.enchantments.armor.*
import net.backupcup.hexed.enchantments.weapon.*
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
    val AVERTING_HEX = AvertingHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR, arrayOf(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/averting.png"))
    val AQUATIQUE_HEX = AquatiqueHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_FEET, arrayOf(EquipmentSlot.FEET),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/aquatique.png"))
    val DYNAMIQUE_HEX = DynamiqueHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_FEET, arrayOf(EquipmentSlot.FEET),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/dynamique.png"))
    val IRONCLAD_HEX = IroncladHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_LEGS, arrayOf(EquipmentSlot.LEGS),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/ironclad.png"))
    val FRANTIC_HEX = FranticHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_LEGS, arrayOf(EquipmentSlot.LEGS),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/frantic.png"))
    val BLOODTHIRSTY_HEX = BloodthirstyHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_CHEST, arrayOf(EquipmentSlot.CHEST),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/bloodthirsty.png"))
    val DISFIGUREMENT_HEX = DisfigurementHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_CHEST, arrayOf(EquipmentSlot.CHEST),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/disfigurement.png"))
    val METAMORPHOSIS_HEX = MetamorphosisHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/metamorphosis.png"))
    val DIVINE_HEX = DivineHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD),
        Identifier(Hexed.MOD_ID, "textures/gui/runes/divine.png"))

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
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "averting"), AVERTING_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "aquatique"), AQUATIQUE_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "dynamique"), DYNAMIQUE_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "ironclad"), IRONCLAD_HEX)
        //Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "frantic"), FRANTIC_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "bloodthirsty"), BLOODTHIRSTY_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "disfigurement"), DISFIGUREMENT_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "metamorphosis"), METAMORPHOSIS_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "divine"), DIVINE_HEX)

        /*
        # - dummy hex, no translations, no logic
        ??? - still to get the idea

        legs: # ??? Frantic - Dodge an attack and travel back 2 blocks (with a cooldown). +10% Chance to miss your attack for every dodge (resets on miss)
        THIS NEEDS AN IDEA REWORK REALLY BADLY

        crossbow:
            FIREWORK RAPID LAUNCHER
        */
    }
}