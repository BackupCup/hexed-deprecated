package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.block.TallCandle
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

object RegisterDecoCandles {
    data class Candle(val color: String, val block: Block, val originalCandle: Item)

    val CANDLE: Block = createTallCandle(MapColor.PALE_YELLOW)
    val WHITE_CANDLE: Block = createTallCandle(MapColor.WHITE)
    val LIGHT_GRAY_CANDLE: Block = createTallCandle(MapColor.LIGHT_GRAY)
    val GRAY_CANDLE: Block = createTallCandle(MapColor.GRAY)
    val BLACK_CANDLE: Block = createTallCandle(MapColor.BLACK)
    val BROWN_CANDLE: Block = createTallCandle(MapColor.BROWN)
    val RED_CANDLE: Block = createTallCandle(MapColor.RED)
    val ORANGE_CANDLE: Block = createTallCandle(MapColor.ORANGE)
    val YELLOW_CANDLE: Block = createTallCandle(MapColor.YELLOW)
    val LIME_CANDLE: Block = createTallCandle(MapColor.LIME)
    val GREEN_CANDLE: Block = createTallCandle(MapColor.GREEN)
    val CYAN_CANDLE: Block = createTallCandle(MapColor.CYAN)
    val LIGHT_BLUE_CANDLE: Block = createTallCandle(MapColor.LIGHT_BLUE)
    val BLUE_CANDLE: Block = createTallCandle(MapColor.BLUE)
    val PURPLE_CANDLE: Block = createTallCandle(MapColor.PURPLE)
    val MAGENTA_CANDLE: Block = createTallCandle(MapColor.MAGENTA)
    val PINK_CANDLE: Block = createTallCandle(MapColor.PINK)

    val candleTypes = listOf(
        Candle("tall_candle", CANDLE, Items.CANDLE),
        Candle("tall_white_candle", WHITE_CANDLE, Items.WHITE_CANDLE),
        Candle("tall_light_gray_candle", LIGHT_GRAY_CANDLE, Items.LIGHT_GRAY_CANDLE),
        Candle("tall_gray_candle", GRAY_CANDLE, Items.GRAY_CANDLE),
        Candle("tall_black_candle", BLACK_CANDLE, Items.BLACK_CANDLE),
        Candle("tall_brown_candle", BROWN_CANDLE, Items.BROWN_CANDLE),
        Candle("tall_red_candle", RED_CANDLE, Items.RED_CANDLE),
        Candle("tall_orange_candle", ORANGE_CANDLE, Items.ORANGE_CANDLE),
        Candle("tall_yellow_candle", YELLOW_CANDLE, Items.YELLOW_CANDLE),
        Candle("tall_lime_candle", LIME_CANDLE, Items.LIME_CANDLE),
        Candle("tall_green_candle", GREEN_CANDLE, Items.GREEN_CANDLE),
        Candle("tall_cyan_candle", CYAN_CANDLE, Items.CYAN_CANDLE),
        Candle("tall_light_blue_candle", LIGHT_BLUE_CANDLE, Items.LIGHT_BLUE_CANDLE),
        Candle("tall_blue_candle", BLUE_CANDLE, Items.BLUE_CANDLE),
        Candle("tall_purple_candle", PURPLE_CANDLE, Items.PURPLE_CANDLE),
        Candle("tall_magenta_candle", MAGENTA_CANDLE, Items.MAGENTA_CANDLE),
        Candle("tall_pink_candle", PINK_CANDLE, Items.PINK_CANDLE)
    )

    fun createTallCandle(color: MapColor): TallCandle {
        return TallCandle(
            FabricBlockSettings.create()
            .strength(.1f)
            .sounds(BlockSoundGroup.CANDLE)
            .mapColor(color)
            .nonOpaque()
            .luminance(TallCandle.STATE_TO_LUMINANCE)
            .pistonBehavior(PistonBehavior.DESTROY)
        )
    }

    fun registerDecoCandles() {
        candleTypes.forEach { (color, block, _) ->
            Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, color), block)
            Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, color), BlockItem(block, FabricItemSettings()))
        }
    }
}