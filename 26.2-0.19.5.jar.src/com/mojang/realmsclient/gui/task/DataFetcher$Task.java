/*     */ package com.mojang.realmsclient.gui.task;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ 
/*     */ public class Task<T> {
/*     */   private final String id;
/*     */   
/*     */   private final Callable<T> updater;
/*     */   
/*     */   private final long period;
/*     */   
/*     */   private final RepeatedDelayStrategy repeatStrategy;
/*     */   
/*     */   private CompletableFuture<DataFetcher.ComputationResult<T>> pendingTask;
/*     */   
/*     */   private DataFetcher.SuccessfulComputationResult<T> lastResult;
/*     */   
/*  57 */   private long nextUpdate = -1L;
/*     */   
/*     */   private Task(String id, Callable<T> updater, long period, RepeatedDelayStrategy repeatStrategy) {
/*  60 */     this.id = id;
/*  61 */     this.updater = updater;
/*  62 */     this.period = period;
/*  63 */     this.repeatStrategy = repeatStrategy;
/*     */   }
/*     */   
/*     */   private void updateIfNeeded(long currentTime) {
/*  67 */     if (this.pendingTask != null) {
/*  68 */       DataFetcher.ComputationResult<T> result = this.pendingTask.getNow(null);
/*  69 */       if (result == null)
/*     */         return; 
/*  73 */       this.pendingTask = null;
/*  74 */       long completionTime = result.time;
/*  75 */       result.value()
/*  76 */         .ifLeft(value -> {
/*     */             this.lastResult = new DataFetcher.SuccessfulComputationResult<>((T)value, completionTime);
/*     */             this.nextUpdate = completionTime + this.period * this.repeatStrategy.delayCyclesAfterSuccess();
/*  80 */           }).ifRight(e -> {
/*     */             long cycles = this.repeatStrategy.delayCyclesAfterFailure();
/*     */             DataFetcher.LOGGER.warn("Failed to process task {}, will repeat after {} cycles", new Object[] { this.id, Long.valueOf(cycles), e });
/*     */             this.nextUpdate = completionTime + this.period * cycles;
/*     */           });
/*     */     } 
/*  87 */     if (this.nextUpdate <= currentTime)
/*  88 */       this.pendingTask = CompletableFuture.supplyAsync(() -> {
/*     */             try {
/*     */               T result = this.updater.call();
/*     */               long completionTime = DataFetcher.this.timeSource.get(DataFetcher.this.resolution);
/*     */               return new DataFetcher.ComputationResult(Either.left(result), completionTime);
/*  93 */             } catch (Exception e) {
/*     */               long completionTime = DataFetcher.this.timeSource.get(DataFetcher.this.resolution);
/*     */               return new DataFetcher.ComputationResult(Either.right(e), completionTime);
/*     */             } 
/*     */           }DataFetcher.this.executor); 
/*     */   }
/*     */   
/*     */   public void reset() {
/* 102 */     this.pendingTask = null;
/* 103 */     this.lastResult = null;
/* 104 */     this.nextUpdate = -1L;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\task\DataFetcher$Task.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */