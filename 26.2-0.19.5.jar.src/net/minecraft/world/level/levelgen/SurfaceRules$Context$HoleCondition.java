/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ final class HoleCondition extends SurfaceRules.LazyXZCondition {
/*     */   private HoleCondition(SurfaceRules.Context context) {
/* 218 */     super(context);
/*     */   }
/*     */   
/*     */   protected boolean compute() {
/* 223 */     return (this.context.surfaceDepth <= 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Context$HoleCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */