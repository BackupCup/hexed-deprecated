package net.backupcup.hexed.item.carnage

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.util.CustomHandTexture
import net.backupcup.hexed.util.TaintedItem
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterial
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

class BalefulCarnageItem(
    toolMaterial: ToolMaterial?,
    attackDamage: Int,
    attackSpeed: Float,
    override val itemId: String,
    settings: Settings?,
): SwordItem(
    toolMaterial, attackDamage, attackSpeed, settings
), CustomHandTexture, TaintedItem<String> {
    override fun getHandTexture(): ModelIdentifier {
        return ModelIdentifier(Hexed.MOD_ID, "${itemId}_hand", "inventory")
    }

    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        tooltip?.add(Text.translatable("tooltip.hexed.${itemId}").formatted(Formatting.GRAY))
        super.appendTooltip(stack, world, tooltip, context)
    }


}