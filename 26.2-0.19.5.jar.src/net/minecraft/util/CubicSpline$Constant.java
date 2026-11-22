/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ @VisibleForDebug
/*     */ public final class Constant<I> extends Record implements CubicSpline<I> {
/*     */   private final float value;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/CubicSpline$Constant;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #288	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Constant;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Constant<TI;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/CubicSpline$Constant;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #288	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Constant;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Constant<TI;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/CubicSpline$Constant;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #288	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Constant;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Constant<TI;>;
/*     */   }
/*     */   
/*     */   public float value() {
/* 288 */     return this.value;
/*     */   }
/*     */   
/*     */   public Constant(float value) {
/* 289 */     this.value = value;
/*     */   }
/*     */   
/*     */   public String parityString() {
/* 292 */     return String.format(Locale.ROOT, "k=%.3f", new Object[] { Float.valueOf(this.value) });
/*     */   }
/*     */   
/*     */   public float minValue() {
/* 297 */     return this.value;
/*     */   }
/*     */   
/*     */   public float maxValue() {
/* 302 */     return this.value;
/*     */   }
/*     */   
/*     */   public CubicSpline<I> mapCoordinates(UnaryOperator<I> mapper) {
/* 307 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\CubicSpline$Constant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */