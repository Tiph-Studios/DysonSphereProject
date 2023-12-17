package com.tiph.dysonsphereproject.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;

public abstract class DysonEnergyBlockEntity extends DysonBlockEntity implements IEnergyStorage {

  protected int energy;

  protected DysonEnergyBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
    super(entityType, pos, state);
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    if (!this.canReceive()) {
      return 0;
    } else {
      int energyReceived =
          Math.min(
              this.getMaxEnergyStored() - this.energy, Math.min(this.getMaxReceive(), maxReceive));
      if (!simulate) {
        this.energy += energyReceived;
      }

      return energyReceived;
    }
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    if (!this.canExtract()) {
      return 0;
    } else {
      int energyExtracted = Math.min(this.energy, Math.min(this.getMaxExtract(), maxExtract));
      if (!simulate) {
        this.energy -= energyExtracted;
      }

      return energyExtracted;
    }
  }

  @Override
  public int getEnergyStored() {
    return energy;
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    tag.putInt("energy", energy);
    super.saveAdditional(tag);
  }

  @Override
  public void load(CompoundTag tag) {
    this.energy = tag.getInt("energy");
    super.load(tag);
  }

  abstract int getMaxExtract();

  abstract int getMaxReceive();
}
