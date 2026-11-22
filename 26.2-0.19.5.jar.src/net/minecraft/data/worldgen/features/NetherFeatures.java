/*     */ package net.minecraft.data.worldgen.features;
/*     */ 
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TwistingVinesConfig;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class NetherFeatures {
/*  28 */   public static final ResourceKey<ConfiguredFeature<?, ?>> DELTA = FeatureUtils.createKey("delta");
/*     */   
/*  29 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_BASALT_COLUMNS = FeatureUtils.createKey("small_basalt_columns");
/*     */   
/*  30 */   public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_BASALT_COLUMNS = FeatureUtils.createKey("large_basalt_columns");
/*     */   
/*  31 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BASALT_BLOBS = FeatureUtils.createKey("basalt_blobs");
/*     */   
/*  32 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BLACKSTONE_BLOBS = FeatureUtils.createKey("blackstone_blobs");
/*     */   
/*  33 */   public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWSTONE_EXTRA = FeatureUtils.createKey("glowstone_extra");
/*     */   
/*  35 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSON_FOREST_VEGETATION = FeatureUtils.createKey("crimson_forest_vegetation");
/*     */   
/*  36 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSON_FOREST_VEGETATION_BONEMEAL = FeatureUtils.createKey("crimson_forest_vegetation_bonemeal");
/*     */   
/*  38 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WARPED_FOREST_VEGETION = FeatureUtils.createKey("warped_forest_vegetation");
/*     */   
/*  39 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WARPED_FOREST_VEGETATION_BONEMEAL = FeatureUtils.createKey("warped_forest_vegetation_bonemeal");
/*     */   
/*  41 */   public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_SPROUTS = FeatureUtils.createKey("nether_sprouts");
/*     */   
/*  42 */   public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_SPROUTS_BONEMEAL = FeatureUtils.createKey("nether_sprouts_bonemeal");
/*     */   
/*  44 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TWISTING_VINES = FeatureUtils.createKey("twisting_vines");
/*     */   
/*  45 */   public static final ResourceKey<ConfiguredFeature<?, ?>> TWISTING_VINES_BONEMEAL = FeatureUtils.createKey("twisting_vines_bonemeal");
/*     */   
/*  46 */   public static final ResourceKey<ConfiguredFeature<?, ?>> WEEPING_VINES = FeatureUtils.createKey("weeping_vines");
/*     */   
/*  47 */   public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSON_ROOTS = FeatureUtils.createKey("crimson_roots");
/*     */   
/*  49 */   public static final ResourceKey<ConfiguredFeature<?, ?>> BASALT_PILLAR = FeatureUtils.createKey("basalt_pillar");
/*     */   
/*  50 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_LAVA_NETHER = FeatureUtils.createKey("spring_lava_nether");
/*     */   
/*  51 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_NETHER_CLOSED = FeatureUtils.createKey("spring_nether_closed");
/*     */   
/*  52 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_NETHER_OPEN = FeatureUtils.createKey("spring_nether_open");
/*     */   
/*  54 */   public static final ResourceKey<ConfiguredFeature<?, ?>> FIRE = FeatureUtils.createKey("patch_fire");
/*     */   
/*  55 */   public static final ResourceKey<ConfiguredFeature<?, ?>> SOUL_FIRE = FeatureUtils.createKey("patch_soul_fire");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
/*  58 */     FeatureUtils.register(context, DELTA, Feature.DELTA_FEATURE, new DeltaFeatureConfiguration(Blocks.LAVA
/*  59 */           .defaultBlockState(), Blocks.MAGMA_BLOCK
/*  60 */           .defaultBlockState(), 
/*  61 */           (IntProvider)UniformInt.of(3, 7), 
/*  62 */           (IntProvider)UniformInt.of(0, 2)));
/*  64 */     FeatureUtils.register(context, SMALL_BASALT_COLUMNS, Feature.BASALT_COLUMNS, new ColumnFeatureConfiguration(
/*  65 */           (IntProvider)ConstantInt.of(1), 
/*  66 */           (IntProvider)UniformInt.of(1, 4)));
/*  68 */     FeatureUtils.register(context, LARGE_BASALT_COLUMNS, Feature.BASALT_COLUMNS, new ColumnFeatureConfiguration(
/*  69 */           (IntProvider)UniformInt.of(2, 3), 
/*  70 */           (IntProvider)UniformInt.of(5, 10)));
/*  72 */     FeatureUtils.register(context, BASALT_BLOBS, Feature.REPLACE_BLOBS, new ReplaceSphereConfiguration(Blocks.NETHERRACK
/*  73 */           .defaultBlockState(), Blocks.BASALT
/*  74 */           .defaultBlockState(), 
/*  75 */           (IntProvider)UniformInt.of(3, 7)));
/*  77 */     FeatureUtils.register(context, BLACKSTONE_BLOBS, Feature.REPLACE_BLOBS, new ReplaceSphereConfiguration(Blocks.NETHERRACK
/*  78 */           .defaultBlockState(), Blocks.BLACKSTONE
/*  79 */           .defaultBlockState(), 
/*  80 */           (IntProvider)UniformInt.of(3, 7)));
/*  82 */     FeatureUtils.register(context, GLOWSTONE_EXTRA, Feature.GLOWSTONE_BLOB);
/*  87 */     WeightedStateProvider crimsonVegetationProvider = new WeightedStateProvider(WeightedList.builder().add(Blocks.CRIMSON_ROOTS.defaultBlockState(), 87).add(Blocks.CRIMSON_FUNGUS.defaultBlockState(), 11).add(Blocks.WARPED_FUNGUS.defaultBlockState(), 1));
/*  89 */     FeatureUtils.register(context, CRIMSON_FOREST_VEGETATION, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig((BlockStateProvider)crimsonVegetationProvider, 8, 4));
/*  94 */     FeatureUtils.register(context, CRIMSON_FOREST_VEGETATION_BONEMEAL, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig((BlockStateProvider)crimsonVegetationProvider, 3, 1));
/* 104 */     WeightedStateProvider warpedVegetationProvider = new WeightedStateProvider(WeightedList.builder().add(Blocks.WARPED_ROOTS.defaultBlockState(), 85).add(Blocks.CRIMSON_ROOTS.defaultBlockState(), 1).add(Blocks.WARPED_FUNGUS.defaultBlockState(), 13).add(Blocks.CRIMSON_FUNGUS.defaultBlockState(), 1));
/* 106 */     FeatureUtils.register(context, WARPED_FOREST_VEGETION, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig((BlockStateProvider)warpedVegetationProvider, 8, 4));
/* 111 */     FeatureUtils.register(context, WARPED_FOREST_VEGETATION_BONEMEAL, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig((BlockStateProvider)warpedVegetationProvider, 3, 1));
/* 117 */     FeatureUtils.register(context, NETHER_SPROUTS, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig(
/* 118 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.NETHER_SPROUTS), 8, 4));
/* 122 */     FeatureUtils.register(context, NETHER_SPROUTS_BONEMEAL, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig(
/* 123 */           (BlockStateProvider)BlockStateProvider.simple(Blocks.NETHER_SPROUTS), 3, 1));
/* 128 */     FeatureUtils.register(context, TWISTING_VINES, Feature.TWISTING_VINES, new TwistingVinesConfig(8, 4, 8));
/* 133 */     FeatureUtils.register(context, TWISTING_VINES_BONEMEAL, Feature.TWISTING_VINES, new TwistingVinesConfig(3, 1, 2));
/* 138 */     FeatureUtils.register(context, WEEPING_VINES, Feature.WEEPING_VINES);
/* 139 */     FeatureUtils.register(context, CRIMSON_ROOTS, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.CRIMSON_ROOTS)));
/* 141 */     FeatureUtils.register(context, BASALT_PILLAR, Feature.BASALT_PILLAR);
/* 142 */     FeatureUtils.register(context, SPRING_LAVA_NETHER, Feature.SPRING, new SpringConfiguration(Fluids.LAVA
/* 143 */           .defaultFluidState(), true, 4, 1, 
/*     */           
/* 147 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.GRAVEL, Blocks.MAGMA_BLOCK, Blocks.BLACKSTONE })));
/* 149 */     FeatureUtils.register(context, SPRING_NETHER_CLOSED, Feature.SPRING, new SpringConfiguration(Fluids.LAVA
/* 150 */           .defaultFluidState(), false, 5, 0, 
/*     */           
/* 154 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.NETHERRACK })));
/* 156 */     FeatureUtils.register(context, SPRING_NETHER_OPEN, Feature.SPRING, new SpringConfiguration(Fluids.LAVA
/* 157 */           .defaultFluidState(), false, 4, 1, 
/*     */           
/* 161 */           (HolderSet)HolderSet.direct(Block::builtInRegistryHolder, (Object[])new Block[] { Blocks.NETHERRACK })));
/* 164 */     FeatureUtils.register(context, FIRE, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.FIRE)));
/* 165 */     FeatureUtils.register(context, SOUL_FIRE, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration((BlockStateProvider)BlockStateProvider.simple(Blocks.SOUL_FIRE)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\features\NetherFeatures.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */