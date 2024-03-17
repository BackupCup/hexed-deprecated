package net.backupcup.hexed.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.util.ClickType
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