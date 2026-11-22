/*    */ package net.minecraft.client.resources.model.cuboid;
/*    */ 
/*    */ import org.joml.Matrix4f;
/*    */ 
/*    */ public final class EulerXYZRotation extends Record implements CuboidRotation.RotationValue {
/*    */   private final float x;
/*    */   
/*    */   private final float y;
/*    */   
/*    */   private final float z;
/*    */   
/*    */   public EulerXYZRotation(float x, float y, float z) {
/* 82 */     this.x = x;
/* 82 */     this.y = y;
/* 82 */     this.z = z;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$EulerXYZRotation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #82	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$EulerXYZRotation;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$EulerXYZRotation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #82	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$EulerXYZRotation;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$EulerXYZRotation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #82	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$EulerXYZRotation;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float x() {
/* 82 */     return this.x;
/*    */   }
/*    */   
/*    */   public float y() {
/* 82 */     return this.y;
/*    */   }
/*    */   
/*    */   public float z() {
/* 82 */     return this.z;
/*    */   }
/*    */   
/*    */   public Matrix4f transformation() {
/* 89 */     return (new Matrix4f()).rotationZYX(this.z * 0.017453292F, this.y * 0.017453292F, this.x * 0.017453292F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\CuboidRotation$EulerXYZRotation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */