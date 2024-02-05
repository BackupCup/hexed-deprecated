package net.backupcup.hexed.datagen

import net.backupcup.hexed.register.RegisterDecoCandles
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import java.util.function.Consumer

class DecorationCandleRecipes(output: FabricDataOutput?) : FabricRecipeProvider(output) {
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
    }
}