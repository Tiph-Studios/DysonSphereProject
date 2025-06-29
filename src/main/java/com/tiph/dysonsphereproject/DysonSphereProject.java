package com.tiph.dysonsphereproject;

import com.mojang.logging.LogUtils;
import com.tiph.dysonsphereproject.common.init.DysonBlockEntities;
import com.tiph.dysonsphereproject.common.init.DysonBlocks;
import com.tiph.dysonsphereproject.common.init.DysonCreativeModeTab;
import com.tiph.dysonsphereproject.common.init.DysonItems;
import com.tiph.dysonsphereproject.screen.DysonMenuTypes;
import com.tiph.dysonsphereproject.screen.WarpDislocatorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DysonSphereProject.MODID)
public class DysonSphereProject {
  // Define mod id in a common place for everything to reference
  public static final String MODID = "dysonsphereproject";

  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  // The constructor for the mod class is the first code that is run when your mod is loaded.
  // FML will recognize some parameter types like IEventBus or ModContainer and pass them in
  // automatically.
  public DysonSphereProject(IEventBus modEventBus, ModContainer modContainer) {
    // Register the commonSetup method for modloading
    modEventBus.addListener(this::commonSetup);

    // Load blocks and items in the creative tabs
    DysonCreativeModeTab.register(modEventBus);

    // Register the Deferred Register to the mod event bus so blocks get registered
    DysonBlocks.register(modEventBus);
    // Register the Deferred Register to the mod event bus so items get registered
    DysonItems.register(modEventBus);
    // Register the Deferred Register to the mod event bus so block entities get registered
    DysonBlockEntities.register(modEventBus);

    DysonMenuTypes.register(modEventBus);
    // Register ourselves for server and other game events we are interested in
    NeoForge.EVENT_BUS.register(this);

    // Register our mod's ModConfigSpec so that FML can create and load the config file for us
    modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
  }

  private void commonSetup(final FMLCommonSetupEvent event) {
    // Some common setup code
    LOGGER.info("HELLO FROM COMMON SETUP");

    final String REALLY_BIG_RABBIT = """

    (\\(\\
    (-.-)
    o_(")(")
    """;

    LOGGER.info("REALLY BIG RABBIT: ");
    LOGGER.info(REALLY_BIG_RABBIT);

    if (Config.logDirtBlock)
      LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

    LOGGER.info("{}{}", Config.magicNumberIntroduction, Config.magicNumber);

    Config.items.forEach(item -> LOGGER.info("ITEM >> {}", item));
  }

  // You can use SubscribeEvent and let the Event Bus discover methods to call
  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
    // Do something when the server starts
    LOGGER.info("HELLO from server starting");
  }

  // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
  @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
  public static class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
      // Some client setup code
      LOGGER.info("HELLO FROM CLIENT SETUP");
      LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
      //todo
      MenuScreens.register(DysonMenuTypes.WARP_DISLOCATOR_MENU.get(), WarpDislocatorScreen::new);
    }
  }
}
