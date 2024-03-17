package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.entity.blazingSkull.BlazingSkullEntity
import net.backupcup.hexed.entity.blazingSkull.BlazingSkullEntityRenderer
import net.backupcup.hexed.entity.blazingSkull.BlazingSkullModel
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier


object RegisterEntities {
    val BLAZING_SKULL: EntityType<BlazingSkullEntity> = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ::BlazingSkullEntity)
        .dimensions(EntityDimensions.fixed(0.75f, 0.9375f)).build()

    fun registerEntities() {
        Registry.register(Registries.ENTITY_TYPE, Identifier(Hexed.MOD_ID, "blazing_skull"), BLAZING_SKULL)
        FabricDefaultAttributeRegistry.register(BLAZING_SKULL, BlazingSkullEntity.createAttributes())
    }

    fun registerEntityModels() {
        val MODEL_SKULL_LAYER = EntityModelLayer(Identifier(Hexed.MOD_ID, "blazing_skull"), "skull")

        EntityRendererRegistry.register(BLAZING_SKULL) { context: EntityRendererFactory.Context ->
            BlazingSkullEntityRenderer(
                context,
                BlazingSkullModel(context.getPart(MODEL_SKULL_LAYER)),
                0.5f
            )
        }

        EntityModelLayerRegistry.registerModelLayer(MODEL_SKULL_LAYER, BlazingSkullModel::getTexturedModelData)
    }
}