package net.backupcup.hexed.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import java.util.function.ToIntFunction

abstract class AbstractSkullCandle(settings: Settings?) : Block(settings) {
    companion object {
        val LIT: BooleanProperty = BooleanProperty.of("lit")
        val HANGING: BooleanProperty = BooleanProperty.of("hanging")
        val CANDLE: IntProperty = IntProperty.of("candle", 0, 17)
        val FACING: DirectionProperty = Properties.HORIZONTAL_FACING

        val STATE_TO_LUMINANCE = ToIntFunction { state: BlockState ->
            if (state.get(LIT) != false) 15 else 0
        }
    }

    private val STANDING_SHAPE: List<VoxelShape> = listOf(
        createCuboidShape(4.0, 0.0, 1.0, 12.0, 12.0, 14.0), //NORTH
        createCuboidShape(4.0, 0.0, 2.0, 12.0, 12.0, 15.0), //SOUTH
        createCuboidShape(2.0, 0.0, 4.0, 15.0, 12.0, 12.0), //WEST
        createCuboidShape(1.0, 0.0, 4.0, 14.0, 12.0, 12.0)  //EAST
    )

    private val HANGING_SHAPE: List<VoxelShape> = listOf(
        createCuboidShape(4.0, 0.0, 7.75, 12.0, 12.0, 16.0), //NORTH
        createCuboidShape(4.0, 0.0, 0.0, 12.0, 12.0, 8.25),  //SOUTH
        createCuboidShape(7.75, 0.0, 4.0, 16.0, 12.0, 12.0), //WEST
        createCuboidShape(0.0, 0.0, 4.0, 8.25, 12.0, 12.0)   //EAST
    )

    private val CANDLE_LIST: List<Item> = listOf(
        Items.CANDLE,
        Items.BLACK_CANDLE,
        Items.BLUE_CANDLE,
        Items.BROWN_CANDLE,
        Items.CYAN_CANDLE,
        Items.GRAY_CANDLE,
        Items.GREEN_CANDLE,
        Items.LIGHT_BLUE_CANDLE,
        Items.LIGHT_GRAY_CANDLE,
        Items.LIME_CANDLE,
        Items.MAGENTA_CANDLE,
        Items.ORANGE_CANDLE,
        Items.PINK_CANDLE,
        Items.PURPLE_CANDLE,
        Items.RED_CANDLE,
        Items.WHITE_CANDLE,
        Items.YELLOW_CANDLE
    )

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        when(state.get(FACING)) {
            Direction.NORTH -> {
                return if (state.get(HANGING)) HANGING_SHAPE[0] else STANDING_SHAPE[0]
            }
            Direction.SOUTH -> {
                return if (state.get(HANGING)) HANGING_SHAPE[1] else STANDING_SHAPE[1]
            }
            Direction.WEST -> {
                return if (state.get(HANGING)) HANGING_SHAPE[2] else STANDING_SHAPE[2]
            }
            Direction.EAST -> {
                return if (state.get(HANGING)) HANGING_SHAPE[3] else STANDING_SHAPE[3]
            }
            else -> {
                return if (state.get(HANGING)) HANGING_SHAPE[0] else STANDING_SHAPE[0]
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        when(state.get(FACING)) {
            Direction.NORTH -> {
                return if (state.get(HANGING)) HANGING_SHAPE[0] else STANDING_SHAPE[0]
            }
            Direction.SOUTH -> {
                return if (state.get(HANGING)) HANGING_SHAPE[1] else STANDING_SHAPE[1]
            }
            Direction.WEST -> {
                return if (state.get(HANGING)) HANGING_SHAPE[2] else STANDING_SHAPE[2]
            }
            Direction.EAST -> {
                return if (state.get(HANGING)) HANGING_SHAPE[3] else STANDING_SHAPE[3]
            }
            else -> {
                return if (state.get(HANGING)) HANGING_SHAPE[0] else STANDING_SHAPE[0]
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onProjectileHit(
        world: World,
        state: BlockState,
        hit: BlockHitResult,
        projectile: ProjectileEntity
    ) {
        if(!world.isClient && projectile.isOnFire && !state.get(LIT)) {
            world.setBlockState(hit.blockPos, state.with(LIT, true))
        }
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState? {
        return state.with(FACING, rotation.rotate(state.get(FACING)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        var blockState = defaultState
        val worldView: WorldView = ctx.world
        val blockPos = ctx.blockPos
        val placementDirections = ctx.placementDirections

        if (!sideCoversSmallSquare(worldView, blockPos.down(), Direction.UP)) {
            for (direction in placementDirections) {
                if (direction.axis.isHorizontal) {
                    blockState = blockState.with(FACING, direction.opposite)
                    if (blockState.canPlaceAt(worldView, blockPos))
                        return blockState.with(HANGING, true)
                }
            }
        }
        return blockState.with(HANGING, false).with(FACING, ctx.horizontalPlayerFacing.opposite)
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        if (sideCoversSmallSquare(world, pos.down(), Direction.UP)) {
            return true
        } else {
            val direction = state.get(FACING) as Direction
            val blockPos = pos.offset(direction.opposite)
            val blockState = world.getBlockState(blockPos)
            return blockState.isSideSolidFullSquare(world, blockPos, direction)
        }
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.get(HANGING)) {
            return if (direction.opposite == state.get(FACING) && !state.canPlaceAt(world, pos)
            ) Blocks.AIR.defaultState else state
        } else {
            return if (!sideCoversSmallSquare(world, pos.down(), Direction.UP)) Blocks.AIR.defaultState else state
        }
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if(player.abilities?.allowModifyWorld == true) {
            val handItem = player.getStackInHand(hand)
            if(handItem.isEmpty && state.get(LIT)) {
                world.setBlockState(pos, state.with(LIT, false))
                player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1f, 1f)
                world.addParticle(
                    ParticleTypes.LARGE_SMOKE,
                    pos.x + .5, pos.y + 1.0, pos.z + .5,
                    0.0, 0.0625, 0.0)
                return ActionResult.success(world.isClient)
            }
            if((handItem.isOf(Items.FLINT_AND_STEEL) || handItem.isOf(Items.FIRE_CHARGE)) && !state.get(LIT
                )) {
                world.setBlockState(pos, state.with(LIT, true))
                player.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1f, 1f)
                if(!player.isCreative) {
                    if(handItem.isOf(Items.FLINT_AND_STEEL)) {
                        handItem.damage(1, player.random, player.server?.playerManager?.getPlayer(player.uuid))
                    } else {
                        handItem.decrement(1)
                    }
                }
                return ActionResult.success(world.isClient)
            }

            if (CANDLE_LIST.contains(handItem.item) && state.get(CANDLE) == 0) {
                val matchedCandleOrdinal: Int = 1 + (CANDLE_LIST.indexOf(handItem.item))
                world.setBlockState(pos, state.with(CANDLE, matchedCandleOrdinal))
                if (!player.isCreative) {
                    handItem.decrement(1)
                }
                return ActionResult.success(world.isClient)
            }
            if (handItem.isEmpty && !state.get(LIT) && state.get(CANDLE) != 0) {
                dropStack(world, pos, ItemStack(CANDLE_LIST[state.get(CANDLE) - 1]))
                world.setBlockState(pos, state.with(CANDLE, 0))
                player.playSound(SoundEvents.BLOCK_CANDLE_BREAK, 1f, 1f)
                return ActionResult.success(world.isClient)
            }
        }
        return ActionResult.PASS
    }

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        if (!newState.isOf(this)) {
            if (state.get(CANDLE) != 0) {
                dropStack(world, pos, ItemStack(CANDLE_LIST[state.get(CANDLE) - 1]))
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        val randomFloat = random.nextFloat()
        if (state.get(LIT)) {
            val offset = offsetParticles(state, arrayOf(0.5, 1.125, 0.5))

            if (randomFloat < .3f) {
                world.addParticle(ParticleTypes.SMOKE,
                    pos.x + offset[0], pos.y + offset[1], pos.z + offset[2],
                    0.0, 0.0, 0.0)
                if(randomFloat < .15f) {
                    world.playSound(
                        pos.x + offset[0], pos.y + offset[1], pos.z + offset[2],
                        SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS,
                        random.nextFloat() + 1f, random.nextFloat() * .7f + .3f, false)
                }
            }
            world.addParticle(ParticleTypes.SMALL_FLAME,
                pos.x + offset[0], pos.y + offset[1], pos.z + offset[2],
                0.0, 0.0, 0.0)
        }
    }

    private fun offsetParticles(state: BlockState, array: Array<Double>): Array<Double> {
        if (state.get(HANGING)) array[1] += 0.0625

        when(state.get(FACING)) {
            Direction.NORTH -> {array[2] += if (state.get(HANGING)) 0.25 else 0.0625}
            Direction.SOUTH -> {array[2] += if (state.get(HANGING)) -0.25 else -0.0625}
            Direction.WEST -> {array[0] += if (state.get(HANGING)) 0.25 else 0.0625}
            Direction.EAST -> {array[0] += if (state.get(HANGING)) -0.25 else -0.0625}
            else -> {}
        }

        return array
    }
}