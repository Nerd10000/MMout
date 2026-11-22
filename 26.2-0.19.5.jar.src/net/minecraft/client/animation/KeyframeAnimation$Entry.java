/*     */ package net.minecraft.client.animation;
/*     */ 
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ final class Entry extends Record {
/*     */   private final ModelPart part;
/*     */   
/*     */   private final AnimationChannel.Target target;
/*     */   
/*     */   private final Keyframe[] keyframes;
/*     */   
/*     */   private Entry(ModelPart part, AnimationChannel.Target target, Keyframe[] keyframes) {
/*  81 */     this.part = part;
/*  81 */     this.target = target;
/*  81 */     this.keyframes = keyframes;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/animation/KeyframeAnimation$Entry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #81	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/animation/KeyframeAnimation$Entry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/animation/KeyframeAnimation$Entry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #81	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/animation/KeyframeAnimation$Entry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/animation/KeyframeAnimation$Entry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #81	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/animation/KeyframeAnimation$Entry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ModelPart part() {
/*  81 */     return this.part;
/*     */   }
/*     */   
/*     */   public AnimationChannel.Target target() {
/*  81 */     return this.target;
/*     */   }
/*     */   
/*     */   public Keyframe[] keyframes() {
/*  81 */     return this.keyframes;
/*     */   }
/*     */   
/*     */   public void apply(float secondsSinceStart, float targetScale, Vector3f scratchVector) {
/*     */     float lerpAlpha;
/*  88 */     int prev = Math.max(0, Mth.binarySearch(0, this.keyframes.length, i -> (secondsSinceStart <= this.keyframes[i].timestamp())) - 1);
/*  89 */     int next = Math.min(this.keyframes.length - 1, prev + 1);
/*  91 */     Keyframe previousFrame = this.keyframes[prev];
/*  92 */     Keyframe nextFrame = this.keyframes[next];
/*  94 */     float keyframeTimeDelta = secondsSinceStart - previousFrame.timestamp();
/*  96 */     if (next != prev) {
/*  97 */       lerpAlpha = Mth.clamp(keyframeTimeDelta / (nextFrame.timestamp() - previousFrame.timestamp()), 0.0F, 1.0F);
/*     */     } else {
/*  99 */       lerpAlpha = 0.0F;
/*     */     } 
/* 102 */     nextFrame.interpolation().apply(scratchVector, lerpAlpha, this.keyframes, prev, next, targetScale);
/* 103 */     this.target.apply(this.part, scratchVector);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\animation\KeyframeAnimation$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */