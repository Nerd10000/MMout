/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ abstract class SplitterBase<K, U, V> {
/*     */   private int lastScheduledIndex;
/*     */   
/*     */   private int currentIndex;
/*     */   
/*     */   private final CompletableFuture<?>[] tasks;
/*     */   
/*     */   private int batchIndex;
/*     */   
/*     */   private final ParallelMapTransform.Container<K, U, V> container;
/*     */   
/*     */   private SplitterBase(BiFunction<K, U, V> operation, int size, int taskCount) {
/* 115 */     this.container = new ParallelMapTransform.Container<>(operation, size);
/* 116 */     this.tasks = (CompletableFuture<?>[])new CompletableFuture[taskCount];
/*     */   }
/*     */   
/*     */   private int pendingBatchSize() {
/* 120 */     return this.currentIndex - this.lastScheduledIndex;
/*     */   }
/*     */   
/*     */   public CompletableFuture<Map<K, V>> scheduleTasks(Map<K, U> input, Executor executor) {
/* 124 */     input.forEach((key, inputValue) -> {
/*     */           this.container.put(this.currentIndex++, (K)key, (U)inputValue);
/*     */           if (pendingBatchSize() == batchSize(this.batchIndex)) {
/*     */             this.tasks[this.batchIndex++] = scheduleBatch(this.container, this.lastScheduledIndex, this.currentIndex, executor);
/*     */             this.lastScheduledIndex = this.currentIndex;
/*     */           } 
/*     */         });
/* 132 */     assert this.currentIndex == this.container.size();
/* 133 */     assert this.lastScheduledIndex == this.currentIndex;
/* 134 */     assert this.batchIndex == this.tasks.length;
/* 136 */     return scheduleFinalOperation(CompletableFuture.allOf(this.tasks), this.container);
/*     */   }
/*     */   
/*     */   protected abstract int batchSize(int paramInt);
/*     */   
/*     */   protected abstract CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> paramContainer, int paramInt1, int paramInt2, Executor paramExecutor);
/*     */   
/*     */   protected abstract CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> paramCompletableFuture, ParallelMapTransform.Container<K, U, V> paramContainer);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\ParallelMapTransform$SplitterBase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */