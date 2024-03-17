package net.backupcup.hexed.entity.blazingSkull

import net.minecraft.client.model.*
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack

// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
class BlazingSkullModel(root: ModelPart) : EntityModel<BlazingSkullEntity?>() {
    private var skull: ModelPart = root.getChild("skull")

    companion object {
        fun getTexturedModelData(): TexturedModelData {
            val modelData = ModelData()
            val modelPartData = modelData.root

            modelPartData.addChild(
                "skull",
                ModelPartBuilder.create()
                    .uv(0, 0).cuboid(-4.0f, -15.0f, -4.0f, 8.0f, 8.0f, 8.0f, Dilation(0.0f))
                    .uv(7, 16).cuboid(-3.0f, -7.0f, -3.5f, 6.0f, 2.0f, 2.0f, Dilation(0.0f)),
                ModelTransform.pivot(0.0f, 27.0f, 0.0f))

            return TexturedModelData.of(modelData, 32, 32)
        }
    }

    override fun setAngles(
        entity: BlazingSkullEntity?,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        //skull.pitch = headPitch
    }

    override fun render(
        matrices: MatrixStack?,
        vertexConsumer: VertexConsumer?,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        skull.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha)
    }
}