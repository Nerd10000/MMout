/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.world.level.TicketStorage;
/*    */ 
/*    */ public class LoadingChunkTracker extends ChunkTracker {
/*  7 */   private static final int MAX_LEVEL = ChunkLevel.MAX_LEVEL + 1;
/*    */   
/*    */   private final DistanceManager distanceManager;
/*    */   
/*    */   private final TicketStorage ticketStorage;
/*    */   
/*    */   public LoadingChunkTracker(DistanceManager distanceManager, TicketStorage ticketStorage) {
/* 14 */     super(MAX_LEVEL + 1, 16, 256);
/* 15 */     this.distanceManager = distanceManager;
/* 16 */     this.ticketStorage = ticketStorage;
/* 17 */     ticketStorage.setLoadingChunkUpdatedListener(this::update);
/*    */   }
/*    */   
/*    */   protected int getLevelFromSource(long to) {
/* 22 */     return this.ticketStorage.getTicketLevelAt(to, false);
/*    */   }
/*    */   
/*    */   protected int getLevel(long node) {
/* 27 */     if (!this.distanceManager.isChunkToRemove(node)) {
/* 28 */       ChunkHolder chunk = this.distanceManager.getChunk(node);
/* 29 */       if (chunk != null)
/* 30 */         return chunk.getTicketLevel(); 
/*    */     } 
/* 33 */     return MAX_LEVEL;
/*    */   }
/*    */   
/*    */   protected void setLevel(long node, int level) {
/* 38 */     ChunkHolder chunk = this.distanceManager.getChunk(node);
/* 39 */     int oldLevel = (chunk == null) ? MAX_LEVEL : chunk.getTicketLevel();
/* 40 */     if (oldLevel == level)
/*    */       return; 
/* 43 */     chunk = this.distanceManager.updateChunkScheduling(node, level, chunk, oldLevel);
/* 44 */     if (chunk != null)
/* 45 */       this.distanceManager.chunksToUpdateFutures.add(chunk); 
/*    */   }
/*    */   
/*    */   public int runDistanceUpdates(int count) {
/* 50 */     return runUpdates(count);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\LoadingChunkTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */