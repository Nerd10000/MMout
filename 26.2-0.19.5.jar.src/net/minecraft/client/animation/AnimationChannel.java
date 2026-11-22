/*    */ package net.minecraft.client.animation;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public final class AnimationChannel extends Record {
/*    */   private final Target target;
/*    */   
/*    */   private final Keyframe[] keyframes;
/*    */   
/*    */   public AnimationChannel(Target target, Keyframe... keyframes) {
/*  8 */     this.target = target;
/*  8 */     this.keyframes = keyframes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/animation/AnimationChannel;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/animation/AnimationChannel;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/animation/AnimationChannel;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/animation/AnimationChannel;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/animation/AnimationChannel;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/animation/AnimationChannel;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Target target() {
/*  8 */     return this.target;
/*    */   }
/*    */   
/*    */   public Keyframe[] keyframes() {
/*  8 */     return this.keyframes;
/*    */   }
/*    */   
/*    */   public static interface Interpolation {
/*    */     Vector3f apply(Vector3f param1Vector3f, float param1Float1, Keyframe[] param1ArrayOfKeyframe, int param1Int1, int param1Int2, float param1Float2);
/*    */   }
/*    */   
/*    */   public static class Targets {
/* 22 */     public static final AnimationChannel.Target POSITION = ModelPart::offsetPos;
/*    */     
/* 23 */     public static final AnimationChannel.Target ROTATION = ModelPart::offsetRotation;
/*    */     
/* 24 */     public static final AnimationChannel.Target SCALE = ModelPart::offsetScale;
/*    */   }
/*    */   
/*    */   public static class Interpolations {
/*    */     public static final AnimationChannel.Interpolation LINEAR;
/*    */     
/*    */     public static final AnimationChannel.Interpolation CATMULLROM;
/*    */     
/*    */     static {
/* 28 */       LINEAR = ((vector, alpha, keyframes, prev, next, targetScale) -> {
/*    */           Vector3fc point0 = keyframes[prev].postTarget();
/*    */           Vector3fc point1 = keyframes[next].preTarget();
/*    */           return point0.lerp(point1, alpha, vector).mul(targetScale);
/*    */         });
/* 35 */       CATMULLROM = ((vector, alpha, keyframes, prev, next, targetScale) -> {
/*    */           Vector3fc point0 = keyframes[Math.max(0, prev - 1)].postTarget();
/*    */           Vector3fc point1 = keyframes[prev].postTarget();
/*    */           Vector3fc point2 = keyframes[next].postTarget();
/*    */           Vector3fc point3 = keyframes[Math.min(keyframes.length - 1, next + 1)].postTarget();
/*    */           vector.set(Mth.catmullrom(alpha, point0.x(), point1.x(), point2.x(), point3.x()) * targetScale, Mth.catmullrom(alpha, point0.y(), point1.y(), point2.y(), point3.y()) * targetScale, Mth.catmullrom(alpha, point0.z(), point1.z(), point2.z(), point3.z()) * targetScale);
/*    */           return vector;
/*    */         });
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface Target {
/*    */     void apply(ModelPart param1ModelPart, Vector3f param1Vector3f);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\animation\AnimationChannel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */