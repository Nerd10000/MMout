/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ class CapturingStream extends OutputStream {
/*     */   private ByteArrayOutputStream buffer;
/*     */   
/*     */   public void write(byte[] b, int off, int len) {
/* 353 */     if (this.buffer != null)
/* 354 */       this.buffer.write(b, off, len); 
/*     */   }
/*     */   
/*     */   public void write(int b) {
/* 360 */     if (this.buffer != null)
/* 361 */       this.buffer.write(b); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\NativeLibrariesBootstrap$CapturingStream.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */