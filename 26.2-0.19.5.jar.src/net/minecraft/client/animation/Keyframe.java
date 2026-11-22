/*    */ package net.minecraft.client.animation;
/*    */ 
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public final class Keyframe extends Record {
/*    */   private final float timestamp;
/*    */   
/*    */   private final Vector3fc preTarget;
/*    */   
/*    */   private final Vector3fc postTarget;
/*    */   
/*    */   private final AnimationChannel.Interpolation interpolation;
/*    */   
/*    */   public AnimationChannel.Interpolation interpolation() {
/*  5 */     return this.interpolation;
/*    */   }
/*    */   
/*    */   public Vector3fc postTarget() {
/*  5 */     return this.postTarget;
/*    */   }
/*    */   
/*    */   public Vector3fc preTarget() {
/*  5 */     return this.preTarget;
/*    */   }
/*    */   
/*    */   public float timestamp() {
/*  5 */     return this.timestamp;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/animation/Keyframe;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/animation/Keyframe;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/animation/Keyframe;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/animation/Keyframe;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/animation/Keyframe;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/animation/Keyframe;
/*    */   }
/*    */   
/*    */   public Keyframe(float timestamp, Vector3fc preTarget, Vector3fc postTarget, AnimationChannel.Interpolation interpolation) {
/*  5 */     this.timestamp = timestamp;
/*  5 */     this.preTarget = preTarget;
/*  5 */     this.postTarget = postTarget;
/*  5 */     this.interpolation = interpolation;
/*    */   }
/*    */   
/*    */   public Keyframe(float timestamp, Vector3fc postTarget, AnimationChannel.Interpolation interpolation) {
/* 12 */     this(timestamp, postTarget, postTarget, interpolation);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\animation\Keyframe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */