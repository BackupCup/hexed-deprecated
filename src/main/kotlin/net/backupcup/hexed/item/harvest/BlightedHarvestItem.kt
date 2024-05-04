package net.backupcup.hexed.item.harvest

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.register.RegisterDamageTypes
import net.backupcup.hexed.register.RegisterSounds
import net.backupcup.hexed.register.RegisterStatusEffects
import net.backupcup.hexed.util.CustomHandTexture
import net.backupcup.hexed.util.TaintedItem
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterial
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import net.minecraft.world.World
import kotlin.math.ceil

class BlightedHarvestItem(
    toolMaterial: ToolMaterial?,
    attackDamage: Int,
    val attackSpeed: Float,
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

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) return super.use(world, user, hand)

        val itemStack = user.getStackInHand(hand)
        val attackCooldown = ceil((4f - attackSpeed) * 20).toInt()

        val maxHealthEntity: LivingEntity = findHealthiestEntity(user)
        maxHealthEntity.addStatusEffect(StatusEffectInstance(
            RegisterStatusEffects.BLIGHTED,
            attackCooldown * 4, 0,
            false, true, true
        ))

        world.playSoundFromEntity(null, user,
            RegisterSounds.ACCURSED_ALTAR_TAINT, SoundCategory.PLAYERS,
            0.5f, 1.5f)

        user.damage(RegisterDamageTypes.of(world, RegisterDamageTypes.ABLAZE_DAMAGE), user.health / 10f)
        user.itemCooldownManager.set(this, attackCooldown * 2)
        itemStack.damage(5, user) {p -> p.sendToolBreakStatus(hand)}

        return super.use(world, user, hand)
    }

    private fun findHealthiestEntity(target: LivingEntity): LivingEntity {
        var searchBox: Box = Box(target.blockPos).expand(16.0 + 2 * (target.maxHealth - target.health))

        var closestEntity: Pair<Float, LivingEntity> = Pair(0f, target)
        target.entityWorld.getNonSpectatingEntities(LivingEntity::class.java, searchBox).forEach { foundEntity ->
            val healthRatio: Float = foundEntity.maxHealth / foundEntity.health
            if (healthRatio > closestEntity.first) closestEntity = Pair(healthRatio, foundEntity)
        }

        return closestEntity.second
    }
}