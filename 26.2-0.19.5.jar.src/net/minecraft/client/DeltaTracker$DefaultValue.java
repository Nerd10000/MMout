/*     */ package net.minecraft.client;
/*     */ 
/*     */ public class DefaultValue implements DeltaTracker {
/*     */   private final float value;
/*     */   
/*     */   private DefaultValue(float value) {
/* 107 */     this.value = value;
/*     */   }
/*     */   
/*     */   public float getGameTimeDeltaTicks() {
/* 112 */     return this.value;
/*     */   }
/*     */   
/*     */   public float getGameTimeDeltaPartialTick(boolean ignored) {
/* 117 */     return this.value;
/*     */   }
/*     */   
/*     */   public float getRealtimeDeltaTicks() {
/* 122 */     return this.value;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\DeltaTracker$DefaultValue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */