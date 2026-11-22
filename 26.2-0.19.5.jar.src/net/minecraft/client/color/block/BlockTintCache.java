/*     */ package net.minecraft.client.color.block;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class BlockTintCache {
/*     */   private static final int MAX_CACHE_ENTRIES = 256;
/*     */   
/*     */   private static class CacheData {
/*  18 */     private final Int2ObjectArrayMap<int[]> cache = new Int2ObjectArrayMap(16);
/*     */     
/*  19 */     private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
/*     */     
/*  20 */     private static final int BLOCKS_PER_LAYER = Mth.square(16);
/*     */     
/*     */     private volatile boolean invalidated;
/*     */     
/*     */     public int[] getLayer(int y) {
/*  25 */       this.lock.readLock().lock();
/*     */       try {
/*  27 */         int[] existing = (int[])this.cache.get(y);
/*  28 */         if (existing != null)
/*  29 */           return existing; 
/*     */       } finally {
/*  32 */         this.lock.readLock().unlock();
/*     */       } 
/*  35 */       this.lock.writeLock().lock();
/*     */       try {
/*  38 */         return (int[])this.cache.computeIfAbsent(y, n -> allocateLayer());
/*     */       } finally {
/*  40 */         this.lock.writeLock().unlock();
/*     */       } 
/*     */     }
/*     */     
/*     */     private int[] allocateLayer() {
/*  45 */       int[] newCache = new int[BLOCKS_PER_LAYER];
/*  46 */       Arrays.fill(newCache, -1);
/*  47 */       return newCache;
/*     */     }
/*     */     
/*     */     public boolean isInvalidated() {
/*  51 */       return this.invalidated;
/*     */     }
/*     */     
/*     */     public void invalidate() {
/*  55 */       this.invalidated = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LatestCacheInfo {
/*  60 */     private int x = Integer.MIN_VALUE;
/*     */     
/*  61 */     private int z = Integer.MIN_VALUE;
/*     */     
/*     */     private BlockTintCache.CacheData cache;
/*     */   }
/*     */   
/*  68 */   private final ThreadLocal<LatestCacheInfo> latestChunkOnThread = ThreadLocal.withInitial(LatestCacheInfo::new);
/*     */   
/*  72 */   private final Long2ObjectLinkedOpenHashMap<CacheData> cache = new Long2ObjectLinkedOpenHashMap(256, 0.25F);
/*     */   
/*  73 */   private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
/*     */   
/*     */   private final ToIntFunction<BlockPos> source;
/*     */   
/*     */   public BlockTintCache(ToIntFunction<BlockPos> source) {
/*  77 */     this.source = source;
/*     */   }
/*     */   
/*     */   public int getColor(BlockPos pos) {
/*  81 */     int chunkX = SectionPos.blockToSectionCoord(pos.getX());
/*  82 */     int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
/*  84 */     LatestCacheInfo chunkInfo = this.latestChunkOnThread.get();
/*  85 */     if (chunkInfo.x != chunkX || chunkInfo.z != chunkZ || chunkInfo.cache == null || chunkInfo.cache.isInvalidated()) {
/*  86 */       chunkInfo.x = chunkX;
/*  87 */       chunkInfo.z = chunkZ;
/*  88 */       chunkInfo.cache = findOrCreateChunkCache(chunkX, chunkZ);
/*     */     } 
/*  90 */     int[] layer = chunkInfo.cache.getLayer(pos.getY());
/*  92 */     int x = pos.getX() & 0xF;
/*  93 */     int z = pos.getZ() & 0xF;
/*  94 */     int index = z << 4 | x;
/*  95 */     int cached = layer[index];
/*  96 */     if (cached != -1)
/*  97 */       return cached; 
/*  99 */     int calculated = this.source.applyAsInt(pos);
/* 100 */     layer[index] = calculated;
/* 101 */     return calculated;
/*     */   }
/*     */   
/*     */   public void invalidateForChunk(int chunkX, int chunkZ) {
/*     */     try {
/* 106 */       this.lock.writeLock().lock();
/* 108 */       for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 109 */         for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 110 */           long key = ChunkPos.pack(chunkX + offsetX, chunkZ + offsetZ);
/* 111 */           CacheData removed = (CacheData)this.cache.remove(key);
/* 112 */           if (removed != null)
/* 113 */             removed.invalidate(); 
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 118 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void invalidateAll() {
/*     */     try {
/* 124 */       this.lock.writeLock().lock();
/* 125 */       this.cache.values().forEach(CacheData::invalidate);
/* 126 */       this.cache.clear();
/*     */     } finally {
/* 128 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private CacheData findOrCreateChunkCache(int x, int z) {
/* 133 */     long key = ChunkPos.pack(x, z);
/* 134 */     this.lock.readLock().lock();
/*     */     try {
/* 136 */       CacheData existing = (CacheData)this.cache.get(key);
/* 137 */       if (existing != null)
/* 138 */         return existing; 
/*     */     } finally {
/* 141 */       this.lock.readLock().unlock();
/*     */     } 
/* 144 */     this.lock.writeLock().lock();
/*     */     try {
/* 147 */       CacheData existingNow = (CacheData)this.cache.get(key);
/* 148 */       if (existingNow != null)
/* 149 */         return existingNow; 
/* 151 */       CacheData newCache = new CacheData();
/* 153 */       if (this.cache.size() >= 256) {
/* 154 */         CacheData cacheData = (CacheData)this.cache.removeFirst();
/* 155 */         if (cacheData != null)
/* 156 */           cacheData.invalidate(); 
/*     */       } 
/* 159 */       this.cache.put(key, newCache);
/* 160 */       return newCache;
/*     */     } finally {
/* 163 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\block\BlockTintCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */