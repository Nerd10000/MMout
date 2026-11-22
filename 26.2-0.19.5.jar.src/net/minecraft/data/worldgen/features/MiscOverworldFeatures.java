/*     */ package net.minecraft.data.worldgen.features;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.random.Weighted;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.PotentSulfurBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.PotentSulfurState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.LakeFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockBlobConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.CompositeFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TemplateFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.WeightedRandomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class MiscOverworldFeatures {
/*  46 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_SPIKE = FeatureUtils.createKey("ice_spike");
/*     */   
/*  47 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_PATCH = FeatureUtils.createKey("ice_patch");
/*     */   
/*  48 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_ROCK = FeatureUtils.createKey("forest_rock");
/*     */   
/*  49 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ICEBERG_PACKED = FeatureUtils.createKey("iceberg_packed");
/*     */   
/*  50 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ICEBERG_BLUE = FeatureUtils.createKey("iceberg_blue");
/*     */   
/*  51 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BLUE_ICE = FeatureUtils.createKey("blue_ice");
/*     */   
/*  55 */   public static final ResourceKey<ConfiguredFeature<?, ?>> LAKE_LAVA = FeatureUtils.createKey("lake_lava");
/*     */   
/*  56 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_POOL = FeatureUtils.createKey("sulfur_pool");
/*     */   
/*  57 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_SPRING = FeatureUtils.createKey("sulfur_spring");
/*     */   
/*  59 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DISK_CLAY = FeatureUtils.createKey("disk_clay");
/*     */   
/*  60 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DISK_GRAVEL = FeatureUtils.createKey("disk_gravel");
/*     */   
/*  61 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DISK_SAND = FeatureUtils.createKey("disk_sand");
/*     */   
/*  62 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FREEZE_TOP_LAYER = FeatureUtils.createKey("freeze_top_layer");
/*     */   
/*  64 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DISK_GRASS = FeatureUtils.createKey("disk_grass");
/*     */   
/*  66 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BONUS_CHEST = FeatureUtils.createKey("bonus_chest");
/*     */   
/*  67 */   public static final ResourceKey<ConfiguredFeature<?, ?>> VOID_START_PLATFORM = FeatureUtils.createKey("void_start_platform");
/*     */   
/*  68 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DESERT_WELL = FeatureUtils.createKey("desert_well");
/*     */   
/*  72 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_LAVA_OVERWORLD = FeatureUtils.createKey("spring_lava_overworld");
/*     */   
/*  73 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_LAVA_FROZEN = FeatureUtils.createKey("spring_lava_frozen");
/*     */   
/*  74 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_WATER = FeatureUtils.createKey("spring_water");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
/*  78 */     FeatureUtils.register(context, ICE_SPIKE, Feature.SPIKE, new SpikeConfiguration(Blocks.PACKED_ICE
/*  79 */           .defaultBlockState(), 
/*  80 */           BlockPredicate.matchesBlocks(new Block[] { Blocks.SNOW_BLOCK }, ), BlockPredicate.matchesTag(BlockTags.ICE_SPIKE_REPLACEABLE)));
/*  83 */     FeatureUtils.register(context, ICE_PATCH, Feature.DISK, new DiskConfiguration(
/*  84 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PACKED_ICE), 
/*  85 */           BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.PODZOL, Blocks.COARSE_DIRT, Blocks.MYCELIUM, Blocks.SNOW_BLOCK, Blocks.ICE)), 
/*     */           
/*  94 */           (IntProvider)UniformInt.of(2, 3), 1));
/*  97 */     FeatureUtils.register(context, FOREST_ROCK, Feature.BLOCK_BLOB, new BlockBlobConfiguration(Blocks.MOSSY_COBBLESTONE
/*  98 */           .defaultBlockState(), 
/*  99 */           BlockPredicate.matchesTag(BlockTags.FOREST_ROCK_CAN_PLACE_ON)));
/* 101 */     FeatureUtils.register(context, ICEBERG_PACKED, Feature.ICEBERG, new BlockStateConfiguration(Blocks.PACKED_ICE
/* 102 */           .defaultBlockState()));
/* 104 */     FeatureUtils.register(context, ICEBERG_BLUE, Feature.ICEBERG, new BlockStateConfiguration(Blocks.BLUE_ICE
/* 105 */           .defaultBlockState()));
/* 107 */     FeatureUtils.register(context, BLUE_ICE, Feature.BLUE_ICE);
/* 111 */     FeatureUtils.register(context, LAKE_LAVA, Feature.LAKE, new LakeFeature.Configuration(
/* 112 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.LAVA.defaultBlockState()), 
/* 113 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.STONE.defaultBlockState()), 
/* 114 */           BlockPredicate.alwaysTrue(), 
/* 115 */           BlockPredicate.not(BlockPredicate.matchesTag(BlockTags.FEATURES_CANNOT_REPLACE)), 
/* 116 */           BlockPredicate.not(BlockPredicate.matchesTag(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE))));
/* 119 */     FeatureUtils.register(context, SULFUR_POOL, Feature.SEQUENCE, new CompositeFeatureConfiguration((HolderSet)HolderSet.direct(new Holder[] { PlacementUtils.inlinePlaced(Feature.LAKE, (FeatureConfiguration)new LakeFeature.Configuration(
/* 121 */                   (BlockStateProvider)BlockStateProvider.simple(Blocks.WATER.defaultBlockState()), 
/* 122 */                   (BlockStateProvider)BlockStateProvider.simple(Blocks.SULFUR.defaultBlockState()), 
/* 123 */                   BlockPredicate.not(BlockPredicate.matchesBlocks(new Block[] { Blocks.SULFUR_SPIKE }, )), BlockPredicate.not(BlockPredicate.matchesTag(BlockTags.FEATURES_CANNOT_REPLACE)), 
/* 125 */                   BlockPredicate.not(BlockPredicate.matchesTag(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE))), new PlacementModifier[0]), 
/*     */               
/* 127 */               PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration(
/*     */                   
/* 129 */                   (BlockStateProvider)BlockStateProvider.simple((BlockState)Blocks.POTENT_SULFUR.defaultBlockState().setValue((Property)PotentSulfurBlock.STATE, (Comparable)PotentSulfurState.WET))), new PlacementModifier[] { (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.DOWN, 
/* 132 */                     BlockPredicate.allOf(
/* 133 */                       BlockPredicate.solid(), 
/* 134 */                       BlockPredicate.matchesFluids(Direction.UP.getUnitVec3i(), new Fluid[] { (Fluid)Fluids.WATER })), 4) }) })));
/* 140 */     BiFunction<Integer, Integer, Holder<PlacedFeature>> tuffCover = (count, spread) -> PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.TUFF)), new PlacementModifier[] { (PlacementModifier)CountPlacement.of(count.intValue()), (PlacementModifier)RandomOffsetPlacement.ofTriangle(spread.intValue(), 3), (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), 4), (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.solid()) });
/* 148 */     Function<WeightedList<TemplateFeatureConfiguration.TemplateEntry>, Holder<PlacedFeature>> sulfurSprings = entries -> PlacementUtils.inlinePlaced(Feature.TEMPLATE, (FeatureConfiguration)new TemplateFeatureConfiguration(entries), new PlacementModifier[] { (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-7)) });
/* 153 */     FeatureUtils.register(context, SULFUR_SPRING, Feature.WEIGHTED_RANDOM_SELECTOR, new WeightedRandomFeatureConfiguration(WeightedList.of(new Weighted[] { new Weighted(
/* 154 */                 PlacementUtils.inlinePlaced(Feature.SEQUENCE, (FeatureConfiguration)new CompositeFeatureConfiguration((HolderSet)HolderSet.direct(new Holder[] { tuffCover.apply(Integer.valueOf(64), Integer.valueOf(7)), sulfurSprings
/* 156 */                         .apply(WeightedList.of((Object[])new TemplateFeatureConfiguration.TemplateEntry[] { TemplateFeatureConfiguration.TemplateEntry.of(Identifier.withDefaultNamespace("spring/sulfur_spring_small_1")), 
/* 158 */                               TemplateFeatureConfiguration.TemplateEntry.of(Identifier.withDefaultNamespace("spring/sulfur_spring_small_2")), 
/* 159 */                               TemplateFeatureConfiguration.TemplateEntry.of(Identifier.withDefaultNamespace("spring/sulfur_spring_small_3")), 
/* 160 */                               TemplateFeatureConfiguration.TemplateEntry.of(Identifier.withDefaultNamespace("spring/sulfur_spring_small_4")) })) })), new PlacementModifier[0]), 200), new Weighted(PlacementUtils.inlinePlaced(Feature.SEQUENCE, (FeatureConfiguration)new CompositeFeatureConfiguration((HolderSet)HolderSet.direct(new Holder[] { tuffCover.apply(Integer.valueOf(80), Integer.valueOf(8)), sulfurSprings
/* 164 */                         .apply(WeightedList.of((Object[])new TemplateFeatureConfiguration.TemplateEntry[] { TemplateFeatureConfiguration.TemplateEntry.of(Identifier.withDefaultNamespace("spring/sulfur_spring_medium_1")), 
/* 166 */                               TemplateFeatureConfiguration.TemplateEntry.of(Identifier.withDefaultNamespace("spring/sulfur_spring_medium_2")), 
/* 167 */                               TemplateFeatureConfiguration.TemplateEntry.of(Identifier.withDefaultNamespace("spring/sulfur_spring_medium_3")) })) })), new PlacementModifier[0]), 90), new Weighted(PlacementUtils.inlinePlaced(Feature.SEQUENCE, (FeatureConfiguration)new CompositeFeatureConfiguration((HolderSet)HolderSet.direct(new Holder[] { tuffCover.apply(Integer.valueOf(96), Integer.valueOf(9)), sulfurSprings
/* 171 */                         .apply(WeightedList.of((Object[])new TemplateFeatureConfiguration.TemplateEntry[] { TemplateFeatureConfiguration.TemplateEntry.of(Identifier.withDefaultNamespace("spring/sulfur_spring_large_1")), 
/* 173 */                               TemplateFeatureConfiguration.TemplateEntry.of(Identifier.withDefaultNamespace("spring/sulfur_spring_large_2")) })) })), new PlacementModifier[0]), 20), new Weighted(PlacementUtils.inlinePlaced(Feature.SEQUENCE, (FeatureConfiguration)new CompositeFeatureConfiguration((HolderSet)HolderSet.direct(new Holder[] { tuffCover.apply(Integer.valueOf(128), Integer.valueOf(10)), sulfurSprings
/* 177 */                         .apply(WeightedList.of(
/* 178 */                             TemplateFeatureConfiguration.TemplateEntry.of(Identifier.withDefaultNamespace("spring/sulfur_spring_extra_large_1")))) })), new PlacementModifier[0]), 5) })));
/* 182 */     FeatureUtils.register(context, DISK_CLAY, Feature.DISK, new DiskConfiguration(
/* 183 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.CLAY), 
/* 184 */           BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.CLAY)), 
/* 185 */           (IntProvider)UniformInt.of(2, 3), 1));
/* 188 */     FeatureUtils.register(context, DISK_GRAVEL, Feature.DISK, new DiskConfiguration(
/* 189 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.GRAVEL), 
/* 190 */           BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK)), 
/* 191 */           (IntProvider)UniformInt.of(2, 5), 2));
/* 194 */     FeatureUtils.register(context, DISK_SAND, Feature.DISK, new DiskConfiguration((BlockStateProvider)new RuleBasedStateProvider(
/* 195 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.SAND), 
/* 196 */             List.of(new RuleBasedStateProvider.Rule(
/* 197 */                 BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.AIR }), (BlockStateProvider)BlockStateProvider.simple(Blocks.SANDSTONE)))), 
/*     */           
/* 201 */           BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK)), 
/* 202 */           (IntProvider)UniformInt.of(2, 6), 2));
/* 205 */     FeatureUtils.register(context, FREEZE_TOP_LAYER, Feature.FREEZE_TOP_LAYER);
/* 207 */     FeatureUtils.register(context, DISK_GRASS, Feature.DISK, new DiskConfiguration((BlockStateProvider)new RuleBasedStateProvider(
/* 208 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.DIRT), 
/* 209 */             List.of(new RuleBasedStateProvider.Rule(
/* 210 */                 BlockPredicate.not(BlockPredicate.anyOf(
/* 211 */                     BlockPredicate.solid(Direction.UP.getUnitVec3i()), 
/* 212 */                     BlockPredicate.matchesFluids(Direction.UP.getUnitVec3i(), new Fluid[] { (Fluid)Fluids.WATER }))), (BlockStateProvider)BlockStateProvider.simple(Blocks.GRASS_BLOCK)))), 
/*     */           
/* 217 */           BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.MUD)), 
/* 218 */           (IntProvider)UniformInt.of(2, 6), 2));
/* 224 */     FeatureUtils.register(context, BONUS_CHEST, (Feature<NoneFeatureConfiguration>)Feature.BONUS_CHEST);
/* 225 */     FeatureUtils.register(context, VOID_START_PLATFORM, Feature.VOID_START_PLATFORM);
/* 226 */     FeatureUtils.register(context, DESERT_WELL, Feature.DESERT_WELL);
/* 230 */     FeatureUtils.register(context, SPRING_LAVA_OVERWORLD, Feature.SPRING, new SpringConfiguration(Fluids.LAVA
/* 231 */           .defaultFluidState(), true, 4, 1, 
/*     */           
/* 235 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DEEPSLATE, Blocks.TUFF, Blocks.CALCITE, Blocks.DIRT })));
/* 237 */     FeatureUtils.register(context, SPRING_LAVA_FROZEN, Feature.SPRING, new SpringConfiguration(Fluids.LAVA
/* 238 */           .defaultFluidState(), true, 4, 1, 
/*     */           
/* 242 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW, Blocks.PACKED_ICE })));
/* 244 */     FeatureUtils.register(context, SPRING_WATER, Feature.SPRING, new SpringConfiguration(Fluids.WATER
/* 245 */           .defaultFluidState(), true, 4, 1, 
/*     */           
/* 249 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { 
/*     */               Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DEEPSLATE, Blocks.TUFF, Blocks.CALCITE, Blocks.DIRT, Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW, 
/*     */               Blocks.PACKED_ICE })));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\features\MiscOverworldFeatures.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */