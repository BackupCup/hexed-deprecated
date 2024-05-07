package net.backupcup.hexed.item.carnage

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.register.RegisterDamageTypes
import net.backupcup.hexed.register.RegisterStatusEffects
import net.backupcup.hexed.util.CustomHandTexture
import net.backupcup.hexed.util.HexHelper
import net.backupcup.hexed.util.TaintedItem
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.entity.LivingEntity
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

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        if (attacker.entityWorld.isClient) return super.postHit(stack, target, attacker)

        HexHelper.entityMultiplyingEffect(
            target, RegisterStatusEffects.DEATH_MARK, 200, 50,
            ambient = false, showParticles = true, showIcon = true)
        if (attacker.hasStatusEffect(RegisterStatusEffects.IRONCLAD)) {
            if (attacker.getStatusEffect(RegisterStatusEffects.IRONCLAD)!!.amplifier < attacker.maxHealth / 2f) {
                HexHelper.entityMultiplyingEffect(
                    attacker, RegisterStatusEffects.IRONCLAD, 80, 5)
            }
        } else {
            HexHelper.entityMultiplyingEffect(
                attacker, RegisterStatusEffects.IRONCLAD, 80, 5)
        }

        if (target.getStatusEffect(RegisterStatusEffects.DEATH_MARK)!!.amplifier >= 4 || target.hasStatusEffect(RegisterStatusEffects.VINDICTIVE)) {
            target.removeStatusEffect(RegisterStatusEffects.DEATH_MARK)
            HexHelper.entityMultiplyingEffect(
                target, RegisterStatusEffects.VINDICTIVE, 100, 20,
                ambient = false, showParticles = true, showIcon = true)
        }

        return super.postHit(stack, target, attacker)
    }
}