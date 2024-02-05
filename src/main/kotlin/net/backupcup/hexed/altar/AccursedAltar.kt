package net.backupcup.hexed.altar

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.block.AbstractTallCandle
import net.backupcup.hexed.register.RegisterBlockEntities
import net.backupcup.hexed.register.RegisterBlocks
import net.backupcup.hexed.register.RegisterSounds
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.sound.SoundCategory
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.*
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.stream.Stream

class AccursedAltar(settings: Settings?
) : BlockWithEntity(settings
), BlockEntityProvider {
    companion object {
        val FACING: DirectionProperty = Properties.HORIZONTAL_FACING
        val ACTIVE: BooleanProperty = BooleanProperty.of("active")
    }

    init {
        defaultState = defaultState
            .with(ACTIVE, false)
            .with(FACING, Direction.NORTH)
    }

    private val SHAPE = Stream.of(
        createCuboidShape(0.0, 5.0, 0.0, 3.0, 16.0, 3.0),
        createCuboidShape(13.0, 5.0, 0.0, 16.0, 16.0, 3.0),
        createCuboidShape(2.0, 0.0, 2.0, 14.0, 8.0, 14.0),
        createCuboidShape(1.0, 8.0, 1.0, 15.0, 12.0, 15.0),
        createCuboidShape(13.0, 5.0, 13.0, 16.0, 16.0, 16.0),
        createCuboidShape(0.0, 5.0, 13.0, 3.0, 16.0, 16.0)
    ).reduce { v1, v2 -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR) }.get();

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(FACING)?.add(ACTIVE)
    }

    override fun appendTooltip(
        stack: ItemStack?,
        world: BlockView?,
        tooltip: MutableList<Text>?,
        options: TooltipContext?
    ) {
        super.appendTooltip(stack, world, tooltip, options)
    }

    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity {
        return AccursedAltarBlockEntity(pos, state)
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return SHAPE
    }

    @Deprecated("Deprecated in Java")
    override fun getCollisionShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return SHAPE
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState? {
        return state.with(FACING, rotation.rotate(state.get(FACING)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return defaultState.with(FACING, ctx.horizontalPlayerFacing.opposite)
    }

    override fun onUse(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult {
        if (!world!!.isClient) {
            if(!state!!.get(ACTIVE)) {
                player?.sendMessage(Text.translatable("message.hexed.altar_no_candles")
                    .formatted(Formatting.RED).formatted(Formatting.BOLD).formatted(Formatting.ITALIC), true)
                return ActionResult.SUCCESS
            }

            val screenHandlerFactory: NamedScreenHandlerFactory? = state.createScreenHandlerFactory(world, pos)

            if (screenHandlerFactory != null) {
                player?.openHandledScreen(screenHandlerFactory)
            }
        }

        return ActionResult.SUCCESS
    }

    override fun <T : BlockEntity?> getTicker(
        world: World?,
        state: BlockState?,
        type: BlockEntityType<T>?
    ): BlockEntityTicker<T>? {
        return checkType(type, RegisterBlockEntities.ACCURSED_ALTAR_BLOCK_ENTITY) { world, pos, state, blockEntity ->
            if (blockEntity is AccursedAltarBlockEntity) {
                if (world.isClient) AccursedAltarBlockEntity.clientTick(world, pos, state, blockEntity)
                else {
                    AccursedAltarBlockEntity.tick(world, pos, state, blockEntity)
                }
            }
        }
    }
}