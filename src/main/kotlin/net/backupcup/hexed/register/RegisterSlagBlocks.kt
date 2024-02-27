package net.backupcup.hexed.register

import net.backupcup.hexed.Hexed
import net.backupcup.hexed.block.BrimstoneSlagBlock
import net.backupcup.hexed.block.BrimstoneSlagPillar
import net.backupcup.hexed.block.LavendinCinderBlock
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.GrassBlock
import net.minecraft.block.MapColor
import net.minecraft.block.SlabBlock
import net.minecraft.block.StairsBlock
import net.minecraft.block.WallBlock
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

object RegisterSlagBlocks {

    val CHISELED_BRIMSTONE_SLAG: Block = Block(
        FabricBlockSettings.create()
            .strength(3.5f)
            .sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.DARK_DULL_PINK)
            .pistonBehavior(PistonBehavior.NORMAL)
            .requiresTool())

    val BRIMSTONE_SLAG_PILLAR: Block = BrimstoneSlagPillar(
        FabricBlockSettings.create()
            .strength(3.5f)
            .sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.DARK_DULL_PINK)
            .nonOpaque()
            .pistonBehavior(PistonBehavior.NORMAL)
            .requiresTool())


    val LAVENDIN_CINDER: Block = LavendinCinderBlock(
        FabricBlockSettings.create()
        .strength(3.5f)
        .sounds(BlockSoundGroup.STONE)
        .mapColor(MapColor.PINK)
        .pistonBehavior(PistonBehavior.NORMAL)
        .requiresTool()
    )

    val BRIMSTONE_SLAG: Block = BrimstoneSlagBlock(
        FabricBlockSettings.create()
        .strength(3.5f)
        .sounds(BlockSoundGroup.STONE)
        .mapColor(MapColor.DARK_DULL_PINK)
        .pistonBehavior(PistonBehavior.NORMAL)
        .requiresTool())

    //Stair, Slab, Wall
    val BRIMSTONE_SLAG_STAIRS: Block = StairsBlock(BRIMSTONE_SLAG.defaultState,
        FabricBlockSettings.create()
            .strength(3.5f)
            .sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.DARK_DULL_PINK)
            .pistonBehavior(PistonBehavior.NORMAL)
            .requiresTool())
    val BRIMSTONE_SLAG_SLAB: Block = SlabBlock(
        FabricBlockSettings.create()
            .strength(3.5f)
            .sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.DARK_DULL_PINK)
            .pistonBehavior(PistonBehavior.NORMAL)
            .requiresTool())
    val BRIMSTONE_SLAG_WALL: Block = WallBlock(
        FabricBlockSettings.create()
            .strength(3.5f)
            .sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.DARK_DULL_PINK)
            .pistonBehavior(PistonBehavior.NORMAL)
            .requiresTool())


    val BRIMSTONE_BRICKS: Block = Block(
        FabricBlockSettings.create()
        .strength(3.5f)
        .sounds(BlockSoundGroup.STONE)
        .mapColor(MapColor.DARK_DULL_PINK)
        .pistonBehavior(PistonBehavior.NORMAL)
            .requiresTool())

    //Stair, Slab, Wall
    val BRIMSTONE_BRICKS_STAIRS: Block = StairsBlock(BRIMSTONE_BRICKS.defaultState,
        FabricBlockSettings.create()
            .strength(3.5f)
            .sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.DARK_DULL_PINK)
            .pistonBehavior(PistonBehavior.NORMAL)
            .requiresTool())
    val BRIMSTONE_BRICKS_SLAB: Block = SlabBlock(
        FabricBlockSettings.create()
            .strength(3.5f)
            .sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.DARK_DULL_PINK)
            .pistonBehavior(PistonBehavior.NORMAL)
            .requiresTool())
    val BRIMSTONE_BRICKS_WALL: Block = WallBlock(
        FabricBlockSettings.create()
            .strength(3.5f)
            .sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.DARK_DULL_PINK)
            .pistonBehavior(PistonBehavior.NORMAL)
            .requiresTool())


    val SMOOTH_BRIMSTONE_SLAG: Block = Block(
        FabricBlockSettings.create()
        .strength(3.5f)
        .sounds(BlockSoundGroup.STONE)
        .mapColor(MapColor.DARK_DULL_PINK)
        .pistonBehavior(PistonBehavior.NORMAL)
        .requiresTool())

    //Stair, Slab, Wall
    val SMOOTH_BRIMSTONE_SLAG_STAIRS: Block = StairsBlock(SMOOTH_BRIMSTONE_SLAG.defaultState,
        FabricBlockSettings.create()
            .strength(3.5f)
            .sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.DARK_DULL_PINK)
            .pistonBehavior(PistonBehavior.NORMAL)
            .requiresTool())
    val SMOOTH_BRIMSTONE_SLAG_SLAB: Block = SlabBlock(
        FabricBlockSettings.create()
            .strength(3.5f)
            .sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.DARK_DULL_PINK)
            .pistonBehavior(PistonBehavior.NORMAL)
            .requiresTool())
    val SMOOTH_BRIMSTONE_SLAG_WALL: Block = WallBlock(
        FabricBlockSettings.create()
            .strength(3.5f)
            .sounds(BlockSoundGroup.STONE)
            .mapColor(MapColor.DARK_DULL_PINK)
            .pistonBehavior(PistonBehavior.NORMAL)
            .requiresTool())

    fun registerSlagBlocks() {
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "brimstone_slag_pillar"), BRIMSTONE_SLAG_PILLAR)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_slag_pillar"), BlockItem(BRIMSTONE_SLAG_PILLAR, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "chiseled_brimstone_slag"), CHISELED_BRIMSTONE_SLAG)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "chiseled_brimstone_slag"), BlockItem(CHISELED_BRIMSTONE_SLAG, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "lavendin_cinder"), LAVENDIN_CINDER)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "lavendin_cinder"), BlockItem(LAVENDIN_CINDER, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "brimstone_slag"), BRIMSTONE_SLAG)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_slag"), BlockItem(BRIMSTONE_SLAG, FabricItemSettings()))
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "brimstone_slag_stairs"), BRIMSTONE_SLAG_STAIRS)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_slag_stairs"), BlockItem(BRIMSTONE_SLAG_STAIRS, FabricItemSettings()))
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "brimstone_slag_slab"), BRIMSTONE_SLAG_SLAB)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_slag_slab"), BlockItem(BRIMSTONE_SLAG_SLAB, FabricItemSettings()))
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "brimstone_slag_wall"), BRIMSTONE_SLAG_WALL)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_slag_wall"), BlockItem(BRIMSTONE_SLAG_WALL, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "brimstone_bricks"), BRIMSTONE_BRICKS)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_bricks"), BlockItem(BRIMSTONE_BRICKS, FabricItemSettings()))
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "brimstone_bricks_stairs"), BRIMSTONE_BRICKS_STAIRS)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_bricks_stairs"), BlockItem(BRIMSTONE_BRICKS_STAIRS, FabricItemSettings()))
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "brimstone_bricks_slab"), BRIMSTONE_BRICKS_SLAB)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_bricks_slab"), BlockItem(BRIMSTONE_BRICKS_SLAB, FabricItemSettings()))
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "brimstone_bricks_wall"), BRIMSTONE_BRICKS_WALL)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "brimstone_bricks_wall"), BlockItem(BRIMSTONE_BRICKS_WALL, FabricItemSettings()))

        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "smooth_brimstone_slag"), SMOOTH_BRIMSTONE_SLAG)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "smooth_brimstone_slag"), BlockItem(SMOOTH_BRIMSTONE_SLAG, FabricItemSettings()))
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "smooth_brimstone_slag_stairs"), SMOOTH_BRIMSTONE_SLAG_STAIRS)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "smooth_brimstone_slag_stairs"), BlockItem(SMOOTH_BRIMSTONE_SLAG_STAIRS, FabricItemSettings()))
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "smooth_brimstone_slag_slab"), SMOOTH_BRIMSTONE_SLAG_SLAB)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "smooth_brimstone_slag_slab"), BlockItem(SMOOTH_BRIMSTONE_SLAG_SLAB, FabricItemSettings()))
        Registry.register(Registries.BLOCK, Identifier(Hexed.MOD_ID, "smooth_brimstone_slag_wall"), SMOOTH_BRIMSTONE_SLAG_WALL)
        Registry.register(Registries.ITEM, Identifier(Hexed.MOD_ID, "smooth_brimstone_slag_wall"), BlockItem(SMOOTH_BRIMSTONE_SLAG_WALL, FabricItemSettings()))

    }
}