package net.backupcup.hexed.enchantments.armor

import com.google.common.collect.Multimap
import net.backupcup.hexed.enchantments.AbstractHex
import net.backupcup.hexed.util.AttributeProviding
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.util.Identifier

class DisplacedHex(
    weight: Rarity?,
    target: EnchantmentTarget?,
    slotTypes: Array<out EquipmentSlot>?,
    texturepath: Identifier
) : AbstractHex(
    weight,
    target,
    slotTypes,
    texturepath
), AttributeProviding {
    override fun modifyAttributeMap(
        map: Multimap<EntityAttribute, EntityAttributeModifier>,
        slot: EquipmentSlot,
        level: Int
    ) {
        val armorPointsAttribute = map.entries().filter { it.value.name == "Armor modifier" }[0]
        val armorPoints = armorPointsAttribute.value.value
        val armorToughnessAttribute = map.entries().filter { it.value.name == "Armor toughness" }[0]
        val armorToughness = armorToughnessAttribute.value.value

        map.entries().remove(armorPointsAttribute)
        map.put(
            EntityAttributes.GENERIC_ARMOR,
            EntityAttributeModifier(armorPointsAttribute.value.id, armorPointsAttribute.value.name, armorToughness, EntityAttributeModifier.Operation.ADDITION)
        )

        map.entries().remove(armorToughnessAttribute)
        map.put(
            EntityAttributes.GENERIC_ARMOR_TOUGHNESS,
            EntityAttributeModifier(armorToughnessAttribute.value.id, armorToughnessAttribute.value.name, armorPoints, EntityAttributeModifier.Operation.ADDITION)
        )
    }
}