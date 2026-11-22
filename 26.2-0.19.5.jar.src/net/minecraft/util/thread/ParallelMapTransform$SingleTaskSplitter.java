/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ class SingleTaskSplitter<K, U, V> extends ParallelMapTransform.SplitterBase<K, U, V> {
/*     */   private SingleTaskSplitter(BiFunction<K, U, V> operation, int size) {
/* 149 */     super(operation, size, size);
/*     */   }
/*     */   
/*     */   protected int batchSize(int index) {
/* 154 */     return 1;
/*     */   }
/*     */   
/*     */   protected CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> container, int startIndex, int endIndex, Executor executor) {
/* 159 */     assert startIndex + 1 == endIndex;
/* 160 */     return CompletableFuture.runAsync(() -> container.applyOperation(startIndex), executor);
/*     */   }
/*     */   
/*     */   protected CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> allTasksDone, ParallelMapTransform.Container<K, U, V> container) {
/* 165 */     return allTasksDone.thenApply(ignored -> {
/*     */           Map<K, V> result = new HashMap<>(container.size());
/*     */           for (int i = 0; i < container.size(); i++)
/*     */             container.copyOut(i, result); 
/*     */           return result;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\ParallelMapTransform$SingleTaskSplitter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */