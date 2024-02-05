package net.backupcup.hexed

import net.backupcup.hexed.datagen.DecorationCandleLoot
import net.backupcup.hexed.datagen.DecorationCandleRecipes
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object HexedDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		val pack: FabricDataGenerator.Pack = fabricDataGenerator.createPack()

		pack.addProvider(::DecorationCandleRecipes)
		pack.addProvider(::DecorationCandleLoot)
	}
}