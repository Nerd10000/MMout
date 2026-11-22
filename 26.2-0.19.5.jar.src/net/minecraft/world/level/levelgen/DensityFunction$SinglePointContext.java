/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ public final class SinglePointContext extends Record implements DensityFunction.FunctionContext {
/*     */   private final int blockX;
/*     */   
/*     */   private final int blockY;
/*     */   
/*     */   private final int blockZ;
/*     */   
/*     */   public SinglePointContext(int blockX, int blockY, int blockZ) {
/* 106 */     this.blockX = blockX;
/* 106 */     this.blockY = blockY;
/* 106 */     this.blockZ = blockZ;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #106	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #106	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #106	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int blockX() {
/* 106 */     return this.blockX;
/*     */   }
/*     */   
/*     */   public int blockY() {
/* 106 */     return this.blockY;
/*     */   }
/*     */   
/*     */   public int blockZ() {
/* 106 */     return this.blockZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunction$SinglePointContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */