/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public class FeaturePlaceContext<FC extends FeatureConfiguration> {
/*    */   private final Optional<ConfiguredFeature<?, ?>> topFeature;
/*    */   
/*    */   private final WorldGenLevel level;
/*    */   
/*    */   private final ChunkGenerator chunkGenerator;
/*    */   
/*    */   private final RandomSource random;
/*    */   
/*    */   private final BlockPos origin;
/*    */   
/*    */   private final FC config;
/*    */   
/*    */   public FeaturePlaceContext(Optional<ConfiguredFeature<?, ?>> topFeature, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, FC config) {
/* 21 */     this.topFeature = topFeature;
/* 22 */     this.level = level;
/* 23 */     this.chunkGenerator = chunkGenerator;
/* 24 */     this.random = random;
/* 25 */     this.origin = origin;
/* 26 */     this.config = config;
/*    */   }
/*    */   
/*    */   public Optional<ConfiguredFeature<?, ?>> topFeature() {
/* 30 */     return this.topFeature;
/*    */   }
/*    */   
/*    */   public WorldGenLevel level() {
/* 34 */     return this.level;
/*    */   }
/*    */   
/*    */   public ChunkGenerator chunkGenerator() {
/* 38 */     return this.chunkGenerator;
/*    */   }
/*    */   
/*    */   public RandomSource random() {
/* 42 */     return this.random;
/*    */   }
/*    */   
/*    */   public BlockPos origin() {
/* 46 */     return this.origin;
/*    */   }
/*    */   
/*    */   public FC config() {
/* 50 */     return this.config;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\FeaturePlaceContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */