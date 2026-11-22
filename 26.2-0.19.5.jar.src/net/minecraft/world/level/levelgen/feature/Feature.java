/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockBlobConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.CompositeFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.EndSpikeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FallenTreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RootSystemConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpeleothemClusterConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpeleothemConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TemplateFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TwistingVinesConfig;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.UnderwaterMagmaConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.WeightedRandomFeatureConfiguration;
/*     */ 
/*     */ public abstract class Feature<FC extends FeatureConfiguration> {
/*  61 */   public static final Feature<NoneFeatureConfiguration> NO_OP = register("no_op", new NoOpFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  62 */   public static final Feature<TreeConfiguration> TREE = register("tree", new TreeFeature(TreeConfiguration.CODEC));
/*     */   
/*  63 */   public static final Feature<FallenTreeConfiguration> FALLEN_TREE = register("fallen_tree", new FallenTreeFeature(FallenTreeConfiguration.CODEC));
/*     */   
/*  65 */   public static final Feature<BlockPileConfiguration> BLOCK_PILE = register("block_pile", new BlockPileFeature(BlockPileConfiguration.CODEC));
/*     */   
/*  66 */   public static final Feature<SpringConfiguration> SPRING = register("spring_feature", new SpringFeature(SpringConfiguration.CODEC));
/*     */   
/*  68 */   public static final Feature<NoneFeatureConfiguration> CHORUS_PLANT = register("chorus_plant", new ChorusPlantFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  69 */   public static final Feature<ReplaceBlockConfiguration> REPLACE_SINGLE_BLOCK = register("replace_single_block", new ReplaceBlockFeature(ReplaceBlockConfiguration.CODEC));
/*     */   
/*  71 */   public static final Feature<NoneFeatureConfiguration> VOID_START_PLATFORM = register("void_start_platform", new VoidStartPlatformFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  72 */   public static final Feature<NoneFeatureConfiguration> DESERT_WELL = register("desert_well", new DesertWellFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  73 */   public static final Feature<FossilFeatureConfiguration> FOSSIL = register("fossil", new FossilFeature(FossilFeatureConfiguration.CODEC));
/*     */   
/*  74 */   public static final Feature<HugeMushroomFeatureConfiguration> HUGE_RED_MUSHROOM = register("huge_red_mushroom", new HugeRedMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
/*     */   
/*  75 */   public static final Feature<HugeMushroomFeatureConfiguration> HUGE_BROWN_MUSHROOM = register("huge_brown_mushroom", new HugeBrownMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
/*     */   
/*  76 */   public static final Feature<SpikeConfiguration> SPIKE = register("spike", new SpikeFeature(SpikeConfiguration.CODEC));
/*     */   
/*  77 */   public static final Feature<NoneFeatureConfiguration> GLOWSTONE_BLOB = register("glowstone_blob", new GlowstoneFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  78 */   public static final Feature<NoneFeatureConfiguration> FREEZE_TOP_LAYER = register("freeze_top_layer", new SnowAndFreezeFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  79 */   public static final Feature<NoneFeatureConfiguration> VINES = register("vines", new VinesFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  80 */   public static final Feature<BlockColumnConfiguration> BLOCK_COLUMN = register("block_column", new BlockColumnFeature(BlockColumnConfiguration.CODEC));
/*     */   
/*  81 */   public static final Feature<VegetationPatchConfiguration> VEGETATION_PATCH = register("vegetation_patch", new VegetationPatchFeature(VegetationPatchConfiguration.CODEC));
/*     */   
/*  82 */   public static final Feature<VegetationPatchConfiguration> WATERLOGGED_VEGETATION_PATCH = register("waterlogged_vegetation_patch", new WaterloggedVegetationPatchFeature(VegetationPatchConfiguration.CODEC));
/*     */   
/*  83 */   public static final Feature<RootSystemConfiguration> ROOT_SYSTEM = register("root_system", new RootSystemFeature(RootSystemConfiguration.CODEC));
/*     */   
/*  84 */   public static final Feature<MultifaceGrowthConfiguration> MULTIFACE_GROWTH = register("multiface_growth", new MultifaceGrowthFeature(MultifaceGrowthConfiguration.CODEC));
/*     */   
/*  85 */   public static final Feature<UnderwaterMagmaConfiguration> UNDERWATER_MAGMA = register("underwater_magma", new UnderwaterMagmaFeature(UnderwaterMagmaConfiguration.CODEC));
/*     */   
/*  86 */   public static final Feature<NoneFeatureConfiguration> MONSTER_ROOM = register("monster_room", new MonsterRoomFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  87 */   public static final Feature<NoneFeatureConfiguration> BLUE_ICE = register("blue_ice", new BlueIceFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  88 */   public static final Feature<BlockStateConfiguration> ICEBERG = register("iceberg", new IcebergFeature(BlockStateConfiguration.CODEC));
/*     */   
/*  89 */   public static final Feature<BlockBlobConfiguration> BLOCK_BLOB = register("block_blob", new BlockBlobFeature(BlockBlobConfiguration.CODEC));
/*     */   
/*  90 */   public static final Feature<DiskConfiguration> DISK = register("disk", new DiskFeature(DiskConfiguration.CODEC));
/*     */   
/*  91 */   public static final Feature<LakeFeature.Configuration> LAKE = register("lake", new LakeFeature(LakeFeature.Configuration.CODEC));
/*     */   
/*  92 */   public static final Feature<OreConfiguration> ORE = register("ore", new OreFeature(OreConfiguration.CODEC));
/*     */   
/*  93 */   public static final Feature<NoneFeatureConfiguration> END_PLATFORM = register("end_platform", new EndPlatformFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  94 */   public static final Feature<EndSpikeConfiguration> END_SPIKE = register("end_spike", new EndSpikeFeature(EndSpikeConfiguration.CODEC));
/*     */   
/*  95 */   public static final Feature<NoneFeatureConfiguration> END_ISLAND = register("end_island", new EndIslandFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  96 */   public static final Feature<EndGatewayConfiguration> END_GATEWAY = register("end_gateway", new EndGatewayFeature(EndGatewayConfiguration.CODEC));
/*     */   
/*  97 */   public static final SeagrassFeature SEAGRASS = register("seagrass", new SeagrassFeature(ProbabilityFeatureConfiguration.CODEC));
/*     */   
/*  98 */   public static final Feature<NoneFeatureConfiguration> KELP = register("kelp", new KelpFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  99 */   public static final Feature<NoneFeatureConfiguration> CORAL_TREE = register("coral_tree", new CoralTreeFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/* 100 */   public static final Feature<NoneFeatureConfiguration> CORAL_MUSHROOM = register("coral_mushroom", new CoralMushroomFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/* 101 */   public static final Feature<NoneFeatureConfiguration> CORAL_CLAW = register("coral_claw", new CoralClawFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/* 102 */   public static final Feature<CountConfiguration> SEA_PICKLE = register("sea_pickle", new SeaPickleFeature(CountConfiguration.CODEC));
/*     */   
/* 103 */   public static final Feature<SimpleBlockConfiguration> SIMPLE_BLOCK = register("simple_block", new SimpleBlockFeature(SimpleBlockConfiguration.CODEC));
/*     */   
/* 104 */   public static final Feature<ProbabilityFeatureConfiguration> BAMBOO = register("bamboo", new BambooFeature(ProbabilityFeatureConfiguration.CODEC));
/*     */   
/* 106 */   public static final Feature<HugeFungusConfiguration> HUGE_FUNGUS = register("huge_fungus", new HugeFungusFeature(HugeFungusConfiguration.CODEC));
/*     */   
/* 107 */   public static final Feature<NetherForestVegetationConfig> NETHER_FOREST_VEGETATION = register("nether_forest_vegetation", new NetherForestVegetationFeature(NetherForestVegetationConfig.CODEC));
/*     */   
/* 108 */   public static final Feature<NoneFeatureConfiguration> WEEPING_VINES = register("weeping_vines", new WeepingVinesFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/* 109 */   public static final Feature<TwistingVinesConfig> TWISTING_VINES = register("twisting_vines", new TwistingVinesFeature(TwistingVinesConfig.CODEC));
/*     */   
/* 111 */   public static final Feature<ColumnFeatureConfiguration> BASALT_COLUMNS = register("basalt_columns", new BasaltColumnsFeature(ColumnFeatureConfiguration.CODEC));
/*     */   
/* 112 */   public static final Feature<DeltaFeatureConfiguration> DELTA_FEATURE = register("delta_feature", new DeltaFeature(DeltaFeatureConfiguration.CODEC));
/*     */   
/* 113 */   public static final Feature<ReplaceSphereConfiguration> REPLACE_BLOBS = register("netherrack_replace_blobs", new ReplaceBlobsFeature(ReplaceSphereConfiguration.CODEC));
/*     */   
/* 115 */   public static final Feature<LayerConfiguration> FILL_LAYER = register("fill_layer", new FillLayerFeature(LayerConfiguration.CODEC));
/*     */   
/* 116 */   public static final BonusChestFeature BONUS_CHEST = register("bonus_chest", new BonusChestFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/* 117 */   public static final Feature<NoneFeatureConfiguration> BASALT_PILLAR = register("basalt_pillar", new BasaltPillarFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/* 118 */   public static final Feature<OreConfiguration> SCATTERED_ORE = register("scattered_ore", new ScatteredOreFeature(OreConfiguration.CODEC));
/*     */   
/*     */   @Deprecated
/* 121 */   public static final Feature<RandomFeatureConfiguration> RANDOM_SELECTOR = register("random_selector", new RandomSelectorFeature(RandomFeatureConfiguration.CODEC));
/*     */   
/* 122 */   public static final Feature<WeightedRandomFeatureConfiguration> WEIGHTED_RANDOM_SELECTOR = register("weighted_random_selector", new WeightedRandomSelectorFeature(WeightedRandomFeatureConfiguration.CODEC));
/*     */   
/* 123 */   public static final Feature<CompositeFeatureConfiguration> SIMPLE_RANDOM_SELECTOR = register("simple_random_selector", new SimpleRandomSelectorFeature(CompositeFeatureConfiguration.CODEC));
/*     */   
/* 124 */   public static final Feature<RandomBooleanFeatureConfiguration> RANDOM_BOOLEAN_SELECTOR = register("random_boolean_selector", new RandomBooleanSelectorFeature(RandomBooleanFeatureConfiguration.CODEC));
/*     */   
/* 126 */   public static final Feature<CompositeFeatureConfiguration> SEQUENCE = register("sequence", new SequenceFeature(CompositeFeatureConfiguration.CODEC));
/*     */   
/* 127 */   public static final Feature<TemplateFeatureConfiguration> TEMPLATE = register("template", new TemplateFeature(TemplateFeatureConfiguration.CODEC));
/*     */   
/* 129 */   public static final Feature<GeodeConfiguration> GEODE = register("geode", new GeodeFeature(GeodeConfiguration.CODEC));
/*     */   
/* 131 */   public static final Feature<SpeleothemClusterConfiguration> SPELEOTHEM_CLUSTER = register("speleothem_cluster", new SpeleothemClusterFeature(SpeleothemClusterConfiguration.CODEC));
/*     */   
/* 132 */   public static final Feature<LargeDripstoneConfiguration> LARGE_DRIPSTONE = register("large_dripstone", new LargeDripstoneFeature(LargeDripstoneConfiguration.CODEC));
/*     */   
/* 133 */   public static final Feature<SpeleothemConfiguration> SPELEOTHEM = register("speleothem", new SpeleothemFeature(SpeleothemConfiguration.CODEC));
/*     */   
/* 135 */   public static final Feature<SculkPatchConfiguration> SCULK_PATCH = register("sculk_patch", new SculkPatchFeature(SculkPatchConfiguration.CODEC));
/*     */   
/*     */   private final MapCodec<ConfiguredFeature<FC, Feature<FC>>> configuredCodec;
/*     */   
/*     */   private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String name, F feature) {
/* 138 */     return (F)Registry.register(BuiltInRegistries.FEATURE, name, feature);
/*     */   }
/*     */   
/*     */   public Feature(Codec<FC> codec) {
/* 144 */     this.configuredCodec = codec.fieldOf("config").xmap(c -> new ConfiguredFeature<>(this, c), ConfiguredFeature::config);
/*     */   }
/*     */   
/*     */   public MapCodec<ConfiguredFeature<FC, Feature<FC>>> configuredCodec() {
/* 148 */     return this.configuredCodec;
/*     */   }
/*     */   
/*     */   protected void setBlock(LevelWriter level, BlockPos pos, BlockState blockState) {
/* 152 */     level.setBlock(pos, blockState, 3);
/*     */   }
/*     */   
/*     */   public static Predicate<BlockState> isReplaceable(TagKey<Block> cannotReplaceTag) {
/* 156 */     return s -> !s.is(cannotReplaceTag);
/*     */   }
/*     */   
/*     */   protected void safeSetBlock(WorldGenLevel level, BlockPos pos, BlockState state, Predicate<BlockState> canReplace) {
/* 162 */     if (canReplace.test(level.getBlockState(pos)))
/* 163 */       level.setBlock(pos, state, 2); 
/*     */   }
/*     */   
/*     */   public boolean place(FC config, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
/* 170 */     if (level.ensureCanWrite(origin))
/* 171 */       return place(new FeaturePlaceContext<>(Optional.empty(), level, chunkGenerator, random, origin, config)); 
/* 173 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean checkNeighbors(Function<BlockPos, BlockState> blockGetter, BlockPos pos, Predicate<BlockState> predicate) {
/* 177 */     BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/* 178 */     for (Direction direction : Direction.values()) {
/* 179 */       neighborPos.setWithOffset((Vec3i)pos, direction);
/* 180 */       if (predicate.test(blockGetter.apply(neighborPos)))
/* 181 */         return true; 
/*     */     } 
/* 184 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isAdjacentToAir(Function<BlockPos, BlockState> blockGetter, BlockPos pos) {
/* 188 */     return checkNeighbors(blockGetter, pos, BlockBehaviour.BlockStateBase::isAir);
/*     */   }
/*     */   
/*     */   protected void markAboveForPostProcessing(WorldGenLevel level, BlockPos placePos) {
/* 194 */     BlockPos.MutableBlockPos pos = placePos.mutable();
/* 195 */     for (int i = 0; i < 2; i++) {
/* 196 */       pos.move(Direction.UP);
/* 197 */       if (level.getBlockState((BlockPos)pos).isAir())
/*     */         return; 
/* 200 */       level.getChunk((BlockPos)pos).markPosForPostProcessing((BlockPos)pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract boolean place(FeaturePlaceContext<FC> paramFeaturePlaceContext);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\Feature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */