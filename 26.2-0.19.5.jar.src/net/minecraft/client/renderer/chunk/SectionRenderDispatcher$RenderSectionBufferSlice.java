/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ 
/*     */ public final class RenderSectionBufferSlice extends Record {
/*     */   private final GpuBuffer vertexBuffer;
/*     */   
/*     */   private final long vertexBufferOffset;
/*     */   
/*     */   private final GpuBuffer indexBuffer;
/*     */   
/*     */   private final long indexBufferOffset;
/*     */   
/*     */   public RenderSectionBufferSlice(GpuBuffer vertexBuffer, long vertexBufferOffset, GpuBuffer indexBuffer, long indexBufferOffset) {
/* 125 */     this.vertexBuffer = vertexBuffer;
/* 125 */     this.vertexBufferOffset = vertexBufferOffset;
/* 125 */     this.indexBuffer = indexBuffer;
/* 125 */     this.indexBufferOffset = indexBufferOffset;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSectionBufferSlice;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #125	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSectionBufferSlice;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSectionBufferSlice;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #125	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSectionBufferSlice;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSectionBufferSlice;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #125	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSectionBufferSlice;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public GpuBuffer vertexBuffer() {
/* 125 */     return this.vertexBuffer;
/*     */   }
/*     */   
/*     */   public long vertexBufferOffset() {
/* 125 */     return this.vertexBufferOffset;
/*     */   }
/*     */   
/*     */   public GpuBuffer indexBuffer() {
/* 125 */     return this.indexBuffer;
/*     */   }
/*     */   
/*     */   public long indexBufferOffset() {
/* 125 */     return this.indexBufferOffset;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\SectionRenderDispatcher$RenderSectionBufferSlice.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */