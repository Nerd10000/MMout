/*     */ package net.minecraft.client.resources.server;
/*     */ 
/*     */ import java.util.UUID;
/*     */ 
/*     */ class null implements PackLoadFeedback {
/*     */   public void reportUpdate(UUID id, PackLoadFeedback.Update result) {
/* 108 */     DownloadedPackSource.this.packFeedback.reportUpdate(id, result);
/*     */   }
/*     */   
/*     */   public void reportFinalResult(UUID id, PackLoadFeedback.FinalResult result) {
/* 113 */     DownloadedPackSource.this.packFeedback.reportFinalResult(id, result);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\DownloadedPackSource$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */