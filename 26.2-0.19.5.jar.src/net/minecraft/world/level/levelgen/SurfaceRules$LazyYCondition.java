/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ abstract class LazyYCondition extends SurfaceRules.LazyCondition {
/*     */   protected LazyYCondition(SurfaceRules.Context context) {
/* 327 */     super(context);
/*     */   }
/*     */   
/*     */   protected long getContextLastUpdate() {
/* 332 */     return this.context.lastUpdateY;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$LazyYCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */