package com.tiph.dysonsphereproject.common.blocks;

import com.tiph.dysonsphereproject.common.api.IResource;

public enum BasicBlocks implements IResource {
  EXAMPLE_BASIC_BLOCK("example_basic_block", 2.0f);

  private final String registrySuffix;
  private final float destroyTime;

  BasicBlocks(String registrySuffix, float destroyTime) {
    this.registrySuffix = registrySuffix;
    this.destroyTime = destroyTime;
  }

  @Override
  public String getRegistrySuffix() {
    return this.registrySuffix;
  }

  public float getDestroyTime() {
    return this.destroyTime;
  }
}
