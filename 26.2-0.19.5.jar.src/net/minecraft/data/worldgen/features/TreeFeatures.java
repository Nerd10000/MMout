/*     */ package net.minecraft.data.worldgen.features;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.util.valueproviders.WeightedListInt;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.HugeMushroomBlock;
/*     */ import net.minecraft.world.level.block.MangrovePropaguleBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FallenTreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.BushFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaJungleFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaPineFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.PineFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.AboveRootPlacement;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.MangroveRootPlacement;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.MangroveRootPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLogsDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.CreakingHeartDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.PaleMossDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.PlaceOnGroundDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.UpwardsBranchingTrunkPlacer;
/*     */ 
/*     */ public class TreeFeatures {
/*  76 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSON_FUNGUS = FeatureUtils.createKey("crimson_fungus");
/*     */   
/*  77 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSON_FUNGUS_PLANTED = FeatureUtils.createKey("crimson_fungus_planted");
/*     */   
/*  78 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WARPED_FUNGUS = FeatureUtils.createKey("warped_fungus");
/*     */   
/*  79 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WARPED_FUNGUS_PLANTED = FeatureUtils.createKey("warped_fungus_planted");
/*     */   
/*  83 */   public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_BROWN_MUSHROOM = FeatureUtils.createKey("huge_brown_mushroom");
/*     */   
/*  84 */   public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_RED_MUSHROOM = FeatureUtils.createKey("huge_red_mushroom");
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobTree(Block oakLog, Block oakLeaves, int baseHeight, int heightRandA, int heightRandB, int blobRadius, BlockStateProvider belowTrunkProvider) {
/*  89 */     return new TreeConfiguration.TreeConfigurationBuilder(
/*  90 */         (BlockStateProvider)BlockStateProvider.simple(oakLog), (TrunkPlacer)new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB), 
/*     */         
/*  92 */         (BlockStateProvider)BlockStateProvider.simple(oakLeaves), (FoliagePlacer)new BlobFoliagePlacer(
/*  93 */           (IntProvider)ConstantInt.of(blobRadius), (IntProvider)ConstantInt.of(0), 3), (FeatureSize)new TwoLayersFeatureSize(1, 0, 1), belowTrunkProvider);
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createOak(BlockStateProvider belowTrunkProvider) {
/* 100 */     return createStraightBlobTree(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 4, 2, 0, 2, belowTrunkProvider).ignoreVines();
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createDarkOak(BlockStateProvider belowTrunkProvider) {
/* 104 */     return new TreeConfiguration.TreeConfigurationBuilder(
/* 105 */         (BlockStateProvider)BlockStateProvider.simple(Blocks.DARK_OAK_LOG), (TrunkPlacer)new DarkOakTrunkPlacer(6, 2, 1), 
/*     */         
/* 107 */         (BlockStateProvider)BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES), (FoliagePlacer)new DarkOakFoliagePlacer(
/* 108 */           (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(0)), (FeatureSize)new ThreeLayersFeatureSize(1, 1, 0, 1, 2, 
/* 109 */           OptionalInt.empty()), belowTrunkProvider);
/*     */   }
/*     */   
/*     */   private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenOak() {
/* 115 */     return createFallenTrees(Blocks.OAK_LOG, 4, 7)
/* 116 */       .stumpDecorators((List)ImmutableList.of(TrunkVineDecorator.INSTANCE));
/*     */   }
/*     */   
/*     */   private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenBirch(int maxHeight) {
/* 121 */     return createFallenTrees(Blocks.BIRCH_LOG, 5, maxHeight);
/*     */   }
/*     */   
/*     */   private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenJungle() {
/* 125 */     return createFallenTrees(Blocks.JUNGLE_LOG, 4, 11)
/* 126 */       .stumpDecorators((List)ImmutableList.of(TrunkVineDecorator.INSTANCE));
/*     */   }
/*     */   
/*     */   private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenSpruce() {
/* 131 */     return createFallenTrees(Blocks.SPRUCE_LOG, 6, 10);
/*     */   }
/*     */   
/*     */   private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenTrees(Block logBlock, int minLength, int maxLength) {
/* 135 */     return (new FallenTreeConfiguration.FallenTreeConfigurationBuilder((BlockStateProvider)BlockStateProvider.simple(logBlock), (IntProvider)UniformInt.of(minLength, maxLength)))
/* 136 */       .logDecorators((List)ImmutableList.of(new AttachedToLogsDecorator(0.1F, (BlockStateProvider)new WeightedStateProvider(
/*     */               
/* 138 */               WeightedList.builder()
/* 139 */               .add(Blocks.RED_MUSHROOM.defaultBlockState(), 2)
/* 140 */               .add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1)), 
/* 141 */             List.of(Direction.UP))));
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createBirch(BlockStateProvider belowTrunkProvider) {
/* 145 */     return createStraightBlobTree(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, 5, 2, 0, 2, belowTrunkProvider).ignoreVines();
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createSuperBirch(BlockStateProvider belowTrunkProvider) {
/* 149 */     return createStraightBlobTree(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, 5, 2, 6, 2, belowTrunkProvider).ignoreVines();
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createJungleTree(BlockStateProvider belowTrunkProvider) {
/* 153 */     return createStraightBlobTree(Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES, 4, 8, 0, 2, belowTrunkProvider);
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder createFancyOak(BlockStateProvider belowTrunkProvider) {
/* 157 */     return (new TreeConfiguration.TreeConfigurationBuilder(
/* 158 */         (BlockStateProvider)BlockStateProvider.simple(Blocks.OAK_LOG), (TrunkPlacer)new FancyTrunkPlacer(3, 11, 0), 
/*     */         
/* 160 */         (BlockStateProvider)BlockStateProvider.simple(Blocks.OAK_LEAVES), (FoliagePlacer)new FancyFoliagePlacer(
/* 161 */           (IntProvider)ConstantInt.of(2), (IntProvider)ConstantInt.of(4), 4), (FeatureSize)new TwoLayersFeatureSize(0, 0, 0, 
/* 162 */           OptionalInt.of(4)), belowTrunkProvider))
/*     */       
/* 164 */       .ignoreVines();
/*     */   }
/*     */   
/*     */   private static TreeConfiguration.TreeConfigurationBuilder cherry(BlockStateProvider belowTrunkProvider) {
/* 168 */     return (new TreeConfiguration.TreeConfigurationBuilder(
/* 169 */         (BlockStateProvider)BlockStateProvider.simple(Blocks.CHERRY_LOG), (TrunkPlacer)new CherryTrunkPlacer(7, 1, 0, (IntProvider)new WeightedListInt(
/*     */             
/* 171 */             WeightedList.builder()
/* 172 */             .add(ConstantInt.of(1), 1)
/* 173 */             .add(ConstantInt.of(2), 1)
/* 174 */             .add(ConstantInt.of(3), 1)
/* 175 */             .build()), 
/* 176 */           (IntProvider)UniformInt.of(2, 4), 
/* 177 */           UniformInt.of(-4, -3), 
/* 178 */           (IntProvider)UniformInt.of(-1, 0)), 
/*     */         
/* 180 */         (BlockStateProvider)BlockStateProvider.simple(Blocks.CHERRY_LEAVES), (FoliagePlacer)new CherryFoliagePlacer(
/* 181 */           (IntProvider)ConstantInt.of(4), (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(5), 0.25F, 0.5F, 0.16666667F, 0.33333334F), (FeatureSize)new TwoLayersFeatureSize(1, 0, 2), belowTrunkProvider))
/*     */       
/* 184 */       .ignoreVines();
/*     */   }
/*     */   
/* 189 */   public static final ResourceKey<ConfiguredFeature<?, ?>> OAK = FeatureUtils.createKey("oak");
/*     */   
/* 190 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_OAK = FeatureUtils.createKey("dark_oak");
/*     */   
/* 191 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK = FeatureUtils.createKey("pale_oak");
/*     */   
/* 192 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_BONEMEAL = FeatureUtils.createKey("pale_oak_bonemeal");
/*     */   
/* 193 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_CREAKING = FeatureUtils.createKey("pale_oak_creaking");
/*     */   
/* 194 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH = FeatureUtils.createKey("birch");
/*     */   
/* 196 */   public static final ResourceKey<ConfiguredFeature<?, ?>> ACACIA = FeatureUtils.createKey("acacia");
/*     */   
/* 197 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRUCE = FeatureUtils.createKey("spruce");
/*     */   
/* 198 */   public static final ResourceKey<ConfiguredFeature<?, ?>> PINE = FeatureUtils.createKey("pine");
/*     */   
/* 199 */   public static final ResourceKey<ConfiguredFeature<?, ?>> JUNGLE_TREE = FeatureUtils.createKey("jungle_tree");
/*     */   
/* 200 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK = FeatureUtils.createKey("fancy_oak");
/*     */   
/* 202 */   public static final ResourceKey<ConfiguredFeature<?, ?>> JUNGLE_TREE_NO_VINE = FeatureUtils.createKey("jungle_tree_no_vine");
/*     */   
/* 204 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_JUNGLE_TREE = FeatureUtils.createKey("mega_jungle_tree");
/*     */   
/* 205 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_SPRUCE = FeatureUtils.createKey("mega_spruce");
/*     */   
/* 206 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_PINE = FeatureUtils.createKey("mega_pine");
/*     */   
/* 207 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SUPER_BIRCH_BEES_0002 = FeatureUtils.createKey("super_birch_bees_0002");
/*     */   
/* 208 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SUPER_BIRCH_BEES = FeatureUtils.createKey("super_birch_bees");
/*     */   
/* 210 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SWAMP_OAK = FeatureUtils.createKey("swamp_oak");
/*     */   
/* 211 */   public static final ResourceKey<ConfiguredFeature<?, ?>> JUNGLE_BUSH = FeatureUtils.createKey("jungle_bush");
/*     */   
/* 212 */   public static final ResourceKey<ConfiguredFeature<?, ?>> AZALEA_TREE = FeatureUtils.createKey("azalea_tree");
/*     */   
/* 214 */   public static final ResourceKey<ConfiguredFeature<?, ?>> MANGROVE = FeatureUtils.createKey("mangrove");
/*     */   
/* 216 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_MANGROVE = FeatureUtils.createKey("tall_mangrove");
/*     */   
/* 218 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CHERRY = FeatureUtils.createKey("cherry");
/*     */   
/* 220 */   public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_BEES_0002_LEAF_LITTER = FeatureUtils.createKey("oak_bees_0002_leaf_litter");
/*     */   
/* 221 */   public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_BEES_002 = FeatureUtils.createKey("oak_bees_002");
/*     */   
/* 222 */   public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_BEES_005 = FeatureUtils.createKey("oak_bees_005");
/*     */   
/* 223 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_BEES_0002 = FeatureUtils.createKey("birch_bees_0002");
/*     */   
/* 224 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_BEES_0002_LEAF_LITTER = FeatureUtils.createKey("birch_bees_0002_leaf_litter");
/*     */   
/* 225 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_BEES_002 = FeatureUtils.createKey("birch_bees_002");
/*     */   
/* 226 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_BEES_005 = FeatureUtils.createKey("birch_bees_005");
/*     */   
/* 227 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK_BEES_0002_LEAF_LITTER = FeatureUtils.createKey("fancy_oak_bees_0002_leaf_litter");
/*     */   
/* 228 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK_BEES_002 = FeatureUtils.createKey("fancy_oak_bees_002");
/*     */   
/* 229 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK_BEES_005 = FeatureUtils.createKey("fancy_oak_bees_005");
/*     */   
/* 230 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK_BEES = FeatureUtils.createKey("fancy_oak_bees");
/*     */   
/* 231 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CHERRY_BEES_005 = FeatureUtils.createKey("cherry_bees_005");
/*     */   
/* 233 */   public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_LEAF_LITTER = FeatureUtils.createKey("oak_leaf_litter");
/*     */   
/* 234 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_OAK_LEAF_LITTER = FeatureUtils.createKey("dark_oak_leaf_litter");
/*     */   
/* 235 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_LEAF_LITTER = FeatureUtils.createKey("birch_leaf_litter");
/*     */   
/* 236 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK_LEAF_LITTER = FeatureUtils.createKey("fancy_oak_leaf_litter");
/*     */   
/* 238 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_OAK_TREE = FeatureUtils.createKey("fallen_oak_tree");
/*     */   
/* 239 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_JUNGLE_TREE = FeatureUtils.createKey("fallen_jungle_tree");
/*     */   
/* 240 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_SPRUCE_TREE = FeatureUtils.createKey("fallen_spruce_tree");
/*     */   
/* 241 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_BIRCH_TREE = FeatureUtils.createKey("fallen_birch_tree");
/*     */   
/* 242 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_SUPER_BIRCH_TREE = FeatureUtils.createKey("fallen_super_birch_tree");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
/* 245 */     HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
/* 248 */     BlockPredicate stemReplaceableBlocks = BlockPredicate.matchesBlocks(new Block[] { 
/* 248 */           Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.CHERRY_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.PALE_OAK_SAPLING, Blocks.MANGROVE_PROPAGULE, Blocks.DANDELION, 
/* 248 */           Blocks.TORCHFLOWER, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, 
/* 248 */           Blocks.CORNFLOWER, Blocks.WITHER_ROSE, Blocks.LILY_OF_THE_VALLEY, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.WHEAT, Blocks.SUGAR_CANE, Blocks.ATTACHED_PUMPKIN_STEM, Blocks.ATTACHED_MELON_STEM, Blocks.PUMPKIN_STEM, 
/* 248 */           Blocks.MELON_STEM, Blocks.LILY_PAD, Blocks.NETHER_WART, Blocks.COCOA, Blocks.CARROTS, Blocks.POTATOES, Blocks.CHORUS_PLANT, Blocks.CHORUS_FLOWER, Blocks.TORCHFLOWER_CROP, Blocks.PITCHER_CROP, 
/* 248 */           Blocks.BEETROOTS, Blocks.SWEET_BERRY_BUSH, Blocks.WARPED_FUNGUS, Blocks.CRIMSON_FUNGUS, Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT, Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT, 
/* 248 */           Blocks.SPORE_BLOSSOM, Blocks.AZALEA, Blocks.FLOWERING_AZALEA, Blocks.MOSS_CARPET, Blocks.PINK_PETALS, Blocks.WILDFLOWERS, Blocks.BIG_DRIPLEAF, Blocks.BIG_DRIPLEAF_STEM, Blocks.SMALL_DRIPLEAF });
/* 310 */     FeatureUtils.register(context, CRIMSON_FUNGUS, Feature.HUGE_FUNGUS, new HugeFungusConfiguration(Blocks.CRIMSON_NYLIUM
/* 311 */           .defaultBlockState(), Blocks.CRIMSON_STEM
/* 312 */           .defaultBlockState(), Blocks.NETHER_WART_BLOCK
/* 313 */           .defaultBlockState(), Blocks.SHROOMLIGHT
/* 314 */           .defaultBlockState(), stemReplaceableBlocks, false));
/* 318 */     FeatureUtils.register(context, CRIMSON_FUNGUS_PLANTED, Feature.HUGE_FUNGUS, new HugeFungusConfiguration(Blocks.CRIMSON_NYLIUM
/* 319 */           .defaultBlockState(), Blocks.CRIMSON_STEM
/* 320 */           .defaultBlockState(), Blocks.NETHER_WART_BLOCK
/* 321 */           .defaultBlockState(), Blocks.SHROOMLIGHT
/* 322 */           .defaultBlockState(), stemReplaceableBlocks, true));
/* 326 */     FeatureUtils.register(context, WARPED_FUNGUS, Feature.HUGE_FUNGUS, new HugeFungusConfiguration(Blocks.WARPED_NYLIUM
/* 327 */           .defaultBlockState(), Blocks.WARPED_STEM
/* 328 */           .defaultBlockState(), Blocks.WARPED_WART_BLOCK
/* 329 */           .defaultBlockState(), Blocks.SHROOMLIGHT
/* 330 */           .defaultBlockState(), stemReplaceableBlocks, false));
/* 334 */     FeatureUtils.register(context, WARPED_FUNGUS_PLANTED, Feature.HUGE_FUNGUS, new HugeFungusConfiguration(Blocks.WARPED_NYLIUM
/* 335 */           .defaultBlockState(), Blocks.WARPED_STEM
/* 336 */           .defaultBlockState(), Blocks.WARPED_WART_BLOCK
/* 337 */           .defaultBlockState(), Blocks.SHROOMLIGHT
/* 338 */           .defaultBlockState(), stemReplaceableBlocks, true));
/* 345 */     FeatureUtils.register(context, HUGE_BROWN_MUSHROOM, Feature.HUGE_BROWN_MUSHROOM, new HugeMushroomFeatureConfiguration(
/* 346 */           (BlockStateProvider)BlockStateProvider.simple((BlockState)((BlockState)Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState().setValue((Property)HugeMushroomBlock.UP, Boolean.valueOf(true))).setValue((Property)HugeMushroomBlock.DOWN, Boolean.valueOf(false))), 
/* 347 */           (BlockStateProvider)BlockStateProvider.simple((BlockState)((BlockState)Blocks.MUSHROOM_STEM.defaultBlockState().setValue((Property)HugeMushroomBlock.UP, Boolean.valueOf(false))).setValue((Property)HugeMushroomBlock.DOWN, Boolean.valueOf(false))), 3, 
/*     */           
/* 349 */           BlockPredicate.matchesTag(BlockTags.HUGE_BROWN_MUSHROOM_CAN_PLACE_ON)));
/* 351 */     FeatureUtils.register(context, HUGE_RED_MUSHROOM, Feature.HUGE_RED_MUSHROOM, new HugeMushroomFeatureConfiguration(
/* 352 */           (BlockStateProvider)BlockStateProvider.simple((BlockState)Blocks.RED_MUSHROOM_BLOCK.defaultBlockState().setValue((Property)HugeMushroomBlock.DOWN, Boolean.valueOf(false))), 
/* 353 */           (BlockStateProvider)BlockStateProvider.simple((BlockState)((BlockState)Blocks.MUSHROOM_STEM.defaultBlockState().setValue((Property)HugeMushroomBlock.UP, Boolean.valueOf(false))).setValue((Property)HugeMushroomBlock.DOWN, Boolean.valueOf(false))), 2, 
/*     */           
/* 355 */           BlockPredicate.matchesTag(BlockTags.HUGE_RED_MUSHROOM_CAN_PLACE_ON)));
/* 360 */     BeehiveDecorator beehive0002 = new BeehiveDecorator(0.002F);
/* 361 */     BeehiveDecorator beehive001 = new BeehiveDecorator(0.01F);
/* 362 */     BeehiveDecorator beehive002 = new BeehiveDecorator(0.02F);
/* 363 */     BeehiveDecorator beehive005 = new BeehiveDecorator(0.05F);
/* 364 */     BeehiveDecorator beehive = new BeehiveDecorator(1.0F);
/* 368 */     PlaceOnGroundDecorator sparseLeafLitter = new PlaceOnGroundDecorator(96, 4, 2, (BlockStateProvider)new WeightedStateProvider(VegetationFeatures.leafLitterPatchBuilder(1, 3)));
/* 369 */     PlaceOnGroundDecorator thickLeafLitter = new PlaceOnGroundDecorator(150, 2, 2, (BlockStateProvider)new WeightedStateProvider(VegetationFeatures.leafLitterPatchBuilder(1, 4)));
/* 373 */     HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
/* 374 */     BlockStateProvider belowTrunkProvider = TreeConfiguration.defaultPlaceBelowTreeTrunkProvider(biomes);
/* 375 */     FeatureUtils.register(context, OAK, Feature.TREE, 
/* 376 */         createOak(belowTrunkProvider)
/* 377 */         .build());
/* 379 */     FeatureUtils.register(context, DARK_OAK, Feature.TREE, 
/* 380 */         createDarkOak(belowTrunkProvider)
/* 381 */         .ignoreVines()
/* 382 */         .build());
/* 384 */     FeatureUtils.register(context, PALE_OAK, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 385 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_OAK_LOG), (TrunkPlacer)new DarkOakTrunkPlacer(6, 2, 1), 
/*     */           
/* 387 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_OAK_LEAVES), (FoliagePlacer)new DarkOakFoliagePlacer(
/* 388 */             (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(0)), (FeatureSize)new ThreeLayersFeatureSize(1, 1, 0, 1, 2, 
/* 389 */             OptionalInt.empty()), belowTrunkProvider))
/*     */         
/* 392 */         .decorators((List)ImmutableList.of(new PaleMossDecorator(0.15F, 0.4F, 0.8F)))
/*     */         
/* 395 */         .ignoreVines()
/* 396 */         .build());
/* 398 */     FeatureUtils.register(context, PALE_OAK_BONEMEAL, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 399 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_OAK_LOG), (TrunkPlacer)new DarkOakTrunkPlacer(6, 2, 1), 
/*     */           
/* 401 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_OAK_LEAVES), (FoliagePlacer)new DarkOakFoliagePlacer(
/* 402 */             (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(0)), (FeatureSize)new ThreeLayersFeatureSize(1, 1, 0, 1, 2, 
/* 403 */             OptionalInt.empty()), belowTrunkProvider))
/*     */         
/* 406 */         .ignoreVines()
/* 407 */         .build());
/* 409 */     FeatureUtils.register(context, PALE_OAK_CREAKING, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 410 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_OAK_LOG), (TrunkPlacer)new DarkOakTrunkPlacer(6, 2, 1), 
/*     */           
/* 412 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.PALE_OAK_LEAVES), (FoliagePlacer)new DarkOakFoliagePlacer(
/* 413 */             (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(0)), (FeatureSize)new ThreeLayersFeatureSize(1, 1, 0, 1, 2, 
/* 414 */             OptionalInt.empty()), belowTrunkProvider))
/*     */         
/* 417 */         .decorators((List)ImmutableList.of(new PaleMossDecorator(0.15F, 0.4F, 0.8F), new CreakingHeartDecorator(1.0F)))
/*     */         
/* 421 */         .ignoreVines()
/* 422 */         .build());
/* 424 */     FeatureUtils.register(context, BIRCH, Feature.TREE, 
/* 425 */         createBirch(belowTrunkProvider)
/* 426 */         .build());
/* 429 */     FeatureUtils.register(context, ACACIA, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 430 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.ACACIA_LOG), (TrunkPlacer)new ForkingTrunkPlacer(5, 2, 2), 
/*     */           
/* 432 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.ACACIA_LEAVES), (FoliagePlacer)new AcaciaFoliagePlacer(
/* 433 */             (IntProvider)ConstantInt.of(2), (IntProvider)ConstantInt.of(0)), (FeatureSize)new TwoLayersFeatureSize(1, 0, 2), belowTrunkProvider))
/*     */         
/* 437 */         .ignoreVines()
/* 438 */         .build());
/* 440 */     FeatureUtils.register(context, CHERRY, Feature.TREE, cherry(belowTrunkProvider)
/* 441 */         .build());
/* 443 */     FeatureUtils.register(context, CHERRY_BEES_005, Feature.TREE, cherry(belowTrunkProvider)
/* 444 */         .decorators(List.of(beehive005))
/* 445 */         .build());
/* 447 */     FeatureUtils.register(context, SPRUCE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 448 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LOG), (TrunkPlacer)new StraightTrunkPlacer(5, 2, 1), 
/*     */           
/* 450 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LEAVES), (FoliagePlacer)new SpruceFoliagePlacer(
/* 451 */             (IntProvider)UniformInt.of(2, 3), (IntProvider)UniformInt.of(0, 2), (IntProvider)UniformInt.of(1, 2)), (FeatureSize)new TwoLayersFeatureSize(2, 0, 2), belowTrunkProvider))
/*     */         
/* 455 */         .ignoreVines()
/* 456 */         .build());
/* 458 */     FeatureUtils.register(context, PINE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 459 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LOG), (TrunkPlacer)new StraightTrunkPlacer(6, 4, 0), 
/*     */           
/* 461 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LEAVES), (FoliagePlacer)new PineFoliagePlacer(
/* 462 */             (IntProvider)ConstantInt.of(1), (IntProvider)ConstantInt.of(1), (IntProvider)UniformInt.of(3, 4)), (FeatureSize)new TwoLayersFeatureSize(2, 0, 2), belowTrunkProvider))
/*     */         
/* 466 */         .ignoreVines()
/* 467 */         .build());
/* 469 */     FeatureUtils.register(context, JUNGLE_TREE, Feature.TREE, 
/* 470 */         createJungleTree(belowTrunkProvider)
/* 471 */         .decorators((List)ImmutableList.of(new CocoaDecorator(0.2F), TrunkVineDecorator.INSTANCE, new LeaveVineDecorator(0.25F)))
/*     */         
/* 476 */         .ignoreVines()
/* 477 */         .build());
/* 479 */     FeatureUtils.register(context, FANCY_OAK, Feature.TREE, 
/* 480 */         createFancyOak(belowTrunkProvider)
/* 481 */         .build());
/* 483 */     FeatureUtils.register(context, JUNGLE_TREE_NO_VINE, Feature.TREE, 
/* 484 */         createJungleTree(belowTrunkProvider)
/* 485 */         .ignoreVines()
/* 486 */         .build());
/* 489 */     FeatureUtils.register(context, MEGA_JUNGLE_TREE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 490 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.JUNGLE_LOG), (TrunkPlacer)new MegaJungleTrunkPlacer(10, 2, 19), 
/*     */           
/* 492 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.JUNGLE_LEAVES), (FoliagePlacer)new MegaJungleFoliagePlacer(
/* 493 */             (IntProvider)ConstantInt.of(2), (IntProvider)ConstantInt.of(0), 2), (FeatureSize)new TwoLayersFeatureSize(1, 1, 2), belowTrunkProvider))
/*     */         
/* 497 */         .decorators((List)ImmutableList.of(TrunkVineDecorator.INSTANCE, new LeaveVineDecorator(0.25F)))
/*     */         
/* 501 */         .build());
/* 503 */     FeatureUtils.register(context, MEGA_SPRUCE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 504 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LOG), (TrunkPlacer)new GiantTrunkPlacer(13, 2, 14), 
/*     */           
/* 506 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LEAVES), (FoliagePlacer)new MegaPineFoliagePlacer(
/* 507 */             (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(0), (IntProvider)UniformInt.of(13, 17)), (FeatureSize)new TwoLayersFeatureSize(1, 1, 2), belowTrunkProvider))
/*     */         
/* 511 */         .decorators((List)ImmutableList.of(new AlterGroundDecorator(
/* 512 */               (BlockStateProvider)RuleBasedStateProvider.ifTrueThenProvide(BlockPredicate.matchesTag(BlockTags.BENEATH_TREE_PODZOL_REPLACEABLE), Blocks.PODZOL))))
/*     */         
/* 514 */         .build());
/* 516 */     FeatureUtils.register(context, MEGA_PINE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 517 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LOG), (TrunkPlacer)new GiantTrunkPlacer(13, 2, 14), 
/*     */           
/* 519 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.SPRUCE_LEAVES), (FoliagePlacer)new MegaPineFoliagePlacer(
/* 520 */             (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(0), (IntProvider)UniformInt.of(3, 7)), (FeatureSize)new TwoLayersFeatureSize(1, 1, 2), belowTrunkProvider))
/*     */         
/* 524 */         .decorators((List)ImmutableList.of(new AlterGroundDecorator(
/* 525 */               (BlockStateProvider)RuleBasedStateProvider.ifTrueThenProvide(BlockPredicate.matchesTag(BlockTags.BENEATH_TREE_PODZOL_REPLACEABLE), Blocks.PODZOL))))
/*     */         
/* 527 */         .build());
/* 529 */     FeatureUtils.register(context, SUPER_BIRCH_BEES_0002, Feature.TREE, 
/* 530 */         createSuperBirch(belowTrunkProvider)
/* 531 */         .decorators((List)ImmutableList.of(beehive0002))
/* 532 */         .build());
/* 534 */     FeatureUtils.register(context, SUPER_BIRCH_BEES, Feature.TREE, 
/* 535 */         createSuperBirch(belowTrunkProvider)
/* 536 */         .decorators((List)ImmutableList.of(beehive))
/* 537 */         .build());
/* 540 */     FeatureUtils.register(context, SWAMP_OAK, Feature.TREE, 
/* 541 */         createStraightBlobTree(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 5, 3, 0, 3, belowTrunkProvider)
/*     */         
/* 550 */         .decorators((List)ImmutableList.of(new LeaveVineDecorator(0.25F)))
/* 551 */         .build());
/* 553 */     FeatureUtils.register(context, JUNGLE_BUSH, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 554 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.JUNGLE_LOG), (TrunkPlacer)new StraightTrunkPlacer(1, 0, 0), 
/*     */           
/* 556 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.OAK_LEAVES), (FoliagePlacer)new BushFoliagePlacer(
/* 557 */             (IntProvider)ConstantInt.of(2), (IntProvider)ConstantInt.of(1), 2), (FeatureSize)new TwoLayersFeatureSize(0, 0, 0), belowTrunkProvider))
/*     */         
/* 561 */         .build());
/* 563 */     FeatureUtils.register(context, AZALEA_TREE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 564 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.OAK_LOG), (TrunkPlacer)new BendingTrunkPlacer(4, 2, 0, 3, 
/* 565 */             (IntProvider)UniformInt.of(1, 2)), (BlockStateProvider)new WeightedStateProvider(
/* 566 */             WeightedList.builder().add(Blocks.AZALEA_LEAVES.defaultBlockState(), 3).add(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), 1)), (FoliagePlacer)new RandomSpreadFoliagePlacer(
/* 567 */             (IntProvider)ConstantInt.of(3), (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(2), 50), (FeatureSize)new TwoLayersFeatureSize(1, 0, 1), belowTrunkProvider))
/*     */         
/* 571 */         .belowTrunkProvider((BlockStateProvider)BlockStateProvider.simple(Blocks.ROOTED_DIRT))
/* 572 */         .build());
/* 575 */     FeatureUtils.register(context, MANGROVE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 576 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.MANGROVE_LOG), (TrunkPlacer)new UpwardsBranchingTrunkPlacer(2, 1, 4, 
/* 577 */             (IntProvider)UniformInt.of(1, 4), 0.5F, (IntProvider)UniformInt.of(0, 1), (HolderSet)blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)), 
/* 578 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.MANGROVE_LEAVES), (FoliagePlacer)new RandomSpreadFoliagePlacer(
/* 579 */             (IntProvider)ConstantInt.of(3), (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(2), 70), 
/* 580 */           Optional.of(new MangroveRootPlacer(
/* 581 */               (IntProvider)UniformInt.of(1, 3), 
/* 582 */               (BlockStateProvider)BlockStateProvider.simple(Blocks.MANGROVE_ROOTS), 
/* 583 */               Optional.of(new AboveRootPlacement(
/* 584 */                   (BlockStateProvider)BlockStateProvider.simple(Blocks.MOSS_CARPET), 0.5F)), new MangroveRootPlacement((HolderSet)blocks
/*     */                 
/* 588 */                 .getOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH), 
/* 589 */                 (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS }), (BlockStateProvider)BlockStateProvider.simple(Blocks.MUDDY_MANGROVE_ROOTS), 8, 15, 0.2F))), (FeatureSize)new TwoLayersFeatureSize(2, 0, 2), 
/*     */           
/* 597 */           TreeConfiguration.defaultPlaceBelowTreeTrunkProvider(biomes)))
/*     */         
/* 599 */         .decorators(List.of(new LeaveVineDecorator(0.125F), new AttachedToLeavesDecorator(0.14F, 1, 0, (BlockStateProvider)new RandomizedIntStateProvider(
/*     */                 
/* 603 */                 (BlockStateProvider)BlockStateProvider.simple((BlockState)Blocks.MANGROVE_PROPAGULE.defaultBlockState().setValue((Property)MangrovePropaguleBlock.HANGING, Boolean.valueOf(true))), MangrovePropaguleBlock.AGE, 
/*     */                 
/* 605 */                 (IntProvider)UniformInt.of(0, 4)), 2, 
/* 606 */               List.of(Direction.DOWN)), beehive001))
/*     */         
/* 609 */         .ignoreVines()
/* 610 */         .build());
/* 613 */     FeatureUtils.register(context, TALL_MANGROVE, Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
/* 614 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.MANGROVE_LOG), (TrunkPlacer)new UpwardsBranchingTrunkPlacer(4, 1, 9, 
/* 615 */             (IntProvider)UniformInt.of(1, 6), 0.5F, (IntProvider)UniformInt.of(0, 1), (HolderSet)blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)), 
/* 616 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.MANGROVE_LEAVES), (FoliagePlacer)new RandomSpreadFoliagePlacer(
/* 617 */             (IntProvider)ConstantInt.of(3), (IntProvider)ConstantInt.of(0), (IntProvider)ConstantInt.of(2), 70), 
/* 618 */           Optional.of(new MangroveRootPlacer(
/* 619 */               (IntProvider)UniformInt.of(3, 7), 
/* 620 */               (BlockStateProvider)BlockStateProvider.simple(Blocks.MANGROVE_ROOTS), 
/* 621 */               Optional.of(new AboveRootPlacement(
/* 622 */                   (BlockStateProvider)BlockStateProvider.simple(Blocks.MOSS_CARPET), 0.5F)), new MangroveRootPlacement((HolderSet)blocks
/*     */                 
/* 626 */                 .getOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH), 
/* 627 */                 (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS }), (BlockStateProvider)BlockStateProvider.simple(Blocks.MUDDY_MANGROVE_ROOTS), 8, 15, 0.2F))), (FeatureSize)new TwoLayersFeatureSize(3, 0, 2), 
/*     */           
/* 635 */           TreeConfiguration.defaultPlaceBelowTreeTrunkProvider(biomes)))
/*     */         
/* 637 */         .decorators(List.of(new LeaveVineDecorator(0.125F), new AttachedToLeavesDecorator(0.14F, 1, 0, (BlockStateProvider)new RandomizedIntStateProvider(
/*     */                 
/* 641 */                 (BlockStateProvider)BlockStateProvider.simple((BlockState)Blocks.MANGROVE_PROPAGULE.defaultBlockState().setValue((Property)MangrovePropaguleBlock.HANGING, Boolean.valueOf(true))), MangrovePropaguleBlock.AGE, 
/*     */                 
/* 643 */                 (IntProvider)UniformInt.of(0, 4)), 2, 
/* 644 */               List.of(Direction.DOWN)), beehive001))
/*     */         
/* 647 */         .ignoreVines()
/* 648 */         .build());
/* 650 */     FeatureUtils.register(context, OAK_BEES_0002_LEAF_LITTER, Feature.TREE, 
/* 651 */         createOak(belowTrunkProvider)
/* 652 */         .decorators(List.of(beehive0002, sparseLeafLitter, thickLeafLitter))
/* 653 */         .build());
/* 655 */     FeatureUtils.register(context, OAK_BEES_002, Feature.TREE, 
/* 656 */         createOak(belowTrunkProvider)
/* 657 */         .decorators(List.of(beehive002))
/* 658 */         .build());
/* 660 */     FeatureUtils.register(context, OAK_BEES_005, Feature.TREE, 
/* 661 */         createOak(belowTrunkProvider)
/* 662 */         .decorators(List.of(beehive005))
/* 663 */         .build());
/* 665 */     FeatureUtils.register(context, BIRCH_BEES_0002, Feature.TREE, 
/* 666 */         createBirch(belowTrunkProvider)
/* 667 */         .decorators(List.of(beehive0002))
/* 668 */         .build());
/* 670 */     FeatureUtils.register(context, BIRCH_BEES_0002_LEAF_LITTER, Feature.TREE, 
/* 671 */         createBirch(belowTrunkProvider)
/* 672 */         .decorators(List.of(beehive0002, sparseLeafLitter, thickLeafLitter))
/* 673 */         .build());
/* 675 */     FeatureUtils.register(context, BIRCH_BEES_002, Feature.TREE, 
/* 676 */         createBirch(belowTrunkProvider)
/* 677 */         .decorators(List.of(beehive002))
/* 678 */         .build());
/* 680 */     FeatureUtils.register(context, BIRCH_BEES_005, Feature.TREE, 
/* 681 */         createBirch(belowTrunkProvider)
/* 682 */         .decorators(List.of(beehive005))
/* 683 */         .build());
/* 685 */     FeatureUtils.register(context, FANCY_OAK_BEES_0002_LEAF_LITTER, Feature.TREE, 
/* 686 */         createFancyOak(belowTrunkProvider)
/* 687 */         .decorators(List.of(beehive0002, sparseLeafLitter, thickLeafLitter))
/* 688 */         .build());
/* 690 */     FeatureUtils.register(context, FANCY_OAK_BEES_002, Feature.TREE, 
/* 691 */         createFancyOak(belowTrunkProvider)
/* 692 */         .decorators(List.of(beehive002))
/* 693 */         .build());
/* 695 */     FeatureUtils.register(context, FANCY_OAK_BEES_005, Feature.TREE, 
/* 696 */         createFancyOak(belowTrunkProvider)
/* 697 */         .decorators(List.of(beehive005))
/* 698 */         .build());
/* 700 */     FeatureUtils.register(context, FANCY_OAK_BEES, Feature.TREE, 
/* 701 */         createFancyOak(belowTrunkProvider)
/* 702 */         .decorators(List.of(beehive))
/* 703 */         .build());
/* 706 */     FeatureUtils.register(context, OAK_LEAF_LITTER, Feature.TREE, 
/* 707 */         createOak(belowTrunkProvider)
/* 708 */         .decorators((List)ImmutableList.of(sparseLeafLitter, thickLeafLitter))
/* 709 */         .build());
/* 712 */     FeatureUtils.register(context, DARK_OAK_LEAF_LITTER, Feature.TREE, 
/* 713 */         createDarkOak(belowTrunkProvider)
/* 714 */         .ignoreVines()
/* 715 */         .decorators((List)ImmutableList.of(sparseLeafLitter, thickLeafLitter))
/* 716 */         .build());
/* 719 */     FeatureUtils.register(context, BIRCH_LEAF_LITTER, Feature.TREE, 
/* 720 */         createBirch(belowTrunkProvider)
/* 721 */         .decorators((List)ImmutableList.of(sparseLeafLitter, thickLeafLitter))
/* 722 */         .build());
/* 725 */     FeatureUtils.register(context, FANCY_OAK_LEAF_LITTER, Feature.TREE, 
/* 726 */         createFancyOak(belowTrunkProvider)
/* 727 */         .decorators(List.of(sparseLeafLitter, thickLeafLitter))
/* 728 */         .build());
/* 731 */     FeatureUtils.register(context, FALLEN_OAK_TREE, Feature.FALLEN_TREE, 
/* 732 */         createFallenOak()
/* 733 */         .build());
/* 736 */     FeatureUtils.register(context, FALLEN_BIRCH_TREE, Feature.FALLEN_TREE, 
/* 737 */         createFallenBirch(8)
/* 738 */         .build());
/* 741 */     FeatureUtils.register(context, FALLEN_SUPER_BIRCH_TREE, Feature.FALLEN_TREE, 
/* 742 */         createFallenBirch(15)
/* 743 */         .build());
/* 746 */     FeatureUtils.register(context, FALLEN_JUNGLE_TREE, Feature.FALLEN_TREE, 
/* 747 */         createFallenJungle()
/* 748 */         .build());
/* 751 */     FeatureUtils.register(context, FALLEN_SPRUCE_TREE, Feature.FALLEN_TREE, 
/* 752 */         createFallenSpruce()
/* 753 */         .build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\features\TreeFeatures.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */