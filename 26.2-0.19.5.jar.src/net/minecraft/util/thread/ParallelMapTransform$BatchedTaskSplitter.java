/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ class BatchedTaskSplitter<K, U, V> extends ParallelMapTransform.SplitterBase<K, U, V> {
/*     */   private final Map<K, V> result;
/*     */   
/*     */   private final int batchSize;
/*     */   
/*     */   private final int firstUndersizedBatchIndex;
/*     */   
/*     */   private BatchedTaskSplitter(BiFunction<K, U, V> operation, int size, int maxTasks) {
/* 184 */     super(operation, size, maxTasks);
/* 185 */     this.result = new HashMap<>(size);
/* 186 */     this.batchSize = Mth.positiveCeilDiv(size, maxTasks);
/* 188 */     int fullCapacity = this.batchSize * maxTasks;
/* 189 */     int leftoverCapacity = fullCapacity - size;
/* 192 */     this.firstUndersizedBatchIndex = maxTasks - leftoverCapacity;
/* 193 */     assert this.firstUndersizedBatchIndex > 0 && this.firstUndersizedBatchIndex <= maxTasks;
/*     */   }
/*     */   
/*     */   protected CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> container, int startIndex, int endIndex, Executor executor) {
/* 198 */     int batchSize = endIndex - startIndex;
/* 200 */     assert batchSize == this.batchSize || batchSize == this.batchSize - 1;
/* 201 */     return CompletableFuture.runAsync(createTask(this.result, startIndex, endIndex, container), executor);
/*     */   }
/*     */   
/*     */   protected int batchSize(int index) {
/* 206 */     return (index < this.firstUndersizedBatchIndex) ? this.batchSize : (this.batchSize - 1);
/*     */   }
/*     */   
/*     */   private static <K, U, V> Runnable createTask(Map<K, V> result, int startIndex, int endIndex, ParallelMapTransform.Container<K, U, V> container) {
/* 211 */     return () -> {
/*     */         for (int i = startIndex; i < endIndex; i++)
/*     */           container.applyOperation(i); 
/*     */         synchronized (result) {
/*     */           for (int j = startIndex; j < endIndex; j++)
/*     */             container.copyOut(j, result); 
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   protected CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> allTasksDone, ParallelMapTransform.Container<K, U, V> container) {
/* 228 */     Map<K, V> result = this.result;
/* 229 */     return allTasksDone.thenApply(ignored -> result);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\ParallelMapTransform$BatchedTaskSplitter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */