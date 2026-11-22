/*     */ package net.minecraft.data.worldgen.features;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.ProcessorLists;
/*     */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.ClampedInt;
/*     */ import net.minecraft.util.valueproviders.ClampedNormalFloat;
/*     */ import net.minecraft.util.valueproviders.ConstantFloat;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformFloat;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.util.valueproviders.WeightedListInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CaveVines;
/*     */ import net.minecraft.world.level.block.CaveVinesBlock;
/*     */ import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
/*     */ import net.minecraft.world.level.block.SmallDripleafBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.GeodeBlockSettings;
/*     */ import net.minecraft.world.level.levelgen.GeodeCrackSettings;
/*     */ import net.minecraft.world.level.levelgen.GeodeLayerSettings;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.FossilFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.CompositeFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RootSystemConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpeleothemClusterConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpeleothemConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.UnderwaterMagmaConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
/*     */ import net.minecraft.world.level.levelgen.placement.CaveSurface;
/*     */ import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ 
/*     */ public class CaveFeatures {
/*  66 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MONSTER_ROOM = FeatureUtils.createKey("monster_room");
/*     */   
/*  68 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FOSSIL_COAL = FeatureUtils.createKey("fossil_coal");
/*     */   
/*  69 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FOSSIL_DIAMONDS = FeatureUtils.createKey("fossil_diamonds");
/*     */   
/*  72 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DRIPSTONE_CLUSTER = FeatureUtils.createKey("dripstone_cluster");
/*     */   
/*  73 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_SPIKE_CLUSTER = FeatureUtils.createKey("sulfur_spike_cluster");
/*     */   
/*  75 */   public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_DRIPSTONE = FeatureUtils.createKey("large_dripstone");
/*     */   
/*  77 */   public static final ResourceKey<ConfiguredFeature<?, ?>> POINTED_DRIPSTONE = FeatureUtils.createKey("pointed_dripstone");
/*     */   
/*  78 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_SPIKE = FeatureUtils.createKey("sulfur_spike");
/*     */   
/*  80 */   public static final ResourceKey<ConfiguredFeature<?, ?>> UNDERWATER_MAGMA = FeatureUtils.createKey("underwater_magma");
/*     */   
/*  82 */   public static final ResourceKey<ConfiguredFeature<?, ?>> GLOW_LICHEN = FeatureUtils.createKey("glow_lichen");
/*     */   
/*  84 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ROOTED_AZALEA_TREE = FeatureUtils.createKey("rooted_azalea_tree");
/*     */   
/*  85 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ROOTED_SULFUR_SPRING = FeatureUtils.createKey("rooted_sulfur_spring");
/*     */   
/*  87 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_VINE = FeatureUtils.createKey("cave_vine");
/*     */   
/*  88 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_VINE_IN_MOSS = FeatureUtils.createKey("cave_vine_in_moss");
/*     */   
/*  89 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_VEGETATION = FeatureUtils.createKey("moss_vegetation");
/*     */   
/*  90 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_PATCH = FeatureUtils.createKey("moss_patch");
/*     */   
/*  91 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_PATCH_BONEMEAL = FeatureUtils.createKey("moss_patch_bonemeal");
/*     */   
/*     */   private static Holder<PlacedFeature> makeDripleaf(Direction direction) {
/*  94 */     return PlacementUtils.inlinePlaced(Feature.BLOCK_COLUMN, (FeatureConfiguration)new BlockColumnConfiguration(
/*     */           
/*  97 */           List.of(
/*  98 */             BlockColumnConfiguration.layer((IntProvider)new WeightedListInt(
/*  99 */                 WeightedList.builder()
/* 100 */                 .add(UniformInt.of(0, 4), 2)
/* 101 */                 .add(ConstantInt.of(0), 1)
/* 102 */                 .build()), 
/* 103 */               (BlockStateProvider)BlockStateProvider.simple((BlockState)Blocks.BIG_DRIPLEAF_STEM.defaultBlockState().setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)direction))), 
/* 104 */             BlockColumnConfiguration.layer(
/* 105 */               (IntProvider)ConstantInt.of(1), 
/* 106 */               (BlockStateProvider)BlockStateProvider.simple((BlockState)Blocks.BIG_DRIPLEAF.defaultBlockState().setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)direction)))), Direction.UP, BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, true), new PlacementModifier[0]);
/*     */   }
/*     */   
/*     */   private static Holder<PlacedFeature> makeSmallDripleaf() {
/* 116 */     return PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(
/*     */             
/* 119 */             WeightedList.builder()
/* 120 */             .add(Blocks.SMALL_DRIPLEAF.defaultBlockState().setValue((Property)SmallDripleafBlock.FACING, (Comparable)Direction.EAST), 1)
/* 121 */             .add(Blocks.SMALL_DRIPLEAF.defaultBlockState().setValue((Property)SmallDripleafBlock.FACING, (Comparable)Direction.WEST), 1)
/* 122 */             .add(Blocks.SMALL_DRIPLEAF.defaultBlockState().setValue((Property)SmallDripleafBlock.FACING, (Comparable)Direction.NORTH), 1)
/* 123 */             .add(Blocks.SMALL_DRIPLEAF.defaultBlockState().setValue((Property)SmallDripleafBlock.FACING, (Comparable)Direction.SOUTH), 1))), new PlacementModifier[0]);
/*     */   }
/*     */   
/* 129 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DRIPLEAF = FeatureUtils.createKey("dripleaf");
/*     */   
/* 131 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CLAY_WITH_DRIPLEAVES = FeatureUtils.createKey("clay_with_dripleaves");
/*     */   
/* 132 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CLAY_POOL_WITH_DRIPLEAVES = FeatureUtils.createKey("clay_pool_with_dripleaves");
/*     */   
/* 133 */   public static final ResourceKey<ConfiguredFeature<?, ?>> LUSH_CAVES_CLAY = FeatureUtils.createKey("lush_caves_clay");
/*     */   
/* 135 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_PATCH_CEILING = FeatureUtils.createKey("moss_patch_ceiling");
/*     */   
/* 136 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPORE_BLOSSOM = FeatureUtils.createKey("spore_blossom");
/*     */   
/* 138 */   public static final ResourceKey<ConfiguredFeature<?, ?>> AMETHYST_GEODE = FeatureUtils.createKey("amethyst_geode");
/*     */   
/* 140 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SCULK_PATCH_DEEP_DARK = FeatureUtils.createKey("sculk_patch_deep_dark");
/*     */   
/* 142 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SCULK_PATCH_ANCIENT_CITY = FeatureUtils.createKey("sculk_patch_ancient_city");
/*     */   
/* 144 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SCULK_VEIN = FeatureUtils.createKey("sculk_vein");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
/* 147 */     HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
/* 148 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/* 149 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/* 153 */     FeatureUtils.register(context, MONSTER_ROOM, Feature.MONSTER_ROOM);
/* 157 */     List<Identifier> fossilStructures = List.of(
/* 158 */         Identifier.withDefaultNamespace("fossil/spine_1"), 
/* 159 */         Identifier.withDefaultNamespace("fossil/spine_2"), 
/* 160 */         Identifier.withDefaultNamespace("fossil/spine_3"), 
/* 161 */         Identifier.withDefaultNamespace("fossil/spine_4"), 
/* 162 */         Identifier.withDefaultNamespace("fossil/skull_1"), 
/* 163 */         Identifier.withDefaultNamespace("fossil/skull_2"), 
/* 164 */         Identifier.withDefaultNamespace("fossil/skull_3"), 
/* 165 */         Identifier.withDefaultNamespace("fossil/skull_4"));
/* 167 */     List<Identifier> fossilCoalStructures = List.of(
/* 168 */         Identifier.withDefaultNamespace("fossil/spine_1_coal"), 
/* 169 */         Identifier.withDefaultNamespace("fossil/spine_2_coal"), 
/* 170 */         Identifier.withDefaultNamespace("fossil/spine_3_coal"), 
/* 171 */         Identifier.withDefaultNamespace("fossil/spine_4_coal"), 
/* 172 */         Identifier.withDefaultNamespace("fossil/skull_1_coal"), 
/* 173 */         Identifier.withDefaultNamespace("fossil/skull_2_coal"), 
/* 174 */         Identifier.withDefaultNamespace("fossil/skull_3_coal"), 
/* 175 */         Identifier.withDefaultNamespace("fossil/skull_4_coal"));
/* 178 */     Holder.Reference reference = processorLists.getOrThrow(ProcessorLists.FOSSIL_ROT);
/* 180 */     FeatureUtils.register(context, FOSSIL_COAL, Feature.FOSSIL, new FossilFeatureConfiguration(fossilStructures, fossilCoalStructures, (Holder)reference, (Holder)processorLists
/*     */           
/* 184 */           .getOrThrow(ProcessorLists.FOSSIL_COAL), 4));
/* 187 */     FeatureUtils.register(context, FOSSIL_DIAMONDS, Feature.FOSSIL, new FossilFeatureConfiguration(fossilStructures, fossilCoalStructures, (Holder)reference, (Holder)processorLists
/*     */           
/* 191 */           .getOrThrow(ProcessorLists.FOSSIL_DIAMONDS), 4));
/* 197 */     FeatureUtils.register(context, DRIPSTONE_CLUSTER, Feature.SPELEOTHEM_CLUSTER, new SpeleothemClusterConfiguration(Blocks.DRIPSTONE_BLOCK
/* 198 */           .defaultBlockState(), Blocks.POINTED_DRIPSTONE
/* 199 */           .defaultBlockState(), (HolderSet)blocks
/* 200 */           .getOrThrow(BlockTags.DRIPSTONE_REPLACEABLE), 12, 
/*     */           
/* 202 */           (IntProvider)UniformInt.of(3, 6), 
/* 203 */           (IntProvider)UniformInt.of(2, 8), 1, 3, 
/*     */           
/* 206 */           (IntProvider)UniformInt.of(2, 4), 
/* 207 */           (FloatProvider)UniformFloat.of(0.3F, 0.7F), 
/* 208 */           (FloatProvider)ClampedNormalFloat.of(0.1F, 0.3F, 0.1F, 0.9F), 0.1F, 3, 8));
/* 214 */     FeatureUtils.register(context, LARGE_DRIPSTONE, Feature.LARGE_DRIPSTONE, new LargeDripstoneConfiguration((HolderSet)blocks
/* 215 */           .getOrThrow(BlockTags.DRIPSTONE_REPLACEABLE), 30, 
/*     */           
/* 217 */           (IntProvider)ClampedInt.of((IntProvider)UniformInt.of(3, 19), 3, 16), 
/* 218 */           (FloatProvider)UniformFloat.of(0.4F, 2.0F), 0.33F, 
/*     */           
/* 220 */           (FloatProvider)UniformFloat.of(0.3F, 0.9F), 
/* 221 */           (FloatProvider)UniformFloat.of(0.4F, 1.0F), 
/* 222 */           (FloatProvider)UniformFloat.of(0.0F, 0.3F), 4, 0.6F));
/* 227 */     FeatureUtils.register(context, POINTED_DRIPSTONE, Feature.SIMPLE_RANDOM_SELECTOR, new CompositeFeatureConfiguration((HolderSet)HolderSet.direct(new Holder[] { PlacementUtils.inlinePlaced(Feature.SPELEOTHEM, (FeatureConfiguration)new SpeleothemConfiguration(Blocks.DRIPSTONE_BLOCK
/*     */                   
/* 231 */                   .defaultBlockState(), Blocks.POINTED_DRIPSTONE
/* 232 */                   .defaultBlockState(), (HolderSet)blocks
/* 233 */                   .getOrThrow(BlockTags.DRIPSTONE_REPLACEABLE), 0.2F, 0.7F, 0.5F, 0.5F), new PlacementModifier[] { (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12), 
/* 240 */                   (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(1)) }), PlacementUtils.inlinePlaced(Feature.SPELEOTHEM, (FeatureConfiguration)new SpeleothemConfiguration(Blocks.DRIPSTONE_BLOCK
/*     */                   
/* 245 */                   .defaultBlockState(), Blocks.POINTED_DRIPSTONE
/* 246 */                   .defaultBlockState(), (HolderSet)blocks
/* 247 */                   .getOrThrow(BlockTags.DRIPSTONE_REPLACEABLE), 0.2F, 0.7F, 0.5F, 0.5F), new PlacementModifier[] { (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12), 
/* 254 */                   (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)) }) })));
/* 258 */     FeatureUtils.register(context, SULFUR_SPIKE_CLUSTER, Feature.SPELEOTHEM_CLUSTER, new SpeleothemClusterConfiguration(Blocks.SULFUR
/* 259 */           .defaultBlockState(), Blocks.SULFUR_SPIKE
/* 260 */           .defaultBlockState(), (HolderSet)blocks
/* 261 */           .getOrThrow(BlockTags.SULFUR_SPIKE_REPLACEABLE), 12, 
/*     */           
/* 263 */           (IntProvider)UniformInt.of(1, 4), 
/* 264 */           (IntProvider)UniformInt.of(2, 8), 1, 3, 
/*     */           
/* 267 */           (IntProvider)UniformInt.of(2, 4), 
/* 268 */           (FloatProvider)UniformFloat.of(0.3F, 0.7F), (FloatProvider)ConstantFloat.ZERO, 0.1F, 3, 8));
/* 275 */     FeatureUtils.register(context, SULFUR_SPIKE, Feature.SIMPLE_RANDOM_SELECTOR, new CompositeFeatureConfiguration((HolderSet)HolderSet.direct(new Holder[] { PlacementUtils.inlinePlaced(Feature.SPELEOTHEM, (FeatureConfiguration)new SpeleothemConfiguration(Blocks.SULFUR
/*     */                   
/* 279 */                   .defaultBlockState(), Blocks.SULFUR_SPIKE
/* 280 */                   .defaultBlockState(), (HolderSet)blocks
/* 281 */                   .getOrThrow(BlockTags.SULFUR_SPIKE_REPLACEABLE), 0.2F, 0.7F, 0.5F, 0.5F), new PlacementModifier[] { (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12), 
/* 288 */                   (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(1)) }), PlacementUtils.inlinePlaced(Feature.SPELEOTHEM, (FeatureConfiguration)new SpeleothemConfiguration(Blocks.SULFUR
/*     */                   
/* 293 */                   .defaultBlockState(), Blocks.SULFUR_SPIKE
/* 294 */                   .defaultBlockState(), (HolderSet)blocks
/* 295 */                   .getOrThrow(BlockTags.SULFUR_SPIKE_REPLACEABLE), 0.2F, 0.7F, 0.5F, 0.5F), new PlacementModifier[] { (PlacementModifier)EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12), 
/* 302 */                   (PlacementModifier)RandomOffsetPlacement.vertical((IntProvider)ConstantInt.of(-1)) }) })));
/* 306 */     FeatureUtils.register(context, UNDERWATER_MAGMA, Feature.UNDERWATER_MAGMA, new UnderwaterMagmaConfiguration(5, 1, 0.5F));
/* 312 */     MultifaceSpreadeableBlock glowLichenBlock = (MultifaceSpreadeableBlock)Blocks.GLOW_LICHEN;
/* 313 */     FeatureUtils.register(context, GLOW_LICHEN, Feature.MULTIFACE_GROWTH, new MultifaceGrowthConfiguration((Block)glowLichenBlock, 20, false, true, true, 0.5F, 
/*     */           
/* 320 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.STONE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.DRIPSTONE_BLOCK, Blocks.CALCITE, Blocks.TUFF, Blocks.DEEPSLATE, Blocks.SULFUR, Blocks.CINNABAR })));
/* 334 */     FeatureUtils.register(context, ROOTED_AZALEA_TREE, Feature.ROOT_SYSTEM, new RootSystemConfiguration(
/* 335 */           PlacementUtils.inlinePlaced((Holder)configuredFeatures.getOrThrow(TreeFeatures.AZALEA_TREE), new PlacementModifier[0]), 3, 0, 0, 3, (HolderSet)blocks
/*     */           
/* 340 */           .getOrThrow(BlockTags.AZALEA_ROOT_REPLACEABLE), 
/* 341 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.ROOTED_DIRT), 20, 100, 3, 2, 
/*     */           
/* 346 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.HANGING_ROOTS), 20, 2, 
/*     */           
/* 349 */           BlockPredicate.allOf(
/*     */             
/* 351 */             BlockPredicate.anyOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */               
/* 353 */               BlockPredicate.matchesTag(BlockTags.REPLACEABLE_BY_TREES)), 
/*     */             
/* 355 */             BlockPredicate.matchesTag(Direction.DOWN.getUnitVec3i(), BlockTags.AZALEA_GROWS_ON))));
/* 359 */     FeatureUtils.register(context, ROOTED_SULFUR_SPRING, Feature.ROOT_SYSTEM, new RootSystemConfiguration(
/* 360 */           PlacementUtils.inlinePlaced((Holder)configuredFeatures.getOrThrow(MiscOverworldFeatures.SULFUR_SPRING), new PlacementModifier[0]), 5, 8, 2, 3, (HolderSet)blocks
/*     */           
/* 365 */           .getOrThrow(BlockTags.AZALEA_ROOT_REPLACEABLE), 
/* 366 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SULFUR), 20, 184, 1, 1, 
/*     */           
/* 371 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SULFUR), 1, 1, BlockPredicate.ONLY_IN_AIR_PREDICATE));
/* 379 */     WeightedStateProvider caveVinesBodyProvider = new WeightedStateProvider(WeightedList.builder().add(Blocks.CAVE_VINES_PLANT.defaultBlockState(), 4).add(Blocks.CAVE_VINES_PLANT.defaultBlockState().setValue((Property)CaveVines.BERRIES, Boolean.valueOf(true)), 1));
/* 387 */     RandomizedIntStateProvider caveVinesHeadProvider = new RandomizedIntStateProvider((BlockStateProvider)new WeightedStateProvider(WeightedList.builder().add(Blocks.CAVE_VINES.defaultBlockState(), 4).add(Blocks.CAVE_VINES.defaultBlockState().setValue((Property)CaveVines.BERRIES, Boolean.valueOf(true)), 1)), CaveVinesBlock.AGE, (IntProvider)UniformInt.of(23, 25));
/* 389 */     FeatureUtils.register(context, CAVE_VINE, Feature.BLOCK_COLUMN, new BlockColumnConfiguration(List.of(
/* 390 */             BlockColumnConfiguration.layer((IntProvider)new WeightedListInt(
/* 391 */                 WeightedList.builder()
/* 392 */                 .add(UniformInt.of(0, 19), 2)
/* 393 */                 .add(UniformInt.of(0, 2), 3)
/* 394 */                 .add(UniformInt.of(0, 6), 10)
/* 395 */                 .build()), (BlockStateProvider)caveVinesBodyProvider), 
/*     */             
/* 397 */             BlockColumnConfiguration.layer(
/* 398 */               (IntProvider)ConstantInt.of(1), (BlockStateProvider)caveVinesHeadProvider)), Direction.DOWN, BlockPredicate.ONLY_IN_AIR_PREDICATE, true));
/* 405 */     FeatureUtils.register(context, CAVE_VINE_IN_MOSS, Feature.BLOCK_COLUMN, new BlockColumnConfiguration(List.of(
/* 406 */             BlockColumnConfiguration.layer((IntProvider)new WeightedListInt(
/* 407 */                 WeightedList.builder()
/* 408 */                 .add(UniformInt.of(0, 3), 5)
/* 409 */                 .add(UniformInt.of(1, 7), 1)
/* 410 */                 .build()), (BlockStateProvider)caveVinesBodyProvider), 
/*     */             
/* 412 */             BlockColumnConfiguration.layer(
/* 413 */               (IntProvider)ConstantInt.of(1), (BlockStateProvider)caveVinesHeadProvider)), Direction.DOWN, BlockPredicate.ONLY_IN_AIR_PREDICATE, true));
/* 420 */     FeatureUtils.register(context, MOSS_VEGETATION, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(
/* 421 */             WeightedList.builder()
/* 422 */             .add(Blocks.FLOWERING_AZALEA.defaultBlockState(), 4)
/* 423 */             .add(Blocks.AZALEA.defaultBlockState(), 7)
/* 424 */             .add(Blocks.MOSS_CARPET.defaultBlockState(), 25)
/* 425 */             .add(Blocks.SHORT_GRASS.defaultBlockState(), 50)
/* 426 */             .add(Blocks.TALL_GRASS.defaultBlockState(), 10))));
/* 429 */     FeatureUtils.register(context, MOSS_PATCH, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration((HolderSet)blocks
/* 430 */           .getOrThrow(BlockTags.MOSS_REPLACEABLE), 
/* 431 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.MOSS_BLOCK), 
/* 432 */           PlacementUtils.inlinePlaced((Holder)configuredFeatures.getOrThrow(MOSS_VEGETATION), new PlacementModifier[0]), CaveSurface.FLOOR, 
/*     */           
/* 434 */           (IntProvider)ConstantInt.of(1), 0.0F, 5, 0.8F, 
/*     */           
/* 438 */           (IntProvider)UniformInt.of(4, 7), 0.3F));
/* 441 */     FeatureUtils.register(context, MOSS_PATCH_BONEMEAL, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration((HolderSet)blocks
/* 442 */           .getOrThrow(BlockTags.MOSS_REPLACEABLE), 
/* 443 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.MOSS_BLOCK), 
/* 444 */           PlacementUtils.inlinePlaced((Holder)configuredFeatures.getOrThrow(MOSS_VEGETATION), new PlacementModifier[0]), CaveSurface.FLOOR, 
/*     */           
/* 446 */           (IntProvider)ConstantInt.of(1), 0.0F, 5, 0.6F, 
/*     */           
/* 450 */           (IntProvider)UniformInt.of(1, 2), 0.75F));
/* 454 */     FeatureUtils.register(context, DRIPLEAF, Feature.SIMPLE_RANDOM_SELECTOR, new CompositeFeatureConfiguration(
/* 455 */           (HolderSet)HolderSet.direct(new Holder[] { makeSmallDripleaf(), 
/* 457 */               makeDripleaf(Direction.EAST), 
/* 458 */               makeDripleaf(Direction.WEST), 
/* 459 */               makeDripleaf(Direction.SOUTH), 
/* 460 */               makeDripleaf(Direction.NORTH) })));
/* 464 */     FeatureUtils.register(context, CLAY_WITH_DRIPLEAVES, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration((HolderSet)blocks
/* 465 */           .getOrThrow(BlockTags.LUSH_GROUND_REPLACEABLE), 
/* 466 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.CLAY), 
/* 467 */           PlacementUtils.inlinePlaced((Holder)configuredFeatures.getOrThrow(DRIPLEAF), new PlacementModifier[0]), CaveSurface.FLOOR, 
/*     */           
/* 469 */           (IntProvider)ConstantInt.of(3), 0.8F, 2, 0.05F, 
/*     */           
/* 473 */           (IntProvider)UniformInt.of(4, 7), 0.7F));
/* 476 */     FeatureUtils.register(context, CLAY_POOL_WITH_DRIPLEAVES, Feature.WATERLOGGED_VEGETATION_PATCH, new VegetationPatchConfiguration((HolderSet)blocks
/* 477 */           .getOrThrow(BlockTags.LUSH_GROUND_REPLACEABLE), 
/* 478 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.CLAY), 
/* 479 */           PlacementUtils.inlinePlaced((Holder)configuredFeatures.getOrThrow(DRIPLEAF), new PlacementModifier[0]), CaveSurface.FLOOR, 
/*     */           
/* 481 */           (IntProvider)ConstantInt.of(3), 0.8F, 5, 0.1F, 
/*     */           
/* 485 */           (IntProvider)UniformInt.of(4, 7), 0.7F));
/* 488 */     FeatureUtils.register(context, LUSH_CAVES_CLAY, Feature.RANDOM_BOOLEAN_SELECTOR, new RandomBooleanFeatureConfiguration(
/* 489 */           PlacementUtils.inlinePlaced((Holder)configuredFeatures.getOrThrow(CLAY_WITH_DRIPLEAVES), new PlacementModifier[0]), 
/* 490 */           PlacementUtils.inlinePlaced((Holder)configuredFeatures.getOrThrow(CLAY_POOL_WITH_DRIPLEAVES), new PlacementModifier[0])));
/* 493 */     FeatureUtils.register(context, MOSS_PATCH_CEILING, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration((HolderSet)blocks
/* 494 */           .getOrThrow(BlockTags.MOSS_REPLACEABLE), 
/* 495 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.MOSS_BLOCK), 
/* 496 */           PlacementUtils.inlinePlaced((Holder)configuredFeatures.getOrThrow(CAVE_VINE_IN_MOSS), new PlacementModifier[0]), CaveSurface.CEILING, 
/*     */           
/* 498 */           (IntProvider)UniformInt.of(1, 2), 0.0F, 5, 0.08F, 
/*     */           
/* 502 */           (IntProvider)UniformInt.of(4, 7), 0.3F));
/* 505 */     FeatureUtils.register(context, SPORE_BLOSSOM, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
/* 506 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPORE_BLOSSOM)));
/* 509 */     FeatureUtils.register(context, AMETHYST_GEODE, Feature.GEODE, new GeodeConfiguration(new GeodeBlockSettings(
/*     */             
/* 511 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.AIR), 
/* 512 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.AMETHYST_BLOCK), 
/* 513 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.BUDDING_AMETHYST), 
/* 514 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.CALCITE), 
/* 515 */             (BlockStateProvider)BlockStateProvider.simple(Blocks.SMOOTH_BASALT), 
/* 516 */             List.of(Blocks.SMALL_AMETHYST_BUD
/* 517 */               .defaultBlockState(), Blocks.MEDIUM_AMETHYST_BUD
/* 518 */               .defaultBlockState(), Blocks.LARGE_AMETHYST_BUD
/* 519 */               .defaultBlockState(), Blocks.AMETHYST_CLUSTER
/* 520 */               .defaultBlockState()), (HolderSet)blocks
/* 521 */             .getOrThrow(BlockTags.FEATURES_CANNOT_REPLACE), (HolderSet)blocks
/* 522 */             .getOrThrow(BlockTags.GEODE_INVALID_BLOCKS)), new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 4.2D), new GeodeCrackSettings(0.95D, 2.0D, 2), 0.35D, 0.083D, true, 
/*     */           
/* 529 */           (IntProvider)UniformInt.of(4, 6), 
/* 530 */           (IntProvider)UniformInt.of(3, 4), 
/* 531 */           (IntProvider)UniformInt.of(1, 2), -16, 16, 0.05D, 1));
/* 538 */     FeatureUtils.register(context, SCULK_PATCH_DEEP_DARK, Feature.SCULK_PATCH, new SculkPatchConfiguration(10, 32, 64, 0, 1, 
/*     */           
/* 544 */           (IntProvider)ConstantInt.of(0), 0.5F));
/* 548 */     FeatureUtils.register(context, SCULK_PATCH_ANCIENT_CITY, Feature.SCULK_PATCH, new SculkPatchConfiguration(10, 32, 64, 0, 1, 
/*     */           
/* 554 */           (IntProvider)UniformInt.of(1, 3), 0.5F));
/* 558 */     MultifaceSpreadeableBlock sculkVeinBlock = (MultifaceSpreadeableBlock)Blocks.SCULK_VEIN;
/* 559 */     FeatureUtils.register(context, SCULK_VEIN, Feature.MULTIFACE_GROWTH, new MultifaceGrowthConfiguration((Block)sculkVeinBlock, 20, true, true, true, 1.0F, 
/*     */           
/* 566 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.STONE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.DRIPSTONE_BLOCK, Blocks.CALCITE, Blocks.TUFF, Blocks.DEEPSLATE })));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\features\CaveFeatures.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */