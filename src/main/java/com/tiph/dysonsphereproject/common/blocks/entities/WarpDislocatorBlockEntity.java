package com.tiph.dysonsphereproject.common.blocks.entities;

import com.tiph.dysonsphereproject.common.init.DysonBlockEntities;
import com.tiph.dysonsphereproject.common.init.DysonItems;
import com.tiph.dysonsphereproject.common.items.BasicItems;
import com.tiph.dysonsphereproject.screen.WarpDislocatorMenu;
import com.tiph.dysonsphereproject.util.OrbitalCollectorSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WarpDislocatorBlockEntity extends DysonEnergyBlockEntity implements MenuProvider {

  //
  // ENERGY STORAGE
  //
  private static final int MAX_CAPACITY = 100_000;
  private static final int MAX_RECEIVE = 500;
  private static final int FIRING_ENERGY_COST = 50_000;

  //
  // ITEM STORAGE
  //
  private final ItemStackHandler itemHandler = new WarpDislocatorItemStackHandler(1);
  private static final int INPUT_SLOT = 0;

  // Time in ticks it takes to fire an orbital collector
  private static final int DISLOCATION_TIME = 60;

  private int dislocationProgress = 0;

  public WarpDislocatorBlockEntity(BlockPos pos, BlockState state) {
    super(DysonBlockEntities.WARP_DISLOCATOR_ENTITY.get(), pos, state);
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    itemHandler.serialize(output);
    output.putInt("dislocationProgress", dislocationProgress);
    super.saveAdditional(output);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    itemHandler.deserialize(input);
    dislocationProgress = input.getInt("dislocationProgress").orElseThrow();
    super.loadAdditional(input);
  }

  @Override
  public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
    final SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
    for (int i = 0; i < itemHandler.getSlots(); i++) {
      inventory.setItem(i, itemHandler.getStackInSlot(i));
    }
    Containers.dropContents(this.level, this.worldPosition, inventory);
    super.preRemoveSideEffects(pos, state);
  }

  public void tick(final Level level, final BlockPos pos, final BlockState blockState) {

    if (!hasOrbitalCollector() || !hasRequiredEnergy()) {
      dislocationProgress = 0;
      return;
    }

    if (dislocationProgress >= DISLOCATION_TIME) {
      fireCollector(level);
      setChanged(level, pos, blockState);
      dislocationProgress = 0;
    } else {

      if (dislocationProgress == 0) {
        // Start animation/sound
      }

      dislocationProgress++;
      // todo - not sure how animations and sounds work
      //  but maybe we need to continue running them here
    }
  }

  private boolean hasOrbitalCollector() {
    return itemHandler.getStackInSlot(INPUT_SLOT).getItem()
        == DysonItems.getBasicItem(BasicItems.ORBITAL_COLLECTOR).get();
  }

  private boolean hasRequiredEnergy() {
    return this.getEnergyStored() >= FIRING_ENERGY_COST;
  }

  private void fireCollector(final Level level) {
    if (level.isClientSide) {
      return;
    }

    // Register orbital collector in the DIMENSION
    // CURRENTLY ORBITAL COLLECTORS CAN BE SENT IN ANY DIMENSION
    // In the future we may want to save this to be by player/team
    saveCollector(level);

    // Remove the orbital collector from the block
    itemHandler.extractItem(INPUT_SLOT, 1, false);
  }

  void saveCollector(final Level level) {
    if (level instanceof ServerLevel serverLevel) {
      final OrbitalCollectorSavedData data =
          OrbitalCollectorSavedData.getOrbitalCollectorData(serverLevel);
      data.addCollector();
    }
  }

  @Override
  public @NotNull Component getDisplayName() {
    return Component.translatable("block.dysonsphereproject.warp_dislocator");
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(
      int i, @NotNull Inventory inventory, @NotNull Player player) {
    return new WarpDislocatorMenu(
        i,
        inventory,
        this,
        new ContainerData() {
          @Override
          public int get(int i) {
            return i;
          }

          @Override
          public void set(int p_39285_, int p_39286_) {}

          @Override
          public int getCount() {
            return 1;
          }
        });
  }

  //
  // OVERRIDE METHODS - ENERGY STORAGE
  //

  @Override
  public int getMaxEnergyStored() {
    return MAX_CAPACITY;
  }

  @Override
  int getMaxExtract() {
    // Should not have energy extracted.
    return 0;
  }

  @Override
  int getMaxReceive() {
    return MAX_RECEIVE;
  }

  @Override
  public boolean canExtract() {
    return false;
  }

  @Override
  public boolean canReceive() {
    return true;
  }

  //
  // CUSTOM ITEM STACK HANDLER
  //

  private static class WarpDislocatorItemStackHandler extends ItemStackHandler {

    public WarpDislocatorItemStackHandler(int size) {
      super(size);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
      return stack.getItem() == DysonItems.getBasicItem(BasicItems.ORBITAL_COLLECTOR).get();
    }
  }

}
