/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Map;
/*     */ import java.util.function.IntFunction;
/*     */ 
/*     */ class null implements StreamCodec<B, M> {
/*     */   public void encode(B output, M map) {
/* 489 */     ByteBufCodecs.writeCount((ByteBuf)output, map.size(), maxSize);
/* 490 */     map.forEach((k, v) -> {
/*     */           keyCodec.encode(output, k);
/*     */           valueCodec.encode(output, v);
/*     */         });
/*     */   }
/*     */   
/*     */   public M decode(B input) {
/* 498 */     int count = ByteBufCodecs.readCount((ByteBuf)input, maxSize);
/* 499 */     Map<K, V> map = constructor.apply(Math.min(count, 65536));
/* 500 */     for (int i = 0; i < count; i++) {
/* 501 */       K key = keyCodec.decode(input);
/* 502 */       V value = valueCodec.decode(input);
/* 503 */       map.put(key, value);
/*     */     } 
/* 505 */     return (M)map;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$25.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */