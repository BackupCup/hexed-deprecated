package net.backupcup.hexed.datagen

import net.backupcup.hexed.register.RegisterBlocks
import net.backupcup.hexed.register.RegisterItems
import net.backupcup.hexed.register.RegisterSlagBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
import java.util.concurrent.CompletableFuture

class DatagenTagsBlocks(output: FabricDataOutput?,
                  registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>?
) : FabricTagProvider.BlockTagProvider(output, registriesFuture) {
    override fun configure(arg: RegistryWrapper.WrapperLookup?) {
        getOrCreateTagBuilder(BlockTags.STAIRS)
            .add(RegisterSlagBlocks.BRIMSTONE_SLAG_STAIRS)
            .add(RegisterSlagBlocks.BRIMSTONE_BRICKS_STAIRS)
            .add(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_STAIRS)
        getOrCreateTagBuilder(BlockTags.SLABS)
            .add(RegisterSlagBlocks.BRIMSTONE_SLAG_SLAB)
            .add(RegisterSlagBlocks.BRIMSTONE_BRICKS_SLAB)
            .add(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_SLAB)
        getOrCreateTagBuilder(BlockTags.WALLS)
            .add(RegisterSlagBlocks.BRIMSTONE_SLAG_WALL)
            .add(RegisterSlagBlocks.BRIMSTONE_BRICKS_WALL)
            .add(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_WALL)

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
            .add(RegisterSlagBlocks.LAVENDIN_CINDER)
            .add(RegisterSlagBlocks.BRIMSTONE_SLAG)
            .add(RegisterSlagBlocks.BRIMSTONE_SLAG_SLAB)
            .add(RegisterSlagBlocks.BRIMSTONE_SLAG_STAIRS)
            .add(RegisterSlagBlocks.BRIMSTONE_SLAG_WALL)
            .add(RegisterSlagBlocks.BRIMSTONE_BRICKS)
            .add(RegisterSlagBlocks.BRIMSTONE_BRICKS_SLAB)
            .add(RegisterSlagBlocks.BRIMSTONE_BRICKS_STAIRS)
            .add(RegisterSlagBlocks.BRIMSTONE_BRICKS_WALL)
            .add(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG)
            .add(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_STAIRS)
            .add(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_WALL)
            .add(RegisterSlagBlocks.SMOOTH_BRIMSTONE_SLAG_SLAB)
            .add(RegisterSlagBlocks.CHISELED_BRIMSTONE_SLAG)
            .add(RegisterSlagBlocks.BRIMSTONE_SLAG_PILLAR)
            .add(RegisterBlocks.ACCURSED_ALTAR)
    }
}