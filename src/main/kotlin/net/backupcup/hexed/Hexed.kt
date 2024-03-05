package net.backupcup.hexed

import net.backupcup.hexed.loot.ModifyLootTables
import net.backupcup.hexed.register.*
import net.fabricmc.api.ModInitializer

object Hexed : ModInitializer {
	const val MOD_ID: String = "hexed"

	override fun onInitialize() {
		RegisterItems.registerItems()
		RegisterArmor.registerArmor()
		RegisterScreenHandlers.registerScreenHandlers()
		RegisterBlocks.registerBlocks()
		RegisterBlockEntities.registerBlockEntities()
		RegisterDecoCandles.registerDecoCandles()
		RegisterSounds.registerSounds()
		RegisterStatusEffects.registerStatusEffects()
		RegisterEnchantments.registerHexes()
		RegisterTags.registerTags()
		RegisterWorldGen.registerWorldGen()
		RegisterItemGroupMain.registerItemGroup()
		RegisterItemGroupDeco.registerItemGroup()

		ModifyLootTables.registerLootModifiers()
	}
}