package net.backupcup.hexed

import net.backupcup.hexed.datagen.*
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object HexedDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		val pack: FabricDataGenerator.Pack = fabricDataGenerator.createPack()

		//pack.addProvider(::DatagenRecipes)
		pack.addProvider(::DatagenLoot)
		pack.addProvider(::DatagenTags)
		pack.addProvider(::DatagenModels)
		//pack.addProvider(::DatagenBiomes)
	}
}