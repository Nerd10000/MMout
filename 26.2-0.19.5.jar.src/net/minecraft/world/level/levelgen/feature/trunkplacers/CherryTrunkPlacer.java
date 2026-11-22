/*     */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.IntProviders;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ 
/*     */ public class CherryTrunkPlacer extends TrunkPlacer {
/*     */   private static final Codec<UniformInt> BRANCH_START_CODEC;
/*     */   
/*     */   public static final MapCodec<CherryTrunkPlacer> CODEC;
/*     */   
/*     */   private final IntProvider branchCount;
/*     */   
/*     */   private final IntProvider branchHorizontalLength;
/*     */   
/*     */   private final UniformInt branchStartOffsetFromTop;
/*     */   
/*     */   private final UniformInt secondBranchStartOffsetFromTop;
/*     */   
/*     */   private final IntProvider branchEndOffsetFromTop;
/*     */   
/*     */   static {
/*  25 */     BRANCH_START_CODEC = UniformInt.MAP_CODEC.codec().validate(u -> (u.maxInclusive() - u.minInclusive() < 1) ? DataResult.error(()) : DataResult.success(u));
/*  32 */     CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).and(i.group((App)IntProviders.codec(1, 3).fieldOf("branch_count").forGetter(()), (App)IntProviders.codec(2, 16).fieldOf("branch_horizontal_length").forGetter(()), (App)IntProviders.validateCodec(-16, 0, BRANCH_START_CODEC).fieldOf("branch_start_offset_from_top").forGetter(()), (App)IntProviders.codec(-16, 16).fieldOf("branch_end_offset_from_top").forGetter(()))).apply((Applicative)i, CherryTrunkPlacer::new));
/*     */   }
/*     */   
/*     */   public CherryTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, IntProvider branchCount, IntProvider branchHorizontalLength, UniformInt branchStartOffsetFromTop, IntProvider branchEndOffsetFromTop) {
/*  47 */     super(baseHeight, heightRandA, heightRandB);
/*  48 */     this.branchCount = branchCount;
/*  49 */     this.branchHorizontalLength = branchHorizontalLength;
/*  50 */     this.branchStartOffsetFromTop = branchStartOffsetFromTop;
/*  51 */     this.secondBranchStartOffsetFromTop = UniformInt.of(branchStartOffsetFromTop.minInclusive(), branchStartOffsetFromTop.maxInclusive() - 1);
/*  52 */     this.branchEndOffsetFromTop = branchEndOffsetFromTop;
/*     */   }
/*     */   
/*     */   protected TrunkPlacerType<?> type() {
/*  57 */     return TrunkPlacerType.CHERRY_TRUNK_PLACER;
/*     */   }
/*     */   
/*     */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/*     */     int trunkHeight;
/*  69 */     placeBelowTrunkBlock(level, trunkSetter, random, origin.below(), config);
/*  80 */     int firstBranchOffsetFromOrigin = Math.max(0, treeHeight - 1 + this.branchStartOffsetFromTop.sample(random));
/*  81 */     int secondBranchOffsetFromOrigin = Math.max(0, treeHeight - 1 + this.secondBranchStartOffsetFromTop.sample(random));
/*  82 */     if (secondBranchOffsetFromOrigin >= firstBranchOffsetFromOrigin)
/*  83 */       secondBranchOffsetFromOrigin++; 
/*  86 */     int branchCount = this.branchCount.sample(random);
/*  87 */     boolean hasMiddleBranch = (branchCount == 3);
/*  88 */     boolean hasBothSideBranches = (branchCount >= 2);
/*  91 */     if (hasMiddleBranch) {
/*  92 */       trunkHeight = treeHeight;
/*  93 */     } else if (hasBothSideBranches) {
/*  94 */       trunkHeight = Math.max(firstBranchOffsetFromOrigin, secondBranchOffsetFromOrigin) + 1;
/*     */     } else {
/*  96 */       trunkHeight = firstBranchOffsetFromOrigin + 1;
/*     */     } 
/*  99 */     for (int y = 0; y < trunkHeight; y++)
/* 100 */       placeLog(level, trunkSetter, random, origin.above(y), config); 
/* 103 */     List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<>();
/* 105 */     if (hasMiddleBranch)
/* 106 */       attachments.add(new FoliagePlacer.FoliageAttachment(origin.above(trunkHeight), 0, false)); 
/* 109 */     BlockPos.MutableBlockPos logPos = new BlockPos.MutableBlockPos();
/* 110 */     Direction treeDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 111 */     Function<BlockState, BlockState> sidewaysStateModifier = state -> (BlockState)state.trySetValue((Property)RotatedPillarBlock.AXIS, (Comparable)treeDirection.getAxis());
/* 113 */     attachments.add(generateBranch(level, trunkSetter, random, treeHeight, origin, config, sidewaysStateModifier, treeDirection, firstBranchOffsetFromOrigin, (firstBranchOffsetFromOrigin < trunkHeight - 1), logPos));
/* 115 */     if (hasBothSideBranches)
/* 116 */       attachments.add(generateBranch(level, trunkSetter, random, treeHeight, origin, config, sidewaysStateModifier, treeDirection.getOpposite(), secondBranchOffsetFromOrigin, (secondBranchOffsetFromOrigin < trunkHeight - 1), logPos)); 
/* 119 */     return attachments;
/*     */   }
/*     */   
/*     */   private FoliagePlacer.FoliageAttachment generateBranch(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config, Function<BlockState, BlockState> sidewaysStateModifier, Direction branchDirection, int offsetFromOrigin, boolean middleContinuesUpwards, BlockPos.MutableBlockPos logPos) {
/* 135 */     logPos.set((Vec3i)origin).move(Direction.UP, offsetFromOrigin);
/* 137 */     int branchEndPosOffsetFromOrigin = treeHeight - 1 + this.branchEndOffsetFromTop.sample(random);
/* 139 */     boolean extendBranchAwayFromTrunk = (middleContinuesUpwards || branchEndPosOffsetFromOrigin < offsetFromOrigin);
/* 140 */     int distanceToTrunk = this.branchHorizontalLength.sample(random) + (extendBranchAwayFromTrunk ? 1 : 0);
/* 143 */     BlockPos branchEndPos = origin.relative(branchDirection, distanceToTrunk).above(branchEndPosOffsetFromOrigin);
/* 145 */     int stepsHorizontally = extendBranchAwayFromTrunk ? 2 : 1;
/* 147 */     for (int i = 0; i < stepsHorizontally; i++)
/* 148 */       placeLog(level, trunkSetter, random, (BlockPos)logPos.move(branchDirection), config, sidewaysStateModifier); 
/* 151 */     Direction verticalDirection = (branchEndPos.getY() > logPos.getY()) ? Direction.UP : Direction.DOWN;
/*     */     while (true) {
/* 154 */       int distance = logPos.distManhattan((Vec3i)branchEndPos);
/* 155 */       if (distance == 0)
/*     */         break; 
/* 159 */       float chanceToGrowVertically = Math.abs(branchEndPos.getY() - logPos.getY()) / distance;
/* 160 */       boolean growVertically = (random.nextFloat() < chanceToGrowVertically);
/* 162 */       logPos.move(growVertically ? verticalDirection : branchDirection);
/* 163 */       placeLog(level, trunkSetter, random, (BlockPos)logPos, config, growVertically ? Function.<BlockState>identity() : sidewaysStateModifier);
/*     */     } 
/* 166 */     return new FoliagePlacer.FoliageAttachment(branchEndPos.above(), 0, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\CherryTrunkPlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */