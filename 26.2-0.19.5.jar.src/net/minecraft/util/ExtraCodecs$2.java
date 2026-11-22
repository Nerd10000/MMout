/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ 
/*     */ class null implements Codec<E> {
/*     */   public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
/* 293 */     if (ops.compressMaps())
/* 294 */       return compressed.encode(input, ops, prefix); 
/* 296 */     return normal.encode(input, ops, prefix);
/*     */   }
/*     */   
/*     */   public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
/* 301 */     if (ops.compressMaps())
/* 302 */       return compressed.decode(ops, input); 
/* 304 */     return normal.decode(ops, input);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 309 */     return String.valueOf(normal) + " orCompressed " + String.valueOf(normal);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExtraCodecs$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */