package net.backupcup.hexed.util

import net.backupcup.hexed.altar.AccursedAltarScreenHandler
import net.backupcup.hexed.enchantments.AbstractHex
import net.backupcup.hexed.register.RegisterTags.CALAMITOUS_ARMOR
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerListener

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

    fun hasEnchantmentInSlot(stack: ItemStack, key: Enchantment): Boolean {
        return EnchantmentHelper.get(stack).containsKey(key)
    }

    fun hasFullRobes(armorStack: Iterable<ItemStack>): Boolean {
        for (piece in armorStack) {
            if (!piece.isIn(CALAMITOUS_ARMOR)) return false
        }
        return true
    }

    fun hasFullRobes(entity: LivingEntity): Boolean {
        entity.armorItems.forEach { piece ->
            if (!piece.isIn(CALAMITOUS_ARMOR)) return false
        }
        return true
    }

    fun entityMultiplyingEffect(user: LivingEntity, effect: StatusEffect, duration: Int, decayLength: Int) {
        if (user.hasStatusEffect(effect)) {
            val effectAmplifier = user.getStatusEffect(effect)?.amplifier?.plus(1)

            for (i in 0..effectAmplifier!!) {
                user.addStatusEffect(
                    StatusEffectInstance(
                        effect,
                        duration + (effectAmplifier - i) * decayLength, i,
                        true, false, true
                    )
                )
            }
        } else {
            user.addStatusEffect(
                StatusEffectInstance(
                    effect,
                    duration, 0,
                    true, false, true
                )
            )
        }
    }
}