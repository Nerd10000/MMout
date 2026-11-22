/*     */ package net.minecraft.server;
/*     */ 
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.progress.ChunkLoadStatusView;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ class null implements ChunkLoadStatusView {
/*     */   private ChunkMap chunkMap;
/*     */   
/*     */   private int centerChunkX;
/*     */   
/*     */   private int centerChunkZ;
/*     */   
/*     */   public void moveTo(ResourceKey<Level> dimension, ChunkPos centerChunk) {
/* 387 */     ServerLevel level = MinecraftServer.this.getLevel(dimension);
/* 388 */     this.chunkMap = (level != null) ? (level.getChunkSource()).chunkMap : null;
/* 389 */     this.centerChunkX = centerChunk.x();
/* 390 */     this.centerChunkZ = centerChunk.z();
/*     */   }
/*     */   
/*     */   public ChunkStatus get(int x, int z) {
/* 395 */     if (this.chunkMap == null)
/* 396 */       return null; 
/* 398 */     return this.chunkMap.getLatestStatus(ChunkPos.pack(x + this.centerChunkX - radius, z + this.centerChunkZ - radius));
/*     */   }
/*     */   
/*     */   public int radius() {
/* 406 */     return radius;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\MinecraftServer$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */