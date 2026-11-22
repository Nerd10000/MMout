/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ public class Result implements AutoCloseable {
/*     */   private final long offset;
/*     */   
/*     */   private final int size;
/*     */   
/*     */   private final int generation;
/*     */   
/*     */   private boolean closed;
/*     */   
/*     */   private Result(long offset, int size, int generation) {
/* 157 */     this.offset = offset;
/* 158 */     this.size = size;
/* 159 */     this.generation = generation;
/*     */   }
/*     */   
/*     */   public ByteBuffer byteBuffer() {
/* 163 */     if (!ByteBufferBuilder.this.isValid(this.generation))
/* 164 */       throw new IllegalStateException("Buffer is no longer valid"); 
/* 166 */     return MemoryUtil.memByteBuffer(ByteBufferBuilder.this.pointer + this.offset, this.size);
/*     */   }
/*     */   
/*     */   public int size() {
/* 170 */     return this.size;
/*     */   }
/*     */   
/*     */   public void close() {
/* 175 */     if (this.closed)
/*     */       return; 
/* 178 */     this.closed = true;
/* 179 */     if (ByteBufferBuilder.this.isValid(this.generation))
/* 180 */       ByteBufferBuilder.this.freeResult(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\ByteBufferBuilder$Result.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */