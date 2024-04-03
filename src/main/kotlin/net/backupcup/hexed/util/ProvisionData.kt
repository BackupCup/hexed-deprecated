package net.backupcup.hexed.util

interface ProvisionInterface {
    fun getProvisionData(): ProvisionData
    fun setProvisionData(data: ProvisionData)
}

data class ProvisionData(
    var isUIActive: Boolean,
    var indicatorPos: Int,
    var reloadSpeed: Int
)