/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.CaveFeatures;
/*     */ import net.minecraft.data.worldgen.features.VegetationFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.ClampedNormalInt;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;
/*     */ 
/*     */ public class CavePlacements {
/*  31 */   public static final ResourceKey<PlacedFeature> MONSTER_ROOM = PlacementUtils.createKey("monster_room");
/*     */   
/*  32 */   public static final ResourceKey<PlacedFeature> MONSTER_ROOM_DEEP = PlacementUtils.createKey("monster_room_deep");
/*     */   
/*  35 */   public static final ResourceKey<PlacedFeature> FOSSIL_UPPER = PlacementUtils.createKey("fossil_upper");
/*     */   
/*  36 */   public static final ResourceKey<PlacedFeature> FOSSIL_LOWER = PlacementUtils.createKey("fossil_lower");
/*     */   
/*  39 */   public static final ResourceKey<PlacedFeature> DRIPSTONE_CLUSTER = PlacementUtils.createKey("dripstone_cluster");
/*     */   
/*  40 */   public static final ResourceKey<PlacedFeature> SULFUR_SPIKE_CLUSTER = PlacementUtils.createKey("sulfur_spike_cluster");
/*     */   
/*  41 */   public static final ResourceKey<PlacedFeature> LARGE_DRIPSTONE = PlacementUtils.createKey("large_dripstone");
/*     */   
/*  42 */   public static final ResourceKey<PlacedFeature> POINTED_DRIPSTONE = PlacementUtils.createKey("pointed_dripstone");
/*     */   
/*  43 */   public static final ResourceKey<PlacedFeature> SULFUR_SPIKE = PlacementUtils.createKey("sulfur_spike");
/*     */   
/*  44 */   public static final ResourceKey<PlacedFeature> UNDERWATER_MAGMA = PlacementUtils.createKey("underwater_magma");
/*     */   
/*  45 */   public static final ResourceKey<PlacedFeature> GLOW_LICHEN = PlacementUtils.createKey("glow_lichen");
/*     */   
/*  46 */   public static final ResourceKey<PlacedFeature> ROOTED_AZALEA_TREE = PlacementUtils.createKey("rooted_azalea_tree");
/*     */   
/*  47 */   public static final ResourceKey<PlacedFeature> ROOTED_SULFUR_SPRING = PlacementUtils.createKey("rooted_sulfur_spring");
/*     */   
/*  48 */   public static final ResourceKey<PlacedFeature> CAVE_VINES = PlacementUtils.createKey("cave_vines");
/*     */   
/*  49 */   public static final ResourceKey<PlacedFeature> LUSH_CAVES_VEGETATION = PlacementUtils.createKey("lush_caves_vegetation");
/*     */   
/*  50 */   public static final ResourceKey<PlacedFeature> LUSH_CAVES_CLAY = PlacementUtils.createKey("lush_caves_clay");
/*     */   
/*  51 */   public static final ResourceKey<PlacedFeature> LUSH_CAVES_CEILING_VEGETATION = PlacementUtils.createKey("lush_caves_ceiling_vegetation");
/*     */   
/*  52 */   public static final ResourceKey<PlacedFeature> SPORE_BLOSSOM = PlacementUtils.createKey("spore_blossom");
/*     */   
/*  53 */   public static final ResourceKey<PlacedFeature> CLASSIC_VINES = PlacementUtils.createKey("classic_vines_cave_feature");
/*     */   
/*  54 */   public static final ResourceKey<PlacedFeature> AMETHYST_GEODE = PlacementUtils.createKey("amethyst_geode");
/*     */   
/*  57 */   public static final ResourceKey<PlacedFeature> SCULK_PATCH_DEEP_DARK = PlacementUtils.createKey("sculk_patch_deep_dark");
/*     */   
/*  58 */   public static final ResourceKey<PlacedFeature> SCULK_PATCH_ANCIENT_CITY = PlacementUtils.createKey("sculk_patch_ancient_city");
/*     */   
/*  59 */   public static final ResourceKey<PlacedFeature> SCULK_VEIN = PlacementUtils.createKey("sculk_vein");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/*  62 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/*  63 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(CaveFeatures.MONSTER_ROOM);
/*  64 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(CaveFeatures.FOSSIL_COAL);
/*  65 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(CaveFeatures.FOSSIL_DIAMONDS);
/*  66 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(CaveFeatures.DRIPSTONE_CLUSTER);
/*  67 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(CaveFeatures.SULFUR_SPIKE_CLUSTER);
/*  68 */     Holder.Reference reference6 = configuredFeatures.getOrThrow(CaveFeatures.LARGE_DRIPSTONE);
/*  69 */     Holder.Reference reference7 = configuredFeatures.getOrThrow(CaveFeatures.POINTED_DRIPSTONE);
/*  70 */     Holder.Reference reference8 = configuredFeatures.getOrThrow(CaveFeatures.SULFUR_SPIKE);
/*  71 */     Holder.Reference reference9 = configuredFeatures.getOrThrow(CaveFeatures.UNDERWATER_MAGMA);
/*  72 */     Holder.Reference reference10 = configuredFeatures.getOrThrow(CaveFeatures.GLOW_LICHEN);
/*  73 */     Holder.Reference reference11 = configuredFeatures.getOrThrow(CaveFeatures.ROOTED_AZALEA_TREE);
/*  74 */     Holder.Reference reference12 = configuredFeatures.getOrThrow(CaveFeatures.ROOTED_SULFUR_SPRING);
/*  75 */     Holder.Reference reference13 = configuredFeatures.getOrThrow(CaveFeatures.CAVE_VINE);
/*  76 */     Holder.Reference reference14 = configuredFeatures.getOrThrow(CaveFeatures.MOSS_PATCH);
/*  77 */     Holder.Reference reference15 = configuredFeatures.getOrThrow(CaveFeatures.LUSH_CAVES_CLAY);
/*  78 */     Holder.Reference reference16 = configuredFeatures.getOrThrow(CaveFeatures.MOSS_PATCH_CEILING);
/*  79 */     Holder.Reference reference17 = configuredFeatures.getOrThrow(CaveFeatures.SPORE_BLOSSOM);
/*  80 */     Holder.Reference reference18 = configuredFeatures.getOrThrow(VegetationFeatures.VINES);
/*  81 */     Holder.Reference reference19 = configuredFeatures.getOrThrow(CaveFeatures.AMETHYST_GEODE);
/*  82 */     Holder.Reference reference20 = configuredFeatures.getOrThrow(CaveFeatures.SCULK_PATCH_DEEP_DARK);
/*  83 */     Holder.Reference reference21 = configuredFeatures.getOrThrow(CaveFeatures.SCULK_PATCH_ANCIENT_CITY);
/*  84 */     Holder.Reference reference22 = configuredFeatures.getOrThrow(CaveFeatures.SCULK_VEIN);
/*  87 */     PlacementUtils.register(context, MONSTER_ROOM, (Holder<ConfiguredFeature<?, ?>>)reference1, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(10), 
/*  89 */           (PlacementModifier)InSquarePlacement.spread(), 
/*  90 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.top()), 
/*  91 */           (PlacementModifier)BiomeFilter.biome() });
/*  93 */     PlacementUtils.register(context, MONSTER_ROOM_DEEP, (Holder<ConfiguredFeature<?, ?>>)reference1, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(4), 
/*  95 */           (PlacementModifier)InSquarePlacement.spread(), 
/*  96 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(-1)), 
/*  97 */           (PlacementModifier)BiomeFilter.biome() });
/* 102 */     PlacementUtils.register(context, FOSSIL_UPPER, (Holder<ConfiguredFeature<?, ?>>)reference2, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(64), 
/* 104 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 105 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.top()), 
/* 106 */           (PlacementModifier)BiomeFilter.biome() });
/* 108 */     PlacementUtils.register(context, FOSSIL_LOWER, (Holder<ConfiguredFeature<?, ?>>)reference3, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(64), 
/* 110 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 111 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(-8)), 
/* 112 */           (PlacementModifier)BiomeFilter.biome() });
/* 117 */     PlacementUtils.register(context, DRIPSTONE_CLUSTER, (Holder<ConfiguredFeature<?, ?>>)reference4, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(48, 96)), 
/* 119 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 121 */           (PlacementModifier)BiomeFilter.biome() });
/* 123 */     PlacementUtils.register(context, SULFUR_SPIKE_CLUSTER, (Holder<ConfiguredFeature<?, ?>>)reference5, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(48, 96)), 
/* 125 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 127 */           (PlacementModifier)BiomeFilter.biome() });
/* 129 */     PlacementUtils.register(context, LARGE_DRIPSTONE, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(10, 48)), 
/* 131 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 133 */           (PlacementModifier)BiomeFilter.biome() });
/* 135 */     PlacementUtils.register(context, POINTED_DRIPSTONE, (Holder<ConfiguredFeature<?, ?>>)reference7, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(192, 256)), 
/* 137 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 139 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(1, 5)), 
/* 140 */           (PlacementModifier)RandomOffsetPlacement.of(
/* 141 */             (IntProvider)ClampedNormalInt.of(0.0F, 3.0F, -10, 10), 
/* 142 */             (IntProvider)ClampedNormalInt.of(0.0F, 0.6F, -2, 2)), 
/*     */           
/* 144 */           (PlacementModifier)BiomeFilter.biome() });
/* 146 */     PlacementUtils.register(context, SULFUR_SPIKE, (Holder<ConfiguredFeature<?, ?>>)reference8, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(192, 256)), 
/* 148 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 150 */           (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(1, 5)), 
/* 151 */           (PlacementModifier)RandomOffsetPlacement.of(
/* 152 */             (IntProvider)ClampedNormalInt.of(0.0F, 3.0F, -10, 10), 
/* 153 */             (IntProvider)ClampedNormalInt.of(0.0F, 0.6F, -2, 2)), 
/*     */           
/* 155 */           (PlacementModifier)BiomeFilter.biome() });
/* 157 */     PlacementUtils.register(context, UNDERWATER_MAGMA, (Holder<ConfiguredFeature<?, ?>>)reference9, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(44, 52)), 
/* 159 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 161 */           (PlacementModifier)SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, -2147483648, -2), 
/* 162 */           (PlacementModifier)BiomeFilter.biome() });
/* 164 */     PlacementUtils.register(context, GLOW_LICHEN, (Holder<ConfiguredFeature<?, ?>>)reference10, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(104, 157)), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 167 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 168 */           (PlacementModifier)SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, -2147483648, -13), 
/* 169 */           (PlacementModifier)BiomeFilter.biome() });
/* 171 */     PlacementUtils.register(context, ROOTED_AZALEA_TREE, (Holder<ConfiguredFeature<?, ?>>)reference11, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(1, 2)), 
/* 173 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 175 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 176 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/* 177 */           (PlacementModifier)BiomeFilter.biome() });
/* 179 */     PlacementUtils.register(context, ROOTED_SULFUR_SPRING, (Holder<ConfiguredFeature<?, ?>>)reference12, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(1, 2)), 
/* 181 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 183 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 184 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/* 185 */           (PlacementModifier)BiomeFilter.biome() });
/* 187 */     PlacementUtils.register(context, CAVE_VINES, (Holder<ConfiguredFeature<?, ?>>)reference13, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(188), 
/* 189 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 191 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 192 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/* 193 */           (PlacementModifier)BiomeFilter.biome() });
/* 195 */     PlacementUtils.register(context, LUSH_CAVES_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference14, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(125), 
/* 197 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 199 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 200 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(1)), 
/* 201 */           (PlacementModifier)BiomeFilter.biome() });
/* 203 */     PlacementUtils.register(context, LUSH_CAVES_CLAY, (Holder<ConfiguredFeature<?, ?>>)reference15, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(62), 
/* 205 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 207 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 208 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(1)), 
/* 209 */           (PlacementModifier)BiomeFilter.biome() });
/* 211 */     PlacementUtils.register(context, LUSH_CAVES_CEILING_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference16, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(125), 
/* 213 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 215 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 216 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/* 217 */           (PlacementModifier)BiomeFilter.biome() });
/* 219 */     PlacementUtils.register(context, SPORE_BLOSSOM, (Holder<ConfiguredFeature<?, ?>>)reference17, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(25), 
/* 221 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 223 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), 
/* 224 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/* 225 */           (PlacementModifier)BiomeFilter.biome() });
/* 227 */     PlacementUtils.register(context, CLASSIC_VINES, (Holder<ConfiguredFeature<?, ?>>)reference18, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(256), 
/* 229 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 231 */           (PlacementModifier)BiomeFilter.biome() });
/* 233 */     PlacementUtils.register(context, AMETHYST_GEODE, (Holder<ConfiguredFeature<?, ?>>)reference19, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(24), 
/* 235 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 236 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(30)), 
/* 237 */           (PlacementModifier)BiomeFilter.biome() });
/* 240 */     PlacementUtils.register(context, SCULK_PATCH_DEEP_DARK, (Holder<ConfiguredFeature<?, ?>>)reference20, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)ConstantInt.of(256)), 
/* 242 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 244 */           (PlacementModifier)BiomeFilter.biome() });
/* 247 */     PlacementUtils.register(context, SCULK_PATCH_ANCIENT_CITY, (Holder<ConfiguredFeature<?, ?>>)reference21, new PlacementModifier[0]);
/* 249 */     PlacementUtils.register(context, SCULK_VEIN, (Holder<ConfiguredFeature<?, ?>>)reference22, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(204, 250)), 
/* 251 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 253 */           (PlacementModifier)BiomeFilter.biome() });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\placement\CavePlacements.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */