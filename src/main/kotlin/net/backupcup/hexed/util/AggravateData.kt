package net.backupcup.hexed.util

import net.minecraft.item.ItemStack
import net.minecraft.util.Hand

interface AggravateInterface {
    fun getAggravateData(): AggravateData
    fun setAggravateData(data: AggravateData)
}

data class AggravateData(var bowHand: Hand, var chargeAmount: Int, var chargedTicks: Long)