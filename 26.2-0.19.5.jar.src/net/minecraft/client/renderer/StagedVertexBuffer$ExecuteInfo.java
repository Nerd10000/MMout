/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ 
/*     */ public final class ExecuteInfo extends Record {
/*     */   private final GpuBuffer vertexBuffer;
/*     */   
/*     */   private final GpuBuffer indexBuffer;
/*     */   
/*     */   private final IndexType indexType;
/*     */   
/*     */   private final int baseVertex;
/*     */   
/*     */   private final int firstIndex;
/*     */   
/*     */   private final int indexCount;
/*     */   
/*     */   public ExecuteInfo(GpuBuffer vertexBuffer, GpuBuffer indexBuffer, IndexType indexType, int baseVertex, int firstIndex, int indexCount) {
/* 375 */     this.vertexBuffer = vertexBuffer;
/* 375 */     this.indexBuffer = indexBuffer;
/* 375 */     this.indexType = indexType;
/* 375 */     this.baseVertex = baseVertex;
/* 375 */     this.firstIndex = firstIndex;
/* 375 */     this.indexCount = indexCount;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/StagedVertexBuffer$ExecuteInfo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #375	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$ExecuteInfo;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/StagedVertexBuffer$ExecuteInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #375	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$ExecuteInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/StagedVertexBuffer$ExecuteInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #375	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$ExecuteInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public GpuBuffer vertexBuffer() {
/* 375 */     return this.vertexBuffer;
/*     */   }
/*     */   
/*     */   public GpuBuffer indexBuffer() {
/* 375 */     return this.indexBuffer;
/*     */   }
/*     */   
/*     */   public IndexType indexType() {
/* 375 */     return this.indexType;
/*     */   }
/*     */   
/*     */   public int baseVertex() {
/* 375 */     return this.baseVertex;
/*     */   }
/*     */   
/*     */   public int firstIndex() {
/* 375 */     return this.firstIndex;
/*     */   }
/*     */   
/*     */   public int indexCount() {
/* 375 */     return this.indexCount;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\StagedVertexBuffer$ExecuteInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */