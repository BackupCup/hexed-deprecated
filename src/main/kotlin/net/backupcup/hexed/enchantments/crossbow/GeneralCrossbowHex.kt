package net.backupcup.hexed.enchantments.crossbow

import net.backupcup.hexed.enchantments.AbstractHex
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.util.Identifier

class GeneralCrossbowHex(weight: Rarity?, target: EnchantmentTarget?, slotTypes: Array<out EquipmentSlot>?,
                         texturepath: Identifier
) : AbstractHex(weight, target,
    slotTypes, texturepath
)