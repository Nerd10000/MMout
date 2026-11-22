/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.commons.io.output.TeeOutputStream;
/*     */ 
/*     */ class CapturingPrintStream extends PrintStream {
/*     */   private final NativeLibrariesBootstrap.CapturingStream collector;
/*     */   
/*     */   public CapturingPrintStream(OutputStream out) {
/* 371 */     super((OutputStream)new TeeOutputStream(out, logStream), false, StandardCharsets.UTF_8);
/* 372 */     this.collector = logStream;
/*     */   }
/*     */   
/*     */   public synchronized void startCapturing() {
/* 376 */     this.collector.buffer = new ByteArrayOutputStream();
/*     */   }
/*     */   
/*     */   public synchronized String stopCapturing() {
/* 380 */     ByteArrayOutputStream buffer = this.collector.buffer;
/* 381 */     this.collector.buffer = null;
/* 382 */     if (buffer != null)
/* 383 */       return buffer.toString(StandardCharsets.UTF_8); 
/* 385 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\NativeLibrariesBootstrap$CapturingPrintStream.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */