/*     */ package net.minecraft.client.resources.server;
/*     */ 
/*     */ import java.util.concurrent.Executor;
/*     */ 
/*     */ class null implements Runnable {
/*     */   private boolean scheduledInMainExecutor;
/*     */   
/*     */   private boolean hasUpdates;
/*     */   
/*     */   public void run() {
/* 228 */     this.hasUpdates = true;
/* 229 */     if (!this.scheduledInMainExecutor) {
/* 230 */       this.scheduledInMainExecutor = true;
/* 231 */       mainThreadExecutor.execute(this::runAllUpdates);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void runAllUpdates() {
/* 236 */     while (this.hasUpdates) {
/* 237 */       this.hasUpdates = false;
/* 238 */       DownloadedPackSource.this.manager.tick();
/*     */     } 
/* 240 */     this.scheduledInMainExecutor = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\DownloadedPackSource$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */