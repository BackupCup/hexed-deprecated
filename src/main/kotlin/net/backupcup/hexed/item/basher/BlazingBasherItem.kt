package net.backupcup.hexed.item.basher

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.register.RegisterStatusEffects
import net.backupcup.hexed.util.CustomHandTexture
import net.backupcup.hexed.util.TaintedItem
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterial
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

class BlazingBasherItem(
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

        if (target.isDead) {
            println("WILL-O-THE-WISP EXPLOSION HERE")
        }

        if (target.health + this.attackDamage >= target.maxHealth) {
            target.addStatusEffect(StatusEffectInstance(
                RegisterStatusEffects.ABLAZE,
                60, 0,
                false, true, true
            ))

            attacker.addStatusEffect(StatusEffectInstance(
                RegisterStatusEffects.AFLAME,
                30, 0,
                false, true, true
            ))
        }

        return super.postHit(stack, target, attacker)
    }
}