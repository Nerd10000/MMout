/*     */ package com.mojang.blaze3d.systems;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import java.util.function.BiConsumer;
/*     */ 
/*     */ public final class Draw<T> extends Record {
/*     */   private final int slot;
/*     */   
/*     */   private final GpuBuffer vertexBuffer;
/*     */   
/*     */   private final GpuBuffer indexBuffer;
/*     */   
/*     */   private final IndexType indexType;
/*     */   
/*     */   private final int firstIndex;
/*     */   
/*     */   private final int indexCount;
/*     */   
/*     */   private final int baseVertex;
/*     */   
/*     */   private final BiConsumer<T, RenderPass.UniformUploader> uniformUploaderConsumer;
/*     */   
/*     */   public BiConsumer<T, RenderPass.UniformUploader> uniformUploaderConsumer() {
/* 313 */     return this.uniformUploaderConsumer;
/*     */   }
/*     */   
/*     */   public int baseVertex() {
/* 313 */     return this.baseVertex;
/*     */   }
/*     */   
/*     */   public int indexCount() {
/* 313 */     return this.indexCount;
/*     */   }
/*     */   
/*     */   public int firstIndex() {
/* 313 */     return this.firstIndex;
/*     */   }
/*     */   
/*     */   public IndexType indexType() {
/* 313 */     return this.indexType;
/*     */   }
/*     */   
/*     */   public GpuBuffer indexBuffer() {
/* 313 */     return this.indexBuffer;
/*     */   }
/*     */   
/*     */   public GpuBuffer vertexBuffer() {
/* 313 */     return this.vertexBuffer;
/*     */   }
/*     */   
/*     */   public int slot() {
/* 313 */     return this.slot;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/RenderPass$Draw;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #313	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/RenderPass$Draw;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #313	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw<TT;>;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/RenderPass$Draw;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #313	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw<TT;>;
/*     */   }
/*     */   
/*     */   public Draw(int slot, GpuBuffer vertexBuffer, GpuBuffer indexBuffer, IndexType indexType, int firstIndex, int indexCount, int baseVertex, BiConsumer<T, RenderPass.UniformUploader> uniformUploaderConsumer) {
/* 313 */     this.slot = slot;
/* 313 */     this.vertexBuffer = vertexBuffer;
/* 313 */     this.indexBuffer = indexBuffer;
/* 313 */     this.indexType = indexType;
/* 313 */     this.firstIndex = firstIndex;
/* 313 */     this.indexCount = indexCount;
/* 313 */     this.baseVertex = baseVertex;
/* 313 */     this.uniformUploaderConsumer = uniformUploaderConsumer;
/*     */   }
/*     */   
/*     */   public Draw(int slot, GpuBuffer vertexBuffer, GpuBuffer indexBuffer, IndexType indexType, int firstIndex, int indexCount, int baseVertex) {
/* 315 */     this(slot, vertexBuffer, indexBuffer, indexType, firstIndex, indexCount, baseVertex, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\RenderPass$Draw.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */