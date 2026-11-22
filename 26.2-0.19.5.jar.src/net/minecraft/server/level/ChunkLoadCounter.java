/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ 
/*    */ public class ChunkLoadCounter {
/* 11 */   private final List<ChunkHolder> pendingChunks = new ArrayList<>();
/*    */   
/*    */   private int totalChunks;
/*    */   
/*    */   public void track(ServerLevel level, Runnable scheduler) {
/* 15 */     ServerChunkCache chunkSource = level.getChunkSource();
/* 17 */     LongOpenHashSet longOpenHashSet = new LongOpenHashSet();
/* 18 */     chunkSource.runDistanceManagerUpdates();
/* 19 */     chunkSource.chunkMap.allChunksWithAtLeastStatus(ChunkStatus.FULL)
/* 20 */       .forEach(chunkHolder -> alreadyLoadedChunks.add(chunkHolder.getPos().pack()));
/* 22 */     scheduler.run();
/* 24 */     chunkSource.runDistanceManagerUpdates();
/* 25 */     chunkSource.chunkMap.allChunksWithAtLeastStatus(ChunkStatus.FULL).forEach(chunkHolder -> {
/*    */           if (!alreadyLoadedChunks.contains(chunkHolder.getPos().pack())) {
/*    */             this.pendingChunks.add(chunkHolder);
/*    */             this.totalChunks++;
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   public int readyChunks() {
/* 34 */     return this.totalChunks - pendingChunks();
/*    */   }
/*    */   
/*    */   public int pendingChunks() {
/* 38 */     this.pendingChunks.removeIf(chunkHolder -> (chunkHolder.getLatestStatus() == ChunkStatus.FULL));
/* 39 */     return this.pendingChunks.size();
/*    */   }
/*    */   
/*    */   public int totalChunks() {
/* 43 */     return this.totalChunks;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkLoadCounter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */