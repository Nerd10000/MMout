/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface FloatInterpolator {
/*     */   static FloatInterpolator constant(float value) {
/* 967 */     return progress -> value;
/*     */   }
/*     */   
/*     */   float get(float paramFloat);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$FloatInterpolator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */