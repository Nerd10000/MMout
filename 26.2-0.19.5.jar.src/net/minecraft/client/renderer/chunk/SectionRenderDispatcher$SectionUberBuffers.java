/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.UberGpuBuffer;
/*     */ 
/*     */ final class SectionUberBuffers extends Record {
/*     */   private final UberGpuBuffer<SectionMesh> vertexBuffer;
/*     */   
/*     */   private final UberGpuBuffer<SectionMesh> indexBuffer;
/*     */   
/*     */   private SectionUberBuffers(UberGpuBuffer<SectionMesh> vertexBuffer, UberGpuBuffer<SectionMesh> indexBuffer) {
/* 613 */     this.vertexBuffer = vertexBuffer;
/* 613 */     this.indexBuffer = indexBuffer;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$SectionUberBuffers;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #613	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$SectionUberBuffers;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$SectionUberBuffers;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #613	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$SectionUberBuffers;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$SectionUberBuffers;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #613	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$SectionUberBuffers;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public UberGpuBuffer<SectionMesh> vertexBuffer() {
/* 613 */     return this.vertexBuffer;
/*     */   }
/*     */   
/*     */   public UberGpuBuffer<SectionMesh> indexBuffer() {
/* 613 */     return this.indexBuffer;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\SectionRenderDispatcher$SectionUberBuffers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */