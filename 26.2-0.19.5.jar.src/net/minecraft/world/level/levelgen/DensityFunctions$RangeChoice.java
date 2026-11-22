/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ 
/*     */ final class RangeChoice extends Record implements DensityFunction {
/*     */   private final DensityFunction input;
/*     */   
/*     */   private final double minInclusive;
/*     */   
/*     */   private final double maxExclusive;
/*     */   
/*     */   private final DensityFunction whenInRange;
/*     */   
/*     */   private final DensityFunction whenOutOfRange;
/*     */   
/*     */   public static final MapCodec<RangeChoice> DATA_CODEC;
/*     */   
/*     */   private RangeChoice(DensityFunction input, double minInclusive, double maxExclusive, DensityFunction whenInRange, DensityFunction whenOutOfRange) {
/* 662 */     this.input = input;
/* 662 */     this.minInclusive = minInclusive;
/* 662 */     this.maxExclusive = maxExclusive;
/* 662 */     this.whenInRange = whenInRange;
/* 662 */     this.whenOutOfRange = whenOutOfRange;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #662	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #662	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #662	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DensityFunction input() {
/* 662 */     return this.input;
/*     */   }
/*     */   
/*     */   public double minInclusive() {
/* 662 */     return this.minInclusive;
/*     */   }
/*     */   
/*     */   public double maxExclusive() {
/* 662 */     return this.maxExclusive;
/*     */   }
/*     */   
/*     */   public DensityFunction whenInRange() {
/* 662 */     return this.whenInRange;
/*     */   }
/*     */   
/*     */   public DensityFunction whenOutOfRange() {
/* 662 */     return this.whenOutOfRange;
/*     */   }
/*     */   
/*     */   static {
/* 663 */     DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.CODEC.fieldOf("input").forGetter(RangeChoice::input), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("min_inclusive").forGetter(RangeChoice::minInclusive), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("max_exclusive").forGetter(RangeChoice::maxExclusive), (App)DensityFunction.CODEC.fieldOf("when_in_range").forGetter(RangeChoice::whenInRange), (App)DensityFunction.CODEC.fieldOf("when_out_of_range").forGetter(RangeChoice::whenOutOfRange)).apply((Applicative)i, RangeChoice::new));
/*     */   }
/*     */   
/* 671 */   public static final KeyDispatchDataCodec<RangeChoice> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 675 */     double inputValue = this.input.compute(context);
/* 676 */     if (inputValue >= this.minInclusive && inputValue < this.maxExclusive)
/* 677 */       return this.whenInRange.compute(context); 
/* 679 */     return this.whenOutOfRange.compute(context);
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 684 */     this.input.fillArray(output, contextProvider);
/* 685 */     for (int i = 0; i < output.length; i++) {
/* 686 */       double v = output[i];
/* 687 */       if (v >= this.minInclusive && v < this.maxExclusive) {
/* 688 */         output[i] = this.whenInRange.compute(contextProvider.forIndex(i));
/*     */       } else {
/* 690 */         output[i] = this.whenOutOfRange.compute(contextProvider.forIndex(i));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 697 */     return new RangeChoice(visitor.apply(this.input), this.minInclusive, this.maxExclusive, visitor.apply(this.whenInRange), visitor.apply(this.whenOutOfRange));
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 702 */     return Math.min(this.whenInRange.minValue(), this.whenOutOfRange.minValue());
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 707 */     return Math.max(this.whenInRange.maxValue(), this.whenOutOfRange.maxValue());
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 712 */     return (KeyDispatchDataCodec)CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$RangeChoice.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */