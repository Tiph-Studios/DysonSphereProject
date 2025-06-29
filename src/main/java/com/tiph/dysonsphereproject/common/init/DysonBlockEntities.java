package com.tiph.dysonsphereproject.common.init;

import com.tiph.dysonsphereproject.DysonSphereProject;
import com.tiph.dysonsphereproject.common.blocks.GroundStationBlock;
import com.tiph.dysonsphereproject.common.blocks.SolarGeneratorBlock;
import com.tiph.dysonsphereproject.common.blocks.WarpDislocatorBlock;
import com.tiph.dysonsphereproject.common.blocks.entities.GroundStationBlockEntity;
import com.tiph.dysonsphereproject.common.blocks.entities.SolarGeneratorBlockEntity;
import com.tiph.dysonsphereproject.common.blocks.entities.WarpDislocatorBlockEntity;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DysonBlockEntities {

  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
      DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, DysonSphereProject.MODID);

  private DysonBlockEntities() {
    throw new IllegalStateException("Do not instantiate.");
  }

  public static void register(final IEventBus eventBus) {
    BLOCK_ENTITIES.register(eventBus);
  }

  public static final Supplier<BlockEntityType<SolarGeneratorBlockEntity>> SOLAR_GENERATOR_ENTITY =
      BLOCK_ENTITIES.register(
          SolarGeneratorBlock.getRegistrySuffix(),
          () ->
              new BlockEntityType<>(
                  SolarGeneratorBlockEntity::new, DysonBlocks.SOLAR_GENERATOR.get()));

  public static final Supplier<BlockEntityType<WarpDislocatorBlockEntity>> WARP_DISLOCATOR_ENTITY =
      BLOCK_ENTITIES.register(
          WarpDislocatorBlock.getRegistrySuffix(),
          () ->
              new BlockEntityType<>(
                  WarpDislocatorBlockEntity::new, DysonBlocks.WARP_DISLOCATOR.get()));

  public static final Supplier<BlockEntityType<GroundStationBlockEntity>> GROUND_STATION_ENTITY =
      BLOCK_ENTITIES.register(
          GroundStationBlock.getRegistrySuffix(),
          () ->
              new BlockEntityType<>(
                  GroundStationBlockEntity::new, DysonBlocks.GROUND_STATION.get()));
}
