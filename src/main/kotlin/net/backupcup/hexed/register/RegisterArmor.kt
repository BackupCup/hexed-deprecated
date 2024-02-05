package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.armor.CalamitousArmorItem
import net.backupcup.hexed.armor.HexedArmorMaterial
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RegisterArmor {
    val CALAMITOUS_HELMET: Item = CalamitousArmorItem(HexedArmorMaterial.calamitous, ArmorItem.Type.HELMET, FabricItemSettings())
    val CALAMITOUS_CHESTPLATE: Item = CalamitousArmorItem(HexedArmorMaterial.calamitous, ArmorItem.Type.CHESTPLATE, FabricItemSettings())
    val CALAMITOUS_LEGGINGS: Item = CalamitousArmorItem(HexedArmorMaterial.calamitous, ArmorItem.Type.LEGGINGS, FabricItemSettings())
    val CALAMITOUS_BOOTS: Item = CalamitousArmorItem(HexedArmorMaterial.calamitous, ArmorItem.Type.BOOTS, FabricItemSettings())

    fun registerArmor() {
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "calamitous_hood"), CALAMITOUS_HELMET)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "calamitous_robes"), CALAMITOUS_CHESTPLATE)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "calamitous_cassock"), CALAMITOUS_LEGGINGS)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "calamitous_boots"), CALAMITOUS_BOOTS)
    }
}