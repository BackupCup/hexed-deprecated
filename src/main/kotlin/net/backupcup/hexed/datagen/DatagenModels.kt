package net.backupcup.hexed.datagen

import net.backupcup.hexed.register.RegisterSlagBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator

class DatagenModels(output: FabricDataOutput?) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        val slagPool = blockStateModelGenerator.registerCubeAllModelTexturePool(RegisterSlagBlocks.BRIMSTONE_SLAG)
        val bricksPool = blockStateModelGenerator.registerCubeAllModelTexturePool(RegisterSlagBlocks.BRIMSTONE_BRICKS)
        val smoothPool = blockStateModelGenerator.registerCubeAllModelTexturePool(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG)

        slagPool.stairs(RegisterSlagBlocks.BRIMSTONE_SLAG_STAIRS)
        slagPool.slab(RegisterSlagBlocks.BRIMSTONE_SLAG_SLAB)
        slagPool.wall(RegisterSlagBlocks.BRIMSTONE_SLAG_WALL)

        bricksPool.stairs(RegisterSlagBlocks.BRIMSTONE_BRICKS_STAIRS)
        bricksPool.slab(RegisterSlagBlocks.BRIMSTONE_BRICKS_SLAB)
        bricksPool.wall(RegisterSlagBlocks.BRIMSTONE_BRICKS_WALL)

        smoothPool.stairs(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_STAIRS)
        smoothPool.slab(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_SLAB)
        smoothPool.wall(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_WALL)
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator?) {
    }
}