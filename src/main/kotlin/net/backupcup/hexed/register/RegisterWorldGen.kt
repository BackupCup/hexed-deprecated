package net.backupcup.hexed.register

import com.terraformersmc.biolith.api.biome.BiomePlacement
import net.minecraft.world.biome.BiomeKeys
import net.minecraft.world.biome.source.util.MultiNoiseUtil


object RegisterWorldGen {
    fun registerWorldGen() {
        BiomePlacement.addNether(BiomeKeys.END_HIGHLANDS,
            MultiNoiseUtil.NoiseHypercube(
                MultiNoiseUtil.ParameterRange.of(0.25f, 0.75f),
                MultiNoiseUtil.ParameterRange.of(-0.75f, -0.25f),
                MultiNoiseUtil.ParameterRange.of(0.5f, 1.0f),
                MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f),
                MultiNoiseUtil.ParameterRange.of(0.0f),
                MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f),
                0L))
    }
}