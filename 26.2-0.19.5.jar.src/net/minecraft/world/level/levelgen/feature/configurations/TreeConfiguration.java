/*     */ package net.minecraft.world.level.levelgen.feature.configurations;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
/*     */ 
/*     */ public class TreeConfiguration implements FeatureConfiguration {
/*  23 */   public static final BlockPredicate CAN_PLACE_BELOW_TREE_TRUNKS = BlockPredicate.not(BlockPredicate.matchesTag(BlockTags.CANNOT_REPLACE_BELOW_TREE_TRUNK));
/*     */   
/*     */   public static final Codec<TreeConfiguration> CODEC;
/*     */   
/*     */   public final BlockStateProvider trunkProvider;
/*     */   
/*     */   public final TrunkPlacer trunkPlacer;
/*     */   
/*     */   public final BlockStateProvider foliageProvider;
/*     */   
/*     */   public final FoliagePlacer foliagePlacer;
/*     */   
/*     */   public final Optional<RootPlacer> rootPlacer;
/*     */   
/*     */   public final FeatureSize minimumSize;
/*     */   
/*     */   public final List<TreeDecorator> decorators;
/*     */   
/*     */   public final boolean ignoreVines;
/*     */   
/*     */   public final BlockStateProvider belowTrunkProvider;
/*     */   
/*     */   static {
/*  25 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(()), (App)TrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("foliage_provider").forGetter(()), (App)FoliagePlacer.CODEC.fieldOf("foliage_placer").forGetter(()), (App)RootPlacer.CODEC.optionalFieldOf("root_placer").forGetter(()), (App)FeatureSize.CODEC.fieldOf("minimum_size").forGetter(()), (App)TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(()), (App)Codec.BOOL.fieldOf("ignore_vines").orElse(Boolean.valueOf(false)).forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("below_trunk_provider").forGetter(())).apply((Applicative)i, TreeConfiguration::new));
/*     */   }
/*     */   
/*     */   protected TreeConfiguration(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, Optional<RootPlacer> rootPlacer, FeatureSize minimumSize, List<TreeDecorator> decorators, boolean ignoreVines, BlockStateProvider belowTrunkProvider) {
/*  48 */     this.trunkProvider = trunkProvider;
/*  49 */     this.trunkPlacer = trunkPlacer;
/*  50 */     this.foliageProvider = foliageProvider;
/*  51 */     this.foliagePlacer = foliagePlacer;
/*  52 */     this.rootPlacer = rootPlacer;
/*  53 */     this.minimumSize = minimumSize;
/*  54 */     this.decorators = decorators;
/*  55 */     this.ignoreVines = ignoreVines;
/*  56 */     this.belowTrunkProvider = belowTrunkProvider;
/*     */   }
/*     */   
/*     */   public static BlockStateProvider defaultPlaceBelowTreeTrunkProvider(HolderGetter<Biome> biomes) {
/*  60 */     return (BlockStateProvider)RuleBasedStateProvider.ifTrueThenProvide(CAN_PLACE_BELOW_TREE_TRUNKS, Blocks.DIRT);
/*     */   }
/*     */   
/*     */   public static class TreeConfigurationBuilder {
/*     */     public final BlockStateProvider trunkProvider;
/*     */     
/*     */     private final TrunkPlacer trunkPlacer;
/*     */     
/*     */     public final BlockStateProvider foliageProvider;
/*     */     
/*     */     private final FoliagePlacer foliagePlacer;
/*     */     
/*     */     private final Optional<RootPlacer> rootPlacer;
/*     */     
/*     */     private final FeatureSize minimumSize;
/*     */     
/*  70 */     private List<TreeDecorator> decorators = (List<TreeDecorator>)ImmutableList.of();
/*     */     
/*     */     private boolean ignoreVines;
/*     */     
/*     */     private BlockStateProvider belowTrunkProvider;
/*     */     
/*     */     public TreeConfigurationBuilder(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, Optional<RootPlacer> rootPlacer, FeatureSize minimumSize, BlockStateProvider belowTrunkProvider) {
/*  75 */       this.trunkProvider = trunkProvider;
/*  76 */       this.trunkPlacer = trunkPlacer;
/*  77 */       this.foliageProvider = foliageProvider;
/*  78 */       this.foliagePlacer = foliagePlacer;
/*  79 */       this.rootPlacer = rootPlacer;
/*  80 */       this.minimumSize = minimumSize;
/*  81 */       this.belowTrunkProvider = belowTrunkProvider;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, FeatureSize minimumSize, BlockStateProvider belowTrunkProvider) {
/*  85 */       this(trunkProvider, trunkPlacer, foliageProvider, foliagePlacer, Optional.empty(), minimumSize, belowTrunkProvider);
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder belowTrunkProvider(BlockStateProvider belowTrunkProvider) {
/*  89 */       this.belowTrunkProvider = belowTrunkProvider;
/*  90 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder decorators(List<TreeDecorator> decorators) {
/*  94 */       this.decorators = decorators;
/*  95 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder ignoreVines() {
/*  99 */       this.ignoreVines = true;
/* 100 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfiguration build() {
/* 104 */       return new TreeConfiguration(this.trunkProvider, this.trunkPlacer, this.foliageProvider, this.foliagePlacer, this.rootPlacer, this.minimumSize, this.decorators, this.ignoreVines, this.belowTrunkProvider);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\TreeConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */