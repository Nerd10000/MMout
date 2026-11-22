/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public abstract class Biomes {
/* 11 */   public static final ResourceKey<Biome> THE_VOID = register("the_void");
/*    */   
/* 13 */   public static final ResourceKey<Biome> PLAINS = register("plains");
/*    */   
/* 14 */   public static final ResourceKey<Biome> SUNFLOWER_PLAINS = register("sunflower_plains");
/*    */   
/* 16 */   public static final ResourceKey<Biome> SNOWY_PLAINS = register("snowy_plains");
/*    */   
/* 17 */   public static final ResourceKey<Biome> ICE_SPIKES = register("ice_spikes");
/*    */   
/* 19 */   public static final ResourceKey<Biome> DESERT = register("desert");
/*    */   
/* 21 */   public static final ResourceKey<Biome> SWAMP = register("swamp");
/*    */   
/* 22 */   public static final ResourceKey<Biome> MANGROVE_SWAMP = register("mangrove_swamp");
/*    */   
/* 24 */   public static final ResourceKey<Biome> FOREST = register("forest");
/*    */   
/* 25 */   public static final ResourceKey<Biome> FLOWER_FOREST = register("flower_forest");
/*    */   
/* 26 */   public static final ResourceKey<Biome> BIRCH_FOREST = register("birch_forest");
/*    */   
/* 28 */   public static final ResourceKey<Biome> DARK_FOREST = register("dark_forest");
/*    */   
/* 29 */   public static final ResourceKey<Biome> PALE_GARDEN = register("pale_garden");
/*    */   
/* 30 */   public static final ResourceKey<Biome> OLD_GROWTH_BIRCH_FOREST = register("old_growth_birch_forest");
/*    */   
/* 31 */   public static final ResourceKey<Biome> OLD_GROWTH_PINE_TAIGA = register("old_growth_pine_taiga");
/*    */   
/* 32 */   public static final ResourceKey<Biome> OLD_GROWTH_SPRUCE_TAIGA = register("old_growth_spruce_taiga");
/*    */   
/* 34 */   public static final ResourceKey<Biome> TAIGA = register("taiga");
/*    */   
/* 35 */   public static final ResourceKey<Biome> SNOWY_TAIGA = register("snowy_taiga");
/*    */   
/* 37 */   public static final ResourceKey<Biome> SAVANNA = register("savanna");
/*    */   
/* 38 */   public static final ResourceKey<Biome> SAVANNA_PLATEAU = register("savanna_plateau");
/*    */   
/* 40 */   public static final ResourceKey<Biome> WINDSWEPT_HILLS = register("windswept_hills");
/*    */   
/* 41 */   public static final ResourceKey<Biome> WINDSWEPT_GRAVELLY_HILLS = register("windswept_gravelly_hills");
/*    */   
/* 42 */   public static final ResourceKey<Biome> WINDSWEPT_FOREST = register("windswept_forest");
/*    */   
/* 43 */   public static final ResourceKey<Biome> WINDSWEPT_SAVANNA = register("windswept_savanna");
/*    */   
/* 45 */   public static final ResourceKey<Biome> JUNGLE = register("jungle");
/*    */   
/* 46 */   public static final ResourceKey<Biome> SPARSE_JUNGLE = register("sparse_jungle");
/*    */   
/* 47 */   public static final ResourceKey<Biome> BAMBOO_JUNGLE = register("bamboo_jungle");
/*    */   
/* 49 */   public static final ResourceKey<Biome> BADLANDS = register("badlands");
/*    */   
/* 50 */   public static final ResourceKey<Biome> ERODED_BADLANDS = register("eroded_badlands");
/*    */   
/* 51 */   public static final ResourceKey<Biome> WOODED_BADLANDS = register("wooded_badlands");
/*    */   
/* 53 */   public static final ResourceKey<Biome> MEADOW = register("meadow");
/*    */   
/* 54 */   public static final ResourceKey<Biome> CHERRY_GROVE = register("cherry_grove");
/*    */   
/* 55 */   public static final ResourceKey<Biome> GROVE = register("grove");
/*    */   
/* 57 */   public static final ResourceKey<Biome> SNOWY_SLOPES = register("snowy_slopes");
/*    */   
/* 58 */   public static final ResourceKey<Biome> FROZEN_PEAKS = register("frozen_peaks");
/*    */   
/* 59 */   public static final ResourceKey<Biome> JAGGED_PEAKS = register("jagged_peaks");
/*    */   
/* 60 */   public static final ResourceKey<Biome> STONY_PEAKS = register("stony_peaks");
/*    */   
/* 62 */   public static final ResourceKey<Biome> RIVER = register("river");
/*    */   
/* 63 */   public static final ResourceKey<Biome> FROZEN_RIVER = register("frozen_river");
/*    */   
/* 65 */   public static final ResourceKey<Biome> BEACH = register("beach");
/*    */   
/* 66 */   public static final ResourceKey<Biome> SNOWY_BEACH = register("snowy_beach");
/*    */   
/* 67 */   public static final ResourceKey<Biome> STONY_SHORE = register("stony_shore");
/*    */   
/* 69 */   public static final ResourceKey<Biome> WARM_OCEAN = register("warm_ocean");
/*    */   
/* 70 */   public static final ResourceKey<Biome> LUKEWARM_OCEAN = register("lukewarm_ocean");
/*    */   
/* 71 */   public static final ResourceKey<Biome> DEEP_LUKEWARM_OCEAN = register("deep_lukewarm_ocean");
/*    */   
/* 72 */   public static final ResourceKey<Biome> OCEAN = register("ocean");
/*    */   
/* 73 */   public static final ResourceKey<Biome> DEEP_OCEAN = register("deep_ocean");
/*    */   
/* 74 */   public static final ResourceKey<Biome> COLD_OCEAN = register("cold_ocean");
/*    */   
/* 75 */   public static final ResourceKey<Biome> DEEP_COLD_OCEAN = register("deep_cold_ocean");
/*    */   
/* 76 */   public static final ResourceKey<Biome> FROZEN_OCEAN = register("frozen_ocean");
/*    */   
/* 77 */   public static final ResourceKey<Biome> DEEP_FROZEN_OCEAN = register("deep_frozen_ocean");
/*    */   
/* 79 */   public static final ResourceKey<Biome> MUSHROOM_FIELDS = register("mushroom_fields");
/*    */   
/* 81 */   public static final ResourceKey<Biome> DRIPSTONE_CAVES = register("dripstone_caves");
/*    */   
/* 82 */   public static final ResourceKey<Biome> LUSH_CAVES = register("lush_caves");
/*    */   
/* 83 */   public static final ResourceKey<Biome> DEEP_DARK = register("deep_dark");
/*    */   
/* 84 */   public static final ResourceKey<Biome> SULFUR_CAVES = register("sulfur_caves");
/*    */   
/* 86 */   public static final ResourceKey<Biome> NETHER_WASTES = register("nether_wastes");
/*    */   
/* 87 */   public static final ResourceKey<Biome> WARPED_FOREST = register("warped_forest");
/*    */   
/* 88 */   public static final ResourceKey<Biome> CRIMSON_FOREST = register("crimson_forest");
/*    */   
/* 89 */   public static final ResourceKey<Biome> SOUL_SAND_VALLEY = register("soul_sand_valley");
/*    */   
/* 90 */   public static final ResourceKey<Biome> BASALT_DELTAS = register("basalt_deltas");
/*    */   
/* 92 */   public static final ResourceKey<Biome> THE_END = register("the_end");
/*    */   
/* 93 */   public static final ResourceKey<Biome> END_HIGHLANDS = register("end_highlands");
/*    */   
/* 94 */   public static final ResourceKey<Biome> END_MIDLANDS = register("end_midlands");
/*    */   
/* 95 */   public static final ResourceKey<Biome> SMALL_END_ISLANDS = register("small_end_islands");
/*    */   
/* 96 */   public static final ResourceKey<Biome> END_BARRENS = register("end_barrens");
/*    */   
/*    */   private static ResourceKey<Biome> register(String name) {
/* 99 */     return ResourceKey.create(Registries.BIOME, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Biomes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */