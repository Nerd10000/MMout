/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ abstract class LazyCondition implements SurfaceRules.Condition {
/*     */   protected final SurfaceRules.Context context;
/*     */   
/*     */   private long lastUpdate;
/*     */   
/*     */   private Boolean result;
/*     */   
/*     */   protected LazyCondition(SurfaceRules.Context context) {
/* 288 */     this.context = context;
/* 289 */     this.lastUpdate = getContextLastUpdate() - 1L;
/*     */   }
/*     */   
/*     */   public boolean test() {
/* 294 */     long lastContextUpdate = getContextLastUpdate();
/* 296 */     if (lastContextUpdate == this.lastUpdate) {
/* 297 */       if (this.result == null)
/* 298 */         throw new IllegalStateException("Update triggered but the result is null"); 
/* 300 */       return this.result.booleanValue();
/*     */     } 
/* 302 */     this.lastUpdate = lastContextUpdate;
/* 304 */     this.result = Boolean.valueOf(compute());
/* 306 */     return this.result.booleanValue();
/*     */   }
/*     */   
/*     */   protected abstract long getContextLastUpdate();
/*     */   
/*     */   protected abstract boolean compute();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$LazyCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */