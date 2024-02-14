package net.backupcup.hexed.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.Direction

class SkeletonSkullCandle(settings: Settings?, override val fireParticle: ParticleEffect = ParticleTypes.SMALL_FLAME) : AbstractSkullCandle(settings) {
    companion object {
        val LIT: BooleanProperty = AbstractSkullCandle.LIT
        val HANGING: BooleanProperty = AbstractSkullCandle.HANGING
        val CANDLE: IntProperty = AbstractSkullCandle.CANDLE
        val FACING: DirectionProperty = AbstractSkullCandle.FACING

        val STATE_TO_LUMINANCE = AbstractSkullCandle.STATE_TO_LUMINANCE
    }

    init {
        defaultState = defaultState
            .with(LIT, false)
            .with(HANGING, false)
            .with(CANDLE, 0)
            .with(FACING, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(LIT, HANGING, CANDLE, FACING)
    }
}