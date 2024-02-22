package net.backupcup.hexed.util

import com.google.common.collect.Multimap
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier

interface AttributeProviding {
    fun modifyAttributeMap(map: Multimap<EntityAttribute, EntityAttributeModifier>, slot: EquipmentSlot, level: Int)
}