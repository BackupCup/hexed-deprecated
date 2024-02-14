package net.backupcup.hexed.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.world.World

class TooltipItem(settings: Settings?, val itemTooltip: List<Text>) : Item(settings) {
    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext?
    ) {
        itemTooltip.forEach { text: Text ->
            tooltip.add(text)
        }
        super.appendTooltip(stack, world, tooltip, context)
    }
}