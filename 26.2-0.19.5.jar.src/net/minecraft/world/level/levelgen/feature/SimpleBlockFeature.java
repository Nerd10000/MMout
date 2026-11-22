/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.DoublePlantBlock;
/*    */ import net.minecraft.world.level.block.MossyCarpetBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*    */ 
/*    */ public class SimpleBlockFeature extends Feature<SimpleBlockConfiguration> {
/*    */   public SimpleBlockFeature(Codec<SimpleBlockConfiguration> codec) {
/* 14 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
/* 19 */     SimpleBlockConfiguration config = context.config();
/* 20 */     WorldGenLevel level = context.level();
/* 21 */     BlockPos origin = context.origin();
/* 22 */     BlockState stateToPlace = config.toPlace().getOptionalState(level, context.random(), origin);
/* 23 */     if (stateToPlace == null)
/* 24 */       return false; 
/* 28 */     if (stateToPlace.canSurvive((LevelReader)level, origin)) {
/* 29 */       if (stateToPlace.getBlock() instanceof DoublePlantBlock) {
/* 30 */         if (level.isEmptyBlock(origin.above())) {
/* 31 */           DoublePlantBlock.placeAt((LevelAccessor)level, stateToPlace, origin, 2);
/*    */         } else {
/* 33 */           return false;
/*    */         } 
/* 35 */       } else if (stateToPlace.getBlock() instanceof MossyCarpetBlock) {
/* 36 */         MossyCarpetBlock.placeAt((LevelAccessor)level, origin, level.getRandom(), 2);
/*    */       } else {
/* 38 */         level.setBlock(origin, stateToPlace, 2);
/*    */       } 
/* 40 */       if (config.scheduleTick())
/* 41 */         level.scheduleTick(origin, level.getBlockState(origin).getBlock(), 1); 
/* 43 */       return true;
/*    */     } 
/* 45 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\SimpleBlockFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */