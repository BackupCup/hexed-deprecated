package net.backupcup.hexed

import net.backupcup.hexed.enchantments.AbstractHex
import net.backupcup.hexed.loot.ModifyLootTables
import net.backupcup.hexed.register.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Hexed : ModInitializer {
	const val MOD_ID: String = "hexed"

	val LOGGER: Logger? = LoggerFactory.getLogger(MOD_ID)

	val SYNC_CONFIG_PACKET = Identifier.of(MOD_ID, "sync_config")
	private var config: Config? = null

	fun getConfig(): Config? {
		return config
	}

	fun setConfig(config: Config) {
		Hexed.config = config
	}

	override fun onInitialize() {
		//Config Sync
		ResourceManagerHelper.get(ResourceType.SERVER_DATA)
			.registerReloadListener(object : SimpleSynchronousResourceReloadListener {
				override fun getFabricId(): Identifier {
					return Identifier.of(MOD_ID, "config")!!
				}

				override fun reload(manager: ResourceManager) {
					config = Config.load()
				}
			})

		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(ServerLifecycleEvents.SyncDataPackContents { player: ServerPlayerEntity, _: Boolean ->
			val buf = PacketByteBufs.create()
			config?.writeToClient(buf)
			ServerPlayNetworking.send(player, SYNC_CONFIG_PACKET, buf)
			if (Config.lastError != null) {
				player.sendMessage(
					Text.literal("[${MOD_ID.uppercase()}]: ")
						.append(Config.lastError).formatted(Formatting.RED)
				)
			}
		})

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
		//RegisterWorldGen.registerWorldGen()
		registerAllGroups()
		RegisterStats.registerStats()
		RegisterEntities.registerEntities()

		ModifyLootTables.registerLootModifiers()
	}
}