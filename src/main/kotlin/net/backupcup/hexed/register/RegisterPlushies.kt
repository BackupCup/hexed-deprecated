package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.block.PlushieBlock
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity

object RegisterPlushies {
    val CALAMAIDAS_PLUSHIE: Block = PlushieBlock(
        FabricBlockSettings.create()
        .strength(1f)
        .sounds(BlockSoundGroup.WOOL)
        .mapColor(MapColor.WHITE_GRAY)
        .nonOpaque()
        .pistonBehavior(PistonBehavior.NORMAL),
        RegisterSounds.ACCURSED_ALTAR_ACTIVATE,
        "tooltip.hexed.calamaidas_plushie"
    )

    val BON_PLUSHIE: Block = PlushieBlock(
        FabricBlockSettings.create()
        .strength(1f)
        .sounds(BlockSoundGroup.WOOL)
        .mapColor(MapColor.ORANGE)
        .nonOpaque()
        .pistonBehavior(PistonBehavior.NORMAL),
        SoundEvents.BLOCK_CAMPFIRE_CRACKLE,
        "tooltip.hexed.bonfire_plushie"
    )

    val MIRI_PLUSHIE: Block = PlushieBlock(
        FabricBlockSettings.create()
        .strength(1f)
        .sounds(BlockSoundGroup.WOOL)
        .mapColor(MapColor.ORANGE)
        .nonOpaque()
        .pistonBehavior(PistonBehavior.NORMAL),
        SoundEvents.BLOCK_BEACON_ACTIVATE,
        "tooltip.hexed.miri_plushie"
    )

    val MILKY_PLUSHIE: Block = PlushieBlock(
        FabricBlockSettings.create()
        .strength(1f)
        .sounds(BlockSoundGroup.WOOL)
        .mapColor(MapColor.ORANGE)
        .nonOpaque()
        .pistonBehavior(PistonBehavior.NORMAL),
        SoundEvents.ENTITY_CAT_PURREOW,
        "tooltip.hexed.milky_plushie"
    )

    fun registerPlushies() {
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "calamaidas_plushie"), CALAMAIDAS_PLUSHIE)
        Registry.register(
            Registries.ITEM, Identifier(Hexed.MOD_ID, "calamaidas_plushie"),
            BlockItem(CALAMAIDAS_PLUSHIE, FabricItemSettings().rarity(Rarity.EPIC))
        )

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "bonfire_plushie"), BON_PLUSHIE)
        Registry.register(
            Registries.ITEM, Identifier(Hexed.MOD_ID, "bonfire_plushie"),
            BlockItem(BON_PLUSHIE, FabricItemSettings().rarity(Rarity.EPIC))
        )

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "yirmiri_plushie"), MIRI_PLUSHIE)
        Registry.register(
            Registries.ITEM, Identifier(Hexed.MOD_ID, "yirmiri_plushie"),
            BlockItem(MIRI_PLUSHIE, FabricItemSettings().rarity(Rarity.EPIC))
        )

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "milkyfur_plushie"), MILKY_PLUSHIE)
        Registry.register(
            Registries.ITEM, Identifier(Hexed.MOD_ID, "milkyfur_plushie"),
            BlockItem(MILKY_PLUSHIE, FabricItemSettings().rarity(Rarity.EPIC))
        )
    }
}