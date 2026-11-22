/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class LifetimeAlpha extends Record {
/*     */   private final float startAlpha;
/*     */   
/*     */   private final float endAlpha;
/*     */   
/*     */   private final float startAtNormalizedAge;
/*     */   
/*     */   private final float endAtNormalizedAge;
/*     */   
/*     */   public LifetimeAlpha(float startAlpha, float endAlpha, float startAtNormalizedAge, float endAtNormalizedAge) {
/* 224 */     this.startAlpha = startAlpha;
/* 224 */     this.endAlpha = endAlpha;
/* 224 */     this.startAtNormalizedAge = startAtNormalizedAge;
/* 224 */     this.endAtNormalizedAge = endAtNormalizedAge;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/Particle$LifetimeAlpha;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #224	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/particle/Particle$LifetimeAlpha;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/Particle$LifetimeAlpha;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #224	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/particle/Particle$LifetimeAlpha;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/Particle$LifetimeAlpha;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #224	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/particle/Particle$LifetimeAlpha;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float startAlpha() {
/* 224 */     return this.startAlpha;
/*     */   }
/*     */   
/*     */   public float endAlpha() {
/* 224 */     return this.endAlpha;
/*     */   }
/*     */   
/*     */   public float startAtNormalizedAge() {
/* 224 */     return this.startAtNormalizedAge;
/*     */   }
/*     */   
/*     */   public float endAtNormalizedAge() {
/* 224 */     return this.endAtNormalizedAge;
/*     */   }
/*     */   
/* 225 */   public static final LifetimeAlpha ALWAYS_OPAQUE = new LifetimeAlpha(1.0F, 1.0F, 0.0F, 1.0F);
/*     */   
/*     */   public boolean isOpaque() {
/* 228 */     return (this.startAlpha >= 1.0F && this.endAlpha >= 1.0F);
/*     */   }
/*     */   
/*     */   public float currentAlphaForAge(int age, int lifetime, float partialTickTime) {
/* 232 */     if (Mth.equal(this.startAlpha, this.endAlpha))
/* 233 */       return this.startAlpha; 
/* 236 */     float timeNormalized = Mth.inverseLerp((age + partialTickTime) / lifetime, this.startAtNormalizedAge, this.endAtNormalizedAge);
/* 237 */     return Mth.clampedLerp(timeNormalized, this.startAlpha, this.endAlpha);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\Particle$LifetimeAlpha.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */