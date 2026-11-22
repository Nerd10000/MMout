/*    */ package net.minecraft.client.color.block;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
/*    */ import java.util.Arrays;
/*    */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ class CacheData {
/* 18 */   private final Int2ObjectArrayMap<int[]> cache = new Int2ObjectArrayMap(16);
/*    */   
/* 19 */   private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
/*    */   
/* 20 */   private static final int BLOCKS_PER_LAYER = Mth.square(16);
/*    */   
/*    */   private volatile boolean invalidated;
/*    */   
/*    */   public int[] getLayer(int y) {
/* 25 */     this.lock.readLock().lock();
/*    */     try {
/* 27 */       int[] existing = (int[])this.cache.get(y);
/* 28 */       if (existing != null)
/* 29 */         return existing; 
/*    */     } finally {
/* 32 */       this.lock.readLock().unlock();
/*    */     } 
/* 35 */     this.lock.writeLock().lock();
/*    */     try {
/* 38 */       return (int[])this.cache.computeIfAbsent(y, n -> allocateLayer());
/*    */     } finally {
/* 40 */       this.lock.writeLock().unlock();
/*    */     } 
/*    */   }
/*    */   
/*    */   private int[] allocateLayer() {
/* 45 */     int[] newCache = new int[BLOCKS_PER_LAYER];
/* 46 */     Arrays.fill(newCache, -1);
/* 47 */     return newCache;
/*    */   }
/*    */   
/*    */   public boolean isInvalidated() {
/* 51 */     return this.invalidated;
/*    */   }
/*    */   
/*    */   public void invalidate() {
/* 55 */     this.invalidated = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\block\BlockTintCache$CacheData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */