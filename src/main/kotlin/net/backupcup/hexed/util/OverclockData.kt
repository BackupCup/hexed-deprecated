package net.backupcup.hexed.util

interface OverclockInterface {
    fun getOverclockData(): OverclockData
    fun setOverclockData(value: OverclockData)
}

data class OverclockData(
    var cooldown: Int,
    var overheat: Int
)