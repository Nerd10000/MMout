/*     */ package net.minecraft.client.model.geom;
/*     */ 
/*     */ public final class Vertex extends Record {
/*     */   private final float x;
/*     */   
/*     */   private final float y;
/*     */   
/*     */   private final float z;
/*     */   
/*     */   private final float u;
/*     */   
/*     */   private final float v;
/*     */   
/*     */   public static final float SCALE_FACTOR = 16.0F;
/*     */   
/*     */   public Vertex(float x, float y, float z, float u, float v) {
/* 369 */     this.x = x;
/* 369 */     this.y = y;
/* 369 */     this.z = z;
/* 369 */     this.u = u;
/* 369 */     this.v = v;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/geom/ModelPart$Vertex;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #369	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelPart$Vertex;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/geom/ModelPart$Vertex;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #369	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelPart$Vertex;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/geom/ModelPart$Vertex;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #369	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/model/geom/ModelPart$Vertex;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float x() {
/* 369 */     return this.x;
/*     */   }
/*     */   
/*     */   public float y() {
/* 369 */     return this.y;
/*     */   }
/*     */   
/*     */   public float z() {
/* 369 */     return this.z;
/*     */   }
/*     */   
/*     */   public float u() {
/* 369 */     return this.u;
/*     */   }
/*     */   
/*     */   public float v() {
/* 369 */     return this.v;
/*     */   }
/*     */   
/*     */   public Vertex remap(float u, float v) {
/* 373 */     return new Vertex(this.x, this.y, this.z, u, v);
/*     */   }
/*     */   
/*     */   public float worldX() {
/* 377 */     return this.x / 16.0F;
/*     */   }
/*     */   
/*     */   public float worldY() {
/* 381 */     return this.y / 16.0F;
/*     */   }
/*     */   
/*     */   public float worldZ() {
/* 385 */     return this.z / 16.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\geom\ModelPart$Vertex.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */