/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ abstract class LazyXZCondition extends SurfaceRules.LazyCondition {
/*     */   protected LazyXZCondition(SurfaceRules.Context context) {
/* 316 */     super(context);
/*     */   }
/*     */   
/*     */   protected long getContextLastUpdate() {
/* 321 */     return this.context.lastUpdateXZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$LazyXZCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */