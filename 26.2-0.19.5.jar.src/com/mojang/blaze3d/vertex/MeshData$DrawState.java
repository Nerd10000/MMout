/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ import com.mojang.blaze3d.IndexType;
/*    */ import com.mojang.blaze3d.PrimitiveTopology;
/*    */ 
/*    */ public final class DrawState extends Record {
/*    */   private final VertexFormat format;
/*    */   
/*    */   private final int vertexCount;
/*    */   
/*    */   private final int indexCount;
/*    */   
/*    */   private final PrimitiveTopology primitiveTopology;
/*    */   
/*    */   private final IndexType indexType;
/*    */   
/*    */   public DrawState(VertexFormat format, int vertexCount, int indexCount, PrimitiveTopology primitiveTopology, IndexType indexType) {
/* 84 */     this.format = format;
/* 84 */     this.vertexCount = vertexCount;
/* 84 */     this.indexCount = indexCount;
/* 84 */     this.primitiveTopology = primitiveTopology;
/* 84 */     this.indexType = indexType;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vertex/MeshData$DrawState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #84	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vertex/MeshData$DrawState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/MeshData$DrawState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #84	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vertex/MeshData$DrawState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/MeshData$DrawState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #84	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vertex/MeshData$DrawState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public VertexFormat format() {
/* 84 */     return this.format;
/*    */   }
/*    */   
/*    */   public int vertexCount() {
/* 84 */     return this.vertexCount;
/*    */   }
/*    */   
/*    */   public int indexCount() {
/* 84 */     return this.indexCount;
/*    */   }
/*    */   
/*    */   public PrimitiveTopology primitiveTopology() {
/* 84 */     return this.primitiveTopology;
/*    */   }
/*    */   
/*    */   public IndexType indexType() {
/* 84 */     return this.indexType;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\MeshData$DrawState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */