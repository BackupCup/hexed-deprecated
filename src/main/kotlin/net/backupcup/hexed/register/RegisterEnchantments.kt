package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.enchantments.AbstractHex
import net.backupcup.hexed.enchantments.armor.BloodthirstyHex
import net.backupcup.hexed.enchantments.armor.DisfigurementHex
import net.backupcup.hexed.enchantments.armor.DisplacedHex
import net.backupcup.hexed.enchantments.armor.GeneralArmorHex
import net.backupcup.hexed.enchantments.bow.GeneralBowHex
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
    private val hexList: MutableList<AbstractHex> = mutableListOf()
    private fun register(hex: AbstractHex): AbstractHex { hexList.add(hex); return hex }

    //WEAPON
    val AFLAME_HEX: AbstractHex = register(
        AflameHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("aflame")))
    val PERSECUTED_HEX = register(
        PersecutedHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("persecuted")))
    val EPHEMERAL_HEX = register(
        EphemeralHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("ephemeral")))
    val VINDICTIVE_HEX = register(
        VindictiveHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("vindictive")))
    val TRAITOROUS_HEX = register(
        TraitorousHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("traitorous")))

    //ARMOR
    val DISPLACED_HEX = register(
        DisplacedHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.ARMOR, arrayOf(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET),
            HexHelper.runeTexture("displaced")))
    val AVERTING_HEX = register(
        GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.ARMOR, arrayOf(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET),
            HexHelper.runeTexture("averting")))
    val AQUATIQUE_HEX = register(
        GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.ARMOR_FEET, arrayOf(EquipmentSlot.FEET),
            HexHelper.runeTexture("aquatique")))
    val DYNAMIQUE_HEX = register(
        GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.ARMOR_FEET, arrayOf(EquipmentSlot.FEET),
            HexHelper.runeTexture("dynamique")))
    val IRONCLAD_HEX = register(
        GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.ARMOR_LEGS, arrayOf(EquipmentSlot.LEGS),
            HexHelper.runeTexture("ironclad")))
    val FRANTIC_HEX = register(
        GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.ARMOR_LEGS, arrayOf(EquipmentSlot.LEGS),
            HexHelper.runeTexture("frantic")))
    val BLOODTHIRSTY_HEX = register(
        BloodthirstyHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.ARMOR_CHEST, arrayOf(EquipmentSlot.CHEST),
            HexHelper.runeTexture("bloodthirsty")))
    val DISFIGUREMENT_HEX = register(
        DisfigurementHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.ARMOR_CHEST, arrayOf(EquipmentSlot.CHEST),
            HexHelper.runeTexture("disfigurement")))
    val METAMORPHOSIS_HEX = register(
        GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD),
            HexHelper.runeTexture("metamorphosis")))
    val DIVINE_HEX = register(
        GeneralArmorHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.ARMOR_HEAD, arrayOf(EquipmentSlot.HEAD),
            HexHelper.runeTexture("divine")))

    //BOW
    val AGGRAVATE_HEX = register(
        GeneralBowHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.BOW, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("aggravate")))
    val VOLATILITY_HEX = register(
        GeneralBowHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.BOW, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("volatility")))
    val PHASED_HEX = register(
        GeneralBowHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.BOW, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("phased")))

    //CROSSBOW
    val CELEBRATION_HEX = register(
        GeneralCrossbowHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.CROSSBOW, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("celebration")))
    val PROVISION_HEX = register(
        GeneralCrossbowHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.CROSSBOW, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("provision")))
    val OVERCLOCK_HEX = register(
        GeneralCrossbowHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.CROSSBOW, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("overclock")))

    //TRIDENT
    val FLARING_HEX = register(
        GeneralTridentHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.TRIDENT, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("flaring")))
    val LINGER_HEX = register(
        GeneralTridentHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.TRIDENT, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("linger")))
    val SEIZE_HEX = register(
        GeneralTridentHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.TRIDENT, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("seize")))
    val SEPULTURE_HEX = register(
        GeneralTridentHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.TRIDENT, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("sepulture")))

    //DIGGER
    val RUINOUS_HEX = register(
        GeneralDiggerHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.DIGGER, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("ruinous")))
    val AMPLIFY_HEX = register(
        GeneralDiggerHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.DIGGER, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("amplify")))
    val OVERBURDEN_HEX = register(
        GeneralDiggerHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.DIGGER, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("overburden")))
    val FAMISHMENT_HEX = register(
        GeneralDiggerHex(Enchantment.Rarity.VERY_RARE,
            EnchantmentTarget.DIGGER, arrayOf(EquipmentSlot.MAINHAND),
            HexHelper.runeTexture("famishment")))

    fun registerHexes() {
        hexList.forEach { hex ->
            Registry.register(Registries.ENCHANTMENT,
                Identifier(Hexed.MOD_ID, hex.texturepath.path.drop("textures/gui/runes/".length).dropLast(".png".length)), hex)
        }

        /*
        Hex Ideas:
        - Bow:
          HITSCAN LOGIC + LANG: Phased - Charge a piercing arrow with increased damage. Deals more damage every time it passes through an enemy. Locks all bows into cooldown once shot
          EVERYTHING: ??? - The bow now fires a slow, heat seeking Spirits. They get all the benefits of other enchantments that are present on the bow. TODO: Leave for now, may come in an update

        - Crossbow:
          : Commencement - Diablo Strike TODO: Leave for now, may come in an update

          FIX:
          isAltarActive should be initialized before get TODO: FIND WHERE THE ISSUE STEMS FROM IG? -- Couldn't replicate the crash, may be because it was linked to altar's state on block entity not udpating
        */
    }
}