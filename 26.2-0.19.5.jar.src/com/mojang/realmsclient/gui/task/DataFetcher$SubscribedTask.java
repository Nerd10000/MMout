/*     */ package com.mojang.realmsclient.gui.task;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ class SubscribedTask<T> {
/*     */   private final DataFetcher.Task<T> task;
/*     */   
/*     */   private final Consumer<T> output;
/*     */   
/* 111 */   private long lastCheckTime = -1L;
/*     */   
/*     */   private SubscribedTask(DataFetcher paramDataFetcher, DataFetcher.Task<T> task, Consumer<T> output) {
/* 114 */     this.task = task;
/* 115 */     this.output = output;
/*     */   }
/*     */   
/*     */   private void update(long currentTime) {
/* 119 */     this.task.updateIfNeeded(currentTime);
/* 120 */     runCallbackIfNeeded();
/*     */   }
/*     */   
/*     */   private void runCallbackIfNeeded() {
/* 124 */     DataFetcher.SuccessfulComputationResult<T> lastResult = this.task.lastResult;
/* 125 */     if (lastResult != null && this.lastCheckTime < lastResult.time) {
/* 126 */       this.output.accept(lastResult.value);
/* 127 */       this.lastCheckTime = lastResult.time;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void runCallback() {
/* 132 */     DataFetcher.SuccessfulComputationResult<T> lastResult = this.task.lastResult;
/* 133 */     if (lastResult != null) {
/* 134 */       this.output.accept(lastResult.value);
/* 135 */       this.lastCheckTime = lastResult.time;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void reset() {
/* 140 */     this.task.reset();
/* 141 */     this.lastCheckTime = -1L;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\task\DataFetcher$SubscribedTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */