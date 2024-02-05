package net.backupcup.hexed.armor

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

class CalamitousArmorItem(
    material: ArmorMaterial?,
    type: Type?,
    settings: Settings?
) : ArmorItem(material, type, settings) {
    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        tooltip?.add(Text.translatable("tooltip.hexed.calamitous_armor.line_1")
            .formatted(Formatting.DARK_RED).formatted(Formatting.ITALIC).formatted(Formatting.BOLD))
        tooltip?.add(Text.translatable("tooltip.hexed.calamitous_armor.line_2")
            .formatted(Formatting.DARK_RED).formatted(Formatting.ITALIC).formatted(Formatting.BOLD))
        tooltip?.add(Text.translatable("tooltip.hexed.calamitous_armor.line_3")
            .formatted(Formatting.GRAY))
        tooltip?.add(Text.translatable("tooltip.hexed.calamitous_armor.line_4")
            .formatted(Formatting.GRAY))
    }
}