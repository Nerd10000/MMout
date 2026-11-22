/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*    */ 
/*    */ public abstract class AbstractHugeMushroomFeature extends Feature<HugeMushroomFeatureConfiguration> {
/*    */   public static final int MIN_MUSHROOM_HEIGHT = 4;
/*    */   
/*    */   public AbstractHugeMushroomFeature(Codec<HugeMushroomFeatureConfiguration> codec) {
/* 17 */     super(codec);
/*    */   }
/*    */   
/*    */   protected void placeTrunk(WorldGenLevel level, RandomSource random, BlockPos origin, HugeMushroomFeatureConfiguration config, int treeHeight, BlockPos.MutableBlockPos blockPos) {
/* 21 */     for (int dy = 0; dy < treeHeight; dy++) {
/* 22 */       blockPos.set((Vec3i)origin).move(Direction.UP, dy);
/* 23 */       placeMushroomBlock((LevelAccessor)level, blockPos, config.stemProvider().getState(level, random, origin));
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void placeMushroomBlock(LevelAccessor level, BlockPos.MutableBlockPos blockPos, BlockState newState) {
/* 28 */     BlockState currentState = level.getBlockState((BlockPos)blockPos);
/* 29 */     if (currentState.isAir() || currentState.is(BlockTags.REPLACEABLE_BY_MUSHROOMS))
/* 30 */       setBlock((LevelWriter)level, (BlockPos)blockPos, newState); 
/*    */   }
/*    */   
/*    */   protected int getTreeHeight(RandomSource random) {
/* 35 */     int treeHeight = random.nextInt(3) + 4;
/* 36 */     if (random.nextInt(12) == 0)
/* 37 */       treeHeight *= 2; 
/* 39 */     return treeHeight;
/*    */   }
/*    */   
/*    */   protected boolean isValidPosition(WorldGenLevel level, BlockPos origin, int treeHeight, BlockPos.MutableBlockPos blockPos, HugeMushroomFeatureConfiguration config) {
/* 43 */     int y = origin.getY();
/* 44 */     if (y < level.getMinY() + 1 || y + treeHeight + 1 > level.getMaxY())
/* 45 */       return false; 
/* 48 */     if (!config.canPlaceOn().test(level, origin.below()))
/* 49 */       return false; 
/* 52 */     for (int dy = 0; dy <= treeHeight; dy++) {
/* 53 */       int radius = getTreeRadiusForHeight(-1, -1, config.foliageRadius(), dy);
/* 54 */       for (int dx = -radius; dx <= radius; dx++) {
/* 55 */         for (int dz = -radius; dz <= radius; dz++) {
/* 56 */           BlockState state = level.getBlockState((BlockPos)blockPos.setWithOffset((Vec3i)origin, dx, dy, dz));
/* 57 */           if (!state.isAir() && !state.is(BlockTags.LEAVES))
/* 58 */             return false; 
/*    */         } 
/*    */       } 
/*    */     } 
/* 63 */     return true;
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<HugeMushroomFeatureConfiguration> context) {
/* 68 */     WorldGenLevel level = context.level();
/* 69 */     BlockPos origin = context.origin();
/* 70 */     RandomSource random = context.random();
/* 71 */     HugeMushroomFeatureConfiguration config = context.config();
/* 72 */     int treeHeight = getTreeHeight(random);
/* 74 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 75 */     if (!isValidPosition(level, origin, treeHeight, blockPos, config))
/* 76 */       return false; 
/* 79 */     makeCap(level, random, origin, treeHeight, blockPos, config);
/* 80 */     placeTrunk(level, random, origin, config, treeHeight, blockPos);
/* 81 */     return true;
/*    */   }
/*    */   
/*    */   protected abstract int getTreeRadiusForHeight(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*    */   
/*    */   protected abstract void makeCap(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, int paramInt, BlockPos.MutableBlockPos paramMutableBlockPos, HugeMushroomFeatureConfiguration paramHugeMushroomFeatureConfiguration);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\AbstractHugeMushroomFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */