package com.tiph.dysonsphereproject.common.blocks;

import com.mojang.serialization.MapCodec;
import com.tiph.dysonsphereproject.common.blocks.entities.WarpDislocatorBlockEntity;
import com.tiph.dysonsphereproject.common.init.DysonBlockEntities;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class WarpDislocatorBlock extends BaseEntityBlock {
  private static final String REGISTRY_SUFFIX = "warp_dislocator";
  private static final float DESTROY_TIME = 2.0f;
  private static final MapCodec<WarpDislocatorBlock> CODEC = simpleCodec(WarpDislocatorBlock::new);


  public WarpDislocatorBlock(Properties p) {
    super(p);
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  public static String getRegistrySuffix() {
    return REGISTRY_SUFFIX;
  }

  public static float getDestroyTime() {
    return DESTROY_TIME;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new WarpDislocatorBlockEntity(pos, state);
  }

  @Override
  public @NotNull InteractionResult use(
      @NotNull BlockState blockState,
      Level level,
      @NotNull BlockPos blockPos,
      @NotNull Player player,
      @NotNull InteractionHand interactionHand,
      @NotNull BlockHitResult blockHitResult) {
    if (!level.isClientSide()) {
      BlockEntity entity = level.getBlockEntity(blockPos);
      if (entity instanceof WarpDislocatorBlockEntity warpEntity) {
        NetworkHooks.openScreen(((ServerPlayer) player), warpEntity, blockPos);
      } else {
        throw new IllegalStateException("Our Container provider is missing!");
      }
    }

    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  @Override
  public RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.MODEL;
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      @NotNull Level level,
      @NotNull BlockState blockState,
      @NotNull BlockEntityType<T> blockEntityType) {

    if (level.isClientSide) {
      return null;
    }

    return createTickerHelper(
        blockEntityType,
        DysonBlockEntities.WARP_DISLOCATOR_ENTITY.get(),
        (level1, pos, blockState1, blockEntity) -> blockEntity.tick(level1, pos, blockState1));
  }

}
