/*     */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ 
/*     */ public class MangroveRootPlacer extends RootPlacer {
/*     */   public static final int ROOT_WIDTH_LIMIT = 8;
/*     */   
/*     */   public static final int ROOT_LENGTH_LIMIT = 15;
/*     */   
/*     */   public static final MapCodec<MangroveRootPlacer> CODEC;
/*     */   
/*     */   private final MangroveRootPlacement mangroveRootPlacement;
/*     */   
/*     */   static {
/*  26 */     CODEC = RecordCodecBuilder.mapCodec(i -> rootPlacerParts(i).and((App)MangroveRootPlacement.CODEC.fieldOf("mangrove_root_placement").forGetter(())).apply((Applicative)i, MangroveRootPlacer::new));
/*     */   }
/*     */   
/*     */   public MangroveRootPlacer(IntProvider trunkOffsetY, BlockStateProvider rootProvider, Optional<AboveRootPlacement> aboveRootPlacement, MangroveRootPlacement mangroveRootPlacement) {
/*  33 */     super(trunkOffsetY, rootProvider, aboveRootPlacement);
/*  34 */     this.mangroveRootPlacement = mangroveRootPlacement;
/*     */   }
/*     */   
/*     */   public boolean placeRoots(WorldGenLevel level, BiConsumer<BlockPos, BlockState> rootSetter, RandomSource random, BlockPos origin, BlockPos trunkOrigin, TreeConfiguration config) {
/*  39 */     List<BlockPos> rootPositions = Lists.newArrayList();
/*  41 */     BlockPos.MutableBlockPos columnPos = origin.mutable();
/*  42 */     while (columnPos.getY() < trunkOrigin.getY()) {
/*  43 */       if (!canPlaceRoot((LevelSimulatedReader)level, (BlockPos)columnPos))
/*  44 */         return false; 
/*  46 */       columnPos.move(Direction.UP);
/*     */     } 
/*  49 */     rootPositions.add(trunkOrigin.below());
/*  52 */     for (Direction dir : Direction.Plane.HORIZONTAL) {
/*  53 */       BlockPos pos = trunkOrigin.relative(dir);
/*  54 */       List<BlockPos> positionsInDirection = Lists.newArrayList();
/*  56 */       if (!simulateRoots((LevelSimulatedReader)level, random, pos, dir, trunkOrigin, positionsInDirection, 0))
/*  57 */         return false; 
/*  60 */       rootPositions.addAll(positionsInDirection);
/*  61 */       rootPositions.add(trunkOrigin.relative(dir));
/*     */     } 
/*  64 */     for (BlockPos rootPos : rootPositions)
/*  65 */       placeRoot(level, rootSetter, random, rootPos, config); 
/*  68 */     return true;
/*     */   }
/*     */   
/*     */   private boolean simulateRoots(LevelSimulatedReader level, RandomSource random, BlockPos rootPos, Direction dir, BlockPos rootOrigin, List<BlockPos> rootPositions, int layer) {
/*  73 */     int maxRootLength = this.mangroveRootPlacement.maxRootLength();
/*  74 */     if (layer == maxRootLength || rootPositions.size() > maxRootLength)
/*  75 */       return false; 
/*  77 */     List<BlockPos> potentialRootPositions = potentialRootPositions(rootPos, dir, random, rootOrigin);
/*  78 */     for (BlockPos pos : potentialRootPositions) {
/*  79 */       if (canPlaceRoot(level, pos)) {
/*  80 */         rootPositions.add(pos);
/*  81 */         if (!simulateRoots(level, random, pos, dir, rootOrigin, rootPositions, layer + 1))
/*  82 */           return false; 
/*     */       } 
/*     */     } 
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   protected List<BlockPos> potentialRootPositions(BlockPos pos, Direction prevDir, RandomSource random, BlockPos rootOrigin) {
/*  91 */     BlockPos below = pos.below();
/*  92 */     BlockPos nextTo = pos.relative(prevDir);
/*  93 */     int width = pos.distManhattan((Vec3i)rootOrigin);
/*  94 */     int maxRootWidth = this.mangroveRootPlacement.maxRootWidth();
/*  95 */     float randomSkewChance = this.mangroveRootPlacement.randomSkewChance();
/*  98 */     if (width > maxRootWidth - 3 && width <= maxRootWidth)
/*  99 */       return (random.nextFloat() < randomSkewChance) ? List.<BlockPos>of(below, nextTo.below()) : List.<BlockPos>of(below); 
/* 103 */     if (width > maxRootWidth)
/* 104 */       return List.of(below); 
/* 108 */     if (random.nextFloat() < randomSkewChance)
/* 109 */       return List.of(below); 
/* 112 */     return random.nextBoolean() ? List.<BlockPos>of(nextTo) : List.<BlockPos>of(below);
/*     */   }
/*     */   
/*     */   protected boolean canPlaceRoot(LevelSimulatedReader level, BlockPos pos) {
/* 117 */     return (super.canPlaceRoot(level, pos) || level.isStateAtPosition(pos, state -> state.is(this.mangroveRootPlacement.canGrowThrough())));
/*     */   }
/*     */   
/*     */   protected void placeRoot(WorldGenLevel level, BiConsumer<BlockPos, BlockState> rootSetter, RandomSource random, BlockPos pos, TreeConfiguration config) {
/* 122 */     if (level.isStateAtPosition(pos, s -> s.is(this.mangroveRootPlacement.muddyRootsIn()))) {
/* 123 */       BlockState muddyRoots = this.mangroveRootPlacement.muddyRootsProvider().getState(level, random, pos);
/* 124 */       rootSetter.accept(pos, getPotentiallyWaterloggedState((LevelSimulatedReader)level, pos, muddyRoots));
/*     */     } else {
/* 126 */       super.placeRoot(level, rootSetter, random, pos, config);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected RootPlacerType<?> type() {
/* 132 */     return RootPlacerType.MANGROVE_ROOT_PLACER;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\MangroveRootPlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */