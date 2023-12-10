package com.tiph.dysonsphereproject.common.items;

import com.tiph.dysonsphereproject.DysonSphereProject;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public abstract class DysonItem extends Item {

  public static final DeferredRegister.Items ITEMS =
      DeferredRegister.createItems(DysonSphereProject.MODID);

  public static final DeferredItem<Item> MIRROR_ITEM =
      ITEMS.registerSimpleItem("mirror", new Item.Properties());

  public DysonItem(final Properties p) {
    super(p);
  }

  public static void register(final IEventBus eventBus) {
    // todo this is braindead
    ExampleItemSubClass.init();
    ITEMS.register(eventBus);
  }
}
