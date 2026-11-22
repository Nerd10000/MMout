/*     */ package net.minecraft.world.level.block.grower;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.features.TreeFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ 
/*     */ public final class TreeGrower {
/*  28 */   private static final Map<String, TreeGrower> GROWERS = (Map<String, TreeGrower>)new Object2ObjectArrayMap();
/*     */   
/*     */   public static final Codec<TreeGrower> CODEC;
/*     */   
/*     */   static {
/*  29 */     Objects.requireNonNull(GROWERS);
/*  29 */     CODEC = Codec.stringResolver(g -> g.name, GROWERS::get);
/*     */   }
/*     */   
/*  31 */   public static final TreeGrower OAK = new TreeGrower("oak", 0.1F, Optional.empty(), Optional.empty(), Optional.of(TreeFeatures.OAK), Optional.of(TreeFeatures.FANCY_OAK), Optional.of(TreeFeatures.OAK_BEES_005), Optional.of(TreeFeatures.FANCY_OAK_BEES_005));
/*     */   
/*  32 */   public static final TreeGrower SPRUCE = new TreeGrower("spruce", 0.5F, Optional.of(TreeFeatures.MEGA_SPRUCE), Optional.of(TreeFeatures.MEGA_PINE), Optional.of(TreeFeatures.SPRUCE), Optional.empty(), Optional.empty(), Optional.empty());
/*     */   
/*  33 */   public static final TreeGrower MANGROVE = new TreeGrower("mangrove", 0.85F, Optional.empty(), Optional.empty(), Optional.of(TreeFeatures.MANGROVE), Optional.of(TreeFeatures.TALL_MANGROVE), Optional.empty(), Optional.empty());
/*     */   
/*  35 */   public static final TreeGrower AZALEA = new TreeGrower("azalea", Optional.empty(), Optional.of(TreeFeatures.AZALEA_TREE), Optional.empty());
/*     */   
/*  36 */   public static final TreeGrower BIRCH = new TreeGrower("birch", Optional.empty(), Optional.of(TreeFeatures.BIRCH), Optional.of(TreeFeatures.BIRCH_BEES_005));
/*     */   
/*  37 */   public static final TreeGrower JUNGLE = new TreeGrower("jungle", Optional.of(TreeFeatures.MEGA_JUNGLE_TREE), Optional.of(TreeFeatures.JUNGLE_TREE_NO_VINE), Optional.empty());
/*     */   
/*  38 */   public static final TreeGrower ACACIA = new TreeGrower("acacia", Optional.empty(), Optional.of(TreeFeatures.ACACIA), Optional.empty());
/*     */   
/*  39 */   public static final TreeGrower CHERRY = new TreeGrower("cherry", Optional.empty(), Optional.of(TreeFeatures.CHERRY), Optional.of(TreeFeatures.CHERRY_BEES_005));
/*     */   
/*  40 */   public static final TreeGrower DARK_OAK = new TreeGrower("dark_oak", Optional.of(TreeFeatures.DARK_OAK), Optional.empty(), Optional.empty());
/*     */   
/*  41 */   public static final TreeGrower PALE_OAK = new TreeGrower("pale_oak", Optional.of(TreeFeatures.PALE_OAK_BONEMEAL), Optional.empty(), Optional.empty());
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final float secondaryChance;
/*     */   
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> megaTree;
/*     */   
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryMegaTree;
/*     */   
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree;
/*     */   
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryTree;
/*     */   
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> flowers;
/*     */   
/*     */   private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryFlowers;
/*     */   
/*     */   public TreeGrower(String name, Optional<ResourceKey<ConfiguredFeature<?, ?>>> megaTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> flowers) {
/*  53 */     this(name, 0.0F, megaTree, Optional.empty(), tree, Optional.empty(), flowers, Optional.empty());
/*     */   }
/*     */   
/*     */   public TreeGrower(String name, float secondaryChance, Optional<ResourceKey<ConfiguredFeature<?, ?>>> megaTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryMegaTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> flowers, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryFlowers) {
/*  57 */     this.name = name;
/*  58 */     this.secondaryChance = secondaryChance;
/*  59 */     this.megaTree = megaTree;
/*  60 */     this.secondaryMegaTree = secondaryMegaTree;
/*  61 */     this.tree = tree;
/*  62 */     this.secondaryTree = secondaryTree;
/*  63 */     this.flowers = flowers;
/*  64 */     this.secondaryFlowers = secondaryFlowers;
/*  66 */     GROWERS.put(name, this);
/*     */   }
/*     */   
/*     */   private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
/*  70 */     if (random.nextFloat() < this.secondaryChance) {
/*  71 */       if (hasFlowers && this.secondaryFlowers.isPresent())
/*  72 */         return this.secondaryFlowers.get(); 
/*  74 */       if (this.secondaryTree.isPresent())
/*  75 */         return this.secondaryTree.get(); 
/*     */     } 
/*  78 */     if (hasFlowers && this.flowers.isPresent())
/*  79 */       return this.flowers.get(); 
/*  81 */     return this.tree.orElse(null);
/*     */   }
/*     */   
/*     */   private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
/*  85 */     if (this.secondaryMegaTree.isPresent() && random.nextFloat() < this.secondaryChance)
/*  86 */       return this.secondaryMegaTree.get(); 
/*  88 */     return this.megaTree.orElse(null);
/*     */   }
/*     */   
/*     */   public boolean growTree(ServerLevel level, ChunkGenerator generator, BlockPos pos, BlockState state, RandomSource random) {
/*  92 */     ResourceKey<ConfiguredFeature<?, ?>> megaFeatureKey = getConfiguredMegaFeature(random);
/*  93 */     if (megaFeatureKey != null) {
/*  94 */       Holder<ConfiguredFeature<?, ?>> holder = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(megaFeatureKey).orElse(null);
/*  95 */       if (holder != null)
/*  96 */         for (int dx = 0; dx >= -1; dx--) {
/*  97 */           for (int dz = 0; dz >= -1; dz--) {
/*  98 */             if (isTwoByTwoSapling(state, (BlockGetter)level, pos, dx, dz)) {
/*  99 */               ConfiguredFeature<?, ?> configuredFeature = (ConfiguredFeature<?, ?>)holder.value();
/* 101 */               BlockState air = Blocks.AIR.defaultBlockState();
/* 102 */               level.setBlock(pos.offset(dx, 0, dz), air, 260);
/* 103 */               level.setBlock(pos.offset(dx + 1, 0, dz), air, 260);
/* 104 */               level.setBlock(pos.offset(dx, 0, dz + 1), air, 260);
/* 105 */               level.setBlock(pos.offset(dx + 1, 0, dz + 1), air, 260);
/* 107 */               if (configuredFeature.place((WorldGenLevel)level, generator, random, pos.offset(dx, 0, dz)))
/* 108 */                 return true; 
/* 110 */               level.setBlock(pos.offset(dx, 0, dz), state, 260);
/* 111 */               level.setBlock(pos.offset(dx + 1, 0, dz), state, 260);
/* 112 */               level.setBlock(pos.offset(dx, 0, dz + 1), state, 260);
/* 113 */               level.setBlock(pos.offset(dx + 1, 0, dz + 1), state, 260);
/* 114 */               return false;
/*     */             } 
/*     */           } 
/*     */         }  
/*     */     } 
/* 121 */     ResourceKey<ConfiguredFeature<?, ?>> featureKey = getConfiguredFeature(random, hasFlowers((LevelAccessor)level, pos));
/* 122 */     if (featureKey == null)
/* 123 */       return false; 
/* 126 */     Holder<ConfiguredFeature<?, ?>> featureHolder = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(featureKey).orElse(null);
/* 127 */     if (featureHolder == null)
/* 128 */       return false; 
/* 131 */     ConfiguredFeature<?, ?> feature = (ConfiguredFeature<?, ?>)featureHolder.value();
/* 133 */     BlockState emptyBlock = level.getFluidState(pos).createLegacyBlock();
/* 134 */     level.setBlock(pos, emptyBlock, 260);
/* 136 */     if (feature.place((WorldGenLevel)level, generator, random, pos)) {
/* 137 */       if (level.getBlockState(pos) == emptyBlock)
/* 138 */         level.sendBlockUpdated(pos, state, emptyBlock, 2); 
/* 140 */       return true;
/*     */     } 
/* 143 */     level.setBlock(pos, state, 260);
/* 144 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isTwoByTwoSapling(BlockState state, BlockGetter level, BlockPos pos, int ox, int oz) {
/* 148 */     Block block = state.getBlock();
/* 149 */     return (level.getBlockState(pos.offset(ox, 0, oz)).is(block) && level
/* 150 */       .getBlockState(pos.offset(ox + 1, 0, oz)).is(block) && level
/* 151 */       .getBlockState(pos.offset(ox, 0, oz + 1)).is(block) && level
/* 152 */       .getBlockState(pos.offset(ox + 1, 0, oz + 1)).is(block));
/*     */   }
/*     */   
/*     */   private boolean hasFlowers(LevelAccessor level, BlockPos pos) {
/* 156 */     for (BlockPos p : BlockPos.MutableBlockPos.betweenClosed(pos.below().north(2).west(2), pos.above().south(2).east(2))) {
/* 157 */       if (level.getBlockState(p).is(BlockTags.FLOWERS))
/* 158 */         return true; 
/*     */     } 
/* 161 */     return false;
/*     */   }
/*     */   
/*     */   public OptionalInt getMinimumHeight(ServerLevel level) {
/* 165 */     ResourceKey<ConfiguredFeature<?, ?>> featureKey = this.tree.orElse(null);
/* 166 */     if (featureKey == null)
/* 167 */       return OptionalInt.empty(); 
/* 173 */     Holder<ConfiguredFeature<?, ?>> featureHolder = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(featureKey).orElse(null);
/* 175 */     if (featureHolder != null) {
/* 175 */       FeatureConfiguration featureConfiguration = ((ConfiguredFeature)featureHolder.value()).config();
/* 175 */       if (featureConfiguration instanceof TreeConfiguration) {
/* 175 */         TreeConfiguration treeConfig = (TreeConfiguration)featureConfiguration;
/* 176 */         return OptionalInt.of(treeConfig.trunkPlacer.getBaseHeight());
/*     */       } 
/*     */     } 
/* 179 */     return OptionalInt.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\grower\TreeGrower.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */