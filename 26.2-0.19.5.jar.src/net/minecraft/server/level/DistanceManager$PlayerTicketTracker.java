/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ class PlayerTicketTracker extends DistanceManager.FixedPlayerDistanceChunkTracker {
/*     */   private int viewDistance;
/*     */   
/* 249 */   private final Long2IntMap queueLevels = Long2IntMaps.synchronize((Long2IntMap)new Long2IntOpenHashMap());
/*     */   
/* 250 */   private final LongSet toUpdate = (LongSet)new LongOpenHashSet();
/*     */   
/*     */   protected PlayerTicketTracker(int maxDistance) {
/* 253 */     super(maxDistance);
/* 254 */     this.viewDistance = 0;
/* 255 */     this.queueLevels.defaultReturnValue(maxDistance + 2);
/*     */   }
/*     */   
/*     */   protected void onLevelChange(long node, int oldLevel, int level) {
/* 260 */     this.toUpdate.add(node);
/*     */   }
/*     */   
/*     */   public void updateViewDistance(int viewDistance) {
/* 264 */     for (ObjectIterator<Long2ByteMap.Entry> objectIterator = this.chunks.long2ByteEntrySet().iterator(); objectIterator.hasNext(); ) {
/* 264 */       Long2ByteMap.Entry entry = objectIterator.next();
/* 265 */       byte level = entry.getByteValue();
/* 266 */       long key = entry.getLongKey();
/* 267 */       onLevelChange(key, level, haveTicketFor(level), (level <= viewDistance));
/*     */     } 
/* 269 */     this.viewDistance = viewDistance;
/*     */   }
/*     */   
/*     */   private void onLevelChange(long key, int level, boolean saw, boolean sees) {
/* 273 */     if (saw != sees) {
/* 274 */       Ticket ticket = new Ticket(TicketType.PLAYER_LOADING, DistanceManager.PLAYER_TICKET_LEVEL);
/* 275 */       if (sees) {
/* 276 */         DistanceManager.this.ticketDispatcher.submit(() -> DistanceManager.this.mainThreadExecutor.execute(()), key, () -> level);
/*     */       } else {
/* 285 */         DistanceManager.this.ticketDispatcher.release(key, () -> DistanceManager.this.mainThreadExecutor.execute(()), true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void runAllUpdates() {
/* 292 */     super.runAllUpdates();
/* 293 */     if (!this.toUpdate.isEmpty()) {
/* 294 */       LongIterator iterator = this.toUpdate.iterator();
/* 295 */       while (iterator.hasNext()) {
/* 296 */         long node = iterator.nextLong();
/* 297 */         int oldLevel = this.queueLevels.get(node);
/* 298 */         int level = getLevel(node);
/* 299 */         if (oldLevel != level) {
/* 300 */           DistanceManager.this.ticketDispatcher.onLevelChange(ChunkPos.unpack(node), () -> this.queueLevels.get(node), level, l -> {
/*     */                 if (l >= this.queueLevels.defaultReturnValue()) {
/*     */                   this.queueLevels.remove(node);
/*     */                 } else {
/*     */                   this.queueLevels.put(node, l);
/*     */                 } 
/*     */               });
/* 307 */           onLevelChange(node, level, haveTicketFor(oldLevel), haveTicketFor(level));
/*     */         } 
/*     */       } 
/* 310 */       this.toUpdate.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean haveTicketFor(int level) {
/* 315 */     return (level <= this.viewDistance);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\DistanceManager$PlayerTicketTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */