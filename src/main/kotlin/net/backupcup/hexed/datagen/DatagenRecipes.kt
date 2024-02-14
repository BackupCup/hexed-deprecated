package net.backupcup.hexed.datagen

import net.backupcup.hexed.register.RegisterArmor
import net.backupcup.hexed.register.RegisterBlocks
import net.backupcup.hexed.register.RegisterDecoCandles
import net.backupcup.hexed.register.RegisterItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import java.util.function.Consumer

class DatagenRecipes(output: FabricDataOutput?) : FabricRecipeProvider(output) {
    override fun generate(exporter: Consumer<RecipeJsonProvider>?) {
        RegisterDecoCandles.candleTypes.forEach { (_, block, candle) ->
            ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, block)
                .pattern("S").input('S', Items.STRING)
                .pattern("C").input('C', candle)
                .pattern("I").input('I', Items.COPPER_INGOT)

                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .criterion(hasItem(candle), conditionsFromItem(candle))
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))

                .offerTo(exporter)
        }

        //
        //SKULL CANDLES
        //
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, RegisterBlocks.SKELETON_SKULL_CANDLE)
            .pattern("I")
            .pattern("S")
            .input('S', Items.SKELETON_SKULL).input('I', Items.IRON_NUGGET)

            .criterion(hasItem(Items.SKELETON_SKULL), conditionsFromItem(Items.SKELETON_SKULL))
            .criterion(hasItem(Items.IRON_NUGGET), conditionsFromItem(Items.IRON_NUGGET))
            .criterion(hasItem(RegisterBlocks.SKELETON_SKULL_CANDLE), conditionsFromItem(RegisterBlocks.SKELETON_SKULL_CANDLE))

            .offerTo(exporter)

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, RegisterBlocks.WITHER_SKULL_CANDLE)
            .pattern("I")
            .pattern("S")
            .input('S', Items.WITHER_SKELETON_SKULL).input('I', Items.GOLD_NUGGET)

            .criterion(hasItem(Items.WITHER_SKELETON_SKULL), conditionsFromItem(Items.WITHER_SKELETON_SKULL))
            .criterion(hasItem(Items.GOLD_NUGGET), conditionsFromItem(Items.GOLD_NUGGET))
            .criterion(hasItem(RegisterBlocks.WITHER_SKULL_CANDLE), conditionsFromItem(RegisterBlocks.WITHER_SKULL_CANDLE))

            .offerTo(exporter)

        //
        //ALTAR
        //
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, RegisterBlocks.ACCURSED_ALTAR)
            .pattern("C C").input('C', RegisterItems.BRIMSTONE_CRYSTAL)
            .pattern("IWI").input('I', Items.GOLD_INGOT).input('W', Items.RED_WOOL)
            .pattern("BGB").input('B', Items.BLACKSTONE).input('G', Items.GILDED_BLACKSTONE)

            .criterion(hasItem(RegisterItems.BRIMSTONE_CRYSTAL), conditionsFromItem(RegisterItems.BRIMSTONE_CRYSTAL))
            .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
            .criterion(hasItem(Items.RED_WOOL), conditionsFromItem(Items.RED_WOOL))
            .criterion(hasItem(Items.BLACKSTONE), conditionsFromItem(Items.BLACKSTONE))
            .criterion(hasItem(Items.GILDED_BLACKSTONE), conditionsFromItem(Items.GILDED_BLACKSTONE))
            .criterion(hasItem(RegisterBlocks.ACCURSED_ALTAR), conditionsFromItem(RegisterBlocks.ACCURSED_ALTAR))

            .offerTo(exporter)

        //
        //ARMOR
        //
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RegisterArmor.CALAMITOUS_BOOTS)
            .pattern("F F")
            .pattern("C C")
            .input('C', RegisterItems.BRIMSTONE_CRYSTAL).input('F', RegisterItems.CALAMITOUS_FABRIC)

            .criterion(hasItem(RegisterItems.CALAMITOUS_FABRIC), conditionsFromItem(RegisterItems.CALAMITOUS_FABRIC))
            .criterion(hasItem(RegisterItems.BRIMSTONE_CRYSTAL), conditionsFromItem(RegisterItems.BRIMSTONE_CRYSTAL))
            .criterion(hasItem(RegisterArmor.CALAMITOUS_BOOTS), conditionsFromItem(RegisterArmor.CALAMITOUS_BOOTS))

            .offerTo(exporter)

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RegisterArmor.CALAMITOUS_LEGGINGS)
            .pattern("FCF")
            .pattern("F F")
            .pattern("F F")
            .input('C', RegisterItems.BRIMSTONE_CRYSTAL).input('F', RegisterItems.CALAMITOUS_FABRIC)

            .criterion(hasItem(RegisterItems.CALAMITOUS_FABRIC), conditionsFromItem(RegisterItems.CALAMITOUS_FABRIC))
            .criterion(hasItem(RegisterItems.BRIMSTONE_CRYSTAL), conditionsFromItem(RegisterItems.BRIMSTONE_CRYSTAL))
            .criterion(hasItem(RegisterArmor.CALAMITOUS_LEGGINGS), conditionsFromItem(RegisterArmor.CALAMITOUS_LEGGINGS))

            .offerTo(exporter)
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RegisterArmor.CALAMITOUS_CHESTPLATE)
            .pattern("F F")
            .pattern("FCF")
            .pattern("FFF")
            .input('C', RegisterItems.BRIMSTONE_CRYSTAL).input('F', RegisterItems.CALAMITOUS_FABRIC)

            .criterion(hasItem(RegisterItems.CALAMITOUS_FABRIC), conditionsFromItem(RegisterItems.CALAMITOUS_FABRIC))
            .criterion(hasItem(RegisterItems.BRIMSTONE_CRYSTAL), conditionsFromItem(RegisterItems.BRIMSTONE_CRYSTAL))
            .criterion(hasItem(RegisterArmor.CALAMITOUS_CHESTPLATE), conditionsFromItem(RegisterArmor.CALAMITOUS_CHESTPLATE))

            .offerTo(exporter)
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RegisterArmor.CALAMITOUS_HELMET)
            .pattern("FCF")
            .pattern("F F")
            .input('C', RegisterItems.BRIMSTONE_CRYSTAL).input('F', RegisterItems.CALAMITOUS_FABRIC)

            .criterion(hasItem(RegisterItems.CALAMITOUS_FABRIC), conditionsFromItem(RegisterItems.CALAMITOUS_FABRIC))
            .criterion(hasItem(RegisterItems.BRIMSTONE_CRYSTAL), conditionsFromItem(RegisterItems.BRIMSTONE_CRYSTAL))
            .criterion(hasItem(RegisterArmor.CALAMITOUS_HELMET), conditionsFromItem(RegisterArmor.CALAMITOUS_HELMET))

            .offerTo(exporter)
    }
}