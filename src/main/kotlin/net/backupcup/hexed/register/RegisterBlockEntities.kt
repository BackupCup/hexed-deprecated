package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.altar.AccursedAltarBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

object RegisterBlockEntities {
    val ACCURSED_ALTAR_BLOCK_ENTITY: BlockEntityType<AccursedAltarBlockEntity> = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        Identifier(Hexed.MOD_ID, "accursed_altar_block_entity"),
        FabricBlockEntityTypeBuilder.create({pos: BlockPos, state: BlockState -> AccursedAltarBlockEntity(pos, state)}, RegisterBlocks.ACCURSED_ALTAR).build()
    )

    fun registerBlockEntities() {
        ACCURSED_ALTAR_BLOCK_ENTITY
    }
}