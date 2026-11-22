/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ final class MapHashBuilder extends RecordBuilder.AbstractUniversalBuilder<HashCode, List<Pair<HashCode, HashCode>>> {
/*     */   public MapHashBuilder() {
/* 236 */     super(paramHashOps);
/*     */   }
/*     */   
/*     */   protected List<Pair<HashCode, HashCode>> initBuilder() {
/* 241 */     return new ArrayList<>();
/*     */   }
/*     */   
/*     */   protected List<Pair<HashCode, HashCode>> append(HashCode key, HashCode value, List<Pair<HashCode, HashCode>> builder) {
/* 246 */     builder.add(Pair.of(key, value));
/* 247 */     return builder;
/*     */   }
/*     */   
/*     */   protected DataResult<HashCode> build(List<Pair<HashCode, HashCode>> builder, HashCode prefix) {
/* 252 */     assert HashOps.this.isEmpty(prefix);
/* 253 */     return DataResult.success(HashOps.hashMap(HashOps.this.hashFunction.newHasher(), builder.stream()).hash());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\HashOps$MapHashBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */