package net.backupcup.hexed.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleEffect
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import java.util.function.ToIntFunction

abstract class AbstractTallCandle(settings: Settings?) : Block(settings) {
    companion object {
        val LIT: BooleanProperty = BooleanProperty.of("lit")
        val TOP: BooleanProperty = BooleanProperty.of("top")
        val STATE_TO_LUMINANCE = ToIntFunction { state: BlockState ->
            if (state.get(LIT) != false) 15 else 0
        }
    }

    abstract val particleExtinguish: ParticleEffect

    private val TOP_SHAPE: Box = Box(
        0.375, -1.0, 0.375,
        0.625, 0.65625, 0.625
    )

    private val BOTTOM_SHAPE: Box = Box(
        0.375, 0.0, 0.375,
        0.625, 1.65625, 0.625
    )

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        if (state!!.get(TOP)) return VoxelShapes.cuboid(TOP_SHAPE)
        return VoxelShapes.cuboid(BOTTOM_SHAPE)
    }

    @Deprecated("Deprecated in Java")
    override fun getCollisionShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        if (state!!.get(TOP)) return VoxelShapes.cuboid(TOP_SHAPE)
        return VoxelShapes.cuboid(BOTTOM_SHAPE)
    }

    @Deprecated("Deprecated in Java")
    override fun onProjectileHit(
        world: World?,
        state: BlockState?,
        hit: BlockHitResult?,
        projectile: ProjectileEntity?
    ) {
        if(!world?.isClient!! && projectile!!.isOnFire && !state?.get(LIT)!!) {
            setLit(world, hit!!.blockPos, state, true)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onUse(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult {
        if(player?.abilities?.allowModifyWorld!!) {
            val handItem = player.getStackInHand(hand)
            if(handItem.isEmpty && state!!.get(BrimstoneCandle.LIT)) {
                setLit(world!!, pos!!, state, false)
                player.playSound(SoundEvents.BLOCK_CANDLE_EXTINGUISH, 1f, 1f)
                world.addParticle(
                    particleExtinguish,
                    pos.x + .5, pos.y + 1.0, pos.z + .5,
                    0.0, 0.0625, 0.0)
                return ActionResult.success(world.isClient)
            }
            if((handItem.isOf(Items.FLINT_AND_STEEL) || handItem.isOf(Items.FIRE_CHARGE)) && !state!!.get(
                    BrimstoneCandle.LIT
                )) {
                setLit(world!!, pos!!, state, true)
                if(!player.isCreative) {
                    if(handItem.isOf(Items.FLINT_AND_STEEL)) {
                        handItem.damage(1, player.random, player.server?.playerManager?.getPlayer(player.uuid))
                    } else {
                        handItem.decrement(1)
                    }
                }
                return ActionResult.success(world.isClient)
            }
        }
        return ActionResult.PASS
    }

    override fun onPlaced(
        world: World?,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack?
    ) {
        if (world!!.canSetBlock(pos?.up(1))) {
            world.setBlockState(pos?.up(1), this.defaultState.with(TallCandle.TOP, true))
        }
    }

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        if (!newState.isOf(this)) {
            breakPieces(world, pos)
        }
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        val upperBlockPos = ctx?.blockPos?.up(1)
        val world = ctx?.world

        if(world?.getBlockState(upperBlockPos)?.canReplace(ctx) == true) {
            return this.defaultState.with(TallCandle.TOP, false)
        }
        return null
    }

    @Deprecated("Deprecated in Java")
    override fun canPlaceAt(state: BlockState?, world: WorldView?, pos: BlockPos?): Boolean {
        return sideCoversSmallSquare(world, pos?.down(), Direction.UP)
    }

    private fun breakPieces(world: WorldAccess?, pos: BlockPos?) {
        if (world!!.getBlockState(pos!!.up()).block == this) {
            if(world.getBlockState(pos.up()).get(TOP)) {
                world.breakBlock(pos.up(), false)
            }
        }
        if (world.getBlockState(pos.down()).block == this) {
            if (!world.getBlockState(pos.down()).get(TOP)) {
                world.breakBlock(pos.down(), false)
            }
        }
    }

    protected fun setLit(world: World, pos: BlockPos, state: BlockState, litValue: Boolean) {
        world.setBlockState(pos, state.with(LIT, litValue), NOTIFY_ALL)

        if(state.get(TOP)) world.setBlockState(pos.down(1), state.with(LIT, litValue).with(
            TOP, false))
        else world.setBlockState(pos.up(1), state.with(LIT, litValue).with(TOP, true))
    }
}