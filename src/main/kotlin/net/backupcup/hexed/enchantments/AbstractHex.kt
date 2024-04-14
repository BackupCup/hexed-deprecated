package net.backupcup.hexed.enchantments

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.util.Identifier

abstract class AbstractHex(
    weight: Rarity?,
    target: EnchantmentTarget?,
    slotTypes: Array<out EquipmentSlot>?,
    val texturepath: Identifier
) : Enchantment(
    weight,
    target,
    slotTypes
) {
    override fun canAccept(other: Enchantment?): Boolean {
        if (isHex(other)) {
            return false
        }
        return super.canAccept(other)
    }

    override fun isTreasure(): Boolean {
        return true
    }

    override fun isCursed(): Boolean {
        return true
    }

    override fun isAvailableForEnchantedBookOffer(): Boolean {
        return false
    }

    override fun isAvailableForRandomSelection(): Boolean {
        return false
    }

    override fun getMaxLevel(): Int {
        return 1
    }

    private fun isHex(enchantment: Enchantment?): Boolean {
        return (enchantment is AbstractHex)
    }
}