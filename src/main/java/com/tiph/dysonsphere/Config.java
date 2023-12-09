package com.tiph.dysonsphere;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your
// config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

  private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
  public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION =
      BUILDER
          .comment("What you want the introduction message to be for the magic number")
          .define("magicNumberIntroduction", "The magic number is... ");
  static final ModConfigSpec SPEC = BUILDER.build();
  private static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK =
      BUILDER.comment("Whether to log the dirt block on common setup").define("logDirtBlock", true);
  private static final ModConfigSpec.IntValue MAGIC_NUMBER =
      BUILDER.comment("A magic number").defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);
  // a list of strings that are treated as resource locations for items
  private static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS =
      BUILDER
          .comment("A list of items to log on common setup.")
          .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);
  private static boolean logDirtBlock;
  private static int magicNumber;
  private static String magicNumberIntroduction;
  private static Set<Item> items;

  private Config() {
    // Do not instantiate
    throw new UnsupportedOperationException("Do not instantiate");
  }

  private static boolean validateItemName(final Object obj) {
    return obj instanceof final String itemName
        && BuiltInRegistries.ITEM.containsKey(new ResourceLocation(itemName));
  }

  @SubscribeEvent
  static void onLoad(final ModConfigEvent event) {
    logDirtBlock = LOG_DIRT_BLOCK.get();
    magicNumber = MAGIC_NUMBER.get();
    magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

    // convert the list of strings into a set of items
    items =
        ITEM_STRINGS.get().stream()
            .map(itemName -> BuiltInRegistries.ITEM.get(new ResourceLocation(itemName)))
            .collect(Collectors.toSet());
  }

  public static boolean isLogDirtBlock() {
    return logDirtBlock;
  }

  public static int getMagicNumber() {
    return magicNumber;
  }

  public static String getMagicNumberIntroduction() {
    return magicNumberIntroduction;
  }

  public static Set<Item> getItems() {
    return items;
  }
}
