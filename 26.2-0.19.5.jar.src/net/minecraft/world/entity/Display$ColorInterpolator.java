/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ final class ColorInterpolator extends Record implements Display.IntInterpolator {
/*     */   private final int previous;
/*     */   
/*     */   private final int current;
/*     */   
/*     */   private ColorInterpolator(int previous, int current) {
/* 957 */     this.previous = previous;
/* 957 */     this.current = current;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$ColorInterpolator;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #957	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$ColorInterpolator;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$ColorInterpolator;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #957	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$ColorInterpolator;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$ColorInterpolator;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #957	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/Display$ColorInterpolator;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int previous() {
/* 957 */     return this.previous;
/*     */   }
/*     */   
/*     */   public int current() {
/* 957 */     return this.current;
/*     */   }
/*     */   
/*     */   public int get(float progress) {
/* 960 */     return ARGB.srgbLerp(progress, this.previous, this.current);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$ColorInterpolator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */