/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.TreeFeatures;
/*     */ import net.minecraft.data.worldgen.features.VegetationFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.valueproviders.ClampedInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.NoiseBasedCountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
/*     */ 
/*     */ public class VegetationPlacements {
/*  40 */   public static final ResourceKey<PlacedFeature> BAMBOO_LIGHT = PlacementUtils.createKey("bamboo_light");
/*     */   
/*  41 */   public static final ResourceKey<PlacedFeature> BAMBOO = PlacementUtils.createKey("bamboo");
/*     */   
/*  42 */   public static final ResourceKey<PlacedFeature> VINES = PlacementUtils.createKey("vines");
/*     */   
/*  44 */   public static final ResourceKey<PlacedFeature> PATCH_SUNFLOWER = PlacementUtils.createKey("patch_sunflower");
/*     */   
/*  45 */   public static final ResourceKey<PlacedFeature> PATCH_PUMPKIN = PlacementUtils.createKey("patch_pumpkin");
/*     */   
/*  47 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_PLAIN = PlacementUtils.createKey("patch_grass_plain");
/*     */   
/*  48 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_MEADOW = PlacementUtils.createKey("patch_grass_meadow");
/*     */   
/*  49 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_FOREST = PlacementUtils.createKey("patch_grass_forest");
/*     */   
/*  50 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_BADLANDS = PlacementUtils.createKey("patch_grass_badlands");
/*     */   
/*  51 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_SAVANNA = PlacementUtils.createKey("patch_grass_savanna");
/*     */   
/*  52 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_NORMAL = PlacementUtils.createKey("patch_grass_normal");
/*     */   
/*  53 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_TAIGA_2 = PlacementUtils.createKey("patch_grass_taiga_2");
/*     */   
/*  54 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_TAIGA = PlacementUtils.createKey("patch_grass_taiga");
/*     */   
/*  55 */   public static final ResourceKey<PlacedFeature> PATCH_GRASS_JUNGLE = PlacementUtils.createKey("patch_grass_jungle");
/*     */   
/*  57 */   public static final ResourceKey<PlacedFeature> GRASS_BONEMEAL = PlacementUtils.createKey("grass_bonemeal");
/*     */   
/*  59 */   public static final ResourceKey<PlacedFeature> PATCH_DEAD_BUSH_2 = PlacementUtils.createKey("patch_dead_bush_2");
/*     */   
/*  60 */   public static final ResourceKey<PlacedFeature> PATCH_DEAD_BUSH = PlacementUtils.createKey("patch_dead_bush");
/*     */   
/*  61 */   public static final ResourceKey<PlacedFeature> PATCH_DEAD_BUSH_BADLANDS = PlacementUtils.createKey("patch_dead_bush_badlands");
/*     */   
/*  62 */   public static final ResourceKey<PlacedFeature> PATCH_DRY_GRASS_BADLANDS = PlacementUtils.createKey("patch_dry_grass_badlands");
/*     */   
/*  63 */   public static final ResourceKey<PlacedFeature> PATCH_DRY_GRASS_DESERT = PlacementUtils.createKey("patch_dry_grass_desert");
/*     */   
/*  65 */   public static final ResourceKey<PlacedFeature> PATCH_MELON = PlacementUtils.createKey("patch_melon");
/*     */   
/*  67 */   public static final ResourceKey<PlacedFeature> PATCH_MELON_SPARSE = PlacementUtils.createKey("patch_melon_sparse");
/*     */   
/*  69 */   public static final ResourceKey<PlacedFeature> PATCH_BERRY_COMMON = PlacementUtils.createKey("patch_berry_common");
/*     */   
/*  70 */   public static final ResourceKey<PlacedFeature> PATCH_BERRY_RARE = PlacementUtils.createKey("patch_berry_rare");
/*     */   
/*  72 */   public static final ResourceKey<PlacedFeature> PATCH_WATERLILY = PlacementUtils.createKey("patch_waterlily");
/*     */   
/*  74 */   public static final ResourceKey<PlacedFeature> PATCH_TALL_GRASS_2 = PlacementUtils.createKey("patch_tall_grass_2");
/*     */   
/*  75 */   public static final ResourceKey<PlacedFeature> PATCH_TALL_GRASS = PlacementUtils.createKey("patch_tall_grass");
/*     */   
/*  76 */   public static final ResourceKey<PlacedFeature> PATCH_LARGE_FERN = PlacementUtils.createKey("patch_large_fern");
/*     */   
/*  77 */   public static final ResourceKey<PlacedFeature> PATCH_BUSH = PlacementUtils.createKey("patch_bush");
/*     */   
/*  78 */   public static final ResourceKey<PlacedFeature> PATCH_LEAF_LITTER = PlacementUtils.createKey("patch_leaf_litter");
/*     */   
/*  80 */   public static final ResourceKey<PlacedFeature> PATCH_CACTUS_DESERT = PlacementUtils.createKey("patch_cactus_desert");
/*     */   
/*  81 */   public static final ResourceKey<PlacedFeature> PATCH_CACTUS_DECORATED = PlacementUtils.createKey("patch_cactus_decorated");
/*     */   
/*  83 */   public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE_SWAMP = PlacementUtils.createKey("patch_sugar_cane_swamp");
/*     */   
/*  84 */   public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE_DESERT = PlacementUtils.createKey("patch_sugar_cane_desert");
/*     */   
/*  85 */   public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE_BADLANDS = PlacementUtils.createKey("patch_sugar_cane_badlands");
/*     */   
/*  86 */   public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE = PlacementUtils.createKey("patch_sugar_cane");
/*     */   
/*  88 */   public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_SWAMP = PlacementUtils.createKey("patch_firefly_bush_swamp");
/*     */   
/*  89 */   public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP = PlacementUtils.createKey("patch_firefly_bush_near_water_swamp");
/*     */   
/*  90 */   public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER = PlacementUtils.createKey("patch_firefly_bush_near_water");
/*     */   
/*  92 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_NETHER = PlacementUtils.createKey("brown_mushroom_nether");
/*     */   
/*  93 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_NETHER = PlacementUtils.createKey("red_mushroom_nether");
/*     */   
/*  94 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_NORMAL = PlacementUtils.createKey("brown_mushroom_normal");
/*     */   
/*  95 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_NORMAL = PlacementUtils.createKey("red_mushroom_normal");
/*     */   
/*  96 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_TAIGA = PlacementUtils.createKey("brown_mushroom_taiga");
/*     */   
/*  97 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_TAIGA = PlacementUtils.createKey("red_mushroom_taiga");
/*     */   
/*  98 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_OLD_GROWTH = PlacementUtils.createKey("brown_mushroom_old_growth");
/*     */   
/*  99 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_OLD_GROWTH = PlacementUtils.createKey("red_mushroom_old_growth");
/*     */   
/* 100 */   public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_SWAMP = PlacementUtils.createKey("brown_mushroom_swamp");
/*     */   
/* 101 */   public static final ResourceKey<PlacedFeature> RED_MUSHROOM_SWAMP = PlacementUtils.createKey("red_mushroom_swamp");
/*     */   
/* 103 */   public static final ResourceKey<PlacedFeature> FLOWER_WARM = PlacementUtils.createKey("flower_warm");
/*     */   
/* 104 */   public static final ResourceKey<PlacedFeature> FLOWER_DEFAULT = PlacementUtils.createKey("flower_default");
/*     */   
/* 105 */   public static final ResourceKey<PlacedFeature> FLOWER_FLOWER_FOREST = PlacementUtils.createKey("flower_flower_forest");
/*     */   
/* 106 */   public static final ResourceKey<PlacedFeature> FLOWER_SWAMP = PlacementUtils.createKey("flower_swamp");
/*     */   
/* 107 */   public static final ResourceKey<PlacedFeature> FLOWER_PLAINS = PlacementUtils.createKey("flower_plains");
/*     */   
/* 108 */   public static final ResourceKey<PlacedFeature> FLOWER_MEADOW = PlacementUtils.createKey("flower_meadow");
/*     */   
/* 109 */   public static final ResourceKey<PlacedFeature> FLOWER_CHERRY = PlacementUtils.createKey("flower_cherry");
/*     */   
/* 110 */   public static final ResourceKey<PlacedFeature> FLOWER_PALE_GARDEN = PlacementUtils.createKey("flower_pale_garden");
/*     */   
/* 111 */   public static final ResourceKey<PlacedFeature> WILDFLOWERS_BIRCH_FOREST = PlacementUtils.createKey("wildflowers_birch_forest");
/*     */   
/* 112 */   public static final ResourceKey<PlacedFeature> WILDFLOWERS_MEADOW = PlacementUtils.createKey("wildflowers_meadow");
/*     */   
/* 114 */   public static final ResourceKey<PlacedFeature> TREES_PLAINS = PlacementUtils.createKey("trees_plains");
/*     */   
/* 115 */   public static final ResourceKey<PlacedFeature> DARK_FOREST_VEGETATION = PlacementUtils.createKey("dark_forest_vegetation");
/*     */   
/* 116 */   public static final ResourceKey<PlacedFeature> PALE_GARDEN_VEGETATION = PlacementUtils.createKey("pale_garden_vegetation");
/*     */   
/* 117 */   public static final ResourceKey<PlacedFeature> FLOWER_FOREST_FLOWERS = PlacementUtils.createKey("flower_forest_flowers");
/*     */   
/* 118 */   public static final ResourceKey<PlacedFeature> FOREST_FLOWERS = PlacementUtils.createKey("forest_flowers");
/*     */   
/* 119 */   public static final ResourceKey<PlacedFeature> PALE_GARDEN_FLOWERS = PlacementUtils.createKey("pale_garden_flowers");
/*     */   
/* 120 */   public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH = PlacementUtils.createKey("pale_moss_patch");
/*     */   
/* 122 */   public static final ResourceKey<PlacedFeature> TREES_FLOWER_FOREST = PlacementUtils.createKey("trees_flower_forest");
/*     */   
/* 123 */   public static final ResourceKey<PlacedFeature> TREES_MEADOW = PlacementUtils.createKey("trees_meadow");
/*     */   
/* 124 */   public static final ResourceKey<PlacedFeature> TREES_CHERRY = PlacementUtils.createKey("trees_cherry");
/*     */   
/* 125 */   public static final ResourceKey<PlacedFeature> TREES_TAIGA = PlacementUtils.createKey("trees_taiga");
/*     */   
/* 126 */   public static final ResourceKey<PlacedFeature> TREES_GROVE = PlacementUtils.createKey("trees_grove");
/*     */   
/* 127 */   public static final ResourceKey<PlacedFeature> TREES_BADLANDS = PlacementUtils.createKey("trees_badlands");
/*     */   
/* 128 */   public static final ResourceKey<PlacedFeature> TREES_SNOWY = PlacementUtils.createKey("trees_snowy");
/*     */   
/* 129 */   public static final ResourceKey<PlacedFeature> TREES_SWAMP = PlacementUtils.createKey("trees_swamp");
/*     */   
/* 130 */   public static final ResourceKey<PlacedFeature> TREES_WINDSWEPT_SAVANNA = PlacementUtils.createKey("trees_windswept_savanna");
/*     */   
/* 131 */   public static final ResourceKey<PlacedFeature> TREES_SAVANNA = PlacementUtils.createKey("trees_savanna");
/*     */   
/* 132 */   public static final ResourceKey<PlacedFeature> BIRCH_TALL = PlacementUtils.createKey("birch_tall");
/*     */   
/* 133 */   public static final ResourceKey<PlacedFeature> TREES_BIRCH = PlacementUtils.createKey("trees_birch");
/*     */   
/* 134 */   public static final ResourceKey<PlacedFeature> TREES_WINDSWEPT_FOREST = PlacementUtils.createKey("trees_windswept_forest");
/*     */   
/* 135 */   public static final ResourceKey<PlacedFeature> TREES_WINDSWEPT_HILLS = PlacementUtils.createKey("trees_windswept_hills");
/*     */   
/* 136 */   public static final ResourceKey<PlacedFeature> TREES_WATER = PlacementUtils.createKey("trees_water");
/*     */   
/* 137 */   public static final ResourceKey<PlacedFeature> TREES_BIRCH_AND_OAK_LEAF_LITTER = PlacementUtils.createKey("trees_birch_and_oak_leaf_litter");
/*     */   
/* 138 */   public static final ResourceKey<PlacedFeature> TREES_SPARSE_JUNGLE = PlacementUtils.createKey("trees_sparse_jungle");
/*     */   
/* 139 */   public static final ResourceKey<PlacedFeature> TREES_OLD_GROWTH_SPRUCE_TAIGA = PlacementUtils.createKey("trees_old_growth_spruce_taiga");
/*     */   
/* 140 */   public static final ResourceKey<PlacedFeature> TREES_OLD_GROWTH_PINE_TAIGA = PlacementUtils.createKey("trees_old_growth_pine_taiga");
/*     */   
/* 141 */   public static final ResourceKey<PlacedFeature> TREES_JUNGLE = PlacementUtils.createKey("trees_jungle");
/*     */   
/* 142 */   public static final ResourceKey<PlacedFeature> BAMBOO_VEGETATION = PlacementUtils.createKey("bamboo_vegetation");
/*     */   
/* 144 */   public static final ResourceKey<PlacedFeature> MUSHROOM_ISLAND_VEGETATION = PlacementUtils.createKey("mushroom_island_vegetation");
/*     */   
/* 146 */   public static final ResourceKey<PlacedFeature> TREES_MANGROVE = PlacementUtils.createKey("trees_mangrove");
/*     */   
/* 148 */   private static final PlacementModifier TREE_THRESHOLD = (PlacementModifier)SurfaceWaterDepthFilter.forMaxDepth(0);
/*     */   
/*     */   public static List<PlacementModifier> worldSurfaceSquaredWithCount(int count) {
/* 151 */     return (List)List.of(
/* 152 */         CountPlacement.of(count), 
/* 153 */         InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */         
/* 155 */         BiomeFilter.biome());
/*     */   }
/*     */   
/*     */   private static List<PlacementModifier> getMushroomPlacement(int rarity, PlacementModifier prefix) {
/* 160 */     ImmutableList.Builder<PlacementModifier> builder = ImmutableList.builder();
/* 161 */     if (prefix != null)
/* 162 */       builder.add(prefix); 
/* 164 */     if (rarity != 0)
/* 165 */       builder.add(RarityFilter.onAverageOnceEvery(rarity)); 
/* 167 */     builder.add(InSquarePlacement.spread());
/* 168 */     builder.add(PlacementUtils.HEIGHTMAP);
/* 169 */     builder.add(BiomeFilter.biome());
/* 170 */     builder.add(CountPlacement.of(96));
/* 171 */     builder.add(RandomOffsetPlacement.ofTriangle(7, 3));
/* 172 */     builder.add(BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE));
/* 173 */     return (List<PlacementModifier>)builder.build();
/*     */   }
/*     */   
/*     */   private static ImmutableList.Builder<PlacementModifier> treePlacementBase(PlacementModifier frequency) {
/* 177 */     return ImmutableList.builder()
/* 178 */       .add(frequency)
/* 179 */       .add(InSquarePlacement.spread())
/* 180 */       .add(TREE_THRESHOLD)
/* 181 */       .add(PlacementUtils.HEIGHTMAP_OCEAN_FLOOR)
/* 182 */       .add(BiomeFilter.biome());
/*     */   }
/*     */   
/*     */   public static List<PlacementModifier> treePlacement(PlacementModifier frequency) {
/* 186 */     return (List<PlacementModifier>)treePlacementBase(frequency).build();
/*     */   }
/*     */   
/*     */   public static List<PlacementModifier> treePlacement(PlacementModifier frequency, Block sapling) {
/* 190 */     return (List<PlacementModifier>)treePlacementBase(frequency)
/* 191 */       .add(BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(sapling.defaultBlockState(), (Vec3i)BlockPos.ZERO)))
/* 192 */       .build();
/*     */   }
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/* 196 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/* 197 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(VegetationFeatures.BAMBOO_NO_PODZOL);
/* 198 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(VegetationFeatures.BAMBOO_SOME_PODZOL);
/* 199 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(VegetationFeatures.VINES);
/* 200 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(VegetationFeatures.SUNFLOWER);
/* 201 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(VegetationFeatures.PUMPKIN);
/* 202 */     Holder.Reference reference6 = configuredFeatures.getOrThrow(VegetationFeatures.GRASS);
/* 203 */     Holder.Reference reference7 = configuredFeatures.getOrThrow(VegetationFeatures.LEAF_LITTER);
/* 204 */     Holder.Reference reference8 = configuredFeatures.getOrThrow(VegetationFeatures.TAIGA_GRASS);
/* 205 */     Holder.Reference reference9 = configuredFeatures.getOrThrow(VegetationFeatures.GRASS_JUNGLE);
/* 206 */     Holder.Reference reference10 = configuredFeatures.getOrThrow(VegetationFeatures.DEAD_BUSH);
/* 207 */     Holder.Reference reference11 = configuredFeatures.getOrThrow(VegetationFeatures.DRY_GRASS);
/* 208 */     Holder.Reference reference12 = configuredFeatures.getOrThrow(VegetationFeatures.FIREFLY_BUSH);
/* 209 */     Holder.Reference reference13 = configuredFeatures.getOrThrow(VegetationFeatures.MELON);
/* 210 */     Holder.Reference reference14 = configuredFeatures.getOrThrow(VegetationFeatures.BERRY_BUSH);
/* 211 */     Holder.Reference reference15 = configuredFeatures.getOrThrow(VegetationFeatures.WATERLILY);
/* 212 */     Holder.Reference reference16 = configuredFeatures.getOrThrow(VegetationFeatures.TALL_GRASS);
/* 213 */     Holder.Reference reference17 = configuredFeatures.getOrThrow(VegetationFeatures.LARGE_FERN);
/* 214 */     Holder.Reference reference18 = configuredFeatures.getOrThrow(VegetationFeatures.BUSH);
/* 215 */     Holder.Reference reference19 = configuredFeatures.getOrThrow(VegetationFeatures.CACTUS);
/* 216 */     Holder.Reference reference20 = configuredFeatures.getOrThrow(VegetationFeatures.SUGAR_CANE);
/* 217 */     Holder.Reference reference21 = configuredFeatures.getOrThrow(VegetationFeatures.BROWN_MUSHROOM);
/* 218 */     Holder.Reference reference22 = configuredFeatures.getOrThrow(VegetationFeatures.RED_MUSHROOM);
/* 219 */     Holder.Reference reference23 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_DEFAULT);
/* 220 */     Holder.Reference reference24 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_FLOWER_FOREST);
/* 221 */     Holder.Reference reference25 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_SWAMP);
/* 222 */     Holder.Reference reference26 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_PLAIN);
/* 223 */     Holder.Reference reference27 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_MEADOW);
/* 224 */     Holder.Reference reference28 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_CHERRY);
/* 225 */     Holder.Reference reference29 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_PALE_GARDEN);
/* 226 */     Holder.Reference reference30 = configuredFeatures.getOrThrow(VegetationFeatures.WILDFLOWER);
/* 227 */     Holder.Reference reference31 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_PLAINS);
/* 228 */     Holder.Reference reference32 = configuredFeatures.getOrThrow(VegetationFeatures.DARK_FOREST_VEGETATION);
/* 229 */     Holder.Reference reference33 = configuredFeatures.getOrThrow(VegetationFeatures.PALE_GARDEN_VEGETATION);
/* 230 */     Holder.Reference reference34 = configuredFeatures.getOrThrow(VegetationFeatures.FOREST_FLOWERS);
/* 231 */     Holder.Reference reference35 = configuredFeatures.getOrThrow(VegetationFeatures.PALE_FOREST_FLOWER);
/* 232 */     Holder.Reference reference36 = configuredFeatures.getOrThrow(VegetationFeatures.PALE_MOSS_PATCH);
/* 233 */     Holder.Reference reference37 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_FLOWER_FOREST);
/* 234 */     Holder.Reference reference38 = configuredFeatures.getOrThrow(VegetationFeatures.MEADOW_TREES);
/* 235 */     Holder.Reference reference39 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_TAIGA);
/* 236 */     Holder.Reference reference40 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_BADLANDS);
/* 237 */     Holder.Reference reference41 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_GROVE);
/* 238 */     Holder.Reference reference42 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_SNOWY);
/* 239 */     Holder.Reference reference43 = configuredFeatures.getOrThrow(TreeFeatures.CHERRY_BEES_005);
/* 240 */     Holder.Reference reference44 = configuredFeatures.getOrThrow(TreeFeatures.SWAMP_OAK);
/* 241 */     Holder.Reference reference45 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_SAVANNA);
/* 242 */     Holder.Reference reference46 = configuredFeatures.getOrThrow(VegetationFeatures.BIRCH_TALL);
/* 243 */     Holder.Reference reference47 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_BIRCH);
/* 244 */     Holder.Reference reference48 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_WINDSWEPT_HILLS);
/* 245 */     Holder.Reference reference49 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_WATER);
/* 246 */     Holder.Reference reference50 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_BIRCH_AND_OAK_LEAF_LITTER);
/* 247 */     Holder.Reference reference51 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_SPARSE_JUNGLE);
/* 248 */     Holder.Reference reference52 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_OLD_GROWTH_SPRUCE_TAIGA);
/* 249 */     Holder.Reference reference53 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_OLD_GROWTH_PINE_TAIGA);
/* 250 */     Holder.Reference reference54 = configuredFeatures.getOrThrow(VegetationFeatures.TREES_JUNGLE);
/* 251 */     Holder.Reference reference55 = configuredFeatures.getOrThrow(VegetationFeatures.BAMBOO_VEGETATION);
/* 252 */     Holder.Reference reference56 = configuredFeatures.getOrThrow(VegetationFeatures.MUSHROOM_ISLAND_VEGETATION);
/* 253 */     Holder.Reference reference57 = configuredFeatures.getOrThrow(VegetationFeatures.MANGROVE_VEGETATION);
/* 255 */     PlacementUtils.register(context, BAMBOO_LIGHT, (Holder<ConfiguredFeature<?, ?>>)reference1, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(4), 
/* 257 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 259 */           (PlacementModifier)BiomeFilter.biome() });
/* 261 */     PlacementUtils.register(context, BAMBOO, (Holder<ConfiguredFeature<?, ?>>)reference2, new PlacementModifier[] { (PlacementModifier)NoiseBasedCountPlacement.of(160, 80.0D, 0.3D), 
/* 263 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 265 */           (PlacementModifier)BiomeFilter.biome() });
/* 267 */     PlacementUtils.register(context, VINES, (Holder<ConfiguredFeature<?, ?>>)reference3, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(127), 
/* 269 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 270 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(100)), 
/* 271 */           (PlacementModifier)BiomeFilter.biome() });
/* 274 */     PlacementUtils.register(context, PATCH_SUNFLOWER, (Holder<ConfiguredFeature<?, ?>>)reference4, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(3), 
/* 276 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 278 */           (PlacementModifier)BiomeFilter.biome(), 
/* 279 */           (PlacementModifier)CountPlacement.of(96), 
/* 280 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 281 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 283 */     PlacementUtils.register(context, PATCH_PUMPKIN, (Holder<ConfiguredFeature<?, ?>>)reference5, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(300), 
/* 285 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 287 */           (PlacementModifier)BiomeFilter.biome(), 
/* 288 */           (PlacementModifier)CountPlacement.of(96), 
/* 289 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 290 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */               
/* 292 */               BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.GRASS_BLOCK }))) });
/* 296 */     PlacementUtils.register(context, PATCH_GRASS_PLAIN, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] { (PlacementModifier)NoiseThresholdCountPlacement.of(-0.8D, 5, 10), 
/* 298 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 300 */           (PlacementModifier)BiomeFilter.biome(), 
/* 301 */           (PlacementModifier)CountPlacement.of(32), 
/* 302 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 303 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 305 */     PlacementUtils.register(context, PATCH_GRASS_MEADOW, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] { (PlacementModifier)NoiseThresholdCountPlacement.of(-0.8D, 5, 10), 
/* 307 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 309 */           (PlacementModifier)BiomeFilter.biome(), 
/* 310 */           (PlacementModifier)CountPlacement.of(16), 
/* 311 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 312 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 314 */     PlacementUtils.register(context, PATCH_GRASS_FOREST, (Holder<ConfiguredFeature<?, ?>>)reference6, Util.copyAndAdd(
/* 315 */           worldSurfaceSquaredWithCount(2), (Object[])new PlacementModifier[] { (PlacementModifier)CountPlacement.of(32), 
/* 317 */             (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 318 */             (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) }));
/* 320 */     PlacementUtils.register(context, PATCH_LEAF_LITTER, (Holder<ConfiguredFeature<?, ?>>)reference7, Util.copyAndAdd(
/* 321 */           worldSurfaceSquaredWithCount(2), (Object[])new PlacementModifier[] { (PlacementModifier)CountPlacement.of(32), 
/* 323 */             (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 324 */             (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */                 
/* 326 */                 BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.GRASS_BLOCK }))) }));
/* 329 */     PlacementUtils.register(context, PATCH_GRASS_BADLANDS, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] { (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 332 */           (PlacementModifier)BiomeFilter.biome(), 
/* 333 */           (PlacementModifier)CountPlacement.of(32), 
/* 334 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 335 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 337 */     PlacementUtils.register(context, PATCH_GRASS_SAVANNA, (Holder<ConfiguredFeature<?, ?>>)reference6, Util.copyAndAdd(
/* 338 */           worldSurfaceSquaredWithCount(20), (Object[])new PlacementModifier[] { (PlacementModifier)CountPlacement.of(32), 
/* 340 */             (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 341 */             (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) }));
/* 343 */     PlacementUtils.register(context, PATCH_GRASS_NORMAL, (Holder<ConfiguredFeature<?, ?>>)reference6, Util.copyAndAdd(
/* 344 */           worldSurfaceSquaredWithCount(5), (Object[])new PlacementModifier[] { (PlacementModifier)CountPlacement.of(32), 
/* 346 */             (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 347 */             (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) }));
/* 349 */     PlacementUtils.register(context, PATCH_GRASS_TAIGA_2, (Holder<ConfiguredFeature<?, ?>>)reference8, new PlacementModifier[] { (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 352 */           (PlacementModifier)BiomeFilter.biome(), 
/* 353 */           (PlacementModifier)CountPlacement.of(32), 
/* 354 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 355 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 357 */     PlacementUtils.register(context, PATCH_GRASS_TAIGA, (Holder<ConfiguredFeature<?, ?>>)reference8, Util.copyAndAdd(
/* 358 */           worldSurfaceSquaredWithCount(7), (Object[])new PlacementModifier[] { (PlacementModifier)CountPlacement.of(32), 
/* 360 */             (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 361 */             (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) }));
/* 363 */     PlacementUtils.register(context, PATCH_GRASS_JUNGLE, (Holder<ConfiguredFeature<?, ?>>)reference9, Util.copyAndAdd(
/* 364 */           worldSurfaceSquaredWithCount(25), (Object[])new PlacementModifier[] { (PlacementModifier)CountPlacement.of(32), 
/* 366 */             (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 367 */             (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */                 
/* 369 */                 BlockPredicate.not(
/* 370 */                   BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.PODZOL })))) }));
/* 375 */     PlacementUtils.register(context, GRASS_BONEMEAL, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] { (PlacementModifier)PlacementUtils.isEmpty() });
/* 377 */     PlacementUtils.register(context, PATCH_DEAD_BUSH_2, (Holder<ConfiguredFeature<?, ?>>)reference10, Util.copyAndAdd(
/* 378 */           worldSurfaceSquaredWithCount(2), (Object[])new PlacementModifier[] { (PlacementModifier)CountPlacement.of(4), 
/* 380 */             (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 381 */             (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) }));
/* 383 */     PlacementUtils.register(context, PATCH_DEAD_BUSH, (Holder<ConfiguredFeature<?, ?>>)reference10, new PlacementModifier[] { (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 386 */           (PlacementModifier)BiomeFilter.biome(), 
/* 387 */           (PlacementModifier)CountPlacement.of(4), 
/* 388 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 389 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 391 */     PlacementUtils.register(context, PATCH_DEAD_BUSH_BADLANDS, (Holder<ConfiguredFeature<?, ?>>)reference10, Util.copyAndAdd(
/* 392 */           worldSurfaceSquaredWithCount(20), (Object[])new PlacementModifier[] { (PlacementModifier)CountPlacement.of(4), 
/* 394 */             (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 395 */             (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) }));
/* 397 */     PlacementUtils.register(context, PATCH_DRY_GRASS_BADLANDS, (Holder<ConfiguredFeature<?, ?>>)reference11, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(6), 
/* 399 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 401 */           (PlacementModifier)BiomeFilter.biome(), 
/* 402 */           (PlacementModifier)CountPlacement.of(64), 
/* 403 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 404 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 406 */     PlacementUtils.register(context, PATCH_DRY_GRASS_DESERT, (Holder<ConfiguredFeature<?, ?>>)reference11, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(3), 
/* 408 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 410 */           (PlacementModifier)BiomeFilter.biome(), 
/* 411 */           (PlacementModifier)CountPlacement.of(64), 
/* 412 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 413 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 416 */     BlockPredicate melonPlacementPredicate = BlockPredicate.allOf(new BlockPredicate[] { BlockPredicate.replaceable(), 
/* 418 */           BlockPredicate.noFluid(), 
/* 419 */           BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.GRASS_BLOCK }) });
/* 421 */     PlacementUtils.register(context, PATCH_MELON, (Holder<ConfiguredFeature<?, ?>>)reference13, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(6), 
/* 423 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 425 */           (PlacementModifier)BiomeFilter.biome(), 
/* 426 */           (PlacementModifier)CountPlacement.of(64), 
/* 427 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 428 */           (PlacementModifier)BlockPredicateFilter.forPredicate(melonPlacementPredicate) });
/* 431 */     PlacementUtils.register(context, PATCH_MELON_SPARSE, (Holder<ConfiguredFeature<?, ?>>)reference13, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(64), 
/* 433 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 435 */           (PlacementModifier)BiomeFilter.biome(), 
/* 436 */           (PlacementModifier)CountPlacement.of(64), 
/* 437 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 438 */           (PlacementModifier)BlockPredicateFilter.forPredicate(melonPlacementPredicate) });
/* 441 */     PlacementUtils.register(context, PATCH_BERRY_COMMON, (Holder<ConfiguredFeature<?, ?>>)reference14, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(32), 
/* 443 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 445 */           (PlacementModifier)BiomeFilter.biome(), 
/* 446 */           (PlacementModifier)CountPlacement.of(96), 
/* 447 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 448 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */               
/* 450 */               BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.GRASS_BLOCK }))) });
/* 453 */     PlacementUtils.register(context, PATCH_BERRY_RARE, (Holder<ConfiguredFeature<?, ?>>)reference14, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(384), 
/* 455 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 457 */           (PlacementModifier)BiomeFilter.biome(), 
/* 458 */           (PlacementModifier)CountPlacement.of(96), 
/* 459 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 460 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */               
/* 462 */               BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.GRASS_BLOCK }))) });
/* 466 */     PlacementUtils.register(context, PATCH_WATERLILY, (Holder<ConfiguredFeature<?, ?>>)reference15, Util.copyAndAdd(
/* 467 */           worldSurfaceSquaredWithCount(4), (Object[])new PlacementModifier[] { (PlacementModifier)CountPlacement.of(10), 
/* 469 */             (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 470 */             (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) }));
/* 473 */     PlacementUtils.register(context, PATCH_TALL_GRASS_2, (Holder<ConfiguredFeature<?, ?>>)reference16, new PlacementModifier[] { (PlacementModifier)NoiseThresholdCountPlacement.of(-0.8D, 0, 7), 
/* 475 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(32), 
/* 476 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 478 */           (PlacementModifier)BiomeFilter.biome(), 
/* 479 */           (PlacementModifier)CountPlacement.of(96), 
/* 480 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 481 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 483 */     PlacementUtils.register(context, PATCH_TALL_GRASS, (Holder<ConfiguredFeature<?, ?>>)reference16, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(5), 
/* 485 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 487 */           (PlacementModifier)BiomeFilter.biome(), 
/* 488 */           (PlacementModifier)CountPlacement.of(96), 
/* 489 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 490 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 492 */     PlacementUtils.register(context, PATCH_LARGE_FERN, (Holder<ConfiguredFeature<?, ?>>)reference17, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(5), 
/* 494 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 496 */           (PlacementModifier)BiomeFilter.biome(), 
/* 497 */           (PlacementModifier)CountPlacement.of(96), 
/* 498 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 499 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 502 */     PlacementUtils.register(context, PATCH_BUSH, (Holder<ConfiguredFeature<?, ?>>)reference18, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(4), 
/* 504 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 506 */           (PlacementModifier)BiomeFilter.biome(), 
/* 507 */           (PlacementModifier)CountPlacement.of(24), 
/* 508 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(5, 3), 
/* 509 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 512 */     PlacementUtils.register(context, PATCH_CACTUS_DESERT, (Holder<ConfiguredFeature<?, ?>>)reference19, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(6), 
/* 514 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 516 */           (PlacementModifier)BiomeFilter.biome(), 
/* 517 */           (PlacementModifier)CountPlacement.of(10), 
/* 518 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 519 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */               
/* 521 */               BlockPredicate.wouldSurvive(Blocks.CACTUS.defaultBlockState(), (Vec3i)BlockPos.ZERO))) });
/* 524 */     PlacementUtils.register(context, PATCH_CACTUS_DECORATED, (Holder<ConfiguredFeature<?, ?>>)reference19, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(13), 
/* 526 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 528 */           (PlacementModifier)BiomeFilter.biome(), 
/* 529 */           (PlacementModifier)CountPlacement.of(10), 
/* 530 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 531 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */               
/* 533 */               BlockPredicate.wouldSurvive(Blocks.CACTUS.defaultBlockState(), (Vec3i)BlockPos.ZERO))) });
/* 537 */     PlacementUtils.register(context, PATCH_SUGAR_CANE_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference20, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(3), 
/* 539 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 541 */           (PlacementModifier)BiomeFilter.biome(), 
/* 542 */           (PlacementModifier)CountPlacement.of(20), 
/* 543 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(4, 0), 
/* 544 */           (PlacementModifier)VegetationFeatures.nearWaterPredicate(Blocks.SUGAR_CANE) });
/* 546 */     PlacementUtils.register(context, PATCH_SUGAR_CANE_DESERT, (Holder<ConfiguredFeature<?, ?>>)reference20, new PlacementModifier[] { (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 549 */           (PlacementModifier)BiomeFilter.biome(), 
/* 550 */           (PlacementModifier)CountPlacement.of(20), 
/* 551 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(4, 0), 
/* 552 */           (PlacementModifier)VegetationFeatures.nearWaterPredicate(Blocks.SUGAR_CANE) });
/* 554 */     PlacementUtils.register(context, PATCH_SUGAR_CANE_BADLANDS, (Holder<ConfiguredFeature<?, ?>>)reference20, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(5), 
/* 556 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 558 */           (PlacementModifier)BiomeFilter.biome(), 
/* 559 */           (PlacementModifier)CountPlacement.of(20), 
/* 560 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(4, 0), 
/* 561 */           (PlacementModifier)VegetationFeatures.nearWaterPredicate(Blocks.SUGAR_CANE) });
/* 563 */     PlacementUtils.register(context, PATCH_SUGAR_CANE, (Holder<ConfiguredFeature<?, ?>>)reference20, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(6), 
/* 565 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 567 */           (PlacementModifier)BiomeFilter.biome(), 
/* 568 */           (PlacementModifier)CountPlacement.of(20), 
/* 569 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(4, 0), 
/* 570 */           (PlacementModifier)VegetationFeatures.nearWaterPredicate(Blocks.SUGAR_CANE) });
/* 572 */     PlacementUtils.register(context, PATCH_FIREFLY_BUSH_NEAR_WATER, (Holder<ConfiguredFeature<?, ?>>)reference12, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(2), 
/* 574 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_NO_LEAVES, 
/*     */           
/* 576 */           (PlacementModifier)BiomeFilter.biome(), 
/* 577 */           (PlacementModifier)VegetationFeatures.nearWaterPredicate(Blocks.FIREFLY_BUSH), 
/* 578 */           (PlacementModifier)CountPlacement.of(20), 
/* 579 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(4, 3), 
/* 580 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 582 */     PlacementUtils.register(context, PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference12, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(3), 
/* 584 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 586 */           (PlacementModifier)BiomeFilter.biome(), 
/* 587 */           (PlacementModifier)VegetationFeatures.nearWaterPredicate(Blocks.FIREFLY_BUSH), 
/* 588 */           (PlacementModifier)CountPlacement.of(20), 
/* 589 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(4, 3), 
/* 590 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 592 */     PlacementUtils.register(context, PATCH_FIREFLY_BUSH_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference12, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(8), 
/* 594 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 596 */           (PlacementModifier)BiomeFilter.biome(), 
/* 597 */           (PlacementModifier)CountPlacement.of(20), 
/* 598 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(4, 3), 
/* 599 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 601 */     PlacementUtils.register(context, BROWN_MUSHROOM_NETHER, (Holder<ConfiguredFeature<?, ?>>)reference21, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(2), 
/* 603 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 605 */           (PlacementModifier)BiomeFilter.biome(), 
/* 606 */           (PlacementModifier)CountPlacement.of(96), 
/* 607 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 608 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 610 */     PlacementUtils.register(context, RED_MUSHROOM_NETHER, (Holder<ConfiguredFeature<?, ?>>)reference22, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(2), 
/* 612 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 614 */           (PlacementModifier)BiomeFilter.biome(), 
/* 615 */           (PlacementModifier)CountPlacement.of(96), 
/* 616 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 617 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 619 */     PlacementUtils.register(context, BROWN_MUSHROOM_NORMAL, (Holder<ConfiguredFeature<?, ?>>)reference21, 
/* 620 */         getMushroomPlacement(256, null));
/* 622 */     PlacementUtils.register(context, RED_MUSHROOM_NORMAL, (Holder<ConfiguredFeature<?, ?>>)reference22, 
/* 623 */         getMushroomPlacement(512, null));
/* 625 */     PlacementUtils.register(context, BROWN_MUSHROOM_TAIGA, (Holder<ConfiguredFeature<?, ?>>)reference21, 
/* 626 */         getMushroomPlacement(4, null));
/* 628 */     PlacementUtils.register(context, RED_MUSHROOM_TAIGA, (Holder<ConfiguredFeature<?, ?>>)reference22, 
/* 629 */         getMushroomPlacement(256, null));
/* 631 */     PlacementUtils.register(context, BROWN_MUSHROOM_OLD_GROWTH, (Holder<ConfiguredFeature<?, ?>>)reference21, 
/* 632 */         getMushroomPlacement(4, (PlacementModifier)CountPlacement.of(3)));
/* 634 */     PlacementUtils.register(context, RED_MUSHROOM_OLD_GROWTH, (Holder<ConfiguredFeature<?, ?>>)reference22, 
/* 635 */         getMushroomPlacement(171, null));
/* 637 */     PlacementUtils.register(context, BROWN_MUSHROOM_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference21, 
/* 638 */         getMushroomPlacement(0, (PlacementModifier)CountPlacement.of(2)));
/* 640 */     PlacementUtils.register(context, RED_MUSHROOM_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference22, 
/* 641 */         getMushroomPlacement(64, null));
/* 644 */     PlacementUtils.register(context, FLOWER_WARM, (Holder<ConfiguredFeature<?, ?>>)reference23, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(16), 
/* 646 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 648 */           (PlacementModifier)BiomeFilter.biome(), 
/* 649 */           (PlacementModifier)CountPlacement.of(64), 
/* 650 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 651 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 653 */     PlacementUtils.register(context, FLOWER_DEFAULT, (Holder<ConfiguredFeature<?, ?>>)reference23, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(32), 
/* 655 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 657 */           (PlacementModifier)BiomeFilter.biome(), 
/* 658 */           (PlacementModifier)CountPlacement.of(64), 
/* 659 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 660 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 662 */     PlacementUtils.register(context, FLOWER_FLOWER_FOREST, (Holder<ConfiguredFeature<?, ?>>)reference24, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(3), 
/* 664 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(2), 
/* 665 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 667 */           (PlacementModifier)BiomeFilter.biome(), 
/* 668 */           (PlacementModifier)CountPlacement.of(96), 
/* 669 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(6, 2), 
/* 670 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 672 */     PlacementUtils.register(context, FLOWER_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference25, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(32), 
/* 674 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 676 */           (PlacementModifier)BiomeFilter.biome(), 
/* 677 */           (PlacementModifier)CountPlacement.of(64), 
/* 678 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(6, 2), 
/* 679 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 681 */     PlacementUtils.register(context, FLOWER_PLAINS, (Holder<ConfiguredFeature<?, ?>>)reference26, new PlacementModifier[] { (PlacementModifier)NoiseThresholdCountPlacement.of(-0.8D, 15, 4), 
/* 683 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(32), 
/* 684 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 686 */           (PlacementModifier)BiomeFilter.biome(), 
/* 687 */           (PlacementModifier)CountPlacement.of(64), 
/* 688 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(6, 2), 
/* 689 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 691 */     PlacementUtils.register(context, FLOWER_CHERRY, (Holder<ConfiguredFeature<?, ?>>)reference28, new PlacementModifier[] { (PlacementModifier)NoiseThresholdCountPlacement.of(-0.8D, 5, 10), 
/* 693 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 695 */           (PlacementModifier)BiomeFilter.biome(), 
/* 696 */           (PlacementModifier)CountPlacement.of(96), 
/* 697 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(6, 2), 
/* 698 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 700 */     PlacementUtils.register(context, FLOWER_MEADOW, (Holder<ConfiguredFeature<?, ?>>)reference27, new PlacementModifier[] { (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 703 */           (PlacementModifier)BiomeFilter.biome(), 
/* 704 */           (PlacementModifier)CountPlacement.of(96), 
/* 705 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(6, 2), 
/* 706 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 708 */     PlacementUtils.register(context, FLOWER_PALE_GARDEN, (Holder<ConfiguredFeature<?, ?>>)reference29, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(32), 
/* 710 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 712 */           (PlacementModifier)BiomeFilter.biome() });
/* 714 */     PlacementUtils.register(context, WILDFLOWERS_BIRCH_FOREST, (Holder<ConfiguredFeature<?, ?>>)reference30, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(3), 
/* 716 */           (PlacementModifier)RarityFilter.onAverageOnceEvery(2), 
/* 717 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 719 */           (PlacementModifier)BiomeFilter.biome(), 
/* 720 */           (PlacementModifier)CountPlacement.of(64), 
/* 721 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(6, 2), 
/* 722 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 725 */     PlacementUtils.register(context, WILDFLOWERS_MEADOW, (Holder<ConfiguredFeature<?, ?>>)reference30, new PlacementModifier[] { (PlacementModifier)NoiseThresholdCountPlacement.of(-0.8D, 5, 10), 
/* 727 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 729 */           (PlacementModifier)BiomeFilter.biome(), 
/* 730 */           (PlacementModifier)CountPlacement.of(8), 
/* 731 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(6, 2), 
/* 732 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 734 */     SurfaceWaterDepthFilter surfaceWaterDepthFilter = SurfaceWaterDepthFilter.forMaxDepth(0);
/* 735 */     PlacementUtils.register(context, TREES_PLAINS, (Holder<ConfiguredFeature<?, ?>>)reference31, new PlacementModifier[] { PlacementUtils.countExtra(0, 0.05F, 1), 
/* 737 */           (PlacementModifier)InSquarePlacement.spread(), (PlacementModifier)surfaceWaterDepthFilter, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */           
/* 740 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), (Vec3i)BlockPos.ZERO)), 
/* 741 */           (PlacementModifier)BiomeFilter.biome() });
/* 743 */     PlacementUtils.register(context, DARK_FOREST_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference32, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(16), 
/* 745 */           (PlacementModifier)InSquarePlacement.spread(), (PlacementModifier)surfaceWaterDepthFilter, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */           
/* 748 */           (PlacementModifier)BiomeFilter.biome() });
/* 751 */     PlacementUtils.register(context, PALE_GARDEN_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference33, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(16), 
/* 753 */           (PlacementModifier)InSquarePlacement.spread(), (PlacementModifier)surfaceWaterDepthFilter, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */           
/* 756 */           (PlacementModifier)BiomeFilter.biome() });
/* 759 */     PlacementUtils.register(context, FLOWER_FOREST_FLOWERS, (Holder<ConfiguredFeature<?, ?>>)reference34, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(7), 
/* 761 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 763 */           (PlacementModifier)CountPlacement.of((IntProvider)ClampedInt.of((IntProvider)UniformInt.of(-1, 3), 0, 3)), 
/* 764 */           (PlacementModifier)BiomeFilter.biome() });
/* 766 */     PlacementUtils.register(context, FOREST_FLOWERS, (Holder<ConfiguredFeature<?, ?>>)reference34, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(7), 
/* 768 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 770 */           (PlacementModifier)CountPlacement.of((IntProvider)ClampedInt.of((IntProvider)UniformInt.of(-3, 1), 0, 1)), 
/* 771 */           (PlacementModifier)BiomeFilter.biome() });
/* 773 */     PlacementUtils.register(context, PALE_GARDEN_FLOWERS, (Holder<ConfiguredFeature<?, ?>>)reference35, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(8), 
/* 775 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_NO_LEAVES, 
/*     */           
/* 777 */           (PlacementModifier)BiomeFilter.biome(), 
/* 778 */           (PlacementModifier)CountPlacement.of(96), 
/* 779 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 780 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 783 */     PlacementUtils.register(context, PALE_MOSS_PATCH, (Holder<ConfiguredFeature<?, ?>>)reference36, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(1), 
/* 785 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_NO_LEAVES, 
/*     */           
/* 787 */           (PlacementModifier)BiomeFilter.biome() });
/* 790 */     PlacementUtils.register(context, TREES_FLOWER_FOREST, (Holder<ConfiguredFeature<?, ?>>)reference37, 
/* 791 */         treePlacement(PlacementUtils.countExtra(6, 0.1F, 1)));
/* 793 */     PlacementUtils.register(context, TREES_MEADOW, (Holder<ConfiguredFeature<?, ?>>)reference38, 
/* 794 */         treePlacement((PlacementModifier)RarityFilter.onAverageOnceEvery(100)));
/* 796 */     PlacementUtils.register(context, TREES_CHERRY, (Holder<ConfiguredFeature<?, ?>>)reference43, 
/* 797 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1), Blocks.CHERRY_SAPLING));
/* 799 */     PlacementUtils.register(context, TREES_TAIGA, (Holder<ConfiguredFeature<?, ?>>)reference39, 
/* 800 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/* 802 */     PlacementUtils.register(context, TREES_GROVE, (Holder<ConfiguredFeature<?, ?>>)reference41, 
/* 803 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/* 805 */     PlacementUtils.register(context, TREES_BADLANDS, (Holder<ConfiguredFeature<?, ?>>)reference40, 
/* 806 */         treePlacement(PlacementUtils.countExtra(5, 0.1F, 1), Blocks.OAK_SAPLING));
/* 808 */     PlacementUtils.register(context, TREES_SNOWY, (Holder<ConfiguredFeature<?, ?>>)reference42, 
/* 809 */         treePlacement(PlacementUtils.countExtra(0, 0.1F, 1), Blocks.SPRUCE_SAPLING));
/* 811 */     PlacementUtils.register(context, TREES_SWAMP, (Holder<ConfiguredFeature<?, ?>>)reference44, new PlacementModifier[] { PlacementUtils.countExtra(2, 0.1F, 1), 
/* 813 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 814 */           (PlacementModifier)SurfaceWaterDepthFilter.forMaxDepth(2), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */           
/* 816 */           (PlacementModifier)BiomeFilter.biome(), 
/* 817 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), (Vec3i)BlockPos.ZERO)) });
/* 819 */     PlacementUtils.register(context, TREES_WINDSWEPT_SAVANNA, (Holder<ConfiguredFeature<?, ?>>)reference45, 
/* 820 */         treePlacement(PlacementUtils.countExtra(2, 0.1F, 1)));
/* 822 */     PlacementUtils.register(context, TREES_SAVANNA, (Holder<ConfiguredFeature<?, ?>>)reference45, 
/* 823 */         treePlacement(PlacementUtils.countExtra(1, 0.1F, 1)));
/* 825 */     PlacementUtils.register(context, BIRCH_TALL, (Holder<ConfiguredFeature<?, ?>>)reference46, 
/* 826 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/* 828 */     PlacementUtils.register(context, TREES_BIRCH, (Holder<ConfiguredFeature<?, ?>>)reference47, 
/* 829 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1), Blocks.BIRCH_SAPLING));
/* 831 */     PlacementUtils.register(context, TREES_WINDSWEPT_FOREST, (Holder<ConfiguredFeature<?, ?>>)reference48, 
/* 832 */         treePlacement(PlacementUtils.countExtra(3, 0.1F, 1)));
/* 834 */     PlacementUtils.register(context, TREES_WINDSWEPT_HILLS, (Holder<ConfiguredFeature<?, ?>>)reference48, 
/* 835 */         treePlacement(PlacementUtils.countExtra(0, 0.1F, 1)));
/* 837 */     PlacementUtils.register(context, TREES_WATER, (Holder<ConfiguredFeature<?, ?>>)reference49, 
/* 838 */         treePlacement(PlacementUtils.countExtra(0, 0.1F, 1)));
/* 840 */     PlacementUtils.register(context, TREES_BIRCH_AND_OAK_LEAF_LITTER, (Holder<ConfiguredFeature<?, ?>>)reference50, 
/* 841 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/* 843 */     PlacementUtils.register(context, TREES_SPARSE_JUNGLE, (Holder<ConfiguredFeature<?, ?>>)reference51, 
/* 844 */         treePlacement(PlacementUtils.countExtra(2, 0.1F, 1)));
/* 846 */     PlacementUtils.register(context, TREES_OLD_GROWTH_SPRUCE_TAIGA, (Holder<ConfiguredFeature<?, ?>>)reference52, 
/* 847 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/* 849 */     PlacementUtils.register(context, TREES_OLD_GROWTH_PINE_TAIGA, (Holder<ConfiguredFeature<?, ?>>)reference53, 
/* 850 */         treePlacement(PlacementUtils.countExtra(10, 0.1F, 1)));
/* 852 */     PlacementUtils.register(context, TREES_JUNGLE, (Holder<ConfiguredFeature<?, ?>>)reference54, 
/* 853 */         treePlacement(PlacementUtils.countExtra(50, 0.1F, 1)));
/* 855 */     PlacementUtils.register(context, BAMBOO_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference55, 
/* 856 */         treePlacement(PlacementUtils.countExtra(30, 0.1F, 1)));
/* 859 */     PlacementUtils.register(context, MUSHROOM_ISLAND_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference56, new PlacementModifier[] { (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 862 */           (PlacementModifier)BiomeFilter.biome() });
/* 865 */     PlacementUtils.register(context, TREES_MANGROVE, (Holder<ConfiguredFeature<?, ?>>)reference57, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(25), 
/* 867 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 868 */           (PlacementModifier)SurfaceWaterDepthFilter.forMaxDepth(5), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, 
/*     */           
/* 870 */           (PlacementModifier)BiomeFilter.biome() });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\placement\VegetationPlacements.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */