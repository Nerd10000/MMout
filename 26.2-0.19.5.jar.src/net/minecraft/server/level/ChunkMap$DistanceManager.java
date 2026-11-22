/*      */ package net.minecraft.server.level;
/*      */ 
/*      */ import java.util.concurrent.Executor;
/*      */ import net.minecraft.world.level.TicketStorage;
/*      */ 
/*      */ class DistanceManager extends DistanceManager {
/*      */   protected DistanceManager(TicketStorage ticketStorage, Executor executor, Executor mainThreadExecutor) {
/* 1344 */     super(ticketStorage, executor, mainThreadExecutor);
/*      */   }
/*      */   
/*      */   protected boolean isChunkToRemove(long node) {
/* 1349 */     return ChunkMap.this.toDrop.contains(node);
/*      */   }
/*      */   
/*      */   protected ChunkHolder getChunk(long node) {
/* 1354 */     return ChunkMap.this.getUpdatingChunkIfPresent(node);
/*      */   }
/*      */   
/*      */   protected ChunkHolder updateChunkScheduling(long node, int level, ChunkHolder chunk, int oldLevel) {
/* 1359 */     return ChunkMap.this.updateChunkScheduling(node, level, chunk, oldLevel);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkMap$DistanceManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */