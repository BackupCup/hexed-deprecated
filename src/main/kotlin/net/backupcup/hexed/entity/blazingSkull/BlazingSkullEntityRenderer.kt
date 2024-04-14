package net.backupcup.hexed.entity.blazingSkull

import net.backupcup.hexed.Hexed
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class BlazingSkullEntityRenderer(
    context: EntityRendererFactory.Context?,
    entityModel: BlazingSkullModel?,
    f: Float
) : MobEntityRenderer<BlazingSkullEntity, BlazingSkullModel>(
    context, entityModel, f
) {
    override fun getTexture(entity: BlazingSkullEntity?): Identifier {
        return Identifier(Hexed.MOD_ID, "textures/entity/blazing_skull.png")
    }

    override fun render(
        mobEntity: BlazingSkullEntity?,
        f: Float,
        g: Float,
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider?,
        i: Int
    ) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i)
    }
}