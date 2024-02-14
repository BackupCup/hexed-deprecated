package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RegisterTags {
    val HEX_ENCHANTMENTS: TagKey<Enchantment> = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier(Hexed.MOD_ID, "hex_enchantments"))
    val PERSISTENT_DEBUFFS: TagKey<StatusEffect> = TagKey.of(RegistryKeys.STATUS_EFFECT, Identifier(Hexed.MOD_ID, "persistent_debuffs"))
    val CALAMITOUS_ARMOR: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier(Hexed.MOD_ID, "calamitous_armor"))

    fun registerTags() {
        HEX_ENCHANTMENTS
        PERSISTENT_DEBUFFS
        CALAMITOUS_ARMOR
    }
}