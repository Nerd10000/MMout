/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import org.apache.commons.lang3.mutable.MutableFloat;
/*     */ 
/*     */ @FunctionalInterface
/*     */ interface FloatAction<T> {
/*     */   float apply(T paramT, float paramFloat);
/*     */   
/*     */   default Enchantment.GenericAction<T> asGeneric(MutableFloat v) {
/* 577 */     return effect -> v.setValue(apply((T)effect, v.floatValue()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\Enchantment$FloatAction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */