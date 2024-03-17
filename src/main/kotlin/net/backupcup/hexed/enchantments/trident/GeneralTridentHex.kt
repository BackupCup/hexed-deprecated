package net.backupcup.hexed.enchantments.trident

import net.backupcup.hexed.enchantments.AbstractHex
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.util.Identifier

class GeneralTridentHex(weight: Rarity?, target: EnchantmentTarget?, slotTypes: Array<out EquipmentSlot>?,
                        texturepath: Identifier
) : AbstractHex(weight, target,
    slotTypes, texturepath
)