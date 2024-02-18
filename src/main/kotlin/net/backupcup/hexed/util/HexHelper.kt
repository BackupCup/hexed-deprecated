package net.backupcup.hexed.util

import net.backupcup.hexed.altar.AccursedAltarScreenHandler
import net.backupcup.hexed.enchantments.AbstractHex
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerListener
import net.minecraft.text.Text

object HexHelper {
    fun generatorListener(context: ScreenHandlerContext, player: PlayerEntity): ScreenHandlerListener {
        return object : ScreenHandlerListener {
            override fun onPropertyUpdate(handler: ScreenHandler, property: Int, value: Int) {}
            override fun onSlotUpdate(handler: ScreenHandler, slotId: Int, stack: ItemStack) {
                val item = handler.getSlot(1).stack
                if (!item.isEmpty) {
                    context.run { world, _ ->
                        val currentHandler = world.server?.playerManager?.getPlayer(player.uuid)?.currentScreenHandler
                        (currentHandler as AccursedAltarScreenHandler).setAvailableHexList(getAvailableHexList(item))
                        currentHandler.setCurrentHex(getAvailableHexList(item)[0])
                    }
                    //this.availableHexList = getAvailableHexList(item)
                    //this.currentHex = availableHexList[0]
                }
            }
        }
    }

    fun getEnchantments(itemStack: ItemStack): List<Enchantment> {
        return EnchantmentHelper.get(itemStack).map { (enchantment, _) -> enchantment}
    }

    fun getHexList(itemStack: ItemStack): List<AbstractHex> {
        return Registries.ENCHANTMENT.filterIsInstance<AbstractHex>().filter { hex ->
            hex.isAcceptableItem(itemStack)}
    }

    fun getAvailableHexList(itemStack: ItemStack): List<AbstractHex> {
        val itemEnchantmentMap = getEnchantments(itemStack)
        return getHexList(itemStack).filterNot { itemEnchantmentMap.contains(it) }
    }
}