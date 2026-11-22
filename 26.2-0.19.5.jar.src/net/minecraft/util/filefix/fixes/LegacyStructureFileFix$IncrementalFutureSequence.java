/*     */ package net.minecraft.util.filefix.fixes;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ 
/*     */ class IncrementalFutureSequence {
/*     */   private final int maxConcurrency;
/*     */   
/*     */   private final List<CompletableFuture<?>> futures;
/*     */   
/*     */   public IncrementalFutureSequence(int maxConcurrency) {
/* 250 */     this.maxConcurrency = maxConcurrency;
/* 251 */     this.futures = new ArrayList<>(maxConcurrency);
/*     */   }
/*     */   
/*     */   public int push(CompletableFuture<?> future) {
/* 255 */     int finished = 0;
/* 256 */     if (this.futures.size() >= this.maxConcurrency)
/* 257 */       finished += waitOnAny(); 
/* 259 */     this.futures.add(future);
/* 260 */     return finished;
/*     */   }
/*     */   
/*     */   private int waitOnAny() {
/* 264 */     int oldSize = this.futures.size();
/* 265 */     CompletableFuture.anyOf((CompletableFuture<?>[])this.futures.toArray(x$0 -> new CompletableFuture[x$0])).join();
/* 266 */     this.futures.removeIf(CompletableFuture::isDone);
/* 267 */     return oldSize - this.futures.size();
/*     */   }
/*     */   
/*     */   public int waitForAll() {
/* 271 */     int oldSize = this.futures.size();
/* 272 */     CompletableFuture.allOf((CompletableFuture<?>[])this.futures.toArray(x$0 -> new CompletableFuture[x$0])).join();
/* 273 */     this.futures.clear();
/* 274 */     return oldSize;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\fixes\LegacyStructureFileFix$IncrementalFutureSequence.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */