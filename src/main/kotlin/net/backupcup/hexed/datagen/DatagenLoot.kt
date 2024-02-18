package net.backupcup.hexed.datagen

import net.backupcup.hexed.register.RegisterBlocks
import net.backupcup.hexed.register.RegisterDecoCandles
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider

class DatagenLoot(dataOutput: FabricDataOutput?) : FabricBlockLootTableProvider(dataOutput){
    override fun generate() {
        RegisterDecoCandles.candleTypes.forEach { (_, block, _) ->
            addDrop(block)
        }

        addDrop(RegisterBlocks.CALAMAIDAS_PLUSHIE)
        addDrop(RegisterBlocks.BRIMSTONE_CANDLE)
        addDrop(RegisterBlocks.LICHLORE_CANDLE)
        addDrop(RegisterBlocks.ACCURSED_ALTAR)
        addDrop(RegisterBlocks.WITHER_SKULL_CANDLE)
        addDrop(RegisterBlocks.SKELETON_SKULL_CANDLE)
    }
}