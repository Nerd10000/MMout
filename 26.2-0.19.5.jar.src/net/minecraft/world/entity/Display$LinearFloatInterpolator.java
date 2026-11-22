/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ final class LinearFloatInterpolator extends Record implements Display.FloatInterpolator {
/*     */   private final float previous;
/*     */   
/*     */   private final float current;
/*     */   
/*     */   private LinearFloatInterpolator(float previous, float current) {
/* 973 */     this.previous = previous;
/* 973 */     this.current = current;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #973	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #973	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #973	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float previous() {
/* 973 */     return this.previous;
/*     */   }
/*     */   
/*     */   public float current() {
/* 973 */     return this.current;
/*     */   }
/*     */   
/*     */   public float get(float progress) {
/* 976 */     return Mth.lerp(progress, this.previous, this.current);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$LinearFloatInterpolator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */