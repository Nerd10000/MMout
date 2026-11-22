/*     */ package net.minecraft.world.level.levelgen.feature.configurations;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
/*     */ 
/*     */ public class TreeConfigurationBuilder {
/*     */   public final BlockStateProvider trunkProvider;
/*     */   
/*     */   private final TrunkPlacer trunkPlacer;
/*     */   
/*     */   public final BlockStateProvider foliageProvider;
/*     */   
/*     */   private final FoliagePlacer foliagePlacer;
/*     */   
/*     */   private final Optional<RootPlacer> rootPlacer;
/*     */   
/*     */   private final FeatureSize minimumSize;
/*     */   
/*  70 */   private List<TreeDecorator> decorators = (List<TreeDecorator>)ImmutableList.of();
/*     */   
/*     */   private boolean ignoreVines;
/*     */   
/*     */   private BlockStateProvider belowTrunkProvider;
/*     */   
/*     */   public TreeConfigurationBuilder(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, Optional<RootPlacer> rootPlacer, FeatureSize minimumSize, BlockStateProvider belowTrunkProvider) {
/*  75 */     this.trunkProvider = trunkProvider;
/*  76 */     this.trunkPlacer = trunkPlacer;
/*  77 */     this.foliageProvider = foliageProvider;
/*  78 */     this.foliagePlacer = foliagePlacer;
/*  79 */     this.rootPlacer = rootPlacer;
/*  80 */     this.minimumSize = minimumSize;
/*  81 */     this.belowTrunkProvider = belowTrunkProvider;
/*     */   }
/*     */   
/*     */   public TreeConfigurationBuilder(BlockStateProvider trunkProvider, TrunkPlacer trunkPlacer, BlockStateProvider foliageProvider, FoliagePlacer foliagePlacer, FeatureSize minimumSize, BlockStateProvider belowTrunkProvider) {
/*  85 */     this(trunkProvider, trunkPlacer, foliageProvider, foliagePlacer, Optional.empty(), minimumSize, belowTrunkProvider);
/*     */   }
/*     */   
/*     */   public TreeConfigurationBuilder belowTrunkProvider(BlockStateProvider belowTrunkProvider) {
/*  89 */     this.belowTrunkProvider = belowTrunkProvider;
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public TreeConfigurationBuilder decorators(List<TreeDecorator> decorators) {
/*  94 */     this.decorators = decorators;
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public TreeConfigurationBuilder ignoreVines() {
/*  99 */     this.ignoreVines = true;
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public TreeConfiguration build() {
/* 104 */     return new TreeConfiguration(this.trunkProvider, this.trunkPlacer, this.foliageProvider, this.foliagePlacer, this.rootPlacer, this.minimumSize, this.decorators, this.ignoreVines, this.belowTrunkProvider);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\TreeConfiguration$TreeConfigurationBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */