/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ 
/*     */ class FixedPlayerDistanceChunkTracker extends ChunkTracker {
/* 204 */   protected final Long2ByteMap chunks = (Long2ByteMap)new Long2ByteOpenHashMap();
/*     */   
/*     */   protected final int maxDistance;
/*     */   
/*     */   protected FixedPlayerDistanceChunkTracker(int maxDistance) {
/* 208 */     super(maxDistance + 2, 16, 256);
/* 209 */     this.maxDistance = maxDistance;
/* 210 */     this.chunks.defaultReturnValue((byte)(maxDistance + 2));
/*     */   }
/*     */   
/*     */   protected int getLevel(long node) {
/* 215 */     return this.chunks.get(node);
/*     */   }
/*     */   
/*     */   protected void setLevel(long node, int level) {
/*     */     byte oldLevel;
/* 221 */     if (level > this.maxDistance) {
/* 222 */       oldLevel = this.chunks.remove(node);
/*     */     } else {
/* 224 */       oldLevel = this.chunks.put(node, (byte)level);
/*     */     } 
/* 226 */     onLevelChange(node, oldLevel, level);
/*     */   }
/*     */   
/*     */   protected void onLevelChange(long node, int oldLevel, int level) {}
/*     */   
/*     */   protected int getLevelFromSource(long to) {
/* 234 */     return havePlayer(to) ? 0 : Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   private boolean havePlayer(long chunkPos) {
/* 238 */     ObjectSet<ServerPlayer> players = (ObjectSet<ServerPlayer>)DistanceManager.this.playersPerChunk.get(chunkPos);
/* 239 */     return (players != null && !players.isEmpty());
/*     */   }
/*     */   
/*     */   public void runAllUpdates() {
/* 243 */     runUpdates(2147483647);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\DistanceManager$FixedPlayerDistanceChunkTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */