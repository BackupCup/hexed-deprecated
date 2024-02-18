package net.backupcup.hexed.altar

import net.backupcup.hexed.block.AbstractTallCandle
import net.backupcup.hexed.block.BrimstoneCandle
import net.backupcup.hexed.register.RegisterBlockEntities
import net.backupcup.hexed.register.RegisterBlocks
import net.backupcup.hexed.register.RegisterSounds
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.particle.DustParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.random.Random

class AccursedAltarBlockEntity(
    pos: BlockPos?, state:
    BlockState?
) : BlockEntity(
    RegisterBlockEntities.ACCURSED_ALTAR_BLOCK_ENTITY,
    pos,
    state
), NamedScreenHandlerFactory {

    private var isActive =
        if(this.world?.getBlockState(this.pos)?.block == RegisterBlocks.ACCURSED_ALTAR)
            { world?.getBlockState(this.pos)?.get(AccursedAltar.ACTIVE) }
        else
            { false }

    fun getActiveState(): Boolean? {
        markDirty()
        return this.world?.getBlockState(this.pos)?.get(AccursedAltar.ACTIVE)
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler {
        return AccursedAltarScreenHandler(syncId, playerInventory, player, ScreenHandlerContext.EMPTY, this)
    }

    override fun getDisplayName(): Text {
        return Text.translatable(cachedState.block.translationKey)
    }

    override fun readNbt(nbt: NbtCompound?) {
        super.readNbt(nbt)
        isActive = nbt?.getBoolean("active")
    }

    override fun writeNbt(nbt: NbtCompound?) {
        super.writeNbt(nbt)
        nbt?.putBoolean("active", isActive == true)
    }

    override fun toInitialChunkDataNbt(): NbtCompound {
        return createNbt()
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? {
        return BlockEntityUpdateS2CPacket.create(this)
    }

    companion object {
        val CANDLE_OFFSETS = listOf(
            listOf(
                BlockPos(-2, 1, -3), BlockPos(-2, 1, 3),
                BlockPos(2, 1, -3), BlockPos(2, 1, 3),
                BlockPos(3, 2, 0), BlockPos(-3, 2, 0)
            ),
            listOf(
                BlockPos(-3, 1, -2), BlockPos(-3, 1, 2),
                BlockPos(3, 1, -2), BlockPos(3, 1, 2),
                BlockPos(0, 2, 3), BlockPos(0, 2, -3)
            )
        )

        fun clientTick(world: World, pos: BlockPos, state: BlockState, blockEntity: AccursedAltarBlockEntity) {
            if (!state.get(AccursedAltar.ACTIVE)) {
                if (state.get(AccursedAltar.FACING).axis == Direction.Axis.Z) {
                    CANDLE_OFFSETS[0].forEach {particlePos ->
                        blockEntity.clientCheckCandles(blockEntity, particlePos, world, pos)
                    }
                } else {
                    CANDLE_OFFSETS[1].forEach {particlePos ->
                        blockEntity.clientCheckCandles(blockEntity, particlePos, world, pos)
                    }
                }
                if (world.time % 20L == 0L) {
                    world.addParticle(
                        ParticleTypes.ANGRY_VILLAGER,
                        pos.x + 0.5,
                        pos.y + 1.0,
                        pos.z + 0.5,
                        Random.nextDouble(-.025, .025), Random.nextDouble(-.025, .025), Random.nextDouble(-.025, .025)
                    )}
            } else {
                world.addParticle(
                    DustParticleEffect.DEFAULT,
                    pos.x + 0.5,
                    pos.y + 1.0,
                    pos.z + 0.5,
                    Random.nextDouble(-.025, .025), Random.nextDouble(-.025, .025), Random.nextDouble(-.025, .025)
                )
            }
        }

        fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: AccursedAltarBlockEntity) {
            if (world.time % 20L == 0L) {
                if (!state.get(AccursedAltar.ACTIVE)) {
                    if (state.get(AccursedAltar.FACING).axis == Direction.Axis.Z) {
                        if (blockEntity.serverCheckCandles(CANDLE_OFFSETS[0], world, pos, blockEntity) == 6)
                            blockEntity.altarStateSet(world, pos, state, true)
                    } else {
                        if (blockEntity.serverCheckCandles(CANDLE_OFFSETS[1], world, pos, blockEntity) == 6)
                            blockEntity.altarStateSet(world, pos, state, true)
                    }
                } else {
                    if (state.get(AccursedAltar.FACING).axis == Direction.Axis.Z) {
                        if (blockEntity.serverCheckCandles(CANDLE_OFFSETS[0], world, pos, blockEntity) < 6)
                            blockEntity.altarStateSet(world, pos, state, false)
                    } else {
                        if (blockEntity.serverCheckCandles(CANDLE_OFFSETS[1], world, pos, blockEntity) < 6)
                            blockEntity.altarStateSet(world, pos, state, false)
                    }
                }
            }
        }
    }

    private fun posGetCandle(world: World, pos: BlockPos, offset: BlockPos): Boolean {
        return world.getBlockState(
            BlockPos(pos.x + offset.x, pos.y + offset.y, pos.z + offset.z)
        ).block == RegisterBlocks.BRIMSTONE_CANDLE
    }

    private fun posGetCandleLit(world: World, pos: BlockPos, offset: BlockPos): Boolean {
        return world.getBlockState(
            BlockPos(pos.x + offset.x, pos.y + offset.y, pos.z + offset.z)
        ).get(BrimstoneCandle.LIT)
    }

    private fun altarStateSet(world: World, pos: BlockPos, state: BlockState, newState: Boolean) {
        if (newState) {
            world.playSound(
                null, pos,
                RegisterSounds.ACCURSED_ALTAR_ACTIVATE, SoundCategory.BLOCKS,
                0.25f, 1f) }
        else {
            world.playSound(
                null, pos,
                SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS,
                0.25f, 1f)}
        world.setBlockState(pos, state.with(AccursedAltar.ACTIVE, newState))
    }

    fun changeCandleState(newState: Boolean) {
        val blockPos: BlockPos = this.pos
        val world: World? = this.world
        val offsetList: List<BlockPos> =
            if (world?.getBlockState(blockPos)?.get(AccursedAltar.FACING)?.axis == Direction.Axis.Z)
                CANDLE_OFFSETS[0]
            else
                CANDLE_OFFSETS[1]

        offsetList.forEach { offset ->
            val candlePos = BlockPos(blockPos.x + offset.x, blockPos.y + offset.y, blockPos.z + offset.z)
            if (world?.getBlockState(candlePos)?.isOf(RegisterBlocks.BRIMSTONE_CANDLE) == true) {
                if (world.getBlockState(candlePos).get(BrimstoneCandle.LIT)) {
                    AbstractTallCandle.setLit(world, candlePos, world.getBlockState(candlePos), newState)
                    world.playSound(
                        null, candlePos,
                        SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS,
                        0.125f, 1f)
                }
            }
        }
    }

    private fun serverCheckCandles(offsetList: List<BlockPos>, world: World, pos: BlockPos, blockEntity: AccursedAltarBlockEntity): Int {
        var candlesMatch = 0

        for (candleOffset in offsetList) {
            if (!blockEntity.posGetCandle(world, pos, candleOffset)) continue
            if (!blockEntity.posGetCandleLit(world, pos, candleOffset)) continue
            candlesMatch++
        }
        return candlesMatch
    }

    fun clientCheckCandles(blockEntity: AccursedAltarBlockEntity, particlePos: BlockPos, world: World, pos: BlockPos) {
        if (!blockEntity.posGetCandle(world, pos, particlePos)) {
            world.addParticle(
                DustParticleEffect.DEFAULT,
                pos.x + particlePos.x.toDouble() + 0.5,
                pos.y + particlePos.y.toDouble() + 0.5,
                pos.z + particlePos.z.toDouble() + 0.5,
                Random.nextDouble(-.025, .025), Random.nextDouble(-.025, .025), Random.nextDouble(-.025, .025)
            )
        } else {
            if(!blockEntity.posGetCandleLit(world, pos, particlePos)) {
                world.addParticle(
                    DustParticleEffect.DEFAULT,
                    pos.x + particlePos.x.toDouble() + 0.5,
                    pos.y + particlePos.y.toDouble() + 2.0,
                    pos.z + particlePos.z.toDouble() + 0.5,
                    Random.nextDouble(-.025, .025), Random.nextDouble(-.025, .025), Random.nextDouble(-.025, .025)
                )
            }
        }
    }
}