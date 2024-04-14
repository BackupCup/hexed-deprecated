package net.backupcup.hexed.enchantments

import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.BowItem
import net.minecraft.item.CrossbowItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

class GeneralRangedHex(weight: Rarity?, target: EnchantmentTarget?, slotTypes: Array<out EquipmentSlot>?,
                       texturepath: Identifier
) : AbstractHex(weight, target,
    slotTypes, texturepath
) {
    override fun isAcceptableItem(stack: ItemStack): Boolean {
        return stack.item is BowItem || stack.item is CrossbowItem
    }
}