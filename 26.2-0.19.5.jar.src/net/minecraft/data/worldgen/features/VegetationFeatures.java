/*     */ package net.minecraft.data.worldgen.features;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*     */ import net.minecraft.data.worldgen.placement.TreePlacements;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.InclusiveRange;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.BiasedToBottomInt;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.util.valueproviders.WeightedListInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FlowerBedBlock;
/*     */ import net.minecraft.world.level.block.LeafLitterBlock;
/*     */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.CompositeFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.DualNoiseProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseThresholdProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CaveSurface;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class VegetationFeatures {
/*  59 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BAMBOO_NO_PODZOL = FeatureUtils.createKey("bamboo_no_podzol");
/*     */   
/*  60 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BAMBOO_SOME_PODZOL = FeatureUtils.createKey("bamboo_some_podzol");
/*     */   
/*  61 */   public static final ResourceKey<ConfiguredFeature<?, ?>> VINES = FeatureUtils.createKey("vines");
/*     */   
/*  65 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BROWN_MUSHROOM = FeatureUtils.createKey("brown_mushroom");
/*     */   
/*  66 */   public static final ResourceKey<ConfiguredFeature<?, ?>> RED_MUSHROOM = FeatureUtils.createKey("red_mushroom");
/*     */   
/*  67 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SUNFLOWER = FeatureUtils.createKey("sunflower");
/*     */   
/*  68 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PUMPKIN = FeatureUtils.createKey("pumpkin");
/*     */   
/*  70 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BERRY_BUSH = FeatureUtils.createKey("berry_bush");
/*     */   
/*  74 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TAIGA_GRASS = FeatureUtils.createKey("taiga_grass");
/*     */   
/*  76 */   public static final ResourceKey<ConfiguredFeature<?, ?>> GRASS = FeatureUtils.createKey("grass");
/*     */   
/*  77 */   public static final ResourceKey<ConfiguredFeature<?, ?>> GRASS_JUNGLE = FeatureUtils.createKey("grass_jungle");
/*     */   
/*  81 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DEAD_BUSH = FeatureUtils.createKey("dead_bush");
/*     */   
/*  82 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DRY_GRASS = FeatureUtils.createKey("dry_grass");
/*     */   
/*  83 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MELON = FeatureUtils.createKey("melon");
/*     */   
/*  84 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WATERLILY = FeatureUtils.createKey("waterlily");
/*     */   
/*  85 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_GRASS = FeatureUtils.createKey("tall_grass");
/*     */   
/*  86 */   public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_FERN = FeatureUtils.createKey("large_fern");
/*     */   
/*  87 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BUSH = FeatureUtils.createKey("bush");
/*     */   
/*  88 */   public static final ResourceKey<ConfiguredFeature<?, ?>> LEAF_LITTER = FeatureUtils.createKey("leaf_litter");
/*     */   
/*  89 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FIREFLY_BUSH = FeatureUtils.createKey("firefly_bush");
/*     */   
/*  93 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CACTUS = FeatureUtils.createKey("cactus");
/*     */   
/*  94 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SUGAR_CANE = FeatureUtils.createKey("sugar_cane");
/*     */   
/*  98 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_DEFAULT = FeatureUtils.createKey("flower_default");
/*     */   
/*  99 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_FLOWER_FOREST = FeatureUtils.createKey("flower_flower_forest");
/*     */   
/* 100 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_SWAMP = FeatureUtils.createKey("flower_swamp");
/*     */   
/* 101 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PLAIN = FeatureUtils.createKey("flower_plain");
/*     */   
/* 102 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_MEADOW = FeatureUtils.createKey("flower_meadow");
/*     */   
/* 103 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_CHERRY = FeatureUtils.createKey("flower_cherry");
/*     */   
/* 104 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PALE_GARDEN = FeatureUtils.createKey("flower_pale_garden");
/*     */   
/* 105 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFLOWER = FeatureUtils.createKey("wildflower");
/*     */   
/* 109 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_FLOWERS = FeatureUtils.createKey("forest_flowers");
/*     */   
/* 110 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_FOREST_FLOWER = FeatureUtils.createKey("pale_forest_flower");
/*     */   
/* 112 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_FOREST_VEGETATION = FeatureUtils.createKey("dark_forest_vegetation");
/*     */   
/* 114 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_VEGETATION = FeatureUtils.createKey("pale_garden_vegetation");
/*     */   
/* 115 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_VEGETATION = FeatureUtils.createKey("pale_moss_vegetation");
/*     */   
/* 116 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH = FeatureUtils.createKey("pale_moss_patch");
/*     */   
/* 117 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH_BONEMEAL = FeatureUtils.createKey("pale_moss_patch_bonemeal");
/*     */   
/* 119 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_FLOWER_FOREST = FeatureUtils.createKey("trees_flower_forest");
/*     */   
/* 121 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MEADOW_TREES = FeatureUtils.createKey("meadow_trees");
/*     */   
/* 123 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_TAIGA = FeatureUtils.createKey("trees_taiga");
/*     */   
/* 125 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BADLANDS = FeatureUtils.createKey("trees_badlands");
/*     */   
/* 127 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_GROVE = FeatureUtils.createKey("trees_grove");
/*     */   
/* 129 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_SAVANNA = FeatureUtils.createKey("trees_savanna");
/*     */   
/* 131 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_SNOWY = FeatureUtils.createKey("trees_snowy");
/*     */   
/* 133 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BIRCH = FeatureUtils.createKey("trees_birch");
/*     */   
/* 135 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_TALL = FeatureUtils.createKey("birch_tall");
/*     */   
/* 137 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_WINDSWEPT_HILLS = FeatureUtils.createKey("trees_windswept_hills");
/*     */   
/* 139 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_WATER = FeatureUtils.createKey("trees_water");
/*     */   
/* 141 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BIRCH_AND_OAK_LEAF_LITTER = FeatureUtils.createKey("trees_birch_and_oak_leaf_litter");
/*     */   
/* 143 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PLAINS = FeatureUtils.createKey("trees_plains");
/*     */   
/* 145 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_SPARSE_JUNGLE = FeatureUtils.createKey("trees_sparse_jungle");
/*     */   
/* 147 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_OLD_GROWTH_SPRUCE_TAIGA = FeatureUtils.createKey("trees_old_growth_spruce_taiga");
/*     */   
/* 149 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_OLD_GROWTH_PINE_TAIGA = FeatureUtils.createKey("trees_old_growth_pine_taiga");
/*     */   
/* 151 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_JUNGLE = FeatureUtils.createKey("trees_jungle");
/*     */   
/* 153 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BAMBOO_VEGETATION = FeatureUtils.createKey("bamboo_vegetation");
/*     */   
/* 155 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MUSHROOM_ISLAND_VEGETATION = FeatureUtils.createKey("mushroom_island_vegetation");
/*     */   
/* 157 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MANGROVE_VEGETATION = FeatureUtils.createKey("mangrove_vegetation");
/*     */   
/*     */   private static final float FALLEN_TREE_ONE_IN_CHANCE = 80.0F;
/*     */   
/*     */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
/* 162 */     HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
/* 163 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/* 164 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(TreeFeatures.HUGE_BROWN_MUSHROOM);
/* 165 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(TreeFeatures.HUGE_RED_MUSHROOM);
/* 166 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(TreeFeatures.FANCY_OAK_BEES_005);
/* 167 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(TreeFeatures.OAK_BEES_005);
/* 168 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(GRASS_JUNGLE);
/* 170 */     HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
/* 171 */     Holder.Reference reference6 = placedFeatures.getOrThrow(TreePlacements.PALE_OAK_CHECKED);
/* 172 */     Holder.Reference reference7 = placedFeatures.getOrThrow(TreePlacements.PALE_OAK_CREAKING_CHECKED);
/* 173 */     Holder.Reference reference8 = placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_CHECKED);
/* 174 */     Holder.Reference reference9 = placedFeatures.getOrThrow(TreePlacements.BIRCH_BEES_002);
/* 175 */     Holder.Reference reference10 = placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_BEES_002);
/* 176 */     Holder.Reference reference11 = placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_BEES);
/* 177 */     Holder.Reference reference12 = placedFeatures.getOrThrow(TreePlacements.PINE_CHECKED);
/* 178 */     Holder.Reference reference13 = placedFeatures.getOrThrow(TreePlacements.SPRUCE_CHECKED);
/* 179 */     Holder.Reference reference14 = placedFeatures.getOrThrow(TreePlacements.PINE_ON_SNOW);
/* 180 */     Holder.Reference reference15 = placedFeatures.getOrThrow(TreePlacements.ACACIA_CHECKED);
/* 181 */     Holder.Reference reference16 = placedFeatures.getOrThrow(TreePlacements.SUPER_BIRCH_BEES_0002);
/* 182 */     Holder.Reference reference17 = placedFeatures.getOrThrow(TreePlacements.BIRCH_BEES_0002_PLACED);
/* 183 */     Holder.Reference reference18 = placedFeatures.getOrThrow(TreePlacements.BIRCH_BEES_0002_LEAF_LITTER);
/* 184 */     Holder.Reference reference19 = placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_BEES_0002_LEAF_LITTER);
/* 185 */     Holder.Reference reference20 = placedFeatures.getOrThrow(TreePlacements.JUNGLE_BUSH);
/* 186 */     Holder.Reference reference21 = placedFeatures.getOrThrow(TreePlacements.MEGA_SPRUCE_CHECKED);
/* 187 */     Holder.Reference reference22 = placedFeatures.getOrThrow(TreePlacements.MEGA_PINE_CHECKED);
/* 188 */     Holder.Reference reference23 = placedFeatures.getOrThrow(TreePlacements.MEGA_JUNGLE_TREE_CHECKED);
/* 189 */     Holder.Reference reference24 = placedFeatures.getOrThrow(TreePlacements.TALL_MANGROVE_CHECKED);
/* 190 */     Holder.Reference reference25 = placedFeatures.getOrThrow(TreePlacements.OAK_CHECKED);
/* 191 */     Holder.Reference reference26 = placedFeatures.getOrThrow(TreePlacements.OAK_BEES_002);
/* 192 */     Holder.Reference reference27 = placedFeatures.getOrThrow(TreePlacements.SUPER_BIRCH_BEES);
/* 193 */     Holder.Reference reference28 = placedFeatures.getOrThrow(TreePlacements.SPRUCE_ON_SNOW);
/* 194 */     Holder.Reference reference29 = placedFeatures.getOrThrow(TreePlacements.OAK_BEES_0002_LEAF_LITTER);
/* 195 */     Holder.Reference reference30 = placedFeatures.getOrThrow(TreePlacements.JUNGLE_TREE_CHECKED);
/* 196 */     Holder.Reference reference31 = placedFeatures.getOrThrow(TreePlacements.MANGROVE_CHECKED);
/* 197 */     Holder.Reference reference32 = placedFeatures.getOrThrow(TreePlacements.OAK_LEAF_LITTER);
/* 198 */     Holder.Reference reference33 = placedFeatures.getOrThrow(TreePlacements.DARK_OAK_LEAF_LITTER);
/* 199 */     Holder.Reference reference34 = placedFeatures.getOrThrow(TreePlacements.BIRCH_LEAF_LITTER);
/* 200 */     Holder.Reference reference35 = placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_LEAF_LITTER);
/* 202 */     Holder.Reference reference36 = placedFeatures.getOrThrow(TreePlacements.FALLEN_OAK_TREE);
/* 203 */     Holder.Reference reference37 = placedFeatures.getOrThrow(TreePlacements.FALLEN_BIRCH_TREE);
/* 204 */     Holder.Reference reference38 = placedFeatures.getOrThrow(TreePlacements.FALLEN_SUPER_BIRCH_TREE);
/* 205 */     Holder.Reference reference39 = placedFeatures.getOrThrow(TreePlacements.FALLEN_JUNGLE_TREE);
/* 206 */     Holder.Reference reference40 = placedFeatures.getOrThrow(TreePlacements.FALLEN_SPRUCE_TREE);
/* 210 */     FeatureUtils.register(context, BAMBOO_NO_PODZOL, Feature.BAMBOO, new ProbabilityFeatureConfiguration(0.0F));
/* 213 */     FeatureUtils.register(context, BAMBOO_SOME_PODZOL, Feature.BAMBOO, new ProbabilityFeatureConfiguration(0.2F));
/* 216 */     FeatureUtils.register(context, VINES, Feature.VINES);
/* 220 */     FeatureUtils.register(context, BROWN_MUSHROOM, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.BROWN_MUSHROOM)));
/* 221 */     FeatureUtils.register(context, RED_MUSHROOM, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.RED_MUSHROOM)));
/* 222 */     FeatureUtils.register(context, SUNFLOWER, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.SUNFLOWER)));
/* 223 */     FeatureUtils.register(context, PUMPKIN, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.PUMPKIN)));
/* 225 */     FeatureUtils.register(context, BERRY_BUSH, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple((BlockState)Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue((Property)SweetBerryBushBlock.AGE, Integer.valueOf(3)))));
/* 229 */     FeatureUtils.register(context, TAIGA_GRASS, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(WeightedList.builder()
/* 230 */             .add(Blocks.SHORT_GRASS.defaultBlockState(), 1)
/* 231 */             .add(Blocks.FERN.defaultBlockState(), 4))));
/* 234 */     FeatureUtils.register(context, GRASS, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.SHORT_GRASS)));
/* 236 */     FeatureUtils.register(context, LEAF_LITTER, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(leafLitterPatchBuilder(1, 3))));
/* 238 */     FeatureUtils.register(context, GRASS_JUNGLE, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(
/* 239 */             WeightedList.builder().add(Blocks.SHORT_GRASS.defaultBlockState(), 3).add(Blocks.FERN.defaultBlockState(), 1))));
/* 244 */     FeatureUtils.register(context, DEAD_BUSH, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.DEAD_BUSH)));
/* 246 */     FeatureUtils.register(context, DRY_GRASS, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(WeightedList.builder()
/* 247 */             .add(Blocks.SHORT_DRY_GRASS.defaultBlockState(), 1)
/* 248 */             .add(Blocks.TALL_DRY_GRASS.defaultBlockState(), 1))));
/* 251 */     FeatureUtils.register(context, MELON, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.MELON)));
/* 252 */     FeatureUtils.register(context, WATERLILY, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.LILY_PAD)));
/* 253 */     FeatureUtils.register(context, TALL_GRASS, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.TALL_GRASS)));
/* 254 */     FeatureUtils.register(context, LARGE_FERN, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.LARGE_FERN)));
/* 255 */     FeatureUtils.register(context, BUSH, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.BUSH)));
/* 259 */     FeatureUtils.register(context, CACTUS, Feature.BLOCK_COLUMN, new BlockColumnConfiguration(
/* 260 */           List.of(
/* 261 */             BlockColumnConfiguration.layer(
/* 262 */               (IntProvider)BiasedToBottomInt.of(1, 3), 
/* 263 */               (BlockStateProvider)BlockStateProvider.simple(Blocks.CACTUS)), 
/*     */             
/* 265 */             BlockColumnConfiguration.layer((IntProvider)new WeightedListInt(
/* 266 */                 WeightedList.builder()
/* 267 */                 .add(ConstantInt.of(0), 3)
/* 268 */                 .add(ConstantInt.of(1), 1)
/* 269 */                 .build()), 
/* 270 */               (BlockStateProvider)BlockStateProvider.simple(Blocks.CACTUS_FLOWER))), Direction.UP, BlockPredicate.ONLY_IN_AIR_PREDICATE, false));
/* 275 */     FeatureUtils.register(context, SUGAR_CANE, Feature.BLOCK_COLUMN, BlockColumnConfiguration.simple(
/* 276 */           (IntProvider)BiasedToBottomInt.of(2, 4), 
/* 277 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SUGAR_CANE)));
/* 280 */     FeatureUtils.register(context, FIREFLY_BUSH, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.FIREFLY_BUSH)));
/* 286 */     WeightedStateProvider weightedStateProvider = new WeightedStateProvider(WeightedList.builder().add(Blocks.POPPY.defaultBlockState(), 2).add(Blocks.DANDELION.defaultBlockState(), 1));
/* 288 */     FeatureUtils.register(context, FLOWER_DEFAULT, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)weightedStateProvider));
/* 291 */     FeatureUtils.register(context, FLOWER_FLOWER_FOREST, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)new NoiseProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D, new double[0]), 0.020833334F, 
/* 292 */             List.of(new BlockState[] { 
/* 293 */                 Blocks.DANDELION.defaultBlockState(), Blocks.POPPY
/* 294 */                 .defaultBlockState(), Blocks.ALLIUM
/* 295 */                 .defaultBlockState(), Blocks.AZURE_BLUET
/* 296 */                 .defaultBlockState(), Blocks.RED_TULIP
/* 297 */                 .defaultBlockState(), Blocks.ORANGE_TULIP
/* 298 */                 .defaultBlockState(), Blocks.WHITE_TULIP
/* 299 */                 .defaultBlockState(), Blocks.PINK_TULIP
/* 300 */                 .defaultBlockState(), Blocks.OXEYE_DAISY
/* 301 */                 .defaultBlockState(), Blocks.CORNFLOWER
/* 302 */                 .defaultBlockState(), 
/* 302 */                 Blocks.LILY_OF_THE_VALLEY
/* 303 */                 .defaultBlockState() }))));
/* 306 */     FeatureUtils.register(context, FLOWER_SWAMP, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.BLUE_ORCHID)));
/* 307 */     FeatureUtils.register(context, FLOWER_PLAIN, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D, new double[0]), 0.005F, -0.8F, 0.33333334F, Blocks.DANDELION
/*     */             
/* 312 */             .defaultBlockState(), 
/* 313 */             List.of(Blocks.ORANGE_TULIP
/* 314 */               .defaultBlockState(), Blocks.RED_TULIP
/* 315 */               .defaultBlockState(), Blocks.PINK_TULIP
/* 316 */               .defaultBlockState(), Blocks.WHITE_TULIP
/* 317 */               .defaultBlockState()), 
/*     */             
/* 319 */             List.of(Blocks.POPPY
/* 320 */               .defaultBlockState(), Blocks.AZURE_BLUET
/* 321 */               .defaultBlockState(), Blocks.OXEYE_DAISY
/* 322 */               .defaultBlockState(), Blocks.CORNFLOWER
/* 323 */               .defaultBlockState()))));
/* 328 */     FeatureUtils.register(context, FLOWER_MEADOW, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)new DualNoiseProvider(new InclusiveRange(
/* 329 */               Integer.valueOf(1), Integer.valueOf(3)), new NormalNoise.NoiseParameters(-10, 1.0D, new double[0]), 1.0F, 2345L, new NormalNoise.NoiseParameters(-3, 1.0D, new double[0]), 1.0F, List.of(Blocks.TALL_GRASS
/* 330 */               .defaultBlockState(), Blocks.ALLIUM
/* 331 */               .defaultBlockState(), Blocks.POPPY
/* 332 */               .defaultBlockState(), Blocks.AZURE_BLUET
/* 333 */               .defaultBlockState(), Blocks.DANDELION
/* 334 */               .defaultBlockState(), Blocks.CORNFLOWER
/* 335 */               .defaultBlockState(), Blocks.OXEYE_DAISY
/* 336 */               .defaultBlockState(), Blocks.SHORT_GRASS
/* 337 */               .defaultBlockState()))));
/* 341 */     FeatureUtils.register(context, FLOWER_CHERRY, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(
/* 342 */             flowerBedPatchBuilder(Blocks.PINK_PETALS))));
/* 345 */     FeatureUtils.register(context, WILDFLOWER, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(flowerBedPatchBuilder(Blocks.WILDFLOWERS))));
/* 347 */     FeatureUtils.register(context, FLOWER_PALE_GARDEN, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.CLOSED_EYEBLOSSOM), true));
/* 351 */     FeatureUtils.register(context, FOREST_FLOWERS, Feature.SIMPLE_RANDOM_SELECTOR, new CompositeFeatureConfiguration(
/*     */           
/* 353 */           (HolderSet)HolderSet.direct(new Holder[] { PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration(
/* 355 */                   (BlockStateProvider)BlockStateProvider.simple(Blocks.LILAC)), new PlacementModifier[] { (PlacementModifier)CountPlacement.of(96), 
/* 357 */                   (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 358 */                   (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) }), PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration(
/* 361 */                   (BlockStateProvider)BlockStateProvider.simple(Blocks.ROSE_BUSH)), new PlacementModifier[] { (PlacementModifier)CountPlacement.of(96), 
/* 363 */                   (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 364 */                   (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) }), PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration(
/* 367 */                   (BlockStateProvider)BlockStateProvider.simple(Blocks.PEONY)), new PlacementModifier[] { (PlacementModifier)CountPlacement.of(96), 
/* 369 */                   (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 370 */                   (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) }), PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, (FeatureConfiguration)new SimpleBlockConfiguration(
/* 373 */                   (BlockStateProvider)BlockStateProvider.simple(Blocks.LILY_OF_THE_VALLEY)), new PlacementModifier[] { (PlacementModifier)CountPlacement.of(96), 
/* 375 */                   (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 376 */                   (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) }) })));
/* 381 */     FeatureUtils.register(context, PALE_FOREST_FLOWER, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.CLOSED_EYEBLOSSOM), true));
/* 383 */     FeatureUtils.register(context, DARK_FOREST_VEGETATION, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 384 */           List.of(new WeightedPlacedFeature(
/* 385 */               PlacementUtils.inlinePlaced((Holder)reference1, new PlacementModifier[0]), 0.025F), new WeightedPlacedFeature(
/* 386 */               PlacementUtils.inlinePlaced((Holder)reference2, new PlacementModifier[0]), 0.05F), new WeightedPlacedFeature((Holder)reference33, 0.6666667F), new WeightedPlacedFeature((Holder)reference37, 0.0025F), new WeightedPlacedFeature((Holder)reference34, 0.2F), new WeightedPlacedFeature((Holder)reference36, 0.0125F), new WeightedPlacedFeature((Holder)reference35, 0.1F)), (Holder)reference32));
/* 396 */     FeatureUtils.register(context, PALE_GARDEN_VEGETATION, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 397 */           List.of(new WeightedPlacedFeature((Holder)reference7, 0.1F), new WeightedPlacedFeature((Holder)reference6, 0.9F)), (Holder)reference6));
/* 404 */     FeatureUtils.register(context, PALE_MOSS_VEGETATION, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)new WeightedStateProvider(
/* 405 */             WeightedList.builder()
/* 406 */             .add(Blocks.PALE_MOSS_CARPET.defaultBlockState(), 25)
/* 407 */             .add(Blocks.SHORT_GRASS.defaultBlockState(), 25)
/* 408 */             .add(Blocks.TALL_GRASS.defaultBlockState(), 10))));
/* 412 */     FeatureUtils.register(context, PALE_MOSS_PATCH, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration((HolderSet)blocks
/* 413 */           .getOrThrow(BlockTags.MOSS_REPLACEABLE), 
/* 414 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_MOSS_BLOCK), 
/* 415 */           PlacementUtils.inlinePlaced((Holder)configuredFeatures.getOrThrow(PALE_MOSS_VEGETATION), new PlacementModifier[0]), CaveSurface.FLOOR, 
/*     */           
/* 417 */           (IntProvider)ConstantInt.of(1), 0.0F, 5, 0.3F, 
/*     */           
/* 421 */           (IntProvider)UniformInt.of(2, 4), 0.75F));
/* 425 */     FeatureUtils.register(context, PALE_MOSS_PATCH_BONEMEAL, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration((HolderSet)blocks
/* 426 */           .getOrThrow(BlockTags.MOSS_REPLACEABLE), 
/* 427 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_MOSS_BLOCK), 
/* 428 */           PlacementUtils.inlinePlaced((Holder)configuredFeatures.getOrThrow(PALE_MOSS_VEGETATION), new PlacementModifier[0]), CaveSurface.FLOOR, 
/*     */           
/* 430 */           (IntProvider)ConstantInt.of(1), 0.0F, 5, 0.6F, 
/*     */           
/* 434 */           (IntProvider)UniformInt.of(1, 2), 0.75F));
/* 438 */     FeatureUtils.register(context, TREES_FLOWER_FOREST, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 439 */           List.of(new WeightedPlacedFeature((Holder)reference37, 0.0025F), new WeightedPlacedFeature((Holder)reference9, 0.2F), new WeightedPlacedFeature((Holder)reference10, 0.1F)), (Holder)reference26));
/* 447 */     FeatureUtils.register(context, MEADOW_TREES, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 448 */           List.of(new WeightedPlacedFeature((Holder)reference11, 0.5F)), (Holder)reference27));
/* 454 */     FeatureUtils.register(context, TREES_TAIGA, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 455 */           List.of(new WeightedPlacedFeature((Holder)reference12, 0.33333334F), new WeightedPlacedFeature((Holder)reference40, 0.0125F)), (Holder)reference13));
/* 462 */     FeatureUtils.register(context, TREES_BADLANDS, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 463 */           List.of(new WeightedPlacedFeature((Holder)reference36, 0.0125F)), (Holder)reference32));
/* 469 */     FeatureUtils.register(context, TREES_GROVE, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 470 */           List.of(new WeightedPlacedFeature((Holder)reference14, 0.33333334F)), (Holder)reference28));
/* 476 */     FeatureUtils.register(context, TREES_SAVANNA, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 477 */           List.of(new WeightedPlacedFeature((Holder)reference15, 0.8F), new WeightedPlacedFeature((Holder)reference36, 0.0125F)), (Holder)reference25));
/* 482 */     FeatureUtils.register(context, TREES_SNOWY, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 483 */           List.of(new WeightedPlacedFeature((Holder)reference40, 0.0125F)), (Holder)reference13));
/* 487 */     FeatureUtils.register(context, TREES_BIRCH, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 488 */           List.of(new WeightedPlacedFeature((Holder)reference37, 0.0125F)), (Holder)reference17));
/* 493 */     FeatureUtils.register(context, BIRCH_TALL, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 494 */           List.of(new WeightedPlacedFeature((Holder)reference38, 0.00625F), new WeightedPlacedFeature((Holder)reference16, 0.5F), new WeightedPlacedFeature((Holder)reference37, 0.0125F)), (Holder)reference17));
/* 501 */     FeatureUtils.register(context, TREES_WINDSWEPT_HILLS, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 502 */           List.of(new WeightedPlacedFeature((Holder)reference40, 0.008325F), new WeightedPlacedFeature((Holder)reference13, 0.666F), new WeightedPlacedFeature((Holder)reference8, 0.1F), new WeightedPlacedFeature((Holder)reference36, 0.0125F)), (Holder)reference25));
/* 511 */     FeatureUtils.register(context, TREES_WATER, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 512 */           List.of(new WeightedPlacedFeature((Holder)reference8, 0.1F)), (Holder)reference25));
/* 516 */     FeatureUtils.register(context, TREES_BIRCH_AND_OAK_LEAF_LITTER, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 517 */           List.of(new WeightedPlacedFeature((Holder)reference37, 0.0025F), new WeightedPlacedFeature((Holder)reference18, 0.2F), new WeightedPlacedFeature((Holder)reference19, 0.1F), new WeightedPlacedFeature((Holder)reference36, 0.0125F)), (Holder)reference29));
/* 526 */     FeatureUtils.register(context, TREES_PLAINS, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 527 */           List.of(new WeightedPlacedFeature(
/* 528 */               PlacementUtils.inlinePlaced((Holder)reference3, new PlacementModifier[0]), 0.33333334F), new WeightedPlacedFeature((Holder)reference36, 0.0125F)), 
/*     */           
/* 530 */           PlacementUtils.inlinePlaced((Holder)reference4, new PlacementModifier[0])));
/* 533 */     FeatureUtils.register(context, TREES_SPARSE_JUNGLE, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 534 */           List.of(new WeightedPlacedFeature((Holder)reference8, 0.1F), new WeightedPlacedFeature((Holder)reference20, 0.5F), new WeightedPlacedFeature((Holder)reference39, 0.0125F)), (Holder)reference30));
/* 542 */     FeatureUtils.register(context, TREES_OLD_GROWTH_SPRUCE_TAIGA, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 543 */           List.of(new WeightedPlacedFeature((Holder)reference21, 0.33333334F), new WeightedPlacedFeature((Holder)reference12, 0.33333334F), new WeightedPlacedFeature((Holder)reference40, 0.0125F)), (Holder)reference13));
/* 551 */     FeatureUtils.register(context, TREES_OLD_GROWTH_PINE_TAIGA, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 552 */           List.of(new WeightedPlacedFeature((Holder)reference21, 0.025641026F), new WeightedPlacedFeature((Holder)reference22, 0.30769232F), new WeightedPlacedFeature((Holder)reference12, 0.33333334F), new WeightedPlacedFeature((Holder)reference40, 0.0125F)), (Holder)reference13));
/* 561 */     FeatureUtils.register(context, TREES_JUNGLE, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 562 */           List.of(new WeightedPlacedFeature((Holder)reference8, 0.1F), new WeightedPlacedFeature((Holder)reference20, 0.5F), new WeightedPlacedFeature((Holder)reference23, 0.33333334F), new WeightedPlacedFeature((Holder)reference39, 0.0125F)), (Holder)reference30));
/* 571 */     FeatureUtils.register(context, BAMBOO_VEGETATION, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 572 */           List.of(new WeightedPlacedFeature((Holder)reference8, 0.05F), new WeightedPlacedFeature((Holder)reference20, 0.15F), new WeightedPlacedFeature((Holder)reference23, 0.7F)), 
/*     */           
/* 577 */           PlacementUtils.inlinePlaced((Holder)reference5, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(32), 
/* 579 */               (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 580 */               (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */                   
/* 582 */                   BlockPredicate.not(
/* 583 */                     BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.PODZOL })))) })));
/* 589 */     FeatureUtils.register(context, MUSHROOM_ISLAND_VEGETATION, Feature.RANDOM_BOOLEAN_SELECTOR, new RandomBooleanFeatureConfiguration(
/* 590 */           PlacementUtils.inlinePlaced((Holder)reference2, new PlacementModifier[0]), 
/* 591 */           PlacementUtils.inlinePlaced((Holder)reference1, new PlacementModifier[0])));
/* 594 */     FeatureUtils.register(context, MANGROVE_VEGETATION, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
/* 595 */           List.of(new WeightedPlacedFeature((Holder)reference24, 0.85F)), (Holder)reference31));
/*     */   }
/*     */   
/*     */   private static WeightedList.Builder<BlockState> flowerBedPatchBuilder(Block flowerBedBlock) {
/* 601 */     return segmentedBlockPatchBuilder(flowerBedBlock, 1, 4, FlowerBedBlock.AMOUNT, FlowerBedBlock.FACING);
/*     */   }
/*     */   
/*     */   public static WeightedList.Builder<BlockState> leafLitterPatchBuilder(int minState, int maxState) {
/* 605 */     return segmentedBlockPatchBuilder(Blocks.LEAF_LITTER, minState, maxState, LeafLitterBlock.AMOUNT, LeafLitterBlock.FACING);
/*     */   }
/*     */   
/*     */   private static WeightedList.Builder<BlockState> segmentedBlockPatchBuilder(Block block, int minState, int maxState, IntegerProperty amountProperty, EnumProperty<Direction> directionProperty) {
/* 609 */     WeightedList.Builder<BlockState> segmentedBlockBuild = WeightedList.builder();
/* 610 */     for (int amount = minState; amount <= maxState; amount++) {
/* 611 */       for (Direction direction : Direction.Plane.HORIZONTAL)
/* 612 */         segmentedBlockBuild.add(((BlockState)block.defaultBlockState().setValue((Property)amountProperty, Integer.valueOf(amount))).setValue((Property)directionProperty, (Comparable)direction), 1); 
/*     */     } 
/* 615 */     return segmentedBlockBuild;
/*     */   }
/*     */   
/*     */   public static BlockPredicateFilter nearWaterPredicate(Block block) {
/* 619 */     return BlockPredicateFilter.forPredicate(
/* 620 */         BlockPredicate.allOf(new BlockPredicate[] { BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.wouldSurvive(block.defaultBlockState(), (Vec3i)BlockPos.ZERO), 
/* 623 */             BlockPredicate.anyOf(new BlockPredicate[] { BlockPredicate.matchesFluids((Vec3i)new BlockPos(1, -1, 0), new Fluid[] { (Fluid)Fluids.WATER, (Fluid)Fluids.FLOWING_WATER }), BlockPredicate.matchesFluids((Vec3i)new BlockPos(-1, -1, 0), new Fluid[] { (Fluid)Fluids.WATER, (Fluid)Fluids.FLOWING_WATER }), BlockPredicate.matchesFluids((Vec3i)new BlockPos(0, -1, 1), new Fluid[] { (Fluid)Fluids.WATER, (Fluid)Fluids.FLOWING_WATER }), BlockPredicate.matchesFluids((Vec3i)new BlockPos(0, -1, -1), new Fluid[] { (Fluid)Fluids.WATER, (Fluid)Fluids.FLOWING_WATER }) }) }));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\features\VegetationFeatures.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */