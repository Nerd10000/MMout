/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.codecs.BaseMapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ 
/*     */ public final class StrictUnboundedMapCodec<K, V> extends Record implements BaseMapCodec<K, V>, Codec<Map<K, V>> {
/*     */   private final Codec<K> keyCodec;
/*     */   
/*     */   private final Codec<V> elementCodec;
/*     */   
/*     */   public StrictUnboundedMapCodec(Codec<K> keyCodec, Codec<V> elementCodec) {
/* 387 */     this.keyCodec = keyCodec;
/* 387 */     this.elementCodec = elementCodec;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #387	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec<TK;TV;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #387	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec<TK;TV;>;
/*     */   }
/*     */   
/*     */   public Codec<K> keyCodec() {
/* 387 */     return this.keyCodec;
/*     */   }
/*     */   
/*     */   public Codec<V> elementCodec() {
/* 387 */     return this.elementCodec;
/*     */   }
/*     */   
/*     */   public <T> DataResult<Map<K, V>> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 390 */     ImmutableMap.Builder<K, V> read = ImmutableMap.builder();
/* 392 */     for (Pair<T, T> pair : (Iterable<Pair<T, T>>)input.entries().toList()) {
/* 393 */       DataResult<K> k = keyCodec().parse(ops, pair.getFirst());
/* 394 */       DataResult<V> v = elementCodec().parse(ops, pair.getSecond());
/* 395 */       DataResult<Pair<K, V>> entry = k.apply2stable(Pair::of, v);
/* 396 */       Optional<DataResult.Error<Pair<K, V>>> error = entry.error();
/* 397 */       if (error.isPresent()) {
/* 398 */         String errorMessage = ((DataResult.Error)error.get()).message();
/* 399 */         return DataResult.error(() -> k.result().isPresent() ? ("Map entry '" + String.valueOf(k.result().get()) + "' : " + errorMessage) : errorMessage);
/*     */       } 
/* 407 */       if (entry.result().isPresent()) {
/* 408 */         Pair<K, V> kvPair = entry.result().get();
/* 409 */         read.put(kvPair.getFirst(), kvPair.getSecond());
/*     */         continue;
/*     */       } 
/* 411 */       return DataResult.error(() -> "Empty or invalid map contents are not allowed");
/*     */     } 
/* 414 */     ImmutableMap immutableMap = read.build();
/* 415 */     return DataResult.success(immutableMap);
/*     */   }
/*     */   
/*     */   public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) {
/* 420 */     return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> decode(ops, map)).map(r -> Pair.of(r, input));
/*     */   }
/*     */   
/*     */   public <T> DataResult<T> encode(Map<K, V> input, DynamicOps<T> ops, T prefix) {
/* 425 */     return encode(input, ops, ops.mapBuilder()).build(prefix);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 430 */     return "StrictUnboundedMapCodec[" + String.valueOf(this.keyCodec) + " -> " + String.valueOf(this.elementCodec) + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExtraCodecs$StrictUnboundedMapCodec.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */