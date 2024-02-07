package net.backupcup.hexed.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockView
import net.minecraft.world.World


class LichloreCandle(
    settings: Settings?,
    override val particleExtinguish: ParticleEffect = ParticleTypes.SNEEZE
) : AbstractTallCandle(settings) {

    companion object {
        val LIT: BooleanProperty = AbstractTallCandle.LIT
        val TOP: BooleanProperty = AbstractTallCandle.TOP
        val STATE_TO_LUMINANCE = AbstractTallCandle.STATE_TO_LUMINANCE
    }

    init {
        defaultState = defaultState
            .with(LIT, false)
            .with(TOP, false)
    }

    override fun appendTooltip(
        stack: ItemStack?,
        world: BlockView?,
        tooltip: MutableList<Text>?,
        options: TooltipContext?
    ) {
        tooltip?.add(Text.translatable("tooltip.hexed.lichlore_candle.line_1")
            .formatted(Formatting.GREEN).formatted(Formatting.ITALIC).formatted(Formatting.BOLD))
        tooltip?.add(Text.translatable("tooltip.hexed.lichlore_candle.line_2")
            .formatted(Formatting.GREEN).formatted(Formatting.ITALIC).formatted(Formatting.BOLD))
        tooltip?.add(Text.translatable("tooltip.hexed.lichlore_candle.line_3")
            .formatted(Formatting.GRAY))
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(LIT, TOP)
    }

    override fun randomDisplayTick(state: BlockState?, world: World?, pos: BlockPos?, random: Random?) {
        val randomFloat = random!!.nextFloat()
        if (state!!.get(LIT) && state.get(TOP)) {
            if(randomFloat < .15f) {
                world!!.addParticle(ParticleTypes.SNEEZE,
                    pos!!.x + .5, pos.y + 1.0, pos.z + .5,
                    0.0, 0.0, 0.0)
                if(randomFloat < .07f) {
                    world.playSound(
                        pos.x + .5, pos.y + 0.85, pos.z + .5,
                        SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS,
                        random.nextFloat() + 1f, random.nextFloat() * .7f + .3f, false)
                }
            }
            world!!.addParticle(ParticleTypes.GLOW,
                pos!!.x + .5, pos.y + 1.0, pos.z + .5,
                0.0, 0.0, 0.0)
        }
    }
}