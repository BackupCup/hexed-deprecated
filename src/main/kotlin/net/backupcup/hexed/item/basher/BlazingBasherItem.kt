package net.backupcup.hexed.item.basher

import io.netty.buffer.Unpooled
import net.backupcup.hexed.Hexed
import net.backupcup.hexed.packets.HexNetworkingConstants
import net.backupcup.hexed.register.RegisterDamageTypes
import net.backupcup.hexed.register.RegisterSounds
import net.backupcup.hexed.register.RegisterStatusEffects
import net.backupcup.hexed.register.RegisterTimedEvents
import net.backupcup.hexed.util.CustomHandTexture
import net.backupcup.hexed.util.HexRandom
import net.backupcup.hexed.util.TaintedItem
import net.backupcup.hexed.util.TimedEventAction
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterial
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.DustParticleEffect
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.cos
import kotlin.math.sin


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
            RegisterTimedEvents.createTimedEvent(false, 20) { BlazingWaveHelper.createExplosion(target, 5.0) }
        } else {
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
        }

        return super.postHit(stack, target, attacker)
    }
}