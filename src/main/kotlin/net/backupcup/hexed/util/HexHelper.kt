package net.backupcup.hexed.util

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.altar.AccursedAltarScreenHandler
import net.backupcup.hexed.enchantments.AbstractHex
import net.backupcup.hexed.register.RegisterEnchantments
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
import net.minecraft.util.Identifier

object  HexHelper {
    private val blockedHexList by lazy { generateHexAvailability() }

    private fun generateHexAvailability(): List<AbstractHex> {
        val tempList: MutableList<AbstractHex> = mutableListOf()

        if (Hexed.getConfig()?.aflameHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.AFLAME_HEX) }
        if (Hexed.getConfig()?.persecutedHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.PERSECUTED_HEX) }
        if (Hexed.getConfig()?.ephemeralHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.EPHEMERAL_HEX) }
        if (Hexed.getConfig()?.vindictiveHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.VINDICTIVE_HEX) }
        if (Hexed.getConfig()?.traitorousHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.TRAITOROUS_HEX) }
        if (Hexed.getConfig()?.displacedHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.DISPLACED_HEX) }
        if (Hexed.getConfig()?.avertingHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.AVERTING_HEX) }
        if (Hexed.getConfig()?.aquatiqueHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.AQUATIQUE_HEX) }
        if (Hexed.getConfig()?.dynamiqueHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.DYNAMIQUE_HEX) }
        if (Hexed.getConfig()?.ironcladHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.IRONCLAD_HEX) }
        if (Hexed.getConfig()?.franticHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.FRANTIC_HEX) }
        if (Hexed.getConfig()?.bloodthirstyHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.BLOODTHIRSTY_HEX) }
        if (Hexed.getConfig()?.disfigurementHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.DISFIGUREMENT_HEX) }
        if (Hexed.getConfig()?.metamorphosisHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.METAMORPHOSIS_HEX) }
        if (Hexed.getConfig()?.divineHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.DIVINE_HEX) }
        if (Hexed.getConfig()?.celebrationHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.CELEBRATION_HEX) }
        if (Hexed.getConfig()?.flaringHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.FLARING_HEX) }
        if (Hexed.getConfig()?.lingerHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.LINGER_HEX) }
        if (Hexed.getConfig()?.seizeHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.SEIZE_HEX) }
        if (Hexed.getConfig()?.sepultureHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.SEPULTURE_HEX) }
        if (Hexed.getConfig()?.ruinousHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.RUINOUS_HEX) }
        if (Hexed.getConfig()?.amplifyHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.AMPLIFY_HEX) }
        if (Hexed.getConfig()?.overburdenHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.OVERBURDEN_HEX) }
        if (Hexed.getConfig()?.famishmentHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.FAMISHMENT_HEX) }
        if (Hexed.getConfig()?.aggravateHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.AGGRAVATE_HEX) }
        if (Hexed.getConfig()?.volatilityHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.VOLATILITY_HEX) }
        if (Hexed.getConfig()?.phasedHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.PHASED_HEX) }
        if (Hexed.getConfig()?.overclockHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.OVERCLOCK_HEX) }
        if (Hexed.getConfig()?.provisionHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.PROVISION_HEX) }
        if (Hexed.getConfig()?.resentfulHex?.shouldRegister == false) { tempList.add(RegisterEnchantments.RESENTFUL_HEX) }

        return tempList
    }

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

    private fun getHexList(itemStack: ItemStack): List<AbstractHex> {
        return Registries.ENCHANTMENT.filterIsInstance<AbstractHex>().filter { hex ->
            hex.isAcceptableItem(itemStack) && !blockedHexList.contains(hex)}
    }

    fun getAvailableHexList(itemStack: ItemStack): List<AbstractHex> {
        return getHexList(itemStack).filterNot { getEnchantments(itemStack).contains(it) }
    }

    fun stackHasEnchantment(stack: ItemStack, key: Enchantment): Boolean {
        return EnchantmentHelper.get(stack).containsKey(key)
    }

    fun stackHasEnchantment(stackList: List<ItemStack>, key: Enchantment): Boolean {
        stackList.forEach { stack ->
            if (EnchantmentHelper.get(stack).containsKey(key)) return true
        }
        return false
    }

    fun stackHasEnchantment(stack: ItemStack, keyList: List<Enchantment>): Boolean {
        keyList.forEach { enchantment ->
            if (EnchantmentHelper.get(stack).containsKey(enchantment)) return true
        }
        return false
    }

    fun stackHasEnchantment(stackList: List<ItemStack>, keyList: List<Enchantment>): Boolean {
        stackList.forEach { stack ->
            keyList.forEach { key -> if (EnchantmentHelper.get(stack).containsKey(key)) return true }
        }
        return false
    }

    fun hasFullRobes(armorStack: Iterable<ItemStack>): Boolean {
        if (Hexed.getConfig()?.shouldArmorApply() == true) {
            for (piece in armorStack) {
                if (!piece.isIn(CALAMITOUS_ARMOR)) return false
            }
            return true
        }
        return false
    }

    fun hasFullRobes(entity: LivingEntity): Boolean {
        if (Hexed.getConfig()?.shouldArmorApply() == true) {
            entity.armorItems.forEach { piece ->
                if (!piece.isIn(CALAMITOUS_ARMOR)) return false
            }
            return true
        }
        return false
    }

    fun runeTexture(string: String): Identifier {
        return Identifier(Hexed.MOD_ID, "textures/gui/runes/$string.png")
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