/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.commons.io.input.CountingInputStream;
/*     */ 
/*     */ class UploadCountingInputStream extends CountingInputStream {
/*     */   private final UploadStatus uploadStatus;
/*     */   
/*     */   private UploadCountingInputStream(InputStream proxy, UploadStatus uploadStatus) {
/* 146 */     super(proxy);
/* 147 */     this.uploadStatus = uploadStatus;
/*     */   }
/*     */   
/*     */   protected void afterRead(int n) throws IOException {
/* 152 */     super.afterRead(n);
/* 153 */     this.uploadStatus.onWrite(getByteCount());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\FileUpload$UploadCountingInputStream.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */