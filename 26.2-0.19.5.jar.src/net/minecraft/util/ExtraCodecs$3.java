/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ class null extends MapCodec<E> {
/*     */   public <T> RecordBuilder<T> encode(E input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 318 */     if (ops.compressMaps())
/* 319 */       return compressed.encode(input, ops, prefix); 
/* 321 */     return normal.encode(input, ops, prefix);
/*     */   }
/*     */   
/*     */   public <T> DataResult<E> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 326 */     if (ops.compressMaps())
/* 327 */       return compressed.decode(ops, input); 
/* 329 */     return normal.decode(ops, input);
/*     */   }
/*     */   
/*     */   public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 334 */     return compressed.keys(ops);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 339 */     return String.valueOf(normal) + " orCompressed " + String.valueOf(normal);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExtraCodecs$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */