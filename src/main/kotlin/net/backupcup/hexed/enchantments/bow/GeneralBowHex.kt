package net.backupcup.hexed.enchantments.bow

import net.backupcup.hexed.enchantments.AbstractHex
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.BowItem
import net.minecraft.item.CrossbowItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

class GeneralBowHex(weight: Rarity?, target: EnchantmentTarget?, slotTypes: Array<out EquipmentSlot>?,
                    texturepath: Identifier
) : AbstractHex(weight, target,
    slotTypes, texturepath
)