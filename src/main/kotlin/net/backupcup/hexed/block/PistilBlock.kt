package net.backupcup.hexed.block

import net.minecraft.block.*
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import java.util.function.ToIntFunction
import kotlin.math.ceil

class PistilBlock(settings: Settings?) : Block(settings), Fertilizable {

    companion object {
        const val maxAge = 25
        var AGE: IntProperty = IntProperty.of("age", 0, maxAge)
        var TYPE: IntProperty = IntProperty.of("type", 0, 2)
        val STATE_TO_LUMINANCE = ToIntFunction { state: BlockState ->
            if (state.get(AGE) <= maxAge / 2)
            { ceil(((maxAge / 2 - state.get(AGE)) / (maxAge / 2).toDouble()) * 15).toInt() }
            else
            { ceil(((state.get(AGE) - maxAge / 2) / (maxAge / 2).toDouble()) * 15).toInt() }
        }
    }

    init {
        defaultState = defaultState
            .with(AGE, 0)
            .with(TYPE, 0)
    }

    val SHAPE_FULL: VoxelShape = createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0)
    val SHAPE_TOP: VoxelShape = createCuboidShape(6.0, 0.0, 6.0, 10.0, 11.0, 10.0)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(AGE, TYPE)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return if (state.get(TYPE) != 2) SHAPE_FULL else SHAPE_TOP
    }

    override fun canPathfindThrough(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        type: NavigationType?
    ): Boolean {
        return true
    }

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack?
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)
        val newState = getState(world, pos).first
        val newAge = getState(world, pos).second
        world.setBlockState(pos, state.with(TYPE, newState).with(AGE, newAge))
    }

    @Deprecated("Deprecated in Java")
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        var newState = 0
        if (world.getBlockState(pos.down()).isOf(this)) { newState = if (!world.getBlockState(pos.up()).isAir) 1 else 2 }

        return if (!(sideCoversSmallSquare(world, pos.down(), Direction.UP) ||
                    world.getBlockState(pos.down()).isOf(this))) Blocks.AIR.defaultState else state.with(TYPE, newState)
    }

    override fun canPlaceAt(state: BlockState?, world: WorldView, pos: BlockPos): Boolean {
        return sideCoversSmallSquare(world, pos.down(), Direction.UP) ||
                (world.getBlockState(pos.down()).isOf(this) && world.getBlockState(pos.down()).get(AGE) < maxAge)
    }

    override fun isFertilizable(world: WorldView, pos: BlockPos, state: BlockState, isClient: Boolean): Boolean {
        if (world.getBlockState(pos).get(AGE) == maxAge) return false

        for (currentAge in (0..maxAge-state.get(AGE))) {
            if (world.getBlockState(pos.up(currentAge+1)).isOf(this)) continue
            if (!world.getBlockState(pos.up(currentAge+1)).isReplaceable) return false

            return world.getBlockState(pos.up(currentAge)).get(AGE) < maxAge
        }
        return false
    }

    override fun canGrow(world: World, random: Random?, pos: BlockPos, state: BlockState): Boolean {
        return true
    }

    override fun grow(world: ServerWorld, random: Random?, pos: BlockPos, state: BlockState) {
        for (currentAge in (0..maxAge-state.get(AGE))) {
            if (world.getBlockState(pos.up(currentAge+1)).isOf(this)) continue
            if (world.getBlockState(pos.up(currentAge+1)).isReplaceable && world.getBlockState(pos.up(currentAge)).get(AGE) < maxAge) {

                val randomBlockChance = kotlin.random.Random.nextInt(currentAge + state.get(AGE), maxAge+2)
                if (randomBlockChance + 1 == maxAge) {
                    if (kotlin.random.Random.nextFloat() >= 0.5) world.setBlockState(pos.up(currentAge+1), Blocks.SHROOMLIGHT.defaultState)
                    else world.setBlockState(pos.up(currentAge+1), Blocks.GLOWSTONE.defaultState)
                } else {
                    world.setBlockState(pos.up(currentAge+1), state
                        .with(TYPE, getState(world, pos.up(currentAge+1)).first)
                        .with(AGE, getState(world, pos.up(currentAge+1)).second))
                }

                return
            }
        }
    }

    fun getState(world: World, pos: BlockPos): Pair<Int, Int> {
        var newState = 0
        var newAge = 1

        if (world.getBlockState(pos.down()).isOf(this)) {
            newAge += world.getBlockState(pos.down()).get(AGE)
            newState = if (!world.getBlockState(pos.up()).isAir) 1 else 2
        } else {
            newAge = kotlin.random.Random.nextInt(0, maxAge)
        }
        return Pair(newState, newAge)
    }
}