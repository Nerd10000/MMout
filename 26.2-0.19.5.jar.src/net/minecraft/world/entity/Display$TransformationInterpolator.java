/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.math.Transformation;
/*     */ 
/*     */ final class TransformationInterpolator extends Record implements Display.GenericInterpolator<Transformation> {
/*     */   private final Transformation previous;
/*     */   
/*     */   private final Transformation current;
/*     */   
/*     */   private TransformationInterpolator(Transformation previous, Transformation current) {
/* 931 */     this.previous = previous;
/* 931 */     this.current = current;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TransformationInterpolator;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #931	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$TransformationInterpolator;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TransformationInterpolator;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #931	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$TransformationInterpolator;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TransformationInterpolator;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #931	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/Display$TransformationInterpolator;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Transformation previous() {
/* 931 */     return this.previous;
/*     */   }
/*     */   
/*     */   public Transformation current() {
/* 931 */     return this.current;
/*     */   }
/*     */   
/*     */   public Transformation get(float progress) {
/* 934 */     if (progress >= 1.0D)
/* 935 */       return this.current; 
/* 937 */     return this.previous.slerp(this.current, progress);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$TransformationInterpolator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */