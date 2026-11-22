/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuFence;
/*     */ import java.util.List;
/*     */ 
/*     */ final class PendingRecycle extends Record implements AutoCloseable {
/*     */   private final List<GpuBuffer> buffers;
/*     */   
/*     */   private final GpuFence fence;
/*     */   
/*     */   private PendingRecycle(List<GpuBuffer> buffers, GpuFence fence) {
/* 316 */     this.buffers = buffers;
/* 316 */     this.fence = fence;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #316	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #316	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #316	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public List<GpuBuffer> buffers() {
/* 316 */     return this.buffers;
/*     */   }
/*     */   
/*     */   public GpuFence fence() {
/* 316 */     return this.fence;
/*     */   }
/*     */   
/*     */   public List<GpuBuffer> tryRecycle() {
/* 318 */     if (this.fence.awaitCompletion(0L)) {
/* 319 */       this.fence.close();
/* 320 */       return this.buffers;
/*     */     } 
/* 322 */     return null;
/*     */   }
/*     */   
/*     */   public void close() {
/* 327 */     this.buffers.forEach(GpuBuffer::close);
/* 328 */     this.fence.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\StagedVertexBuffer$GpuBufferPool$PendingRecycle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */