package net.backupcup.hexed.block

import net.backupcup.hexed.register.RegisterSlagBlocks
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Fertilizable
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.minecraft.world.WorldView

class BrimstoneSlagBlock(settings: Settings?) : Block(settings), Fertilizable {
    override fun isFertilizable(world: WorldView, pos: BlockPos, state: BlockState?, isClient: Boolean): Boolean {
        if (!world.getBlockState(pos.up()).isTransparent(world, pos)) {
            return false
        }
        /* TODO: UNCOMMENT WHEN I ACTUALLY ADD THE BIOME
        for (blockPos in BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            if (!world.getBlockState(blockPos).isOf(RegisterSlagBlocks.LAVENDIN_CINDER)) continue
            return true
        }
        */
        return true
    }

    override fun canGrow(world: World?, random: Random?, pos: BlockPos?, state: BlockState?): Boolean {
        return true
    }

    override fun grow(world: ServerWorld, random: Random?, pos: BlockPos, state: BlockState?) {
        world.setBlockState(pos, RegisterSlagBlocks.LAVENDIN_CINDER.defaultState, NOTIFY_ALL)

        /* TODO: REPLACE WITH THIS WHEN I ACTUALLY ADD THE BIOME
        for (blockPos in BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            val blockState = world.getBlockState(blockPos)
            if (blockState.isOf(RegisterSlagBlocks.LAVENDIN_CINDER)) {
                world.setBlockState(pos, RegisterSlagBlocks.LAVENDIN_CINDER.defaultState, NOTIFY_ALL)
                break
            }
        }
         */
    }
}