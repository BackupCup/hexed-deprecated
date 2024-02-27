package net.backupcup.hexed.block

import net.minecraft.block.*
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import java.util.stream.Stream


class BrimstoneSlagPillar(settings: Settings?) : Block(settings) {
    companion object {
        val STATE: IntProperty = IntProperty.of("state", 0, 3)
    }

    init {
        defaultState = defaultState
            .with(STATE, 0)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(STATE)
    }

    private val SHAPE_BOTH = Stream.of(
        createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0),
        createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
        createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0)
    ).reduce { v1: VoxelShape?, v2: VoxelShape? -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR) }.get()
    private val SHAPE_TOP = VoxelShapes.combineAndSimplify(
        createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0),
        createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0), BooleanBiFunction.OR)
    private val SHAPE_BOTTOM = VoxelShapes.combineAndSimplify(
        createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0),
        createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), BooleanBiFunction.OR)
    private val SHAPE_MIDDLE = createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0)

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return when(state.get(STATE)) {
            0 -> SHAPE_BOTH
            1 -> SHAPE_TOP
            2 -> SHAPE_BOTTOM
            3 -> SHAPE_MIDDLE
            else -> SHAPE_BOTH
        }
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return when(state.get(STATE)) {
            0 -> SHAPE_BOTH
            1 -> SHAPE_TOP
            2 -> SHAPE_BOTTOM
            3 -> SHAPE_MIDDLE
            else -> SHAPE_BOTH
        }
    }

    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack?
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)
        var newState = 0

        if (world.getBlockState(pos.down()).isOf(this)) {newState += 1}
        if (world.getBlockState(pos.up()).isOf(this)) {newState += 2}
        world.setBlockState(pos, state.with(STATE, newState))
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

        if (world.getBlockState(pos.down()).isOf(this)) {newState += 1}
        if (world.getBlockState(pos.up()).isOf(this)) {newState += 2}
        return state.with(STATE, newState)
    }
}