/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.WeightedRandomFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class WeightedRandomSelectorFeature extends Feature<WeightedRandomFeatureConfiguration> {
/*    */   public WeightedRandomSelectorFeature(Codec<WeightedRandomFeatureConfiguration> codec) {
/* 16 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<WeightedRandomFeatureConfiguration> context) {
/* 21 */     WeightedRandomFeatureConfiguration config = context.config();
/* 22 */     RandomSource random = context.random();
/* 23 */     WorldGenLevel level = context.level();
/* 24 */     ChunkGenerator chunkGenerator = context.chunkGenerator();
/* 25 */     BlockPos origin = context.origin();
/* 26 */     Optional<Holder<PlacedFeature>> featureToPlace = config.features().getRandom(random);
/* 27 */     return ((Boolean)featureToPlace.<Boolean>map(placedFeatureHolder -> Boolean.valueOf(((PlacedFeature)placedFeatureHolder.value()).place(level, chunkGenerator, random, origin))).orElse(Boolean.valueOf(false))).booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\WeightedRandomSelectorFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */