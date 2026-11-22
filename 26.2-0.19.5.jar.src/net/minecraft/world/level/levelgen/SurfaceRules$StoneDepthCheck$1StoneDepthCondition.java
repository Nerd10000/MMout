/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ class StoneDepthCondition extends SurfaceRules.LazyYCondition {
/*     */   private StoneDepthCondition() {
/* 547 */     super(paramContext);
/*     */   }
/*     */   
/*     */   protected boolean compute() {
/* 552 */     int stoneDepth = ceiling ? this.context.stoneDepthBelow : this.context.stoneDepthAbove;
/* 553 */     int surfaceDepth = SurfaceRules.StoneDepthCheck.this.addSurfaceDepth ? this.context.surfaceDepth : 0;
/* 554 */     int secondarySurfaceDepth = (SurfaceRules.StoneDepthCheck.this.secondaryDepthRange == 0) ? 0 : (int)Mth.map(this.context.getSurfaceSecondary(), -1.0D, 1.0D, 0.0D, SurfaceRules.StoneDepthCheck.this.secondaryDepthRange);
/* 556 */     return (stoneDepth <= 1 + SurfaceRules.StoneDepthCheck.this.offset + surfaceDepth + secondarySurfaceDepth);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$StoneDepthCheck$1StoneDepthCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */