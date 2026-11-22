/*     */ package net.minecraft.data;
/*     */ 
/*     */ import com.google.common.hash.HashCode;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ class CacheUpdater implements CachedOutput {
/*     */   private final String provider;
/*     */   
/*     */   private final HashCache.ProviderCache oldCache;
/*     */   
/*     */   private final HashCache.ProviderCacheBuilder newCache;
/*     */   
/* 101 */   private final AtomicInteger writes = new AtomicInteger();
/*     */   
/*     */   private volatile boolean closed;
/*     */   
/*     */   private CacheUpdater(String provider, String newVersionId, HashCache.ProviderCache oldCache) {
/* 105 */     this.provider = provider;
/* 106 */     this.oldCache = oldCache;
/* 107 */     this.newCache = new HashCache.ProviderCacheBuilder(newVersionId);
/*     */   }
/*     */   
/*     */   private boolean shouldWrite(Path path, HashCode hash) {
/* 111 */     return (!Objects.equals(this.oldCache.get(path), hash) || !Files.exists(path, new java.nio.file.LinkOption[0]));
/*     */   }
/*     */   
/*     */   public void writeIfNeeded(Path path, byte[] input, HashCode hash) throws IOException {
/* 116 */     if (this.closed)
/* 117 */       throw new IllegalStateException("Cannot write to cache as it has already been closed"); 
/* 119 */     if (shouldWrite(path, hash)) {
/* 120 */       this.writes.incrementAndGet();
/* 121 */       Files.createDirectories(path.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/* 122 */       Files.write(path, input, new java.nio.file.OpenOption[0]);
/*     */     } 
/* 124 */     this.newCache.put(path, hash);
/*     */   }
/*     */   
/*     */   public HashCache.UpdateResult close() {
/* 128 */     this.closed = true;
/* 129 */     return new HashCache.UpdateResult(this.provider, this.newCache.build(), this.writes.get());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\HashCache$CacheUpdater.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */