/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.thread.BlockableEventLoop;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ final class MainThreadExecutor extends BlockableEventLoop<Runnable> {
/*     */   private MainThreadExecutor(Level level) {
/* 596 */     super("Chunk source main thread executor for " + String.valueOf(level.dimension().identifier()), false);
/*     */   }
/*     */   
/*     */   public Runnable wrapRunnable(Runnable runnable) {
/* 601 */     return runnable;
/*     */   }
/*     */   
/*     */   protected boolean shouldRun(Runnable task) {
/* 606 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean scheduleExecutables() {
/* 612 */     return true;
/*     */   }
/*     */   
/*     */   protected Thread getRunningThread() {
/* 617 */     return ServerChunkCache.this.mainThread;
/*     */   }
/*     */   
/*     */   protected void doRunTask(Runnable task) {
/* 622 */     Profiler.get().incrementCounter("runTask");
/* 623 */     super.doRunTask(task);
/*     */   }
/*     */   
/*     */   protected boolean pollTask() {
/* 628 */     if (ServerChunkCache.this.runDistanceManagerUpdates())
/* 629 */       return true; 
/* 631 */     ServerChunkCache.this.lightEngine.tryScheduleUpdate();
/* 632 */     return super.pollTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerChunkCache$MainThreadExecutor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */