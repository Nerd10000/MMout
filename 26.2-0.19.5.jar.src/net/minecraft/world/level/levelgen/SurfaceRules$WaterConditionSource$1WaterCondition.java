/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ class WaterCondition extends SurfaceRules.LazyYCondition {
/*     */   private WaterCondition() {
/* 639 */     super(paramContext);
/*     */   }
/*     */   
/*     */   protected boolean compute() {
/* 644 */     return (this.context.waterHeight == Integer.MIN_VALUE || this.context.blockY + (SurfaceRules.WaterConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= this.context.waterHeight + SurfaceRules.WaterConditionSource.this.offset + this.context.surfaceDepth * SurfaceRules.WaterConditionSource.this.surfaceDepthMultiplier);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$WaterConditionSource$1WaterCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */