package net.backupcup.hexed.block

import net.backupcup.hexed.register.RegisterSlagBlocks
import net.minecraft.block.BlockState
import net.minecraft.fluid.FlowableFluid
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView


abstract class BlazingMagmaFluid: FlowableFluid() {
    override fun getBucketItem(): Item {
        return RegisterSlagBlocks.BLAZING_MAGMA_BUCKET
    }

    override fun getFlowing(): Fluid {
        return RegisterSlagBlocks.FLOW_BLAZING_MAGMA
    }

    override fun getStill(): Fluid {
        return RegisterSlagBlocks.STILL_BLAZING_MAGMA
    }

    override fun matchesType(fluid: Fluid): Boolean {
        return fluid === still || fluid === flowing
    }

    override fun canBeReplacedWith(
        state: FluidState?,
        world: BlockView?,
        pos: BlockPos?,
        fluid: Fluid?,
        direction: Direction?
    ): Boolean {
        return false
    }

    override fun getTickRate(world: WorldView): Int {
        return if (world.dimension.ultrawarm) 15 else 7
    }

    override fun getBlastResistance(): Float {
        return 100f
    }

    override fun toBlockState(state: FluidState?): BlockState {
        return RegisterSlagBlocks.BLAZING_MAGMA.defaultState.with(Properties.LEVEL_15, getBlockStateLevel(state))
    }

    override fun isStill(state: FluidState?): Boolean {
        return false
    }

    override fun isInfinite(world: World?): Boolean {
        return false
    }

    override fun beforeBreakingBlock(world: WorldAccess, pos: BlockPos, state: BlockState) {
        world.playSound(
            null, pos,
            SoundEvents.BLOCK_LAVA_EXTINGUISH,
            SoundCategory.BLOCKS
        )
    }

    override fun getFlowSpeed(world: WorldView): Int {
        return if (world.dimension.ultrawarm) 6 else 3
    }

    override fun getLevelDecreasePerBlock(world: WorldView): Int {
        return if (world.dimension.ultrawarm) 1 else 2
    }


    class Flowing: BlazingMagmaFluid() {
        override fun appendProperties(builder: StateManager.Builder<Fluid?, FluidState?>) {
            super.appendProperties(builder)
            builder.add(LEVEL)
        }

        override fun getLevel(fluidState: FluidState): Int {
            return fluidState.get(LEVEL)
        }

        override fun isStill(state: FluidState?): Boolean { return false }
    }


    class Still: BlazingMagmaFluid() {
        override fun getLevel(state: FluidState?): Int {
            return 8
        }
        override fun isStill(state: FluidState?): Boolean { return true }
    }

}