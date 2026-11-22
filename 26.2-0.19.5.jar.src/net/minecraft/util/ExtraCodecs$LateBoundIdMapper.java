/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Collections;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class LateBoundIdMapper<I, V> {
/* 811 */   private final BiMap<I, V> idToValue = (BiMap<I, V>)HashBiMap.create();
/*     */   
/*     */   public Codec<V> codec(Codec<I> idCodec) {
/* 814 */     BiMap<V, I> valueToId = this.idToValue.inverse();
/* 815 */     Objects.requireNonNull(this.idToValue);
/* 815 */     Objects.requireNonNull(valueToId);
/* 815 */     return ExtraCodecs.idResolverCodec(idCodec, this.idToValue::get, valueToId::get);
/*     */   }
/*     */   
/*     */   public LateBoundIdMapper<I, V> put(I id, V value) {
/* 820 */     Objects.requireNonNull(value, () -> "Value for " + String.valueOf(id) + " is null");
/* 821 */     this.idToValue.put(id, value);
/* 822 */     return this;
/*     */   }
/*     */   
/*     */   public Set<V> values() {
/* 826 */     return Collections.unmodifiableSet(this.idToValue.values());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExtraCodecs$LateBoundIdMapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */