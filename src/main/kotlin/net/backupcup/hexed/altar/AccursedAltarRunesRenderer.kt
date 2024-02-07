package net.backupcup.hexed.altar

import net.backupcup.hexed.register.RegisterBlocks
import net.backupcup.hexed.register.RegisterRunes
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec3d
import kotlin.math.sin


@Environment(EnvType.CLIENT)
class AccursedAltarRunesRenderer: BlockEntityRenderer<AccursedAltarBlockEntity> {

    private val itemStackTexture = listOf(
        ItemStack(RegisterRunes.OMEGA),
        ItemStack(RegisterRunes.MOON),
        ItemStack(RegisterRunes.MAGNESIUM),
        ItemStack(RegisterRunes.MERCURY),
        ItemStack(RegisterRunes.SULFUR),
        ItemStack(RegisterRunes.FIRE)
    )

    // name: Persecuted
    // Attack does [ITEM_ATTACK_DAMAGE]% of max hp of the enemy
    // Hit while holding -> Armor poof
    // [20 Armor -[get hit]> 0 ...[goes back up slowly]... 20]

    // name: Aflame
    // Gives the enemy "Ablaze (more powerful onFire)" effect when hit
    // user is lit on fire on hit

    // name: Ephemeral
    // Damage output of the user diminishes over time, returns naturally after some time
    // Starts off with more damage than it normally would have
    // [base: 8 -> 12 -> 1]
    //
    // 0-5sec 1-10sec 2-15sec 3-20sec 4-25sec 5-30sec


    //no levels:
    // addStatusEffect (RegisterStatusEffects.EPHEMERAL, level 0, 30 seconds)

    //
    //X level
    //X = getStatusEffectAmplifier
    //amplifier = X + 1
    //for i 0..amplifier addStatusEffect(RegisterStatusEffects.EPHEMERAL, level i, 30 + (amplifier - i) * decayLength
    // lvl 0 45
    // lvl 1 40
    // lvl 2 35
    // lvl 3 30
    //

    // name: Resentful
    //
    //
    // [do damage -> be slower, attack faster]

    override fun render(
        entity: AccursedAltarBlockEntity?,
        tickDelta: Float,
        matrices: MatrixStack?,
        vertexConsumers: VertexConsumerProvider?,
        light: Int,
        overlay: Int
    ) {

        if(entity.world?.getBlockState(entity.pos)?.block == RegisterBlocks.ACCURSED_ALTAR) {
            if (entity.world?.getBlockState(entity.pos)?.get(AccursedAltar.ACTIVE) != true) return
        }

        val lightAbove = WorldRenderer.getLightmapCoordinates(entity.world, entity.pos.up())
        var renderVec = Vec3d(0.0, 0.0, -1.0)

        matrices.push()
        matrices.translate(0.5, 1.0, 0.5)
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.world?.time?.plus(tickDelta))?.times(2) ?: 0f))

        itemStackTexture.forEachIndexed { index, stack ->
            matrices.push()

            val indexedOffset = index * 60
            val yOffset = sin((entity.world?.time?.plus(tickDelta)?.plus(indexedOffset))?.div(8.0) ?: 0.0) / 4.0

            matrices.translate(renderVec.x, renderVec.y + yOffset, renderVec.z)
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.world?.time?.plus(tickDelta)?.plus(indexedOffset))?.times(0.25f) ?: 0f))

            MinecraftClient.getInstance().itemRenderer.renderItem(stack, ModelTransformationMode.GROUND,
                lightAbove, overlay, matrices, vertexConsumers, entity.world, 0)

            renderVec = renderVec.rotateY(1.0472f)
            matrices.pop()
        }

        matrices.pop()
    }
}