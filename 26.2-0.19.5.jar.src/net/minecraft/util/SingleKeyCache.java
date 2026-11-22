/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class SingleKeyCache<K, V> {
/*    */   private final Function<K, V> computeValue;
/*    */   
/* 13 */   private K cacheKey = null;
/*    */   
/*    */   private V cachedValue;
/*    */   
/*    */   public SingleKeyCache(Function<K, V> computeValue) {
/* 17 */     this.computeValue = computeValue;
/*    */   }
/*    */   
/*    */   public V getValue(K cacheKey) {
/* 21 */     if (this.cachedValue == null || !Objects.equals(this.cacheKey, cacheKey)) {
/* 22 */       this.cachedValue = this.computeValue.apply(cacheKey);
/* 23 */       this.cacheKey = cacheKey;
/*    */     } 
/* 25 */     return this.cachedValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\SingleKeyCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */