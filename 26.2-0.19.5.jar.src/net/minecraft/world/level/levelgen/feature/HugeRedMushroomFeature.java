/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.HugeMushroomBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*    */ 
/*    */ public class HugeRedMushroomFeature extends AbstractHugeMushroomFeature {
/*    */   public HugeRedMushroomFeature(Codec<HugeMushroomFeatureConfiguration> codec) {
/* 13 */     super(codec);
/*    */   }
/*    */   
/*    */   protected void makeCap(WorldGenLevel level, RandomSource random, BlockPos origin, int treeHeight, BlockPos.MutableBlockPos blockPos, HugeMushroomFeatureConfiguration config) {
/* 18 */     for (int dy = treeHeight - 3; dy <= treeHeight; dy++) {
/* 19 */       int radius = (dy < treeHeight) ? config.foliageRadius() : (config.foliageRadius() - 1);
/* 20 */       int center = config.foliageRadius() - 2;
/* 22 */       for (int dx = -radius; dx <= radius; dx++) {
/* 23 */         for (int dz = -radius; dz <= radius; dz++) {
/* 24 */           boolean minX = (dx == -radius);
/* 25 */           boolean maxX = (dx == radius);
/* 26 */           boolean minZ = (dz == -radius);
/* 27 */           boolean maxZ = (dz == radius);
/* 29 */           boolean xEdge = (minX || maxX);
/* 30 */           boolean zEdge = (minZ || maxZ);
/* 32 */           if (dy >= treeHeight || xEdge != zEdge) {
/* 36 */             blockPos.setWithOffset((Vec3i)origin, dx, dy, dz);
/* 37 */             BlockState state = config.capProvider().getState(level, random, origin);
/* 38 */             if (state.hasProperty((Property)HugeMushroomBlock.WEST) && state
/* 39 */               .hasProperty((Property)HugeMushroomBlock.EAST) && state
/* 40 */               .hasProperty((Property)HugeMushroomBlock.NORTH) && state
/* 41 */               .hasProperty((Property)HugeMushroomBlock.SOUTH) && state
/* 42 */               .hasProperty((Property)HugeMushroomBlock.UP))
/* 49 */               state = (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)state.setValue((Property)HugeMushroomBlock.UP, Boolean.valueOf((dy >= treeHeight - 1)))).setValue((Property)HugeMushroomBlock.WEST, Boolean.valueOf((dx < -center)))).setValue((Property)HugeMushroomBlock.EAST, Boolean.valueOf((dx > center)))).setValue((Property)HugeMushroomBlock.NORTH, Boolean.valueOf((dz < -center)))).setValue((Property)HugeMushroomBlock.SOUTH, Boolean.valueOf((dz > center))); 
/* 51 */             placeMushroomBlock((LevelAccessor)level, blockPos, state);
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getTreeRadiusForHeight(int trunkHeight, int treeHeight, int leafRadius, int yo) {
/* 59 */     int radius = 0;
/* 60 */     if (yo < treeHeight && yo >= treeHeight - 3) {
/* 61 */       radius = leafRadius;
/* 62 */     } else if (yo == treeHeight) {
/* 63 */       radius = leafRadius;
/*    */     } 
/* 65 */     return radius;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\HugeRedMushroomFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */