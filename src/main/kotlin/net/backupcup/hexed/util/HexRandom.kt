package net.backupcup.hexed.util

import net.minecraft.util.math.random.Random
import kotlin.math.ceil

object HexRandom {
    val minecraftRandom = Random.create()

    fun nextBoolean(): Boolean {
        return minecraftRandom.nextBoolean()
    }

    fun nextInt(): Int {
        return minecraftRandom.nextInt()
    }

    fun nextInt(max: Int): Int {
        return ceil(max * minecraftRandom.nextFloat()).toInt()
    }

    fun nextInt(min: Int, max: Int): Int {
        return ceil(min + (max - min) * minecraftRandom.nextFloat()).toInt()
    }

    fun nextDouble(): Double {
        return minecraftRandom.nextDouble()
    }

    fun nextDouble(max: Double): Double {
        return max * minecraftRandom.nextDouble()
    }

    fun nextDouble(min: Double, max: Double): Double {
        return min + (max - min) * minecraftRandom.nextDouble()
    }

    fun nextFloat(): Float {
        return minecraftRandom.nextFloat()
    }

    fun nextFloat(max: Float): Float {
        return max * minecraftRandom.nextFloat()
    }

    fun nextFloat(min: Float, max: Float): Float {
        return min + (max - min) * minecraftRandom.nextFloat()
    }
}