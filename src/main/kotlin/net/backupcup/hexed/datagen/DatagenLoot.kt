package net.backupcup.hexed.datagen

import net.backupcup.hexed.register.RegisterBlocks
import net.backupcup.hexed.register.RegisterDecoCandles
import net.backupcup.hexed.register.RegisterSlagBlocks
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


        addDrop(RegisterSlagBlocks.BRIMSTONE_SLAG)
        addDrop(RegisterSlagBlocks.BRIMSTONE_SLAG_STAIRS)
        addDrop(RegisterSlagBlocks.BRIMSTONE_SLAG_SLAB)
        addDrop(RegisterSlagBlocks.BRIMSTONE_SLAG_WALL)

        addDrop(RegisterSlagBlocks.BRIMSTONE_BRICKS)
        addDrop(RegisterSlagBlocks.BRIMSTONE_BRICKS_STAIRS)
        addDrop(RegisterSlagBlocks.BRIMSTONE_BRICKS_SLAB)
        addDrop(RegisterSlagBlocks.BRIMSTONE_BRICKS_WALL)

        addDrop(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG)
        addDrop(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_STAIRS)
        addDrop(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_SLAB)
        addDrop(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_WALL)

    }
}