/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ class BiomeCondition extends SurfaceRules.LazyYCondition {
/*     */   private BiomeCondition() {
/* 676 */     super(paramContext);
/*     */   }
/*     */   
/*     */   protected boolean compute() {
/* 681 */     return SurfaceRules.BiomeConditionSource.this.biomes.contains(this.context.getBiome());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$BiomeConditionSource$1BiomeCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */