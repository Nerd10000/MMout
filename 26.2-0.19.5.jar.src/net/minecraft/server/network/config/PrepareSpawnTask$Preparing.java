/*     */ package net.minecraft.server.network.config;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ChunkLoadCounter;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.server.level.progress.LevelLoadListener;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ final class Preparing implements PrepareSpawnTask.State {
/*     */   private final ServerLevel spawnLevel;
/*     */   
/*     */   private final CompletableFuture<Vec3> spawnPosition;
/*     */   
/*     */   private final Vec2 spawnAngle;
/*     */   
/*     */   private CompletableFuture<?> chunkLoadFuture;
/*     */   
/* 125 */   private final ChunkLoadCounter chunkLoadCounter = new ChunkLoadCounter();
/*     */   
/*     */   private Preparing(ServerLevel spawnLevel, CompletableFuture<Vec3> spawnPosition, Vec2 spawnAngle) {
/* 128 */     this.spawnLevel = spawnLevel;
/* 129 */     this.spawnPosition = spawnPosition;
/* 130 */     this.spawnAngle = spawnAngle;
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 134 */     this.spawnPosition.cancel(false);
/*     */   }
/*     */   
/*     */   public PrepareSpawnTask.Ready tick() {
/* 138 */     if (!this.spawnPosition.isDone())
/* 139 */       return null; 
/* 142 */     Vec3 spawnPosition = this.spawnPosition.join();
/* 144 */     if (this.chunkLoadFuture == null) {
/* 145 */       ChunkPos spawnChunk = ChunkPos.containing(BlockPos.containing((Position)spawnPosition));
/* 146 */       this.chunkLoadCounter.track(this.spawnLevel, () -> this.chunkLoadFuture = this.spawnLevel.getChunkSource().addTicketAndLoadWithRadius(TicketType.PLAYER_SPAWN, spawnChunk, 3));
/* 149 */       PrepareSpawnTask.this.loadListener.start(LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS, this.chunkLoadCounter.totalChunks());
/* 150 */       PrepareSpawnTask.this.loadListener.updateFocus(this.spawnLevel.dimension(), spawnChunk);
/*     */     } 
/* 153 */     PrepareSpawnTask.this.loadListener.update(LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS, this.chunkLoadCounter.readyChunks(), this.chunkLoadCounter.totalChunks());
/* 155 */     if (!this.chunkLoadFuture.isDone())
/* 156 */       return null; 
/* 159 */     PrepareSpawnTask.this.loadListener.finish(LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS);
/* 161 */     return new PrepareSpawnTask.Ready(PrepareSpawnTask.this, this.spawnLevel, spawnPosition, this.spawnAngle);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\config\PrepareSpawnTask$Preparing.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */