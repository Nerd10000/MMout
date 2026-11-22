/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public final class VertexInfo extends Record {
/*     */   private final FaceInfo.Extent xFace;
/*     */   
/*     */   private final FaceInfo.Extent yFace;
/*     */   
/*     */   private final FaceInfo.Extent zFace;
/*     */   
/*     */   public VertexInfo(FaceInfo.Extent xFace, FaceInfo.Extent yFace, FaceInfo.Extent zFace) {
/* 102 */     this.xFace = xFace;
/* 102 */     this.yFace = yFace;
/* 102 */     this.zFace = zFace;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/FaceInfo$VertexInfo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #102	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/FaceInfo$VertexInfo;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/FaceInfo$VertexInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #102	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/FaceInfo$VertexInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/FaceInfo$VertexInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #102	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/FaceInfo$VertexInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public FaceInfo.Extent xFace() {
/* 102 */     return this.xFace;
/*     */   }
/*     */   
/*     */   public FaceInfo.Extent yFace() {
/* 102 */     return this.yFace;
/*     */   }
/*     */   
/*     */   public FaceInfo.Extent zFace() {
/* 102 */     return this.zFace;
/*     */   }
/*     */   
/*     */   public Vector3f select(Vector3fc min, Vector3fc max) {
/* 104 */     return new Vector3f(this.xFace
/* 105 */         .select(min, max), this.yFace
/* 106 */         .select(min, max), this.zFace
/* 107 */         .select(min, max));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\FaceInfo$VertexInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */