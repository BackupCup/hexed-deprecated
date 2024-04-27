package net.backupcup.hexed.item.labrys

import io.netty.buffer.Unpooled
import net.backupcup.hexed.Hexed
import net.backupcup.hexed.packets.HexNetworkingConstants
import net.backupcup.hexed.register.RegisterDamageTypes
import net.backupcup.hexed.register.RegisterSounds
import net.backupcup.hexed.util.CustomHandTexture
import net.backupcup.hexed.util.TaintedItem
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityGroup
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.AxeItem
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterial
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class BishopLabrysItem(
    material: ToolMaterial?,
    attackDamage: Float,
    attackSpeed: Float,
    override var itemId: String,
    settings: Settings?
): AxeItem(
    material, attackDamage, attackSpeed, settings
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
        tooltip?.add(Text.translatable("tooltip.hexed.bishop_labrys").formatted(Formatting.GRAY))
        super.appendTooltip(stack, world, tooltip, context)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) return super.use(world, user, hand)

        val itemStack = user.getStackInHand(hand)
        var activeCharge: Int = if (itemStack.nbt?.contains("electricCharge") == true) itemStack.nbt?.getInt("electricCharge")?:0 else 0

        if (activeCharge < 2) {
            activeCharge += 1

            world.playSoundFromEntity(null, user,
                RegisterSounds.ELECTRIC_CHARGE, SoundCategory.PLAYERS,
                1f, 1f + 0.25f * activeCharge)
            itemStack.damage(5 * activeCharge, user) {callback -> callback.sendToolBreakStatus(hand)}
            user.itemCooldownManager.set(this, 100)

            itemStack.nbt?.putInt("electricCharge", activeCharge)
        }

        return super.use(world, user, hand)
    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        if (attacker.entityWorld.isClient) return super.postHit(stack, target, attacker)

        var activeCharge: Int = if (stack.nbt?.contains("electricCharge") == true) stack.nbt?.getInt("electricCharge")?:0 else 0

        if (activeCharge > 0) {
            activeCharge -= 1

            var chainedList: MutableList<LivingEntity> = findNearestEntities(target, activeCharge).toMutableList()
            chainedList.remove(target)

            iterateOverEntities(chainedList, attacker.entityWorld, stack, target)
            target.entityWorld.playSoundFromEntity(null, target,
                RegisterSounds.ELECTRIC_DISCHARGE, SoundCategory.PLAYERS,
                0.25f, 1f)

            if (attacker is ServerPlayerEntity) {
                attacker.itemCooldownManager.set(this, 20)
            }

            stack.nbt?.putInt("electricCharge", activeCharge)
        }

        return super.postHit(stack, target, attacker)
    }

    private fun findNearestEntities(target: LivingEntity, activeCharge: Int): List<LivingEntity> {
        var chainedList: MutableList<LivingEntity> = mutableListOf(target)

        for (i in 1..3 * (activeCharge + 1)) {
            val currentEntity = chainedList.last()

            var searchBox: Box = Box(currentEntity.blockPos).expand(10.0 + 5.0 * activeCharge)

            var closestEntity: Pair<Float, LivingEntity?> = Pair(0f, null)
            currentEntity.entityWorld.getNonSpectatingEntities(LivingEntity::class.java, searchBox).forEach { foundEntity ->
                if (!chainedList.contains(foundEntity)) {
                    val distance: Float = target.distanceTo(foundEntity)

                    if (closestEntity.second == null) closestEntity = Pair(distance, foundEntity)
                    else if (distance < closestEntity.first) closestEntity = Pair(distance, foundEntity)
                }
            }

            if (closestEntity.second != null) chainedList.add(closestEntity.second!!) else break
        }

        return chainedList
    }

    private fun iterateOverEntities(chainedList: List<LivingEntity>, world: World, stack: ItemStack, target: LivingEntity) {
        var damage = EnchantmentHelper.getAttackDamage(stack, EntityGroup.DEFAULT)
        var previousEntity: LivingEntity = target

        chainedList.forEach { livingEntity ->
            damage *= 0.8f
            livingEntity.damage(RegisterDamageTypes.of(world, RegisterDamageTypes.ABYSSAL_CRUSH), damage)

            particleBeam(
                livingEntity.entityWorld as ServerWorld,
                previousEntity.pos.add(0.0, previousEntity.height/2.0, 0.0),
                livingEntity.pos.add(0.0, livingEntity.height/2.0, 0.0))
            previousEntity = livingEntity
        }
    }

    private fun particleBeam(world: ServerWorld, start: Vec3d, end: Vec3d) {
        val vector = end.subtract(start).multiply(0.1)
        var pos: Vec3d = start

        for (i in 1..10) {
            world.spawnParticles(
                ParticleTypes.ELECTRIC_SPARK,
                pos.x, pos.y, pos.z, 5,
                vector.x, vector.y, vector.z, 0.0
            )

            pos = pos.add(vector)
        }
    }
}