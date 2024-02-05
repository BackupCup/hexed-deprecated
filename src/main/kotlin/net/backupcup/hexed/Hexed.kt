package net.backupcup.hexed

import net.backupcup.hexed.register.*
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Hexed : ModInitializer {
	val MOD_ID: String = "hexed"
    private val logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		RegisterItems.registerItems()
		RegisterArmor.registerArmor()
		RegisterScreenHandlers.registerScreenHandlers()
		RegisterBlocks.registerBlocks()
		RegisterBlockEntities.registerBlockEntities()
		RegisterDecoCandles.registerDecoCandles()
		RegisterSounds.registerSounds()
		RegisterStatusEffects.registerStatusEffects()
		RegisterItemGroup.registerItemGroup()
	}
}