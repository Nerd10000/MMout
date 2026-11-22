/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RootSystemConfiguration;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ 
/*     */ public class RootSystemFeature extends Feature<RootSystemConfiguration> {
/*     */   public RootSystemFeature(Codec<RootSystemConfiguration> codec) {
/*  19 */     super(codec);
/*     */   }
/*     */   
/*     */   public boolean place(FeaturePlaceContext<RootSystemConfiguration> context) {
/*  24 */     WorldGenLevel level = context.level();
/*  25 */     BlockPos origin = context.origin();
/*  26 */     if (!level.getBlockState(origin).isAir())
/*  27 */       return false; 
/*  30 */     RandomSource random = context.random();
/*  31 */     BlockPos pos = context.origin();
/*  32 */     RootSystemConfiguration config = context.config();
/*  33 */     BlockPos.MutableBlockPos workingPos = pos.mutable();
/*  34 */     if (placeDirtAndTree(level, context.chunkGenerator(), config, random, workingPos, pos))
/*  35 */       placeRoots(level, config, random, pos, workingPos); 
/*  38 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean spaceForTree(WorldGenLevel level, RootSystemConfiguration config, BlockPos pos) {
/*  42 */     BlockPos.MutableBlockPos columnUpPos = pos.mutable();
/*  43 */     for (int i = 1; i <= config.requiredVerticalSpaceForTree(); i++) {
/*  44 */       columnUpPos.move(Direction.UP);
/*  45 */       BlockState state = level.getBlockState((BlockPos)columnUpPos);
/*  46 */       if (!isAllowedTreeSpace(state, i, config.allowedVerticalWaterForTree()))
/*  47 */         return false; 
/*     */     } 
/*  50 */     if (config.levelTestDistance() > 0) {
/*  51 */       BlockPos.MutableBlockPos cornerPos = pos.mutable();
/*  52 */       for (int j = 0; j < 4; j++) {
/*  53 */         cornerPos.move(Direction.from2DDataValue(j), config.levelTestDistance());
/*  54 */         BlockState below = level.getBlockState(cornerPos.below(config.maxLevelDeviation()));
/*  55 */         BlockState above = level.getBlockState(cornerPos.above(config.maxLevelDeviation()));
/*  56 */         if (below.isAir() || !above.isAir())
/*  57 */           return false; 
/*  59 */         cornerPos.set((Vec3i)pos);
/*     */       } 
/*     */     } 
/*  62 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isAllowedTreeSpace(BlockState state, int blocksAboveOrigin, int allowedVerticalWaterHeight) {
/*  66 */     if (state.isAir())
/*  67 */       return true; 
/*  69 */     int blocksAboveGround = blocksAboveOrigin + 1;
/*  70 */     return (blocksAboveGround <= allowedVerticalWaterHeight && state.getFluidState().is(FluidTags.WATER));
/*     */   }
/*     */   
/*     */   private static boolean placeDirtAndTree(WorldGenLevel level, ChunkGenerator generator, RootSystemConfiguration config, RandomSource random, BlockPos.MutableBlockPos workingPos, BlockPos pos) {
/*  75 */     for (int y = 0; y < config.rootColumnMaxHeight(); y++) {
/*  76 */       workingPos.move(Direction.UP);
/*  78 */       if (level.getHeight(Heightmap.Types.WORLD_SURFACE, (BlockPos)workingPos) < workingPos.getY())
/*  79 */         return false; 
/*  82 */       if (config.allowedTreePosition().test(level, workingPos) && 
/*  83 */         spaceForTree(level, config, (BlockPos)workingPos)) {
/*  84 */         BlockPos belowPos = workingPos.below();
/*  85 */         if (level.getFluidState(belowPos).is(FluidTags.LAVA) || !level.getBlockState(belowPos).isSolid())
/*  86 */           return false; 
/*  89 */         if (((PlacedFeature)config.treeFeature().value()).place(level, generator, random, (BlockPos)workingPos)) {
/*  90 */           placeDirt(pos, pos.getY() + y, level, config, random);
/*  91 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/*  96 */     return false;
/*     */   }
/*     */   
/*     */   private static void placeDirt(BlockPos origin, int targetHeight, WorldGenLevel level, RootSystemConfiguration config, RandomSource random) {
/* 100 */     int originX = origin.getX();
/* 101 */     int originZ = origin.getZ();
/* 102 */     BlockPos.MutableBlockPos workingPos = origin.mutable();
/* 103 */     for (int y = origin.getY(); y < targetHeight; y++)
/* 104 */       placeRootedDirt(level, config, random, originX, originZ, workingPos.set(originX, y, originZ)); 
/*     */   }
/*     */   
/*     */   private static void placeRootedDirt(WorldGenLevel level, RootSystemConfiguration config, RandomSource random, int originX, int originZ, BlockPos.MutableBlockPos workingPos) {
/* 109 */     int rootRadius = config.rootRadius();
/* 110 */     Predicate<BlockState> stateTest = s -> s.is(config.rootReplaceable());
/* 111 */     for (int i = 0; i < config.rootPlacementAttempts(); i++) {
/* 112 */       workingPos.setWithOffset((Vec3i)workingPos, random.nextInt(rootRadius) - random.nextInt(rootRadius), 0, random.nextInt(rootRadius) - random.nextInt(rootRadius));
/* 113 */       if (stateTest.test(level.getBlockState((BlockPos)workingPos)))
/* 114 */         level.setBlock((BlockPos)workingPos, config.rootStateProvider().getState(level, random, (BlockPos)workingPos), 2); 
/* 117 */       workingPos.setX(originX);
/* 118 */       workingPos.setZ(originZ);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void placeRoots(WorldGenLevel level, RootSystemConfiguration config, RandomSource random, BlockPos pos, BlockPos.MutableBlockPos workingPos) {
/* 124 */     int rootRadius = config.hangingRootRadius();
/* 125 */     int verticalSpan = config.hangingRootsVerticalSpan();
/* 126 */     for (int i = 0; i < config.hangingRootPlacementAttempts(); i++) {
/* 127 */       workingPos.setWithOffset((Vec3i)pos, random.nextInt(rootRadius) - random.nextInt(rootRadius), random.nextInt(verticalSpan) - random.nextInt(verticalSpan), random.nextInt(rootRadius) - random.nextInt(rootRadius));
/* 128 */       if (level.isEmptyBlock((BlockPos)workingPos)) {
/* 129 */         BlockState targetState = config.hangingRootStateProvider().getState(level, random, (BlockPos)workingPos);
/* 130 */         if (targetState.canSurvive((LevelReader)level, (BlockPos)workingPos) && level.getBlockState(workingPos.above()).isFaceSturdy((BlockGetter)level, (BlockPos)workingPos, Direction.DOWN))
/* 131 */           level.setBlock((BlockPos)workingPos, targetState, 2); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\RootSystemFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */