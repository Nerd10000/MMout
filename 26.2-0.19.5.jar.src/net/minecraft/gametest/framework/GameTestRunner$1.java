/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongArraySet;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ class null implements GameTestListener {
/*     */   private void testCompleted(GameTestInfo testInfo) {
/* 177 */     if (currentBatchTracker.isDone()) {
/* 178 */       GameTestRunner.this.batchListeners.forEach(listener -> listener.testBatchFinished(currentBatch));
/* 179 */       LongArraySet longArraySet = new LongArraySet(GameTestRunner.this.level.getForceLoadedChunks());
/* 180 */       longArraySet.forEach(pos -> GameTestRunner.this.level.setChunkForced(ChunkPos.getX(pos), ChunkPos.getZ(pos), false));
/* 181 */       GameTestRunner.this.runBatch(batchIndex + 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void testStructureLoaded(GameTestInfo testInfo) {}
/*     */   
/*     */   public void testPassed(GameTestInfo testInfo, GameTestRunner runner) {
/* 191 */     testInfo.getTestInstanceBlockEntity().removeBarriers();
/* 192 */     testCompleted(testInfo);
/*     */   }
/*     */   
/*     */   public void testFailed(GameTestInfo testInfo, GameTestRunner runner) {
/* 197 */     if (GameTestRunner.this.haltOnError) {
/* 198 */       GameTestRunner.this.endCurrentEnvironment();
/* 199 */       LongArraySet longArraySet = new LongArraySet(GameTestRunner.this.level.getForceLoadedChunks());
/* 200 */       longArraySet.forEach(pos -> GameTestRunner.this.level.setChunkForced(ChunkPos.getX(pos), ChunkPos.getZ(pos), false));
/* 201 */       GameTestTicker.SINGLETON.clear();
/*     */     } else {
/* 203 */       testCompleted(testInfo);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void testAddedForRerun(GameTestInfo original, GameTestInfo copy, GameTestRunner runner) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestRunner$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */