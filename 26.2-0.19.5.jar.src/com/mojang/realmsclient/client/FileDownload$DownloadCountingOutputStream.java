/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.commons.io.output.CountingOutputStream;
/*     */ 
/*     */ class DownloadCountingOutputStream extends CountingOutputStream {
/*     */   private final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus;
/*     */   
/*     */   public DownloadCountingOutputStream(OutputStream out, RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus) {
/* 375 */     super(out);
/* 376 */     this.downloadStatus = downloadStatus;
/*     */   }
/*     */   
/*     */   protected void afterWrite(int n) throws IOException {
/* 381 */     super.afterWrite(n);
/* 382 */     this.downloadStatus.bytesWritten = getByteCount();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\FileDownload$DownloadCountingOutputStream.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */