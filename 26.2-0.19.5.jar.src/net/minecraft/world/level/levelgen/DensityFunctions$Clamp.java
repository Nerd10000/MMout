/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class Clamp extends Record implements DensityFunctions.PureTransformer {
/*     */   private final DensityFunction input;
/*     */   
/*     */   private final double minValue;
/*     */   
/*     */   private final double maxValue;
/*     */   
/*     */   private static final MapCodec<Clamp> DATA_CODEC;
/*     */   
/*     */   protected Clamp(DensityFunction input, double minValue, double maxValue) {
/* 868 */     this.input = input;
/* 868 */     this.minValue = minValue;
/* 868 */     this.maxValue = maxValue;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #868	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #868	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #868	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DensityFunction input() {
/* 868 */     return this.input;
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 868 */     return this.minValue;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 868 */     return this.maxValue;
/*     */   }
/*     */   
/*     */   static {
/* 869 */     DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.CODEC.fieldOf("input").forGetter(Clamp::input), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("min").forGetter(Clamp::minValue), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("max").forGetter(Clamp::maxValue)).apply((Applicative)i, Clamp::new));
/*     */   }
/*     */   
/* 875 */   public static final KeyDispatchDataCodec<Clamp> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*     */   
/*     */   public double transform(double input) {
/* 879 */     return Mth.clamp(input, this.minValue, this.maxValue);
/*     */   }
/*     */   
/*     */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 884 */     return new Clamp(visitor.apply(this.input), this.minValue, this.maxValue);
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 889 */     return (KeyDispatchDataCodec)CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$Clamp.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */