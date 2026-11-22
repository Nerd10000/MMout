/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.CompositeFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class SimpleRandomSelectorFeature extends Feature<CompositeFeatureConfiguration> {
/*    */   public SimpleRandomSelectorFeature(Codec<CompositeFeatureConfiguration> codec) {
/* 13 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<CompositeFeatureConfiguration> context) {
/* 18 */     RandomSource random = context.random();
/* 19 */     CompositeFeatureConfiguration config = context.config();
/* 20 */     WorldGenLevel level = context.level();
/* 21 */     BlockPos origin = context.origin();
/* 22 */     ChunkGenerator chunkGenerator = context.chunkGenerator();
/* 23 */     int index = random.nextInt(config.features().size());
/* 24 */     PlacedFeature feature = (PlacedFeature)config.features().get(index).value();
/* 25 */     return feature.place(level, chunkGenerator, random, origin);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\SimpleRandomSelectorFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */