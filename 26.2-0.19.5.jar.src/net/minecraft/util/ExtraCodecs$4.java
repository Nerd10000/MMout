/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements Codec.ResultFunction<E> {
/*     */   public <T> DataResult<Pair<E, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<E, T>> a) {
/* 348 */     return a.result().map(r -> a.setLifecycle(decodeLifecycle.apply(r.getFirst()))).orElse(a);
/*     */   }
/*     */   
/*     */   public <T> DataResult<T> coApply(DynamicOps<T> ops, E input, DataResult<T> t) {
/* 353 */     return t.setLifecycle(encodeLifecycle.apply(input));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 358 */     return "WithLifecycle[" + String.valueOf(decodeLifecycle) + " " + String.valueOf(encodeLifecycle) + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExtraCodecs$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */