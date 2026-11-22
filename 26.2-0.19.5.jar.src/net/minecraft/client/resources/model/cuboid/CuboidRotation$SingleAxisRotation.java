/*    */ package net.minecraft.client.resources.model.cuboid;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public final class SingleAxisRotation extends Record implements CuboidRotation.RotationValue {
/*    */   private final Direction.Axis axis;
/*    */   
/*    */   private final float angle;
/*    */   
/*    */   public SingleAxisRotation(Direction.Axis axis, float angle) {
/* 66 */     this.axis = axis;
/* 66 */     this.angle = angle;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$SingleAxisRotation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$SingleAxisRotation;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$SingleAxisRotation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$SingleAxisRotation;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$SingleAxisRotation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$SingleAxisRotation;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Direction.Axis axis() {
/* 66 */     return this.axis;
/*    */   }
/*    */   
/*    */   public float angle() {
/* 66 */     return this.angle;
/*    */   }
/*    */   
/*    */   public Matrix4f transformation() {
/* 72 */     Matrix4f result = new Matrix4f();
/* 73 */     if (this.angle == 0.0F)
/* 74 */       return result; 
/* 76 */     Vector3fc rotateAround = this.axis.getPositive().getUnitVec3f();
/* 77 */     result.rotation(this.angle * 0.017453292F, rotateAround);
/* 78 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\CuboidRotation$SingleAxisRotation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */