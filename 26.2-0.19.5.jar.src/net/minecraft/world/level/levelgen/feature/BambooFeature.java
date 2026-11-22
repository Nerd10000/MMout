/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.BambooStalkBlock;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BambooLeaves;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ 
/*    */ public class BambooFeature extends Feature<ProbabilityFeatureConfiguration> {
/* 18 */   private static final BlockState BAMBOO_TRUNK = (BlockState)((BlockState)((BlockState)Blocks.BAMBOO.defaultBlockState().setValue((Property)BambooStalkBlock.AGE, Integer.valueOf(1))).setValue((Property)BambooStalkBlock.LEAVES, (Comparable)BambooLeaves.NONE)).setValue((Property)BambooStalkBlock.STAGE, Integer.valueOf(0));
/*    */   
/* 19 */   private static final BlockState BAMBOO_FINAL_LARGE = (BlockState)((BlockState)BAMBOO_TRUNK.setValue((Property)BambooStalkBlock.LEAVES, (Comparable)BambooLeaves.LARGE)).setValue((Property)BambooStalkBlock.STAGE, Integer.valueOf(1));
/*    */   
/* 20 */   private static final BlockState BAMBOO_TOP_LARGE = (BlockState)BAMBOO_TRUNK.setValue((Property)BambooStalkBlock.LEAVES, (Comparable)BambooLeaves.LARGE);
/*    */   
/* 21 */   private static final BlockState BAMBOO_TOP_SMALL = (BlockState)BAMBOO_TRUNK.setValue((Property)BambooStalkBlock.LEAVES, (Comparable)BambooLeaves.SMALL);
/*    */   
/*    */   public BambooFeature(Codec<ProbabilityFeatureConfiguration> codec) {
/* 24 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> context) {
/* 29 */     int placed = 0;
/* 31 */     BlockPos origin = context.origin();
/* 32 */     WorldGenLevel level = context.level();
/* 33 */     RandomSource random = context.random();
/* 34 */     ProbabilityFeatureConfiguration config = context.config();
/* 35 */     BlockPos.MutableBlockPos bambooPos = origin.mutable();
/* 36 */     BlockPos.MutableBlockPos podzolPos = origin.mutable();
/* 37 */     if (level.isEmptyBlock((BlockPos)bambooPos)) {
/* 38 */       if (Blocks.BAMBOO.defaultBlockState().canSurvive((LevelReader)level, (BlockPos)bambooPos)) {
/* 39 */         int height = random.nextInt(12) + 5;
/* 42 */         if (random.nextFloat() < config.probability) {
/* 43 */           int r = random.nextInt(4) + 1;
/* 44 */           for (int xx = origin.getX() - r; xx <= origin.getX() + r; xx++) {
/* 45 */             for (int zz = origin.getZ() - r; zz <= origin.getZ() + r; zz++) {
/* 46 */               int xd = xx - origin.getX();
/* 47 */               int zd = zz - origin.getZ();
/* 48 */               if (xd * xd + zd * zd <= r * r) {
/* 52 */                 podzolPos.set(xx, level.getHeight(Heightmap.Types.WORLD_SURFACE, xx, zz) - 1, zz);
/* 53 */                 if (level.getBlockState((BlockPos)podzolPos).is(BlockTags.BENEATH_BAMBOO_PODZOL_REPLACEABLE))
/* 54 */                   level.setBlock((BlockPos)podzolPos, Blocks.PODZOL.defaultBlockState(), 2); 
/*    */               } 
/*    */             } 
/*    */           } 
/*    */         } 
/* 60 */         for (int i = 0; i < height && 
/* 61 */           level.isEmptyBlock((BlockPos)bambooPos); i++) {
/* 62 */           level.setBlock((BlockPos)bambooPos, BAMBOO_TRUNK, 2);
/* 66 */           bambooPos.move(Direction.UP, 1);
/*    */         } 
/* 69 */         if (bambooPos.getY() - origin.getY() >= 3) {
/* 70 */           level.setBlock((BlockPos)bambooPos, BAMBOO_FINAL_LARGE, 2);
/* 71 */           level.setBlock((BlockPos)bambooPos.move(Direction.DOWN, 1), BAMBOO_TOP_LARGE, 2);
/* 72 */           level.setBlock((BlockPos)bambooPos.move(Direction.DOWN, 1), BAMBOO_TOP_SMALL, 2);
/*    */         } 
/*    */       } 
/* 75 */       placed++;
/*    */     } 
/* 78 */     return (placed > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\BambooFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */