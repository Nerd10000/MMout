/*    */ package net.minecraft.client.resources.model.cuboid;
/*    */ 
/*    */ import com.mojang.math.MatrixUtil;
/*    */ import net.minecraft.core.Direction;
/*    */ import org.joml.Math;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public final class CuboidRotation extends Record {
/*    */   private final Vector3fc origin;
/*    */   
/*    */   private final RotationValue value;
/*    */   
/*    */   private final boolean rescale;
/*    */   
/*    */   private final Matrix4fc transform;
/*    */   
/*    */   public CuboidRotation(Vector3fc origin, RotationValue value, boolean rescale, Matrix4fc transform) {
/* 12 */     this.origin = origin;
/* 12 */     this.value = value;
/* 12 */     this.rescale = rescale;
/* 12 */     this.transform = transform;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vector3fc origin() {
/* 12 */     return this.origin;
/*    */   }
/*    */   
/*    */   public RotationValue value() {
/* 12 */     return this.value;
/*    */   }
/*    */   
/*    */   public boolean rescale() {
/* 12 */     return this.rescale;
/*    */   }
/*    */   
/*    */   public Matrix4fc transform() {
/* 12 */     return this.transform;
/*    */   }
/*    */   
/*    */   public CuboidRotation(Vector3fc origin, RotationValue value, boolean rescale) {
/* 19 */     this(origin, value, rescale, (Matrix4fc)computeTransform(value, rescale));
/*    */   }
/*    */   
/*    */   private static Matrix4f computeTransform(RotationValue value, boolean rescale) {
/* 23 */     Matrix4f result = value.transformation();
/* 24 */     if (rescale && !MatrixUtil.isIdentity((Matrix4fc)result)) {
/* 25 */       Vector3fc scale = computeRescale((Matrix4fc)result);
/* 26 */       result.scale(scale);
/*    */     } 
/* 28 */     return result;
/*    */   }
/*    */   
/*    */   private static Vector3fc computeRescale(Matrix4fc rotation) {
/* 40 */     Vector3f scratch = new Vector3f();
/* 41 */     float scaleX = scaleFactorForAxis(rotation, Direction.Axis.X, scratch);
/* 42 */     float scaleY = scaleFactorForAxis(rotation, Direction.Axis.Y, scratch);
/* 43 */     float scaleZ = scaleFactorForAxis(rotation, Direction.Axis.Z, scratch);
/* 44 */     return (Vector3fc)scratch.set(scaleX, scaleY, scaleZ);
/*    */   }
/*    */   
/*    */   private static float scaleFactorForAxis(Matrix4fc rotation, Direction.Axis axis, Vector3f scratch) {
/* 52 */     Vector3f axisUnit = scratch.set(axis.getPositive().getUnitVec3f());
/* 53 */     Vector3f transformedAxisUnit = rotation.transformDirection(axisUnit);
/* 54 */     float absX = Math.abs(transformedAxisUnit.x);
/* 55 */     float absY = Math.abs(transformedAxisUnit.y);
/* 56 */     float absZ = Math.abs(transformedAxisUnit.z);
/* 58 */     float maxComponent = Math.max(Math.max(absX, absY), absZ);
/* 59 */     return 1.0F / maxComponent;
/*    */   }
/*    */   
/*    */   public static final class SingleAxisRotation extends Record implements RotationValue {
/*    */     private final Direction.Axis axis;
/*    */     
/*    */     private final float angle;
/*    */     
/*    */     public SingleAxisRotation(Direction.Axis axis, float angle) {
/* 66 */       this.axis = axis;
/* 66 */       this.angle = angle;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$SingleAxisRotation;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$SingleAxisRotation;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$SingleAxisRotation;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$SingleAxisRotation;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$SingleAxisRotation;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #66	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$SingleAxisRotation;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Direction.Axis axis() {
/* 66 */       return this.axis;
/*    */     }
/*    */     
/*    */     public float angle() {
/* 66 */       return this.angle;
/*    */     }
/*    */     
/*    */     public Matrix4f transformation() {
/* 72 */       Matrix4f result = new Matrix4f();
/* 73 */       if (this.angle == 0.0F)
/* 74 */         return result; 
/* 76 */       Vector3fc rotateAround = this.axis.getPositive().getUnitVec3f();
/* 77 */       result.rotation(this.angle * 0.017453292F, rotateAround);
/* 78 */       return result;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class EulerXYZRotation extends Record implements RotationValue {
/*    */     private final float x;
/*    */     
/*    */     private final float y;
/*    */     
/*    */     private final float z;
/*    */     
/*    */     public EulerXYZRotation(float x, float y, float z) {
/* 82 */       this.x = x;
/* 82 */       this.y = y;
/* 82 */       this.z = z;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$EulerXYZRotation;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #82	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$EulerXYZRotation;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$EulerXYZRotation;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #82	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$EulerXYZRotation;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$EulerXYZRotation;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #82	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidRotation$EulerXYZRotation;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public float x() {
/* 82 */       return this.x;
/*    */     }
/*    */     
/*    */     public float y() {
/* 82 */       return this.y;
/*    */     }
/*    */     
/*    */     public float z() {
/* 82 */       return this.z;
/*    */     }
/*    */     
/*    */     public Matrix4f transformation() {
/* 89 */       return (new Matrix4f()).rotationZYX(this.z * 0.017453292F, this.y * 0.017453292F, this.x * 0.017453292F);
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface RotationValue {
/*    */     Matrix4f transformation();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\CuboidRotation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */