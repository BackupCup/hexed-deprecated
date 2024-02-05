package net.backupcup.hexed.datagen

import net.backupcup.hexed.register.RegisterDecoCandles
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider

class DecorationCandleLoot(dataOutput: FabricDataOutput?) : FabricBlockLootTableProvider(dataOutput){
    override fun generate() {
        RegisterDecoCandles.candleTypes.forEach { (_, block, _) ->
            addDrop(block)
        }
    }
}