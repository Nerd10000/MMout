/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class VinesFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public VinesFeature(Codec<NoneFeatureConfiguration> codec) {
/* 14 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 26 */     WorldGenLevel level = context.level();
/* 27 */     BlockPos origin = context.origin();
/* 28 */     context.config();
/* 29 */     if (!level.isEmptyBlock(origin))
/* 30 */       return false; 
/* 33 */     for (Direction direction : Direction.values()) {
/* 34 */       if (direction != Direction.DOWN)
/* 38 */         if (VineBlock.isAcceptableNeighbour((BlockGetter)level, origin.relative(direction), direction)) {
/* 39 */           level.setBlock(origin, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)VineBlock.getPropertyForFace(direction), Boolean.valueOf(true)), 2);
/* 40 */           return true;
/*    */         }  
/*    */     } 
/* 43 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\VinesFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */