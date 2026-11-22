/*     */ package net.minecraft.client.resources.server;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ 
/*     */ class null implements PackLoadFeedback {
/*     */   public void reportUpdate(UUID id, PackLoadFeedback.Update result) {
/* 419 */     original.reportUpdate(id, result);
/*     */   }
/*     */   
/*     */   public void reportFinalResult(UUID id, PackLoadFeedback.FinalResult status) {
/* 424 */     if (packId.equals(id)) {
/* 425 */       DownloadedPackSource.this.packFeedback = original;
/* 426 */       if (status == PackLoadFeedback.FinalResult.APPLIED) {
/* 427 */         result.complete(null);
/*     */       } else {
/* 429 */         result.completeExceptionally(new IllegalStateException("Failed to apply pack " + String.valueOf(id) + ", reason: " + String.valueOf(status)));
/*     */       } 
/*     */     } 
/* 432 */     original.reportFinalResult(id, status);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\DownloadedPackSource$7.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */