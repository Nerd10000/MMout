/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ 
/*     */ @VisibleForDebug
/*     */ public final class HolderHolder extends Record implements DensityFunction {
/*     */   private final Holder<DensityFunction> function;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #393	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #393	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #393	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Holder<DensityFunction> function() {
/* 393 */     return this.function;
/*     */   }
/*     */   
/*     */   public HolderHolder(Holder<DensityFunction> function) {
/* 394 */     this.function = function;
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 397 */     return ((DensityFunction)this.function.value()).compute(context);
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 402 */     ((DensityFunction)this.function.value()).fillArray(output, contextProvider);
/*     */   }
/*     */   
/*     */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 408 */     return new HolderHolder(Holder.direct(visitor.apply((DensityFunction)this.function.value())));
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 413 */     return this.function.isBound() ? ((DensityFunction)this.function.value()).minValue() : Double.NEGATIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 418 */     return this.function.isBound() ? ((DensityFunction)this.function.value()).maxValue() : Double.POSITIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 424 */     throw new UnsupportedOperationException("Calling .codec() on HolderHolder");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$HolderHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */