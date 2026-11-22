/*     */ package net.minecraft.util;
/*     */ 
/*     */ final class CubicCurve extends Record {
/*     */   private final float a;
/*     */   
/*     */   private final float b;
/*     */   
/*     */   private final float c;
/*     */   
/*     */   private CubicCurve(float a, float b, float c) {
/* 159 */     this.a = a;
/* 159 */     this.b = b;
/* 159 */     this.c = c;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #159	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #159	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #159	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float a() {
/* 159 */     return this.a;
/*     */   }
/*     */   
/*     */   public float b() {
/* 159 */     return this.b;
/*     */   }
/*     */   
/*     */   public float c() {
/* 159 */     return this.c;
/*     */   }
/*     */   
/*     */   public float sample(float t) {
/* 161 */     return ((this.a * t + this.b) * t + this.c) * t;
/*     */   }
/*     */   
/*     */   public float sampleGradient(float t) {
/* 165 */     return (3.0F * this.a * t + 2.0F * this.b) * t + this.c;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\EasingType$CubicBezier$CubicCurve.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */