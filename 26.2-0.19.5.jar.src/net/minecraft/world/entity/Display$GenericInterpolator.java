/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface GenericInterpolator<T> {
/*     */   static <T> GenericInterpolator<T> constant(T value) {
/* 925 */     return progress -> value;
/*     */   }
/*     */   
/*     */   T get(float paramFloat);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$GenericInterpolator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */