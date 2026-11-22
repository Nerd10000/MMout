/*      */ package net.minecraft.world.level.levelgen;
/*      */ 
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.kinds.Applicative;
/*      */ import com.mojang.datafixers.util.Function4;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import net.minecraft.util.KeyDispatchDataCodec;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ 
/*      */ final class YClampedGradient extends Record implements DensityFunction.SimpleFunction {
/*      */   private final int fromY;
/*      */   
/*      */   private final int toY;
/*      */   
/*      */   private final double fromValue;
/*      */   
/*      */   private final double toValue;
/*      */   
/*      */   private static final MapCodec<YClampedGradient> DATA_CODEC;
/*      */   
/*      */   private YClampedGradient(int fromY, int toY, double fromValue, double toValue) {
/* 1322 */     this.fromY = fromY;
/* 1322 */     this.toY = toY;
/* 1322 */     this.fromValue = fromValue;
/* 1322 */     this.toValue = toValue;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1322	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1322	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1322	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public int fromY() {
/* 1322 */     return this.fromY;
/*      */   }
/*      */   
/*      */   public int toY() {
/* 1322 */     return this.toY;
/*      */   }
/*      */   
/*      */   public double fromValue() {
/* 1322 */     return this.fromValue;
/*      */   }
/*      */   
/*      */   public double toValue() {
/* 1322 */     return this.toValue;
/*      */   }
/*      */   
/*      */   static {
/* 1323 */     DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("from_y").forGetter(YClampedGradient::fromY), (App)Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("to_y").forGetter(YClampedGradient::toY), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("from_value").forGetter(YClampedGradient::fromValue), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("to_value").forGetter(YClampedGradient::toValue)).apply((Applicative)i, YClampedGradient::new));
/*      */   }
/*      */   
/* 1330 */   public static final KeyDispatchDataCodec<YClampedGradient> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */   
/*      */   public double compute(DensityFunction.FunctionContext context) {
/* 1334 */     return Mth.clampedMap(context.blockY(), this.fromY, this.toY, this.fromValue, this.toValue);
/*      */   }
/*      */   
/*      */   public double minValue() {
/* 1339 */     return Math.min(this.fromValue, this.toValue);
/*      */   }
/*      */   
/*      */   public double maxValue() {
/* 1344 */     return Math.max(this.fromValue, this.toValue);
/*      */   }
/*      */   
/*      */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1349 */     return (KeyDispatchDataCodec)CODEC;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$YClampedGradient.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */