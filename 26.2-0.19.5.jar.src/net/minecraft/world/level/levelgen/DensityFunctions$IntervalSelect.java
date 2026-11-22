/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.collect.Comparators;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ 
/*     */ final class IntervalSelect extends Record implements DensityFunction {
/*     */   private final DensityFunction input;
/*     */   
/*     */   private final DoubleList thresholds;
/*     */   
/*     */   private final List<DensityFunction> functions;
/*     */   
/*     */   private IntervalSelect(DensityFunction input, DoubleList thresholds, List<DensityFunction> functions) {
/* 716 */     this.input = input;
/* 716 */     this.thresholds = thresholds;
/* 716 */     this.functions = functions;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$IntervalSelect;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #716	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$IntervalSelect;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$IntervalSelect;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #716	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$IntervalSelect;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$IntervalSelect;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #716	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$IntervalSelect;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DensityFunction input() {
/* 716 */     return this.input;
/*     */   }
/*     */   
/*     */   public DoubleList thresholds() {
/* 716 */     return this.thresholds;
/*     */   }
/*     */   
/*     */   public List<DensityFunction> functions() {
/* 716 */     return this.functions;
/*     */   }
/*     */   
/* 717 */   private static final Codec<DoubleList> THRESHOLDS_CODEC = DensityFunctions.NOISE_VALUE_CODEC.listOf().xmap(it.unimi.dsi.fastutil.doubles.DoubleArrayList::new, Function.identity());
/*     */   
/*     */   public static final MapCodec<IntervalSelect> DATA_CODEC;
/*     */   
/*     */   static {
/* 723 */     DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.CODEC.fieldOf("input").forGetter(IntervalSelect::input), (App)THRESHOLDS_CODEC.fieldOf("thresholds").forGetter(IntervalSelect::thresholds), (App)DensityFunction.CODEC.listOf(2, 2147483647).fieldOf("functions").forGetter(IntervalSelect::functions)).apply((Applicative)i, IntervalSelect::new)).validate(IntervalSelect::validate);
/*     */   }
/*     */   
/* 725 */   public static final KeyDispatchDataCodec<IntervalSelect> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*     */   
/*     */   private DataResult<IntervalSelect> validate() {
/* 728 */     if (this.thresholds.size() != this.functions.size() - 1)
/* 729 */       return DataResult.error(() -> "Expected " + this.functions.size() - 1 + " thresholds for " + this.functions.size() + " functions, but got " + this.thresholds.size()); 
/* 731 */     if (!Comparators.isInOrder((Iterable)this.thresholds, Double::compare))
/* 732 */       return DataResult.error(() -> "Threshold values must be ordered from smallest to largest"); 
/* 734 */     return DataResult.success(this);
/*     */   }
/*     */   
/*     */   private double compute(DensityFunction.FunctionContext context, double input) {
/* 738 */     for (int i = 0; i < this.thresholds.size(); i++) {
/* 739 */       if (input < this.thresholds.getDouble(i))
/* 740 */         return ((DensityFunction)this.functions.get(i)).compute(context); 
/*     */     } 
/* 743 */     return ((DensityFunction)this.functions.getLast()).compute(context);
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 748 */     return compute(context, this.input.compute(context));
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 753 */     this.input.fillArray(output, contextProvider);
/* 754 */     for (int i = 0; i < output.length; i++)
/* 755 */       output[i] = compute(contextProvider.forIndex(i), output[i]); 
/*     */   }
/*     */   
/*     */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 761 */     Objects.requireNonNull(visitor);
/* 761 */     return new IntervalSelect(visitor.apply(this.input), this.thresholds, List.copyOf(Lists.transform(this.functions, visitor::apply)));
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 766 */     double minValue = Double.MAX_VALUE;
/* 767 */     for (DensityFunction function : this.functions)
/* 768 */       minValue = Math.min(function.minValue(), minValue); 
/* 770 */     return minValue;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 775 */     double maxValue = -1.7976931348623157E308D;
/* 776 */     for (DensityFunction function : this.functions)
/* 777 */       maxValue = Math.max(function.maxValue(), maxValue); 
/* 779 */     return maxValue;
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<IntervalSelect> codec() {
/* 784 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$IntervalSelect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */