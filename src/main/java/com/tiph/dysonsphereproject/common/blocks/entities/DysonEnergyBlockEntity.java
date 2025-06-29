package com.tiph.dysonsphereproject.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;

public abstract class DysonEnergyBlockEntity extends DysonBlockEntity implements IEnergyStorage {

  private int energy;

  protected DysonEnergyBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
    super(entityType, pos, state);
    energy = 0;
  }

  @Override
  public int receiveEnergy(int receiveAmount, boolean simulate) {
    if (!this.canReceive() || receiveAmount <= 0) {
      return 0;
    } else {
      int energyReceived =
          Math.min(
              this.getMaxEnergyStored() - this.energy,
              Math.min(this.getMaxReceive(), receiveAmount));
      if (!simulate) {
        this.energy += energyReceived;
      }

      return energyReceived;
    }
  }

  @Override
  public int extractEnergy(int extractAmount, boolean simulate) {
    if (!this.canExtract()) {
      return 0;
    } else {
      int energyExtracted = Math.min(this.energy, Math.min(this.getMaxExtract(), extractAmount));
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
  protected void saveAdditional(ValueOutput output) {
    output.putInt("energy", energy);
    super.saveAdditional(output);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    this.energy = input.getInt("energy").orElseThrow();
    super.loadAdditional(input);
  }

  abstract int getMaxExtract();

  abstract int getMaxReceive();
}
