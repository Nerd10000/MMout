/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import org.lwjgl.stb.STBIWriteCallback;
/*     */ 
/*     */ class WriteCallback extends STBIWriteCallback {
/*     */   private final WritableByteChannel output;
/*     */   
/*     */   private IOException exception;
/*     */   
/*     */   private WriteCallback(WritableByteChannel output) {
/* 344 */     this.output = output;
/*     */   }
/*     */   
/*     */   public void invoke(long context, long data, int size) {
/* 349 */     ByteBuffer dataBuf = getData(data, size);
/*     */     try {
/* 351 */       this.output.write(dataBuf);
/* 352 */     } catch (IOException e) {
/* 353 */       this.exception = e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void throwIfException() throws IOException {
/* 358 */     if (this.exception != null)
/* 359 */       throw this.exception; 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\NativeImage$WriteCallback.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */