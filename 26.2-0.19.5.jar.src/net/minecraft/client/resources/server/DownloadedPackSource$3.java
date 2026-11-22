/*     */ package net.minecraft.client.resources.server;
/*     */ 
/*     */ import com.mojang.realmsclient.Unit;
/*     */ import java.util.OptionalLong;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ 
/*     */ class null implements HttpUtil.DownloadProgressListener {
/* 125 */   private final SystemToast.SystemToastId toastId = new SystemToast.SystemToastId();
/*     */   
/* 126 */   private Component title = (Component)Component.empty();
/*     */   
/* 127 */   private Component message = null;
/*     */   
/*     */   private int count;
/*     */   
/*     */   private int failCount;
/*     */   
/* 130 */   private OptionalLong totalBytes = OptionalLong.empty();
/*     */   
/*     */   private void updateToast() {
/* 133 */     DownloadedPackSource.this.minecraft.execute(() -> SystemToast.addOrUpdate(DownloadedPackSource.this.minecraft.gui.toastManager(), this.toastId, this.title, this.message));
/*     */   }
/*     */   
/*     */   private void updateProgress(long bytesSoFar) {
/* 137 */     if (this.totalBytes.isPresent()) {
/* 138 */       this.message = (Component)Component.translatable("download.pack.progress.percent", new Object[] { Long.valueOf(bytesSoFar * 100L / this.totalBytes.getAsLong()) });
/*     */     } else {
/* 140 */       this.message = (Component)Component.translatable("download.pack.progress.bytes", new Object[] { Unit.humanReadable(bytesSoFar) });
/*     */     } 
/* 142 */     updateToast();
/*     */   }
/*     */   
/*     */   public void requestStart() {
/* 147 */     this.count++;
/* 148 */     this.title = (Component)Component.translatable("download.pack.title", new Object[] { Integer.valueOf(this.count), Integer.valueOf(this.val$totalCount) });
/* 149 */     updateToast();
/* 150 */     DownloadedPackSource.LOGGER.debug("Starting pack {}/{} download", Integer.valueOf(this.count), Integer.valueOf(totalCount));
/*     */   }
/*     */   
/*     */   public void downloadStart(OptionalLong sizeBytes) {
/* 155 */     DownloadedPackSource.LOGGER.debug("File size = {} bytes", sizeBytes);
/* 156 */     this.totalBytes = sizeBytes;
/* 157 */     updateProgress(0L);
/*     */   }
/*     */   
/*     */   public void downloadedBytes(long bytesSoFar) {
/* 162 */     DownloadedPackSource.LOGGER.debug("Progress for pack {}: {} bytes", Integer.valueOf(this.count), Long.valueOf(bytesSoFar));
/* 163 */     updateProgress(bytesSoFar);
/*     */   }
/*     */   
/*     */   public void requestFinished(boolean success) {
/* 168 */     if (!success) {
/* 169 */       DownloadedPackSource.LOGGER.info("Pack {} failed to download", Integer.valueOf(this.count));
/* 170 */       this.failCount++;
/*     */     } else {
/* 172 */       DownloadedPackSource.LOGGER.debug("Download ended for pack {}", Integer.valueOf(this.count));
/*     */     } 
/* 175 */     if (this.count == totalCount)
/* 176 */       if (this.failCount > 0) {
/* 177 */         this.title = (Component)Component.translatable("download.pack.failed", new Object[] { Integer.valueOf(this.failCount), Integer.valueOf(this.val$totalCount) });
/* 178 */         this.message = null;
/* 179 */         updateToast();
/*     */       } else {
/* 181 */         SystemToast.forceHide(DownloadedPackSource.this.minecraft.gui.toastManager(), this.toastId);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\DownloadedPackSource$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */