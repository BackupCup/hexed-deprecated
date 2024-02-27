package net.backupcup.hexed.block

import net.backupcup.hexed.register.RegisterSlagBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.PlantBlock
import net.minecraft.block.ShapeContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class LavendinVerdureBlock(settings: Settings?) : PlantBlock(settings) {
    val SHAPE = createCuboidShape(2.0, 0.0, 2.0, 14.0, 13.0, 14.0)
    override fun canPlantOnTop(floor: BlockState, world: BlockView?, pos: BlockPos?): Boolean {
        return floor.isOf(RegisterSlagBlocks.LAVENDIN_CINDER)
    }

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape? {
        return SHAPE
    }
}