package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.block.PlushieBlock
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.loot.v2.LootTableEvents
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.BlockItem
import net.minecraft.loot.LootPool
import net.minecraft.loot.condition.RandomChanceLootCondition
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity

object RegisterPlushies {
    private fun createPlushie(descriptionLang: String?, soundEvent: SoundEvent?, mapColor: MapColor): PlushieBlock {
        return PlushieBlock(
            FabricBlockSettings.create()
                .strength(1f)
                .sounds(BlockSoundGroup.WOOL)
                .mapColor(mapColor)
                .nonOpaque()
                .pistonBehavior(PistonBehavior.NORMAL),
            soundEvent,
            descriptionLang
        )
    }

    private fun createSimplePlushie(mapColor: MapColor): PlushieBlock {
        return PlushieBlock(
            FabricBlockSettings.create()
                .strength(1f)
                .sounds(BlockSoundGroup.WOOL)
                .mapColor(mapColor)
                .nonOpaque()
                .pistonBehavior(PistonBehavior.NORMAL),
            null,
            null
        )
    }

    private fun registerPlushie(plushie: Block, id: String, rarity: Rarity) {
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, id), plushie)
        Registry.register(
            Registries.ITEM, Identifier(Hexed.MOD_ID, id),
            BlockItem(plushie, FabricItemSettings().rarity(rarity))
        )
    }

    //heheheha
    val CALAMAIDAS_PLUSHIE: Block = createPlushie("tooltip.hexed.calamaidas_plushie", RegisterSounds.ACCURSED_ALTAR_ACTIVATE, MapColor.WHITE_GRAY)

    //thumbsup people
    val BON_PLUSHIE: Block = createPlushie("tooltip.hexed.bonfire_plushie", SoundEvents.BLOCK_CAMPFIRE_CRACKLE, MapColor.ORANGE)
    val MIRI_PLUSHIE: Block = createPlushie("tooltip.hexed.miri_plushie", RegisterSounds.TECH_LAUGH, MapColor.GOLD)
    val SMILLY_PLUSHIE: Block = createPlushie("tooltip.hexed.smilly_plushie", null, MapColor.BLACK)

    fun registerPlushies() {
        val chestChance = 0.3125f

        val specialPlushieList: List<Block> = listOf(CALAMAIDAS_PLUSHIE, MIRI_PLUSHIE, BON_PLUSHIE, SMILLY_PLUSHIE)

        registerPlushie(CALAMAIDAS_PLUSHIE, "calamaidas_plushie", Rarity.EPIC)

        registerPlushie(BON_PLUSHIE, "bonfire_plushie", Rarity.EPIC)
        registerPlushie(MIRI_PLUSHIE, "yirmiri_plushie", Rarity.EPIC)
        registerPlushie(SMILLY_PLUSHIE, "smilly_plushie", Rarity.EPIC)

        registerPlushie(createSimplePlushie(MapColor.GREEN), "chronos_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.BRIGHT_RED), "maggie_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.LIME), "zarra_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.LIGHT_BLUE), "wagon_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.WHITE), "josh_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.DULL_PINK), "solace_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.PURPLE), "nightfall_solace_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.BLACK), "phoenix_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.DIAMOND_BLUE), "tiger_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.BLUE), "eternal_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.PINK), "tollish_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.TERRACOTTA_BROWN), "crafter_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.BLACK), "den_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.BROWN), "fzzy_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.BLACK), "kami_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.DULL_PINK), "gobby_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.YELLOW), "silver_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.BROWN), "bread_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.PINK), "assistance_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.GREEN), "monk_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.BLACK), "tenno_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.BLACK), "toboe_plushie", Rarity.UNCOMMON)
        registerPlushie(createSimplePlushie(MapColor.BLACK), "reader_plushie", Rarity.UNCOMMON)

        LootTableEvents.MODIFY.register{ _, _, id, tableBuilder, _->
            if (id.path.startsWith("chests")) {
                val poolBuilder = LootPool.builder()

                Registries.BLOCK.filterIsInstance<PlushieBlock>().forEach { plushie ->
                    poolBuilder
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .conditionally(RandomChanceLootCondition.builder(
                            if (specialPlushieList.contains(plushie)) chestChance else chestChance * 8
                        ))
                        .with(ItemEntry.builder(plushie))
                }

                tableBuilder.pool(poolBuilder)
            }
        }
    }
}