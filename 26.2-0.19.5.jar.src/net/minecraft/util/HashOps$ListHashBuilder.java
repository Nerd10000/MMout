/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.mojang.serialization.DataResult;
/*     */ 
/*     */ class ListHashBuilder extends AbstractListBuilder<HashCode, Hasher> {
/*     */   public ListHashBuilder() {
/* 264 */     super(paramHashOps);
/*     */   }
/*     */   
/*     */   protected Hasher initBuilder() {
/* 269 */     return HashOps.this.hashFunction.newHasher().putByte((byte)4);
/*     */   }
/*     */   
/*     */   protected Hasher append(Hasher hasher, HashCode value) {
/* 274 */     return hasher.putBytes(value.asBytes());
/*     */   }
/*     */   
/*     */   protected DataResult<HashCode> build(Hasher hasher, HashCode prefix) {
/* 279 */     assert prefix.equals(HashOps.this.empty);
/* 280 */     hasher.putByte((byte)5);
/* 281 */     return DataResult.success(hasher.hash());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\HashOps$ListHashBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */