/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.levelgen.synth.SimplexNoise;
/*     */ 
/*     */ public final class EndIslandDensityFunction implements DensityFunction.SimpleFunction {
/* 540 */   public static final KeyDispatchDataCodec<EndIslandDensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(new EndIslandDensityFunction(0L)));
/*     */   
/*     */   private static final float ISLAND_THRESHOLD = -0.9F;
/*     */   
/*     */   private final SimplexNoise islandNoise;
/*     */   
/*     */   public EndIslandDensityFunction(long seed) {
/* 546 */     RandomSource islandRandom = new LegacyRandomSource(seed);
/* 548 */     islandRandom.consumeCount(17292);
/* 549 */     this.islandNoise = new SimplexNoise(islandRandom);
/*     */   }
/*     */   
/*     */   private static float getHeightValue(SimplexNoise islandNoise, int sectionX, int sectionZ) {
/* 553 */     int chunkX = sectionX / 2;
/* 554 */     int chunkZ = sectionZ / 2;
/* 555 */     int subSectionX = sectionX % 2;
/* 556 */     int subSectionZ = sectionZ % 2;
/* 559 */     float doffs = 100.0F - Mth.sqrt((sectionX * sectionX + sectionZ * sectionZ)) * 8.0F;
/* 560 */     doffs = Mth.clamp(doffs, -100.0F, 80.0F);
/* 563 */     for (int xo = -12; xo <= 12; xo++) {
/* 564 */       for (int zo = -12; zo <= 12; zo++) {
/* 565 */         long totalChunkX = (chunkX + xo);
/* 566 */         long totalChunkZ = (chunkZ + zo);
/* 567 */         if (totalChunkX * totalChunkX + totalChunkZ * totalChunkZ > 4096L && islandNoise.getValue(totalChunkX, totalChunkZ) < -0.8999999761581421D) {
/* 568 */           float islandSize = (Mth.abs((float)totalChunkX) * 3439.0F + Mth.abs((float)totalChunkZ) * 147.0F) % 13.0F + 9.0F;
/* 569 */           float xd = (subSectionX - xo * 2);
/* 570 */           float zd = (subSectionZ - zo * 2);
/* 571 */           float newDoffs = 100.0F - Mth.sqrt(xd * xd + zd * zd) * islandSize;
/* 572 */           newDoffs = Mth.clamp(newDoffs, -100.0F, 80.0F);
/* 573 */           doffs = Math.max(doffs, newDoffs);
/*     */         } 
/*     */       } 
/*     */     } 
/* 578 */     return doffs;
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 583 */     return (getHeightValue(this.islandNoise, context.blockX() / 8, context.blockZ() / 8) - 8.0D) / 128.0D;
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 588 */     return -0.84375D;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 593 */     return 0.5625D;
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 598 */     return (KeyDispatchDataCodec)CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$EndIslandDensityFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */