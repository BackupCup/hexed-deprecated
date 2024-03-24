package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.enchantments.armor.*
import net.backupcup.hexed.enchantments.crossbow.GeneralCrossbowHex
import net.backupcup.hexed.enchantments.digger.GeneralDiggerHex
import net.backupcup.hexed.enchantments.trident.GeneralTridentHex
import net.backupcup.hexed.enchantments.weapon.*
import net.backupcup.hexed.util.HexHelper
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
        HexHelper.runeTexture("aflame"))
    val PERSECUTED_HEX = PersecutedHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("persecuted"))
    val EPHEMERAL_HEX = EphemeralHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("ephemeral"))
    val VINDICTIVE_HEX = VindictiveHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("vindictive"))
    val TRAITOROUS_HEX = TraitorousHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("traitorous"))

    //ARMOR
    val DISPLACED_HEX = DisplacedHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR, arrayOf(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET),
        HexHelper.runeTexture("displaced"))
    val AVERTING_HEX = GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR, arrayOf(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET),
        HexHelper.runeTexture("averting"))
    val AQUATIQUE_HEX = GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_FEET, arrayOf(EquipmentSlot.FEET),
        HexHelper.runeTexture("aquatique"))
    val DYNAMIQUE_HEX = GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_FEET, arrayOf(EquipmentSlot.FEET),
        HexHelper.runeTexture("dynamique"))
    val IRONCLAD_HEX = GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_LEGS, arrayOf(EquipmentSlot.LEGS),
        HexHelper.runeTexture("ironclad"))
    val FRANTIC_HEX = GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_LEGS, arrayOf(EquipmentSlot.LEGS),
        HexHelper.runeTexture("frantic"))
    val BLOODTHIRSTY_HEX = BloodthirstyHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_CHEST, arrayOf(EquipmentSlot.CHEST),
        HexHelper.runeTexture("bloodthirsty"))
    val DISFIGUREMENT_HEX = DisfigurementHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_CHEST, arrayOf(EquipmentSlot.CHEST),
        HexHelper.runeTexture("disfigurement"))
    val METAMORPHOSIS_HEX = GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD),
        HexHelper.runeTexture("metamorphosis"))
    val DIVINE_HEX = GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD),
        HexHelper.runeTexture("divine"))

    //BOW
    val TRANSMUTATION_HEX = GeneralCrossbowHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.BOW, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("transmutation")) //TODO

    //CROSSBOW
    val CELEBRATION_HEX = GeneralCrossbowHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.CROSSBOW, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("celebration"))
    val PROVISION_HEX = GeneralCrossbowHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.CROSSBOW, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("provision"))

    //TRIDENT
    val FLARING_HEX = GeneralTridentHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.TRIDENT, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("flaring"))
    val LINGER_HEX = GeneralTridentHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.TRIDENT, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("linger"))
    val SEIZE_HEX = GeneralTridentHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.TRIDENT, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("seize"))
    val SEPULTURE_HEX = GeneralTridentHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.TRIDENT, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("sepulture"))

    //DIGGER
    val RUINOUS_HEX = GeneralDiggerHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.DIGGER, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("ruinous"))
    val AMPLIFY_HEX = GeneralDiggerHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.DIGGER, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("amplify"))
    val OVERBURDEN_HEX = GeneralDiggerHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.DIGGER, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("overburden"))
    val FAMISHMENT_HEX = GeneralDiggerHex(Enchantment.Rarity.VERY_RARE,
        EnchantmentTarget.DIGGER, arrayOf(EquipmentSlot.MAINHAND),
        HexHelper.runeTexture("famishment"))

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
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "frantic"), FRANTIC_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "bloodthirsty"), BLOODTHIRSTY_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "disfigurement"), DISFIGUREMENT_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "metamorphosis"), METAMORPHOSIS_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "divine"), DIVINE_HEX)

        //TODO: Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "transmutation"), TRANSMUTATION_HEX)

        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "celebration"), CELEBRATION_HEX)
        //TODO: Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "provision"), PROVISION_HEX)

        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "flaring"), FLARING_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "linger"), LINGER_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "seize"), SEIZE_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "sepulture"), SEPULTURE_HEX)

        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "ruinous"), RUINOUS_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "amplify"), AMPLIFY_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "overburden"), OVERBURDEN_HEX)
        Registry.register(Registries.ENCHANTMENT, Identifier(Hexed.MOD_ID, "famishment"), FAMISHMENT_HEX)

        /*
        Status Descriptions:
        - Logic: No logic implemented
        - Transl: Translation feature missing
        - Name: Name not assigned
        - Text: Texture missing
        - Idea: Idea still under consideration

        Hex Ideas:
        - Sword:
          basically "Blinding Assault" of the ROR2 Merc, but instead of "cooldown" the user just cant attack for a few seconds

        - Bow:
          : Arrows shot will have random effects on them
          TODO 3 more here

        - Crossbow:
          : Banish - Creates a miniature black hole at the position of the enemy that the projectile hits.
          : Provision - Active Reload mechanic (Rad Gun from Gungeon / Railgunner from ROR2)
          TODO 2 more here
        */
    }
}