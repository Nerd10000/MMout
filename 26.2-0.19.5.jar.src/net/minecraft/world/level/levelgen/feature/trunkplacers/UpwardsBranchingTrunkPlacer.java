/*     */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.IntProviders;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ 
/*     */ public class UpwardsBranchingTrunkPlacer extends TrunkPlacer {
/*     */   public static final MapCodec<UpwardsBranchingTrunkPlacer> CODEC;
/*     */   
/*     */   private final IntProvider extraBranchSteps;
/*     */   
/*     */   private final float placeBranchPerLogProbability;
/*     */   
/*     */   private final IntProvider extraBranchLength;
/*     */   
/*     */   private final HolderSet<Block> canGrowThrough;
/*     */   
/*     */   static {
/*  25 */     CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).and(i.group((App)IntProviders.POSITIVE_CODEC.fieldOf("extra_branch_steps").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("place_branch_per_log_probability").forGetter(()), (App)IntProviders.NON_NEGATIVE_CODEC.fieldOf("extra_branch_length").forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_grow_through").forGetter(()))).apply((Applicative)i, UpwardsBranchingTrunkPlacer::new));
/*     */   }
/*     */   
/*     */   public UpwardsBranchingTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, IntProvider extraBranchSteps, float placeBranchPerLogProbability, IntProvider extraBranchLength, HolderSet<Block> canGrowThrough) {
/*  40 */     super(baseHeight, heightRandA, heightRandB);
/*  41 */     this.extraBranchSteps = extraBranchSteps;
/*  42 */     this.placeBranchPerLogProbability = placeBranchPerLogProbability;
/*  43 */     this.extraBranchLength = extraBranchLength;
/*  44 */     this.canGrowThrough = canGrowThrough;
/*     */   }
/*     */   
/*     */   protected TrunkPlacerType<?> type() {
/*  49 */     return TrunkPlacerType.UPWARDS_BRANCHING_TRUNK_PLACER;
/*     */   }
/*     */   
/*     */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/*  54 */     List<FoliagePlacer.FoliageAttachment> attachments = Lists.newArrayList();
/*  56 */     BlockPos.MutableBlockPos logPos = new BlockPos.MutableBlockPos();
/*  57 */     for (int heightPos = 0; heightPos < treeHeight; heightPos++) {
/*  58 */       int currentHeight = origin.getY() + heightPos;
/*  59 */       if (placeLog(level, trunkSetter, random, (BlockPos)logPos.set(origin.getX(), currentHeight, origin.getZ()), config) && 
/*  60 */         heightPos < treeHeight - 1 && random.nextFloat() < this.placeBranchPerLogProbability) {
/*  61 */         Direction branchDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/*  62 */         int branchLen = this.extraBranchLength.sample(random);
/*  63 */         int branchPos = Math.max(0, branchLen - this.extraBranchLength.sample(random) - 1);
/*  64 */         int branchSteps = this.extraBranchSteps.sample(random);
/*  65 */         placeBranch(level, trunkSetter, random, treeHeight, config, attachments, logPos, currentHeight, branchDir, branchPos, branchSteps);
/*     */       } 
/*  69 */       if (heightPos == treeHeight - 1)
/*  70 */         attachments.add(new FoliagePlacer.FoliageAttachment((BlockPos)logPos.set(origin.getX(), currentHeight + 1, origin.getZ()), 0, false)); 
/*     */     } 
/*  75 */     return attachments;
/*     */   }
/*     */   
/*     */   private void placeBranch(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, TreeConfiguration config, List<FoliagePlacer.FoliageAttachment> attachments, BlockPos.MutableBlockPos logPos, int currentHeight, Direction branchDir, int branchPos, int branchSteps) {
/*  79 */     int heightAlongBranch = currentHeight + branchPos;
/*  80 */     int logX = logPos.getX();
/*  81 */     int logZ = logPos.getZ();
/*  82 */     for (int branchPlacementIndex = branchPos; branchPlacementIndex < treeHeight && branchSteps > 0; branchPlacementIndex++, branchSteps--) {
/*  83 */       if (branchPlacementIndex >= 1) {
/*  86 */         int placementHeight = currentHeight + branchPlacementIndex;
/*  87 */         logX += branchDir.getStepX();
/*  88 */         logZ += branchDir.getStepZ();
/*  89 */         heightAlongBranch = placementHeight;
/*  91 */         if (placeLog(level, trunkSetter, random, (BlockPos)logPos.set(logX, placementHeight, logZ), config))
/*  92 */           heightAlongBranch++; 
/*  95 */         attachments.add(new FoliagePlacer.FoliageAttachment(logPos.immutable(), 0, false));
/*     */       } 
/*     */     } 
/*  97 */     if (heightAlongBranch - currentHeight > 1) {
/*  98 */       BlockPos foliagePos = new BlockPos(logX, heightAlongBranch, logZ);
/*  99 */       attachments.add(new FoliagePlacer.FoliageAttachment(foliagePos, 0, false));
/* 100 */       attachments.add(new FoliagePlacer.FoliageAttachment(foliagePos.below(2), 0, false));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean validTreePos(WorldGenLevel level, BlockPos pos) {
/* 106 */     return (super.validTreePos(level, pos) || level.isStateAtPosition(pos, s -> s.is(this.canGrowThrough)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\UpwardsBranchingTrunkPlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */