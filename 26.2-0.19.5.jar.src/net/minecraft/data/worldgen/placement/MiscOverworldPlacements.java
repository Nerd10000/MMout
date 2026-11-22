/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.VeryBiasedToBottomHeight;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.RarityFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class MiscOverworldPlacements {
/*  33 */   public static final ResourceKey<PlacedFeature> ICE_SPIKE = PlacementUtils.createKey("ice_spike");
/*     */   
/*  34 */   public static final ResourceKey<PlacedFeature> ICE_PATCH = PlacementUtils.createKey("ice_patch");
/*     */   
/*  35 */   public static final ResourceKey<PlacedFeature> FOREST_ROCK = PlacementUtils.createKey("forest_rock");
/*     */   
/*  36 */   public static final ResourceKey<PlacedFeature> ICEBERG_PACKED = PlacementUtils.createKey("iceberg_packed");
/*     */   
/*  37 */   public static final ResourceKey<PlacedFeature> ICEBERG_BLUE = PlacementUtils.createKey("iceberg_blue");
/*     */   
/*  38 */   public static final ResourceKey<PlacedFeature> BLUE_ICE = PlacementUtils.createKey("blue_ice");
/*     */   
/*  40 */   public static final ResourceKey<PlacedFeature> LAKE_LAVA_UNDERGROUND = PlacementUtils.createKey("lake_lava_underground");
/*     */   
/*  41 */   public static final ResourceKey<PlacedFeature> LAKE_LAVA_SURFACE = PlacementUtils.createKey("lake_lava_surface");
/*     */   
/*  42 */   public static final ResourceKey<PlacedFeature> SULFUR_POOL = PlacementUtils.createKey("sulfur_pool");
/*     */   
/*  44 */   public static final ResourceKey<PlacedFeature> DISK_CLAY = PlacementUtils.createKey("disk_clay");
/*     */   
/*  45 */   public static final ResourceKey<PlacedFeature> DISK_GRAVEL = PlacementUtils.createKey("disk_gravel");
/*     */   
/*  46 */   public static final ResourceKey<PlacedFeature> DISK_SAND = PlacementUtils.createKey("disk_sand");
/*     */   
/*  48 */   public static final ResourceKey<PlacedFeature> DISK_GRASS = PlacementUtils.createKey("disk_grass");
/*     */   
/*  50 */   public static final ResourceKey<PlacedFeature> FREEZE_TOP_LAYER = PlacementUtils.createKey("freeze_top_layer");
/*     */   
/*  51 */   public static final ResourceKey<PlacedFeature> VOID_START_PLATFORM = PlacementUtils.createKey("void_start_platform");
/*     */   
/*  53 */   public static final ResourceKey<PlacedFeature> DESERT_WELL = PlacementUtils.createKey("desert_well");
/*     */   
/*  55 */   public static final ResourceKey<PlacedFeature> SPRING_LAVA = PlacementUtils.createKey("spring_lava");
/*     */   
/*  56 */   public static final ResourceKey<PlacedFeature> SPRING_LAVA_FROZEN = PlacementUtils.createKey("spring_lava_frozen");
/*     */   
/*  57 */   public static final ResourceKey<PlacedFeature> SPRING_WATER = PlacementUtils.createKey("spring_water");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/*  60 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/*  61 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(MiscOverworldFeatures.ICE_SPIKE);
/*  62 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(MiscOverworldFeatures.ICE_PATCH);
/*  63 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(MiscOverworldFeatures.FOREST_ROCK);
/*  64 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(MiscOverworldFeatures.ICEBERG_PACKED);
/*  65 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(MiscOverworldFeatures.ICEBERG_BLUE);
/*  66 */     Holder.Reference reference6 = configuredFeatures.getOrThrow(MiscOverworldFeatures.BLUE_ICE);
/*  67 */     Holder.Reference reference7 = configuredFeatures.getOrThrow(MiscOverworldFeatures.LAKE_LAVA);
/*  68 */     Holder.Reference reference8 = configuredFeatures.getOrThrow(MiscOverworldFeatures.SULFUR_POOL);
/*  69 */     Holder.Reference reference9 = configuredFeatures.getOrThrow(MiscOverworldFeatures.DISK_CLAY);
/*  70 */     Holder.Reference reference10 = configuredFeatures.getOrThrow(MiscOverworldFeatures.DISK_GRAVEL);
/*  71 */     Holder.Reference reference11 = configuredFeatures.getOrThrow(MiscOverworldFeatures.DISK_SAND);
/*  72 */     Holder.Reference reference12 = configuredFeatures.getOrThrow(MiscOverworldFeatures.DISK_GRASS);
/*  73 */     Holder.Reference reference13 = configuredFeatures.getOrThrow(MiscOverworldFeatures.FREEZE_TOP_LAYER);
/*  74 */     Holder.Reference reference14 = configuredFeatures.getOrThrow(MiscOverworldFeatures.VOID_START_PLATFORM);
/*  75 */     Holder.Reference reference15 = configuredFeatures.getOrThrow(MiscOverworldFeatures.DESERT_WELL);
/*  76 */     Holder.Reference reference16 = configuredFeatures.getOrThrow(MiscOverworldFeatures.SPRING_LAVA_OVERWORLD);
/*  77 */     Holder.Reference reference17 = configuredFeatures.getOrThrow(MiscOverworldFeatures.SPRING_LAVA_FROZEN);
/*  78 */     Holder.Reference reference18 = configuredFeatures.getOrThrow(MiscOverworldFeatures.SPRING_WATER);
/*  80 */     PlacementUtils.register(context, ICE_SPIKE, (Holder<ConfiguredFeature<?, ?>>)reference1, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(3), 
/*  82 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/*  84 */           (PlacementModifier)BiomeFilter.biome() });
/*  86 */     PlacementUtils.register(context, ICE_PATCH, (Holder<ConfiguredFeature<?, ?>>)reference2, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(2), 
/*  88 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/*  90 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/*  91 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(new Block[] { Blocks.SNOW_BLOCK })), (PlacementModifier)BiomeFilter.biome() });
/*  94 */     PlacementUtils.register(context, FOREST_ROCK, (Holder<ConfiguredFeature<?, ?>>)reference3, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(2), 
/*  96 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/*  98 */           (PlacementModifier)BiomeFilter.biome() });
/* 100 */     PlacementUtils.register(context, ICEBERG_BLUE, (Holder<ConfiguredFeature<?, ?>>)reference5, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(200), 
/* 102 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 103 */           (PlacementModifier)BiomeFilter.biome() });
/* 105 */     PlacementUtils.register(context, ICEBERG_PACKED, (Holder<ConfiguredFeature<?, ?>>)reference4, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(16), 
/* 107 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 108 */           (PlacementModifier)BiomeFilter.biome() });
/* 110 */     PlacementUtils.register(context, BLUE_ICE, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)UniformInt.of(0, 19)), 
/* 112 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 113 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.absolute(30), VerticalAnchor.absolute(61)), 
/* 114 */           (PlacementModifier)BiomeFilter.biome() });
/* 117 */     PlacementUtils.register(context, LAKE_LAVA_UNDERGROUND, (Holder<ConfiguredFeature<?, ?>>)reference7, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(9), 
/* 119 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 120 */           (PlacementModifier)HeightRangePlacement.of((HeightProvider)UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.top())), 
/* 121 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.DOWN, 
/*     */             
/* 123 */             BlockPredicate.allOf(
/* 124 */               BlockPredicate.not(BlockPredicate.ONLY_IN_AIR_PREDICATE), 
/* 125 */               BlockPredicate.insideWorld((Vec3i)new BlockPos(0, -5, 0))), 32), 
/*     */           
/* 129 */           (PlacementModifier)SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, -2147483648, -5), 
/* 130 */           (PlacementModifier)BiomeFilter.biome() });
/* 133 */     PlacementUtils.register(context, LAKE_LAVA_SURFACE, (Holder<ConfiguredFeature<?, ?>>)reference7, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(200), 
/* 135 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, 
/*     */           
/* 137 */           (PlacementModifier)BiomeFilter.biome() });
/* 140 */     PlacementUtils.register(context, SULFUR_POOL, (Holder<ConfiguredFeature<?, ?>>)reference8, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(256), 
/* 142 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, 
/*     */           
/* 145 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.solid()), 
/*     */           
/* 147 */           (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.ONLY_IN_AIR_PREDICATE, 32), 
/*     */           
/* 152 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/*     */           
/* 154 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(new Block[] { Blocks.SULFUR })), (PlacementModifier)BiomeFilter.biome() });
/* 158 */     PlacementUtils.register(context, DISK_CLAY, (Holder<ConfiguredFeature<?, ?>>)reference9, new PlacementModifier[] { (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*     */           
/* 161 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(new Fluid[] { (Fluid)Fluids.WATER })), (PlacementModifier)BiomeFilter.biome() });
/* 164 */     PlacementUtils.register(context, DISK_GRAVEL, (Holder<ConfiguredFeature<?, ?>>)reference10, new PlacementModifier[] { (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*     */           
/* 167 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(new Fluid[] { (Fluid)Fluids.WATER })), (PlacementModifier)BiomeFilter.biome() });
/* 170 */     PlacementUtils.register(context, DISK_SAND, (Holder<ConfiguredFeature<?, ?>>)reference11, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(3), 
/* 172 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*     */           
/* 174 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(new Fluid[] { (Fluid)Fluids.WATER })), (PlacementModifier)BiomeFilter.biome() });
/* 178 */     PlacementUtils.register(context, DISK_GRASS, (Holder<ConfiguredFeature<?, ?>>)reference12, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(1), 
/* 180 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, 
/*     */           
/* 182 */           (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)), 
/* 183 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(new Block[] { Blocks.MUD })), (PlacementModifier)BiomeFilter.biome() });
/* 187 */     PlacementUtils.register(context, FREEZE_TOP_LAYER, (Holder<ConfiguredFeature<?, ?>>)reference13, new PlacementModifier[] { (PlacementModifier)BiomeFilter.biome() });
/* 190 */     PlacementUtils.register(context, VOID_START_PLATFORM, (Holder<ConfiguredFeature<?, ?>>)reference14, new PlacementModifier[] { (PlacementModifier)BiomeFilter.biome() });
/* 194 */     PlacementUtils.register(context, DESERT_WELL, (Holder<ConfiguredFeature<?, ?>>)reference15, new PlacementModifier[] { (PlacementModifier)RarityFilter.onAverageOnceEvery(1000), 
/* 196 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, 
/*     */           
/* 198 */           (PlacementModifier)BiomeFilter.biome() });
/* 201 */     PlacementUtils.register(context, SPRING_LAVA, (Holder<ConfiguredFeature<?, ?>>)reference16, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(20), 
/* 203 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 204 */           (PlacementModifier)HeightRangePlacement.of((HeightProvider)VeryBiasedToBottomHeight.of(VerticalAnchor.bottom(), VerticalAnchor.belowTop(8), 8)), 
/* 205 */           (PlacementModifier)BiomeFilter.biome() });
/* 207 */     PlacementUtils.register(context, SPRING_LAVA_FROZEN, (Holder<ConfiguredFeature<?, ?>>)reference17, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(20), 
/* 209 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 210 */           (PlacementModifier)HeightRangePlacement.of((HeightProvider)VeryBiasedToBottomHeight.of(VerticalAnchor.bottom(), VerticalAnchor.belowTop(8), 8)), 
/* 211 */           (PlacementModifier)BiomeFilter.biome() });
/* 213 */     PlacementUtils.register(context, SPRING_WATER, (Holder<ConfiguredFeature<?, ?>>)reference18, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(25), 
/* 215 */           (PlacementModifier)InSquarePlacement.spread(), 
/* 216 */           (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(192)), 
/* 217 */           (PlacementModifier)BiomeFilter.biome() });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\placement\MiscOverworldPlacements.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */