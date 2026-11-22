/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.io.IOException;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ public abstract class ChunkSource implements AutoCloseable, LightChunkGetter {
/*    */   public LevelChunk getChunk(int x, int z, boolean loadOrGenerate) {
/* 14 */     return (LevelChunk)getChunk(x, z, ChunkStatus.FULL, loadOrGenerate);
/*    */   }
/*    */   
/*    */   public LevelChunk getChunkNow(int x, int z) {
/* 18 */     return getChunk(x, z, false);
/*    */   }
/*    */   
/*    */   public LightChunk getChunkForLighting(int x, int z) {
/* 23 */     return getChunk(x, z, ChunkStatus.EMPTY, false);
/*    */   }
/*    */   
/*    */   public boolean hasChunk(int x, int z) {
/* 27 */     return (getChunk(x, z, ChunkStatus.FULL, false) != null);
/*    */   }
/*    */   
/*    */   public abstract ChunkAccess getChunk(int paramInt1, int paramInt2, ChunkStatus paramChunkStatus, boolean paramBoolean);
/*    */   
/*    */   public abstract void tick(BooleanSupplier paramBooleanSupplier, boolean paramBoolean);
/*    */   
/*    */   public void onSectionEmptinessChanged(int sectionX, int sectionY, int sectionZ, boolean empty) {}
/*    */   
/*    */   public abstract String gatherStats();
/*    */   
/*    */   public abstract int getLoadedChunksCount();
/*    */   
/*    */   public void close() throws IOException {}
/*    */   
/*    */   public abstract LevelLightEngine getLightEngine();
/*    */   
/*    */   public void setSpawnSettings(boolean spawnEnemies) {}
/*    */   
/*    */   public boolean updateChunkForced(ChunkPos pos, boolean forced) {
/* 52 */     return false;
/*    */   }
/*    */   
/*    */   public LongSet getForceLoadedChunks() {
/* 56 */     return LongSet.of();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\ChunkSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */