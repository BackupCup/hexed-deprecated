package net.backupcup.hexed.enchantments.armor

import net.backupcup.hexed.enchantments.AbstractHex
import net.backupcup.hexed.register.RegisterStatusEffects
import net.backupcup.hexed.util.HexHelper
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier

class FranticHex(
    weight: Rarity?,
    target: EnchantmentTarget?,
    slotTypes: Array<out EquipmentSlot>?,
    texturepath: Identifier
): AbstractHex(
    weight,
    target,
    slotTypes,
    texturepath
)