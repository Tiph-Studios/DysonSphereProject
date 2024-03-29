package com.tiph.dysonsphereproject.datagen.loot;

import com.tiph.dysonsphereproject.common.init.DysonBlocks;
import java.util.Collections;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class DysonBlockLootTables extends BlockLootSubProvider {

  public DysonBlockLootTables() {
    super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
  }

  @Override
  protected void generate() {
    this.dropSelf(DysonBlocks.SOLAR_GENERATOR.get());
    this.dropSelf(DysonBlocks.WARP_DISLOCATOR.get());
    this.dropSelf(DysonBlocks.GROUND_STATION.get());

    for (final DeferredBlock<Block> block : DysonBlocks.getBasicBlocks()) {
      this.dropSelf(block.get());
    }
  }

  @Override
  protected @NotNull Iterable<Block> getKnownBlocks() {
    return DysonBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).map(Block.class::cast)
        ::iterator;
  }
}
