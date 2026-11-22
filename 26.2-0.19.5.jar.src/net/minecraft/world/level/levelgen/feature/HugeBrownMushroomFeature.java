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
/*    */ public class HugeBrownMushroomFeature extends AbstractHugeMushroomFeature {
/*    */   public HugeBrownMushroomFeature(Codec<HugeMushroomFeatureConfiguration> codec) {
/* 13 */     super(codec);
/*    */   }
/*    */   
/*    */   protected void makeCap(WorldGenLevel level, RandomSource random, BlockPos origin, int treeHeight, BlockPos.MutableBlockPos blockPos, HugeMushroomFeatureConfiguration config) {
/* 18 */     int radius = config.foliageRadius();
/* 19 */     for (int dx = -radius; dx <= radius; dx++) {
/* 20 */       for (int dz = -radius; dz <= radius; dz++) {
/* 21 */         boolean minX = (dx == -radius);
/* 22 */         boolean maxX = (dx == radius);
/* 23 */         boolean minZ = (dz == -radius);
/* 24 */         boolean maxZ = (dz == radius);
/* 26 */         boolean xEdge = (minX || maxX);
/* 27 */         boolean zEdge = (minZ || maxZ);
/* 28 */         if (!xEdge || !zEdge) {
/* 32 */           blockPos.setWithOffset((Vec3i)origin, dx, treeHeight, dz);
/* 33 */           boolean west = (minX || (zEdge && dx == 1 - radius));
/* 34 */           boolean east = (maxX || (zEdge && dx == radius - 1));
/* 35 */           boolean north = (minZ || (xEdge && dz == 1 - radius));
/* 36 */           boolean south = (maxZ || (xEdge && dz == radius - 1));
/* 37 */           BlockState state = config.capProvider().getState(level, random, origin);
/* 38 */           if (state.hasProperty((Property)HugeMushroomBlock.WEST) && state
/* 39 */             .hasProperty((Property)HugeMushroomBlock.EAST) && state
/* 40 */             .hasProperty((Property)HugeMushroomBlock.NORTH) && state
/* 41 */             .hasProperty((Property)HugeMushroomBlock.SOUTH))
/* 47 */             state = (BlockState)((BlockState)((BlockState)((BlockState)state.setValue((Property)HugeMushroomBlock.WEST, Boolean.valueOf(west))).setValue((Property)HugeMushroomBlock.EAST, Boolean.valueOf(east))).setValue((Property)HugeMushroomBlock.NORTH, Boolean.valueOf(north))).setValue((Property)HugeMushroomBlock.SOUTH, Boolean.valueOf(south)); 
/* 49 */           placeMushroomBlock((LevelAccessor)level, blockPos, state);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getTreeRadiusForHeight(int trunkHeight, int treeHeight, int leafRadius, int yo) {
/* 56 */     return (yo <= 3) ? 0 : leafRadius;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\HugeBrownMushroomFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */