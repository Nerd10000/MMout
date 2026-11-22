/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ class ContextRetrievalCodec extends MapCodec<E> {
/*     */   public <T> RecordBuilder<T> encode(E input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 513 */     return prefix;
/*     */   }
/*     */   
/*     */   public <T> DataResult<E> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 518 */     return getter.apply(ops);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 523 */     return "ContextRetrievalCodec[" + String.valueOf(getter) + "]";
/*     */   }
/*     */   
/*     */   public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 528 */     return Stream.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExtraCodecs$1ContextRetrievalCodec.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */