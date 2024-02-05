package net.backupcup.hexed.altar

import net.backupcup.hexed.block.BrimstoneCandle
import net.backupcup.hexed.register.RegisterBlockEntities
import net.backupcup.hexed.register.RegisterBlocks
import net.backupcup.hexed.register.RegisterSounds
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.DustParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.sound.SoundCategory
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

    private var isActive = if(world?.getBlockState(pos)?.block == RegisterBlocks.ACCURSED_ALTAR) {
        world?.getBlockState(this.pos)?.get(AccursedAltar.ACTIVE)
    } else {
        false
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity?): ScreenHandler {
        return AccursedAltarScreenHandler(syncId, playerInventory)
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
        nbt?.putBoolean("active", isActive!!)
    }

    override fun toInitialChunkDataNbt(): NbtCompound {
        return createNbt()
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? {
        return BlockEntityUpdateS2CPacket.create(this)
    }

    companion object {
        private val CANDLE_OFFSETS = listOf(
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

        fun clientTick(world: World?, pos: BlockPos?, state: BlockState?, blockEntity: AccursedAltarBlockEntity?) {
            if (!state!!.get(AccursedAltar.ACTIVE)) {
                if (state.get(AccursedAltar.FACING) == Direction.NORTH || state.get(AccursedAltar.FACING) == Direction.SOUTH) {
                    CANDLE_OFFSETS[0].forEach {particlePos ->
                        blockEntity?.checkCandles(blockEntity, particlePos, world!!, pos!!)
                    }
                } else {
                    CANDLE_OFFSETS[1].forEach {particlePos ->
                        blockEntity?.checkCandles(blockEntity, particlePos, world!!, pos!!)
                    }
                }
            } else {
                world?.addParticle(
                    DustParticleEffect.DEFAULT,
                    pos!!.x + 0.5,
                    pos.y + 1.0,
                    pos.z + 0.5,
                    Random.nextDouble(-.025, .025), Random.nextDouble(-.025, .025), Random.nextDouble(-.025, .025)
                )
            }
        }

        fun tick(world: World?, pos: BlockPos?, state: BlockState?, blockEntity: AccursedAltarBlockEntity?) {
            if (!state!!.get(AccursedAltar.ACTIVE)) {
                if (state.get(AccursedAltar.FACING) == Direction.NORTH || state.get(AccursedAltar.FACING) == Direction.SOUTH) {
                    CANDLE_OFFSETS[0].forEach {particlePos ->
                        if (!blockEntity!!.checkPosForCandle(world, pos, particlePos)) return
                        if (!blockEntity.checkPosForLitCandle(world, pos, particlePos)) return
                    }
                    world?.setBlockState(pos, state.with(AccursedAltar.ACTIVE, true))
                } else {
                    CANDLE_OFFSETS[1].forEach {particlePos ->
                        if (!blockEntity!!.checkPosForCandle(world, pos, particlePos)) return
                        if (!blockEntity.checkPosForLitCandle(world, pos, particlePos)) return
                    }
                    world?.setBlockState(pos, state.with(AccursedAltar.ACTIVE, true))
                }
                world?.playSound(
                    null,
                    pos,
                    RegisterSounds.ACCURSED_ALTAR_ACTIVATE,
                    SoundCategory.BLOCKS,
                    0.25f,
                    1f
                )
                world?.updateListeners(pos, blockEntity?.cachedState, state, Block.NOTIFY_LISTENERS)
            }
        }
    }

    private fun checkPosForCandle(world: World?, pos: BlockPos?, offset: BlockPos): Boolean {
        return world?.getBlockState(
            BlockPos(pos!!.x + offset.x, pos.y + offset.y, pos.z + offset.z)
        )?.block == RegisterBlocks.BRIMSTONE_CANDLE
    }

    private fun checkPosForLitCandle(world: World?, pos: BlockPos?, offset: BlockPos): Boolean {
        return world?.getBlockState(
            BlockPos(pos!!.x + offset.x, pos.y + offset.y, pos.z + offset.z)
        )!!.get(BrimstoneCandle.LIT)
    }

    fun checkCandles(blockEntity: AccursedAltarBlockEntity, particlePos: BlockPos, world: World, pos: BlockPos) {
        if (!blockEntity.checkPosForCandle(world, pos, particlePos)) {
            world.addParticle(
                DustParticleEffect.DEFAULT,
                pos.x + particlePos.x.toDouble() + 0.5,
                pos.y + particlePos.y.toDouble() + 0.5,
                pos.z + particlePos.z.toDouble() + 0.5,
                Random.nextDouble(-.025, .025), Random.nextDouble(-.025, .025), Random.nextDouble(-.025, .025)
            )
        } else {
            if(!blockEntity.checkPosForLitCandle(world, pos, particlePos)) {
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