/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import com.mojang.blaze3d.IndexType;
/*    */ 
/*    */ public final class SectionDraw extends Record {
/*    */   private final int indexCount;
/*    */   
/*    */   private final IndexType indexType;
/*    */   
/*    */   private final boolean hasCustomIndexBuffer;
/*    */   
/*    */   public SectionDraw(int indexCount, IndexType indexType, boolean hasCustomIndexBuffer) {
/* 12 */     this.indexCount = indexCount;
/* 12 */     this.indexType = indexType;
/* 12 */     this.hasCustomIndexBuffer = hasCustomIndexBuffer;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/chunk/SectionMesh$SectionDraw;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/SectionMesh$SectionDraw;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/chunk/SectionMesh$SectionDraw;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/SectionMesh$SectionDraw;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/chunk/SectionMesh$SectionDraw;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/chunk/SectionMesh$SectionDraw;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int indexCount() {
/* 12 */     return this.indexCount;
/*    */   }
/*    */   
/*    */   public IndexType indexType() {
/* 12 */     return this.indexType;
/*    */   }
/*    */   
/*    */   public boolean hasCustomIndexBuffer() {
/* 12 */     return this.hasCustomIndexBuffer;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\SectionMesh$SectionDraw.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */