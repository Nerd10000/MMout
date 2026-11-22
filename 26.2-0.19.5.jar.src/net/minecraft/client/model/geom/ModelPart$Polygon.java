/*     */ package net.minecraft.client.model.geom;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public final class Polygon extends Record {
/*     */   private final ModelPart.Vertex[] vertices;
/*     */   
/*     */   private final Vector3fc normal;
/*     */   
/*     */   public Polygon(ModelPart.Vertex[] vertices, Vector3fc normal) {
/* 343 */     this.vertices = vertices;
/* 343 */     this.normal = normal;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/geom/ModelPart$Polygon;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #343	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelPart$Polygon;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/geom/ModelPart$Polygon;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #343	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelPart$Polygon;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/geom/ModelPart$Polygon;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #343	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/model/geom/ModelPart$Polygon;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ModelPart.Vertex[] vertices() {
/* 343 */     return this.vertices;
/*     */   }
/*     */   
/*     */   public Vector3fc normal() {
/* 343 */     return this.normal;
/*     */   }
/*     */   
/*     */   public Polygon(ModelPart.Vertex[] vertices, float u0, float v0, float u1, float v1, float xTexSize, float yTexSize, boolean mirror, Direction facing) {
/* 345 */     this(vertices, (mirror ? mirrorFacing(facing) : facing).getUnitVec3f());
/* 347 */     float us = 0.0F / xTexSize;
/* 348 */     float vs = 0.0F / yTexSize;
/* 349 */     vertices[0] = vertices[0].remap(u1 / xTexSize - us, v0 / yTexSize + vs);
/* 350 */     vertices[1] = vertices[1].remap(u0 / xTexSize + us, v0 / yTexSize + vs);
/* 351 */     vertices[2] = vertices[2].remap(u0 / xTexSize + us, v1 / yTexSize - vs);
/* 352 */     vertices[3] = vertices[3].remap(u1 / xTexSize - us, v1 / yTexSize - vs);
/* 354 */     if (mirror) {
/* 355 */       int length = vertices.length;
/* 356 */       for (int i = 0; i < length / 2; i++) {
/* 357 */         ModelPart.Vertex tmp = vertices[i];
/* 358 */         vertices[i] = vertices[length - 1 - i];
/* 359 */         vertices[length - 1 - i] = tmp;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Direction mirrorFacing(Direction facing) {
/* 365 */     return (facing.getAxis() == Direction.Axis.X) ? facing.getOpposite() : facing;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\geom\ModelPart$Polygon.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */