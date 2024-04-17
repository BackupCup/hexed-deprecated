package net.backupcup.hexed.block

import net.backupcup.hexed.register.RegisterSlagBlocks
import net.backupcup.hexed.util.HexRandom
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.Fertilizable
import net.minecraft.block.NyliumBlock
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.minecraft.world.WorldView
import net.minecraft.world.chunk.light.ChunkLightProvider

class LavendinCinderBlock(settings: Settings?) : NyliumBlock(settings), Fertilizable {
    override fun isFertilizable(world: WorldView, pos: BlockPos, state: BlockState?, isClient: Boolean): Boolean {
        return world.getBlockState(pos.up()).isAir
    }

    override fun canGrow(world: World?, random: Random?, pos: BlockPos?, state: BlockState?): Boolean {
        return true
    }

    override fun grow(world: ServerWorld, random: Random, pos: BlockPos, state: BlockState?) {
        if(random.nextFloat() >= 0.5f) world.setBlockState(pos.up(), RegisterSlagBlocks.LAVENDIN_VERDURE.defaultState)
        else world.setBlockState(pos.up(), RegisterSlagBlocks.LAVA_PISTIL.defaultState.with(PistilBlock.AGE, HexRandom.nextInt(0, PistilBlock.maxAge)))
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random?) {
        if (!stayAlive(state, world, pos)) {
            world.setBlockState(pos, RegisterSlagBlocks.BRIMSTONE_SLAG.defaultState)
        }
    }

    private fun stayAlive(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val blockPos = pos.up()
        val blockState = world.getBlockState(blockPos)
        val i = ChunkLightProvider.getRealisticOpacity(
            world,
            state,
            pos,
            blockState,
            blockPos,
            Direction.UP,
            blockState.getOpacity(world, blockPos)
        )
        return i < world.maxLightLevel
    }
}