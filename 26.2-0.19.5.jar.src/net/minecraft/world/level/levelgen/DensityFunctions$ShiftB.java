/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ 
/*     */ public final class ShiftB extends Record implements DensityFunctions.ShiftNoise {
/*     */   private final DensityFunction.NoiseHolder offsetNoise;
/*     */   
/*     */   protected ShiftB(DensityFunction.NoiseHolder offsetNoise) {
/* 830 */     this.offsetNoise = offsetNoise;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #830	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #830	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #830	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DensityFunction.NoiseHolder offsetNoise() {
/* 830 */     return this.offsetNoise;
/*     */   }
/*     */   
/* 831 */   private static final KeyDispatchDataCodec<ShiftB> CODEC = DensityFunctions.singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, ShiftB::new, ShiftB::offsetNoise);
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 835 */     return compute(context.blockZ(), context.blockX(), 0.0D);
/*     */   }
/*     */   
/*     */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 840 */     return new ShiftB(visitor.visitNoise(this.offsetNoise));
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 845 */     return (KeyDispatchDataCodec)CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$ShiftB.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */