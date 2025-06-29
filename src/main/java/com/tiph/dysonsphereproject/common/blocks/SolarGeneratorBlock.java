package com.tiph.dysonsphereproject.common.blocks;

import com.mojang.serialization.MapCodec;
import com.tiph.dysonsphereproject.common.blocks.entities.SolarGeneratorBlockEntity;
import com.tiph.dysonsphereproject.common.init.DysonBlockEntities;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SolarGeneratorBlock extends BaseEntityBlock {
  private static final String REGISTRY_SUFFIX = "solar_generator";
  private static final float DESTROY_TIME = 2.0f;
  private static final MapCodec<SolarGeneratorBlock> CODEC = simpleCodec(SolarGeneratorBlock::new);

  private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16.0, 8.0, 16.0);

  private final int powerGen;

  private SolarGeneratorBlock(Properties p) {
    this(p, 0);
  }

  public SolarGeneratorBlock(Properties p, final int powerGen) {
    super(p);
    this.powerGen = powerGen;
  }

  public static String getRegistrySuffix() {
    return REGISTRY_SUFFIX;
  }

  public static float getDestroyTime() {
    return DESTROY_TIME;
  }

  @Override
  public void appendHoverText(
      @NotNull ItemStack itemStack,
      @Nullable BlockGetter blockGetter,
      List<Component> components,
      @NotNull TooltipFlag tooltipFlag) {
    components.add(
        Component.translatable("tooltip.dysonspherecraft.solar_generator.tooltip", this.powerGen));
    super.appendHoverText(itemStack, blockGetter, components, tooltipFlag);
  }

  @Override
  public @NotNull VoxelShape getShape(
      @NotNull BlockState blockState,
      @NotNull BlockGetter blockGetter,
      @NotNull BlockPos blockPos,
      @NotNull CollisionContext collisionContext) {
    return SHAPE;
  }

  @Override
  public RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.MODEL;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new SolarGeneratorBlockEntity(pos, state);
  }

  @Override
  public @NotNull InteractionResult use(
      @NotNull BlockState blockState,
      Level level,
      @NotNull BlockPos blockPos,
      @NotNull Player player,
      @NotNull InteractionHand interactionHand,
      @NotNull BlockHitResult blockHitResult) {

    //    if (!level.isClientSide) {
    //      final BlockEntity entity = level.getBlockEntity(blockPos);
    //      if (entity instanceof final SolarGeneratorBlockEntity solarGenerator) {
    //        NetworkHooks.openScreen((ServerPlayer) player, solarGenerator, blockPos);
    //      } else {
    //        // Wtf is this
    //        throw new IllegalStateException("Our (container? energy?) provider is missing!");
    //      }
    //    }

    return InteractionResult.sidedSuccess(level.isClientSide);
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
        DysonBlockEntities.SOLAR_GENERATOR_ENTITY.get(),
        (level1, pos, blockState1, blockEntity) -> blockEntity.tick(level1, pos, blockState1));
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }
}
