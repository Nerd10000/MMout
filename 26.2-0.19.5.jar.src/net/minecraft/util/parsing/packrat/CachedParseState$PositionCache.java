/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ class PositionCache {
/*     */   public static final int ENTRY_STRIDE = 2;
/*     */   
/*     */   private static final int NOT_FOUND = -1;
/*     */   
/* 124 */   private Object[] atomCache = new Object[16];
/*     */   
/*     */   private int nextKey;
/*     */   
/*     */   public int findKeyIndex(Atom<?> key) {
/* 128 */     for (int i = 0; i < this.nextKey; i += 2) {
/* 129 */       if (this.atomCache[i] == key)
/* 130 */         return i; 
/*     */     } 
/* 134 */     return -1;
/*     */   }
/*     */   
/*     */   public int allocateNewEntry(Atom<?> key) {
/* 139 */     int newKeyIndex = this.nextKey;
/* 140 */     this.nextKey += 2;
/* 142 */     int newValueIndex = newKeyIndex + 1;
/* 143 */     int currentSize = this.atomCache.length;
/* 144 */     if (newValueIndex >= currentSize) {
/* 145 */       int newSize = Util.growByHalf(currentSize, newValueIndex + 1);
/* 146 */       Object[] newCache = new Object[newSize];
/* 147 */       System.arraycopy(this.atomCache, 0, newCache, 0, currentSize);
/* 148 */       this.atomCache = newCache;
/*     */     } 
/* 151 */     this.atomCache[newKeyIndex] = key;
/* 153 */     return newKeyIndex;
/*     */   }
/*     */   
/*     */   public <T> CachedParseState.CacheEntry<T> getValue(int keyIndex) {
/* 158 */     return (CachedParseState.CacheEntry<T>)this.atomCache[keyIndex + 1];
/*     */   }
/*     */   
/*     */   public void setValue(int keyIndex, CachedParseState.CacheEntry<?> entry) {
/* 162 */     this.atomCache[keyIndex + 1] = entry;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\CachedParseState$PositionCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */