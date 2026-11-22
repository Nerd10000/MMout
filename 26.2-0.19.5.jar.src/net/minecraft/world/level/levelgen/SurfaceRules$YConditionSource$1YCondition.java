/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ class YCondition extends SurfaceRules.LazyYCondition {
/*     */   private YCondition() {
/* 610 */     super(paramContext);
/*     */   }
/*     */   
/*     */   protected boolean compute() {
/* 615 */     return (this.context.blockY + (SurfaceRules.YConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= SurfaceRules.YConditionSource.this.anchor.resolveY(this.context.context) + this.context.surfaceDepth * SurfaceRules.YConditionSource.this.surfaceDepthMultiplier);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$YConditionSource$1YCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */