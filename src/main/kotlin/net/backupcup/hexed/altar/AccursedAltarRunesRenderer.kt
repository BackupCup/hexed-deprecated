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

    override fun render(
        entity: AccursedAltarBlockEntity?,
        tickDelta: Float,
        matrices: MatrixStack?,
        vertexConsumers: VertexConsumerProvider?,
        light: Int,
        overlay: Int
    ) {
        val isActive = if(entity?.world?.getBlockState(entity.pos)?.block == RegisterBlocks.ACCURSED_ALTAR) {
            entity.world?.getBlockState(entity.pos)?.get(AccursedAltar.ACTIVE)
        } else false

        val lightAbove = WorldRenderer.getLightmapCoordinates(entity!!.world, entity.pos.up())

        if (isActive == false) {
            return
        }

        matrices!!.push()
        matrices.translate(0.5, 1.0, 0.5)
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.world!!.time + tickDelta) * 2))

        for (offset in renderOffsets) {
            matrices.push()

            val yOffset = sin((entity.world!!.time + tickDelta + offset[2] as Double) / 8.0) / 4.0

            matrices.translate(offset[0] as Double, 0.0 + yOffset, offset[1] as Double)
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.world!!.time + tickDelta) * 0.25f))

            MinecraftClient.getInstance().itemRenderer.renderItem(offset[3] as ItemStack, ModelTransformationMode.GROUND,
                lightAbove, overlay, matrices, vertexConsumers, entity.world, 0)

            matrices.pop()
        }

        matrices.pop()
    }
}