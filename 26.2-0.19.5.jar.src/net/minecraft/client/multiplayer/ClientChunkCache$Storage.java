/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ 
/*     */ final class Storage {
/*     */   private static final int UPDATE_TRACKING_BUFFERS = 2;
/*     */   
/*     */   private final AtomicReferenceArray<LevelChunk> chunks;
/*     */   
/* 210 */   private final LongOpenHashSet[] addedEmptySections = new LongOpenHashSet[2];
/*     */   
/* 211 */   private final LongOpenHashSet[] removedEmptySections = new LongOpenHashSet[2];
/*     */   
/* 212 */   private final LongOpenHashSet[] addedLoadedChunks = new LongOpenHashSet[2];
/*     */   
/* 213 */   private final LongOpenHashSet[] removedLoadedChunks = new LongOpenHashSet[2];
/*     */   
/*     */   private int updatingSetsIndex;
/*     */   
/*     */   private final int chunkRadius;
/*     */   
/*     */   private final int viewRange;
/*     */   
/*     */   private volatile int viewCenterX;
/*     */   
/*     */   private volatile int viewCenterZ;
/*     */   
/*     */   private int chunkCount;
/*     */   
/*     */   private Storage(int chunkRadius) {
/* 222 */     this.chunkRadius = chunkRadius;
/* 223 */     this.viewRange = chunkRadius * 2 + 1;
/* 224 */     this.chunks = new AtomicReferenceArray<>(this.viewRange * this.viewRange);
/* 225 */     for (int i = 0; i < 2; i++) {
/* 226 */       this.addedEmptySections[i] = new LongOpenHashSet();
/* 227 */       this.removedEmptySections[i] = new LongOpenHashSet();
/* 228 */       this.addedLoadedChunks[i] = new LongOpenHashSet();
/* 229 */       this.removedLoadedChunks[i] = new LongOpenHashSet();
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getIndex(int chunkX, int chunkZ) {
/* 234 */     return Math.floorMod(chunkZ, this.viewRange) * this.viewRange + Math.floorMod(chunkX, this.viewRange);
/*     */   }
/*     */   
/*     */   private void replace(int index, LevelChunk newChunk) {
/* 238 */     LevelChunk removedChunk = this.chunks.getAndSet(index, newChunk);
/* 239 */     if (removedChunk != null) {
/* 240 */       this.chunkCount--;
/* 241 */       onChunkRemoved(removedChunk);
/* 242 */       ClientChunkCache.this.level.unload(removedChunk);
/*     */     } 
/* 245 */     if (newChunk != null) {
/* 246 */       this.chunkCount++;
/* 247 */       onChunkAdded(newChunk);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drop(int index, LevelChunk oldChunk) {
/* 252 */     if (this.chunks.compareAndSet(index, oldChunk, null)) {
/* 253 */       this.chunkCount--;
/* 254 */       onChunkRemoved(oldChunk);
/*     */     } 
/* 256 */     ClientChunkCache.this.level.unload(oldChunk);
/*     */   }
/*     */   
/*     */   public void onSectionEmptinessChanged(int sectionX, int sectionY, int sectionZ, boolean empty) {
/* 260 */     if (!inRange(sectionX, sectionZ))
/*     */       return; 
/* 263 */     long sectionNode = SectionPos.asLong(sectionX, sectionY, sectionZ);
/* 264 */     if (empty) {
/* 265 */       this.addedEmptySections[this.updatingSetsIndex].add(sectionNode);
/*     */     } else {
/* 267 */       this.removedEmptySections[this.updatingSetsIndex].add(sectionNode);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onChunkRemoved(LevelChunk chunk) {
/* 272 */     ChunkPos chunkPos = chunk.getPos();
/* 273 */     this.removedLoadedChunks[this.updatingSetsIndex].add(chunkPos.pack());
/* 274 */     LevelChunkSection[] sections = chunk.getSections();
/* 275 */     for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++)
/* 276 */       this.removedEmptySections[this.updatingSetsIndex].add(SectionPos.asLong(chunkPos.x(), chunk.getSectionYFromSectionIndex(sectionIndex), chunkPos.z())); 
/*     */   }
/*     */   
/*     */   private void onChunkAdded(LevelChunk chunk) {
/* 281 */     ChunkPos chunkPos = chunk.getPos();
/* 282 */     this.addedLoadedChunks[this.updatingSetsIndex].add(chunkPos.pack());
/* 283 */     LevelChunkSection[] sections = chunk.getSections();
/* 284 */     for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
/* 285 */       LevelChunkSection section = sections[sectionIndex];
/* 286 */       if (section.hasOnlyAir())
/* 287 */         this.addedEmptySections[this.updatingSetsIndex].add(SectionPos.asLong(chunkPos.x(), chunk.getSectionYFromSectionIndex(sectionIndex), chunkPos.z())); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void refreshEmptySections(LevelChunk chunk) {
/* 293 */     ChunkPos chunkPos = chunk.getPos();
/* 294 */     LevelChunkSection[] sections = chunk.getSections();
/* 295 */     for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
/* 296 */       LevelChunkSection section = sections[sectionIndex];
/* 297 */       long sectionNode = SectionPos.asLong(chunkPos.x(), chunk.getSectionYFromSectionIndex(sectionIndex), chunkPos.z());
/* 298 */       if (section.hasOnlyAir()) {
/* 299 */         this.addedEmptySections[this.updatingSetsIndex].add(sectionNode);
/*     */       } else {
/* 301 */         this.removedEmptySections[this.updatingSetsIndex].add(sectionNode);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean inRange(int chunkX, int chunkZ) {
/* 307 */     return (Math.abs(chunkX - this.viewCenterX) <= this.chunkRadius && Math.abs(chunkZ - this.viewCenterZ) <= this.chunkRadius);
/*     */   }
/*     */   
/*     */   public LevelChunk getChunk(int index) {
/* 311 */     return this.chunks.get(index);
/*     */   }
/*     */   
/*     */   private void dumpChunks(String file) {
/*     */     try {
/* 316 */       FileOutputStream stream = new FileOutputStream(file);
/*     */       try {
/* 317 */         int chunkRadius = ClientChunkCache.this.storage.chunkRadius;
/* 318 */         for (int z = this.viewCenterZ - chunkRadius; z <= this.viewCenterZ + chunkRadius; z++) {
/* 319 */           for (int x = this.viewCenterX - chunkRadius; x <= this.viewCenterX + chunkRadius; x++) {
/* 320 */             LevelChunk chunk = ClientChunkCache.this.storage.chunks.get(ClientChunkCache.this.storage.getIndex(x, z));
/* 321 */             if (chunk != null) {
/* 322 */               ChunkPos pos = chunk.getPos();
/* 323 */               stream.write(("" + pos.x() + "\t" + pos.x() + "\t" + pos.z() + "\n").getBytes(StandardCharsets.UTF_8));
/*     */             } 
/*     */           } 
/*     */         } 
/* 327 */         stream.close();
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           stream.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/* 327 */     } catch (IOException e) {
/* 328 */       ClientChunkCache.LOGGER.error("Failed to dump chunks to file {}", file, e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientChunkCache$Storage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */