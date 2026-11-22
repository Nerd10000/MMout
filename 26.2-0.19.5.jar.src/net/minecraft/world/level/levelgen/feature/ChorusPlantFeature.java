/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.ChorusFlowerBlock;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class ChorusPlantFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public ChorusPlantFeature(Codec<NoneFeatureConfiguration> codec) {
/* 14 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 19 */     WorldGenLevel level = context.level();
/* 20 */     BlockPos origin = context.origin();
/* 21 */     RandomSource random = context.random();
/* 22 */     if (level.isEmptyBlock(origin) && level.getBlockState(origin.below()).is(BlockTags.SUPPORTS_CHORUS_PLANT)) {
/* 23 */       ChorusFlowerBlock.generatePlant((LevelAccessor)level, origin, random, 8);
/* 24 */       return true;
/*    */     } 
/* 26 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\ChorusPlantFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */