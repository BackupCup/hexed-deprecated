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

    companion object {
        var itemStackTexture = listOf(
            ItemStack(RegisterRunes.TRAITOROUS),
            ItemStack(RegisterRunes.DISPLACED),
            ItemStack(RegisterRunes.METAMORPHOSIS),
            ItemStack(RegisterRunes.AFLAME),
            ItemStack(RegisterRunes.AVERTING),
            ItemStack(RegisterRunes.DISFIGUREMENT)
        )
    }

    override fun render(
        entity: AccursedAltarBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        if (entity.getActiveState() == false) return

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