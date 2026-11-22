/*      */ package net.minecraft.world.level.levelgen;
/*      */ 
/*      */ import java.util.Arrays;
/*      */ import java.util.function.Function;
/*      */ import net.minecraft.util.KeyDispatchDataCodec;
/*      */ 
/*      */ final class Constant extends Record implements DensityFunction.SimpleFunction {
/*      */   private final double value;
/*      */   
/*      */   private Constant(double value) {
/* 1292 */     this.value = value;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1292	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1292	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1292	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public double value() {
/* 1292 */     return this.value;
/*      */   }
/*      */   
/* 1293 */   private static final KeyDispatchDataCodec<Constant> CODEC = DensityFunctions.singleArgumentCodec(DensityFunctions.NOISE_VALUE_CODEC, Constant::new, Constant::value);
/*      */   
/* 1294 */   private static final Constant ZERO = new Constant(0.0D);
/*      */   
/*      */   public double compute(DensityFunction.FunctionContext context) {
/* 1298 */     return this.value;
/*      */   }
/*      */   
/*      */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 1303 */     Arrays.fill(output, this.value);
/*      */   }
/*      */   
/*      */   public double minValue() {
/* 1308 */     return this.value;
/*      */   }
/*      */   
/*      */   public double maxValue() {
/* 1313 */     return this.value;
/*      */   }
/*      */   
/*      */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1318 */     return (KeyDispatchDataCodec)CODEC;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$Constant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */