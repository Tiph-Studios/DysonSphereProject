package com.tiph.dysonsphere.common.items;

import com.tiph.dysonsphere.ExampleMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public abstract class DysonItem extends Item {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExampleMod.MODID);

    public DysonItem(final Properties p) {
        super(p);
    }

    public static void register(final IEventBus eventBus) {
        //todo this is braindead
        SolarGeneratorItem.init();
        ITEMS.register(eventBus);
    }
}
