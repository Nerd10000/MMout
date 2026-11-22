/*      */ package net.minecraft.world.level.levelgen;
/*      */ 
/*      */ import com.google.common.collect.Comparators;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.kinds.Applicative;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import com.mojang.datafixers.util.Function3;
/*      */ import com.mojang.datafixers.util.Function4;
/*      */ import com.mojang.datafixers.util.Function5;
/*      */ import com.mojang.datafixers.util.Function6;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Function;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.util.BoundedFloatFunction;
/*      */ import net.minecraft.util.CubicSpline;
/*      */ import net.minecraft.util.ExtraCodecs;
/*      */ import net.minecraft.util.KeyDispatchDataCodec;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.StringRepresentable;
/*      */ import net.minecraft.util.VisibleForDebug;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.levelgen.synth.BlendedNoise;
/*      */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*      */ import net.minecraft.world.level.levelgen.synth.SimplexNoise;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public final class DensityFunctions {
/*      */   private static final Codec<DensityFunction> CODEC;
/*      */   
/*      */   static final double MAX_REASONABLE_NOISE_VALUE = 1000000.0D;
/*      */   
/*      */   static {
/*   37 */     CODEC = BuiltInRegistries.DENSITY_FUNCTION_TYPE.byNameCodec().dispatch(function -> function.codec().codec(), Function.identity());
/*      */   }
/*      */   
/*   42 */   private static final Codec<Double> NOISE_VALUE_CODEC = Codec.doubleRange(-1000000.0D, 1000000.0D);
/*      */   
/*      */   public static final Codec<DensityFunction> DIRECT_CODEC;
/*      */   
/*      */   static {
/*   47 */     DIRECT_CODEC = Codec.either(NOISE_VALUE_CODEC, CODEC).xmap(either -> (DensityFunction)either.map(DensityFunctions::constant, Function.identity()), function -> {
/*      */           if (function instanceof Constant) {
/*      */             Constant constant = (Constant)function;
/*      */             return Either.left(Double.valueOf(constant.value()));
/*      */           } 
/*      */           return Either.right(function);
/*      */         });
/*      */   }
/*      */   
/*      */   public static MapCodec<? extends DensityFunction> bootstrap(Registry<MapCodec<? extends DensityFunction>> registry) {
/*   55 */     register(registry, "blend_alpha", BlendAlpha.CODEC);
/*   56 */     register(registry, "blend_offset", BlendOffset.CODEC);
/*   57 */     register(registry, "beardifier", BeardifierMarker.CODEC);
/*   58 */     register(registry, "old_blended_noise", BlendedNoise.CODEC);
/*   59 */     for (Marker.Type value : Marker.Type.values())
/*   60 */       register(registry, value.getSerializedName(), (KeyDispatchDataCodec)value.codec); 
/*   62 */     register(registry, "noise", (KeyDispatchDataCodec)Noise.CODEC);
/*   63 */     register(registry, "end_islands", (KeyDispatchDataCodec)EndIslandDensityFunction.CODEC);
/*   64 */     register(registry, "shifted_noise", (KeyDispatchDataCodec)ShiftedNoise.CODEC);
/*   65 */     register(registry, "range_choice", (KeyDispatchDataCodec)RangeChoice.CODEC);
/*   66 */     register(registry, "interval_select", (KeyDispatchDataCodec)IntervalSelect.CODEC);
/*   67 */     register(registry, "shift_a", (KeyDispatchDataCodec)ShiftA.CODEC);
/*   68 */     register(registry, "shift_b", (KeyDispatchDataCodec)ShiftB.CODEC);
/*   69 */     register(registry, "shift", (KeyDispatchDataCodec)Shift.CODEC);
/*   70 */     register(registry, "clamp", (KeyDispatchDataCodec)Clamp.CODEC);
/*   71 */     for (Mapped.Type value : Mapped.Type.values())
/*   72 */       register(registry, value.getSerializedName(), (KeyDispatchDataCodec)value.codec); 
/*   74 */     for (TwoArgumentSimpleFunction.Type value : TwoArgumentSimpleFunction.Type.values())
/*   75 */       register(registry, value.getSerializedName(), (KeyDispatchDataCodec)value.codec); 
/*   77 */     register(registry, "spline", (KeyDispatchDataCodec)Spline.CODEC);
/*   78 */     register(registry, "constant", (KeyDispatchDataCodec)Constant.CODEC);
/*   79 */     register(registry, "y_clamped_gradient", (KeyDispatchDataCodec)YClampedGradient.CODEC);
/*   80 */     return register(registry, "find_top_surface", (KeyDispatchDataCodec)FindTopSurface.CODEC);
/*      */   }
/*      */   
/*      */   private static MapCodec<? extends DensityFunction> register(Registry<MapCodec<? extends DensityFunction>> registry, String name, KeyDispatchDataCodec<? extends DensityFunction> codec) {
/*   84 */     return (MapCodec<? extends DensityFunction>)Registry.register(registry, name, codec.codec());
/*      */   }
/*      */   
/*      */   private static <A, O> KeyDispatchDataCodec<O> singleArgumentCodec(Codec<A> argumentCodec, Function<A, O> constructor, Function<O, A> getter) {
/*   88 */     return KeyDispatchDataCodec.of(argumentCodec.fieldOf("argument").xmap(constructor, getter));
/*      */   }
/*      */   
/*      */   private static <O> KeyDispatchDataCodec<O> singleFunctionArgumentCodec(Function<DensityFunction, O> constructor, Function<O, DensityFunction> getter) {
/*   92 */     return singleArgumentCodec(DensityFunction.CODEC, constructor, getter);
/*      */   }
/*      */   
/*      */   private static <O> KeyDispatchDataCodec<O> doubleFunctionArgumentCodec(BiFunction<DensityFunction, DensityFunction, O> constructor, Function<O, DensityFunction> firstArgumentGetter, Function<O, DensityFunction> secondArgumentGetter) {
/*   96 */     return KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.CODEC.fieldOf("argument1").forGetter(firstArgumentGetter), (App)DensityFunction.CODEC.fieldOf("argument2").forGetter(secondArgumentGetter)).apply((Applicative)i, constructor)));
/*      */   }
/*      */   
/*      */   private static <O> KeyDispatchDataCodec<O> makeCodec(MapCodec<O> dataCodec) {
/*  103 */     return KeyDispatchDataCodec.of(dataCodec);
/*      */   }
/*      */   
/*      */   public static DensityFunction interpolated(DensityFunction function) {
/*  110 */     return new Marker(Marker.Type.Interpolated, function);
/*      */   }
/*      */   
/*      */   public static DensityFunction flatCache(DensityFunction function) {
/*  114 */     return new Marker(Marker.Type.FlatCache, function);
/*      */   }
/*      */   
/*      */   public static DensityFunction cache2d(DensityFunction function) {
/*  118 */     return new Marker(Marker.Type.Cache2D, function);
/*      */   }
/*      */   
/*      */   public static DensityFunction cacheOnce(DensityFunction function) {
/*  122 */     return new Marker(Marker.Type.CacheOnce, function);
/*      */   }
/*      */   
/*      */   public static DensityFunction cacheAllInCell(DensityFunction function) {
/*  126 */     return new Marker(Marker.Type.CacheAllInCell, function);
/*      */   }
/*      */   
/*      */   public static DensityFunction mappedNoise(Holder<NormalNoise.NoiseParameters> noiseData, @Deprecated double xzScale, double yScale, double minTarget, double maxTarget) {
/*  130 */     return mapFromUnitTo(new Noise(new DensityFunction.NoiseHolder(noiseData), xzScale, yScale), minTarget, maxTarget);
/*      */   }
/*      */   
/*      */   public static DensityFunction mappedNoise(Holder<NormalNoise.NoiseParameters> noiseData, double yScale, double minTarget, double maxTarget) {
/*  134 */     return mappedNoise(noiseData, 1.0D, yScale, minTarget, maxTarget);
/*      */   }
/*      */   
/*      */   public static DensityFunction mappedNoise(Holder<NormalNoise.NoiseParameters> noiseData, double minTarget, double maxTarget) {
/*  138 */     return mappedNoise(noiseData, 1.0D, 1.0D, minTarget, maxTarget);
/*      */   }
/*      */   
/*      */   public static DensityFunction shiftedNoise2d(DensityFunction shiftX, DensityFunction shiftZ, double xzScale, Holder<NormalNoise.NoiseParameters> noiseData) {
/*  142 */     return new ShiftedNoise(shiftX, zero(), shiftZ, xzScale, 0.0D, new DensityFunction.NoiseHolder(noiseData));
/*      */   }
/*      */   
/*      */   public static DensityFunction noise(Holder<NormalNoise.NoiseParameters> noiseData) {
/*  146 */     return noise(noiseData, 1.0D, 1.0D);
/*      */   }
/*      */   
/*      */   public static DensityFunction noise(Holder<NormalNoise.NoiseParameters> noiseData, double xzScale, double yScale) {
/*  150 */     return new Noise(new DensityFunction.NoiseHolder(noiseData), xzScale, yScale);
/*      */   }
/*      */   
/*      */   public static DensityFunction noise(Holder<NormalNoise.NoiseParameters> noiseData, double yScale) {
/*  154 */     return noise(noiseData, 1.0D, yScale);
/*      */   }
/*      */   
/*      */   public static DensityFunction rangeChoice(DensityFunction input, double minInclusive, double maxExclusive, DensityFunction whenInRange, DensityFunction whenOutOfRange) {
/*  158 */     return new RangeChoice(input, minInclusive, maxExclusive, whenInRange, whenOutOfRange);
/*      */   }
/*      */   
/*      */   public static DensityFunction intervalSelect(DensityFunction input, DoubleList thresholds, List<DensityFunction> functions) {
/*  162 */     return new IntervalSelect(input, thresholds, functions);
/*      */   }
/*      */   
/*      */   public static DensityFunction shiftA(Holder<NormalNoise.NoiseParameters> noiseData) {
/*  166 */     return new ShiftA(new DensityFunction.NoiseHolder(noiseData));
/*      */   }
/*      */   
/*      */   public static DensityFunction shiftB(Holder<NormalNoise.NoiseParameters> noiseData) {
/*  170 */     return new ShiftB(new DensityFunction.NoiseHolder(noiseData));
/*      */   }
/*      */   
/*      */   public static DensityFunction shift(Holder<NormalNoise.NoiseParameters> noiseData) {
/*  174 */     return new Shift(new DensityFunction.NoiseHolder(noiseData));
/*      */   }
/*      */   
/*      */   public static DensityFunction blendDensity(DensityFunction input) {
/*  178 */     return new Marker(Marker.Type.BlendDensity, input);
/*      */   }
/*      */   
/*      */   public static DensityFunction endIslands(long seed) {
/*  182 */     return new EndIslandDensityFunction(seed);
/*      */   }
/*      */   
/*      */   public static DensityFunction add(DensityFunction f1, DensityFunction f2) {
/*  186 */     return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.ADD, f1, f2);
/*      */   }
/*      */   
/*      */   public static DensityFunction mul(DensityFunction f1, DensityFunction f2) {
/*  190 */     return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.MUL, f1, f2);
/*      */   }
/*      */   
/*      */   public static DensityFunction min(DensityFunction f1, DensityFunction f2) {
/*  194 */     return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.MIN, f1, f2);
/*      */   }
/*      */   
/*      */   public static DensityFunction max(DensityFunction f1, DensityFunction f2) {
/*  198 */     return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.MAX, f1, f2);
/*      */   }
/*      */   
/*      */   public static DensityFunction spline(CubicSpline<Spline.Coordinate> spline) {
/*  202 */     return new Spline(spline);
/*      */   }
/*      */   
/*      */   public static DensityFunction zero() {
/*  206 */     return Constant.ZERO;
/*      */   }
/*      */   
/*      */   public static DensityFunction constant(double value) {
/*  210 */     return new Constant(value);
/*      */   }
/*      */   
/*      */   public static DensityFunction yClampedGradient(int fromY, int toY, double fromValue, double toValue) {
/*  214 */     return new YClampedGradient(fromY, toY, fromValue, toValue);
/*      */   }
/*      */   
/*      */   public static DensityFunction map(DensityFunction function, Mapped.Type type) {
/*  218 */     return Mapped.create(type, function);
/*      */   }
/*      */   
/*      */   private static DensityFunction mapFromUnitTo(DensityFunction function, double min, double max) {
/*  222 */     double middle = (min + max) * 0.5D;
/*  223 */     double factor = (max - min) * 0.5D;
/*  225 */     return add(constant(middle), mul(constant(factor), function));
/*      */   }
/*      */   
/*      */   public static DensityFunction blendAlpha() {
/*  229 */     return BlendAlpha.INSTANCE;
/*      */   }
/*      */   
/*      */   public static DensityFunction blendOffset() {
/*  233 */     return BlendOffset.INSTANCE;
/*      */   }
/*      */   
/*      */   public static DensityFunction lerp(DensityFunction alpha, DensityFunction first, DensityFunction second) {
/*  237 */     if (first instanceof Constant) {
/*  237 */       Constant constant = (Constant)first;
/*  238 */       return lerp(alpha, constant.value, second);
/*      */     } 
/*  240 */     DensityFunction alphaCached = cacheOnce(alpha);
/*  241 */     DensityFunction oneMinusAlpha = add(mul(alphaCached, constant(-1.0D)), constant(1.0D));
/*  242 */     return add(mul(first, oneMinusAlpha), mul(second, alphaCached));
/*      */   }
/*      */   
/*      */   public static DensityFunction lerp(DensityFunction factor, double first, DensityFunction second) {
/*  247 */     return add(mul(factor, add(second, constant(-first))), constant(first));
/*      */   }
/*      */   
/*      */   public static DensityFunction findTopSurface(DensityFunction density, DensityFunction upperBound, int lowerBound, int stepSize) {
/*  251 */     return new FindTopSurface(density, upperBound, lowerBound, stepSize);
/*      */   }
/*      */   
/*      */   private static interface TransformerWithContext extends DensityFunction {
/*      */     DensityFunction input();
/*      */     
/*      */     default double compute(DensityFunction.FunctionContext context) {
/*  261 */       return transform(context, input().compute(context));
/*      */     }
/*      */     
/*      */     default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  266 */       input().fillArray(output, contextProvider);
/*  267 */       for (int i = 0; i < output.length; i++)
/*  268 */         output[i] = transform(contextProvider.forIndex(i), output[i]); 
/*      */     }
/*      */     
/*      */     double transform(DensityFunction.FunctionContext param1FunctionContext, double param1Double);
/*      */   }
/*      */   
/*      */   private static interface PureTransformer extends DensityFunction {
/*      */     DensityFunction input();
/*      */     
/*      */     default double compute(DensityFunction.FunctionContext context) {
/*  280 */       return transform(input().compute(context));
/*      */     }
/*      */     
/*      */     default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  285 */       input().fillArray(output, contextProvider);
/*  286 */       for (int i = 0; i < output.length; i++)
/*  287 */         output[i] = transform(output[i]); 
/*      */     }
/*      */     
/*      */     double transform(double param1Double);
/*      */   }
/*      */   
/*      */   enum BlendAlpha implements DensityFunction.SimpleFunction {
/*  296 */     INSTANCE;
/*      */     
/*  297 */     public static final KeyDispatchDataCodec<DensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*      */     
/*      */     static {
/*      */     
/*      */     }
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  301 */       return 1.0D;
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  306 */       Arrays.fill(output, 1.0D);
/*      */     }
/*      */     
/*      */     public double minValue() {
/*  311 */       return 1.0D;
/*      */     }
/*      */     
/*      */     public double maxValue() {
/*  316 */       return 1.0D;
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  321 */       return CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   enum BlendOffset implements DensityFunction.SimpleFunction {
/*  327 */     INSTANCE;
/*      */     
/*  328 */     public static final KeyDispatchDataCodec<DensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*      */     
/*      */     static {
/*      */     
/*      */     }
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  332 */       return 0.0D;
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  337 */       Arrays.fill(output, 0.0D);
/*      */     }
/*      */     
/*      */     public double minValue() {
/*  342 */       return 0.0D;
/*      */     }
/*      */     
/*      */     public double maxValue() {
/*  347 */       return 0.0D;
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  352 */       return CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   public static interface BeardifierOrMarker extends DensityFunction.SimpleFunction {
/*  357 */     public static final KeyDispatchDataCodec<DensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(DensityFunctions.BeardifierMarker.INSTANCE));
/*      */     
/*      */     default KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  361 */       return CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   enum BeardifierMarker implements BeardifierOrMarker {
/*  367 */     INSTANCE;
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  371 */       return 0.0D;
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  376 */       Arrays.fill(output, 0.0D);
/*      */     }
/*      */     
/*      */     public double minValue() {
/*  381 */       return 0.0D;
/*      */     }
/*      */     
/*      */     public double maxValue() {
/*  386 */       return 0.0D;
/*      */     }
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public static final class HolderHolder extends Record implements DensityFunction {
/*      */     private final Holder<DensityFunction> function;
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #393	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #393	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #393	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public Holder<DensityFunction> function() {
/*  393 */       return this.function;
/*      */     }
/*      */     
/*      */     public HolderHolder(Holder<DensityFunction> function) {
/*  394 */       this.function = function;
/*      */     }
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  397 */       return ((DensityFunction)this.function.value()).compute(context);
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  402 */       ((DensityFunction)this.function.value()).fillArray(output, contextProvider);
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*  408 */       return new HolderHolder(Holder.direct(visitor.apply((DensityFunction)this.function.value())));
/*      */     }
/*      */     
/*      */     public double minValue() {
/*  413 */       return this.function.isBound() ? ((DensityFunction)this.function.value()).minValue() : Double.NEGATIVE_INFINITY;
/*      */     }
/*      */     
/*      */     public double maxValue() {
/*  418 */       return this.function.isBound() ? ((DensityFunction)this.function.value()).maxValue() : Double.POSITIVE_INFINITY;
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  424 */       throw new UnsupportedOperationException("Calling .codec() on HolderHolder");
/*      */     }
/*      */   }
/*      */   
/*      */   public static interface MarkerOrMarked extends DensityFunction {
/*      */     DensityFunctions.Marker.Type type();
/*      */     
/*      */     DensityFunction wrapped();
/*      */     
/*      */     default KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  435 */       return (KeyDispatchDataCodec)(type()).codec;
/*      */     }
/*      */     
/*      */     default DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*  440 */       return new DensityFunctions.Marker(type(), visitor.apply(wrapped()));
/*      */     }
/*      */   }
/*      */   
/*      */   static final class Marker extends Record implements MarkerOrMarked {
/*      */     private final Type type;
/*      */     
/*      */     private final DensityFunction wrapped;
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #444	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #444	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #444	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public Type type() {
/*  444 */       return this.type;
/*      */     }
/*      */     
/*      */     public DensityFunction wrapped() {
/*  444 */       return this.wrapped;
/*      */     }
/*      */     
/*      */     Marker(Type type, DensityFunction wrapped) {
/*  445 */       this.type = type;
/*  445 */       this.wrapped = wrapped;
/*      */     }
/*      */     
/*      */     public enum Type implements StringRepresentable {
/*  447 */       Interpolated("interpolated"),
/*  448 */       FlatCache("flat_cache"),
/*  449 */       Cache2D("cache_2d"),
/*  450 */       CacheOnce("cache_once"),
/*  451 */       CacheAllInCell("cache_all_in_cell"),
/*  452 */       BlendDensity("blend_density");
/*      */       
/*      */       private final String name;
/*      */       
/*      */       private final KeyDispatchDataCodec<DensityFunctions.MarkerOrMarked> codec;
/*      */       
/*      */       Type(String name) {
/*  457 */         this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> new DensityFunctions.Marker(this, input), DensityFunctions.MarkerOrMarked::wrapped);
/*  460 */         this.name = name;
/*      */       }
/*      */       
/*      */       public String getSerializedName() {
/*  465 */         return this.name;
/*      */       }
/*      */     }
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  471 */       return this.wrapped.compute(context);
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  476 */       this.wrapped.fillArray(output, contextProvider);
/*      */     }
/*      */     
/*      */     public double minValue() {
/*  481 */       if (this.type == Type.BlendDensity)
/*  483 */         return Double.NEGATIVE_INFINITY; 
/*  485 */       return this.wrapped.minValue();
/*      */     }
/*      */     
/*      */     public double maxValue() {
/*  490 */       if (this.type == Type.BlendDensity)
/*  492 */         return Double.POSITIVE_INFINITY; 
/*  494 */       return this.wrapped.maxValue();
/*      */     }
/*      */   }
/*      */   
/*      */   public enum Type implements StringRepresentable {
/*      */     Interpolated("interpolated"),
/*      */     FlatCache("flat_cache"),
/*      */     Cache2D("cache_2d"),
/*      */     CacheOnce("cache_once"),
/*      */     CacheAllInCell("cache_all_in_cell"),
/*      */     BlendDensity("blend_density");
/*      */     
/*      */     private final String name;
/*      */     
/*      */     private final KeyDispatchDataCodec<DensityFunctions.MarkerOrMarked> codec;
/*      */     
/*      */     Type(String name) {
/*      */       this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> new DensityFunctions.Marker(this, input), DensityFunctions.MarkerOrMarked::wrapped);
/*      */       this.name = name;
/*      */     }
/*      */     
/*      */     public String getSerializedName() {
/*      */       return this.name;
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class Noise extends Record implements DensityFunction {
/*      */     private final DensityFunction.NoiseHolder noise;
/*      */     
/*      */     @Deprecated
/*      */     private final double xzScale;
/*      */     
/*      */     private final double yScale;
/*      */     
/*      */     public static final MapCodec<Noise> DATA_CODEC;
/*      */     
/*      */     protected Noise(DensityFunction.NoiseHolder noise, @Deprecated double xzScale, double yScale) {
/*  498 */       this.noise = noise;
/*  498 */       this.xzScale = xzScale;
/*  498 */       this.yScale = yScale;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #498	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #498	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #498	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public DensityFunction.NoiseHolder noise() {
/*  498 */       return this.noise;
/*      */     }
/*      */     
/*      */     @Deprecated
/*      */     public double xzScale() {
/*  498 */       return this.xzScale;
/*      */     }
/*      */     
/*      */     public double yScale() {
/*  498 */       return this.yScale;
/*      */     }
/*      */     
/*      */     static {
/*  499 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(Noise::noise), (App)Codec.DOUBLE.fieldOf("xz_scale").forGetter(Noise::xzScale), (App)Codec.DOUBLE.fieldOf("y_scale").forGetter(Noise::yScale)).apply((Applicative)i, Noise::new));
/*      */     }
/*      */     
/*  505 */     public static final KeyDispatchDataCodec<Noise> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  509 */       return this.noise.getValue(context.blockX() * this.xzScale, context.blockY() * this.yScale, context.blockZ() * this.xzScale);
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  514 */       contextProvider.fillAllDirectly(output, this);
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*  519 */       return new Noise(visitor.visitNoise(this.noise), this.xzScale, this.yScale);
/*      */     }
/*      */     
/*      */     public double minValue() {
/*  524 */       return -maxValue();
/*      */     }
/*      */     
/*      */     public double maxValue() {
/*  529 */       return this.noise.maxValue();
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  534 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class EndIslandDensityFunction implements DensityFunction.SimpleFunction {
/*  540 */     public static final KeyDispatchDataCodec<EndIslandDensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(new EndIslandDensityFunction(0L)));
/*      */     
/*      */     private static final float ISLAND_THRESHOLD = -0.9F;
/*      */     
/*      */     private final SimplexNoise islandNoise;
/*      */     
/*      */     public EndIslandDensityFunction(long seed) {
/*  546 */       RandomSource islandRandom = new LegacyRandomSource(seed);
/*  548 */       islandRandom.consumeCount(17292);
/*  549 */       this.islandNoise = new SimplexNoise(islandRandom);
/*      */     }
/*      */     
/*      */     private static float getHeightValue(SimplexNoise islandNoise, int sectionX, int sectionZ) {
/*  553 */       int chunkX = sectionX / 2;
/*  554 */       int chunkZ = sectionZ / 2;
/*  555 */       int subSectionX = sectionX % 2;
/*  556 */       int subSectionZ = sectionZ % 2;
/*  559 */       float doffs = 100.0F - Mth.sqrt((sectionX * sectionX + sectionZ * sectionZ)) * 8.0F;
/*  560 */       doffs = Mth.clamp(doffs, -100.0F, 80.0F);
/*  563 */       for (int xo = -12; xo <= 12; xo++) {
/*  564 */         for (int zo = -12; zo <= 12; zo++) {
/*  565 */           long totalChunkX = (chunkX + xo);
/*  566 */           long totalChunkZ = (chunkZ + zo);
/*  567 */           if (totalChunkX * totalChunkX + totalChunkZ * totalChunkZ > 4096L && islandNoise.getValue(totalChunkX, totalChunkZ) < -0.8999999761581421D) {
/*  568 */             float islandSize = (Mth.abs((float)totalChunkX) * 3439.0F + Mth.abs((float)totalChunkZ) * 147.0F) % 13.0F + 9.0F;
/*  569 */             float xd = (subSectionX - xo * 2);
/*  570 */             float zd = (subSectionZ - zo * 2);
/*  571 */             float newDoffs = 100.0F - Mth.sqrt(xd * xd + zd * zd) * islandSize;
/*  572 */             newDoffs = Mth.clamp(newDoffs, -100.0F, 80.0F);
/*  573 */             doffs = Math.max(doffs, newDoffs);
/*      */           } 
/*      */         } 
/*      */       } 
/*  578 */       return doffs;
/*      */     }
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  583 */       return (getHeightValue(this.islandNoise, context.blockX() / 8, context.blockZ() / 8) - 8.0D) / 128.0D;
/*      */     }
/*      */     
/*      */     public double minValue() {
/*  588 */       return -0.84375D;
/*      */     }
/*      */     
/*      */     public double maxValue() {
/*  593 */       return 0.5625D;
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  598 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class ShiftedNoise extends Record implements DensityFunction {
/*      */     private final DensityFunction shiftX;
/*      */     
/*      */     private final DensityFunction shiftY;
/*      */     
/*      */     private final DensityFunction shiftZ;
/*      */     
/*      */     private final double xzScale;
/*      */     
/*      */     private final double yScale;
/*      */     
/*      */     private final DensityFunction.NoiseHolder noise;
/*      */     
/*      */     private static final MapCodec<ShiftedNoise> DATA_CODEC;
/*      */     
/*      */     protected ShiftedNoise(DensityFunction shiftX, DensityFunction shiftY, DensityFunction shiftZ, double xzScale, double yScale, DensityFunction.NoiseHolder noise) {
/*  602 */       this.shiftX = shiftX;
/*  602 */       this.shiftY = shiftY;
/*  602 */       this.shiftZ = shiftZ;
/*  602 */       this.xzScale = xzScale;
/*  602 */       this.yScale = yScale;
/*  602 */       this.noise = noise;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #602	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #602	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #602	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public DensityFunction shiftX() {
/*  602 */       return this.shiftX;
/*      */     }
/*      */     
/*      */     public DensityFunction shiftY() {
/*  602 */       return this.shiftY;
/*      */     }
/*      */     
/*      */     public DensityFunction shiftZ() {
/*  602 */       return this.shiftZ;
/*      */     }
/*      */     
/*      */     public double xzScale() {
/*  602 */       return this.xzScale;
/*      */     }
/*      */     
/*      */     public double yScale() {
/*  602 */       return this.yScale;
/*      */     }
/*      */     
/*      */     public DensityFunction.NoiseHolder noise() {
/*  602 */       return this.noise;
/*      */     }
/*      */     
/*      */     static {
/*  610 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.CODEC.fieldOf("shift_x").forGetter(ShiftedNoise::shiftX), (App)DensityFunction.CODEC.fieldOf("shift_y").forGetter(ShiftedNoise::shiftY), (App)DensityFunction.CODEC.fieldOf("shift_z").forGetter(ShiftedNoise::shiftZ), (App)Codec.DOUBLE.fieldOf("xz_scale").forGetter(ShiftedNoise::xzScale), (App)Codec.DOUBLE.fieldOf("y_scale").forGetter(ShiftedNoise::yScale), (App)DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(ShiftedNoise::noise)).apply((Applicative)i, ShiftedNoise::new));
/*      */     }
/*      */     
/*  619 */     public static final KeyDispatchDataCodec<ShiftedNoise> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  623 */       double x = context.blockX() * this.xzScale + this.shiftX.compute(context);
/*  624 */       double y = context.blockY() * this.yScale + this.shiftY.compute(context);
/*  625 */       double z = context.blockZ() * this.xzScale + this.shiftZ.compute(context);
/*  626 */       return this.noise.getValue(x, y, z);
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  631 */       contextProvider.fillAllDirectly(output, this);
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*  636 */       return new ShiftedNoise(visitor
/*  637 */           .apply(this.shiftX), visitor
/*  638 */           .apply(this.shiftY), visitor
/*  639 */           .apply(this.shiftZ), this.xzScale, this.yScale, visitor
/*      */           
/*  642 */           .visitNoise(this.noise));
/*      */     }
/*      */     
/*      */     public double minValue() {
/*  648 */       return -maxValue();
/*      */     }
/*      */     
/*      */     public double maxValue() {
/*  653 */       return this.noise.maxValue();
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  658 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class RangeChoice extends Record implements DensityFunction {
/*      */     private final DensityFunction input;
/*      */     
/*      */     private final double minInclusive;
/*      */     
/*      */     private final double maxExclusive;
/*      */     
/*      */     private final DensityFunction whenInRange;
/*      */     
/*      */     private final DensityFunction whenOutOfRange;
/*      */     
/*      */     public static final MapCodec<RangeChoice> DATA_CODEC;
/*      */     
/*      */     private RangeChoice(DensityFunction input, double minInclusive, double maxExclusive, DensityFunction whenInRange, DensityFunction whenOutOfRange) {
/*  662 */       this.input = input;
/*  662 */       this.minInclusive = minInclusive;
/*  662 */       this.maxExclusive = maxExclusive;
/*  662 */       this.whenInRange = whenInRange;
/*  662 */       this.whenOutOfRange = whenOutOfRange;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #662	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #662	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #662	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public DensityFunction input() {
/*  662 */       return this.input;
/*      */     }
/*      */     
/*      */     public double minInclusive() {
/*  662 */       return this.minInclusive;
/*      */     }
/*      */     
/*      */     public double maxExclusive() {
/*  662 */       return this.maxExclusive;
/*      */     }
/*      */     
/*      */     public DensityFunction whenInRange() {
/*  662 */       return this.whenInRange;
/*      */     }
/*      */     
/*      */     public DensityFunction whenOutOfRange() {
/*  662 */       return this.whenOutOfRange;
/*      */     }
/*      */     
/*      */     static {
/*  663 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.CODEC.fieldOf("input").forGetter(RangeChoice::input), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("min_inclusive").forGetter(RangeChoice::minInclusive), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("max_exclusive").forGetter(RangeChoice::maxExclusive), (App)DensityFunction.CODEC.fieldOf("when_in_range").forGetter(RangeChoice::whenInRange), (App)DensityFunction.CODEC.fieldOf("when_out_of_range").forGetter(RangeChoice::whenOutOfRange)).apply((Applicative)i, RangeChoice::new));
/*      */     }
/*      */     
/*  671 */     public static final KeyDispatchDataCodec<RangeChoice> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  675 */       double inputValue = this.input.compute(context);
/*  676 */       if (inputValue >= this.minInclusive && inputValue < this.maxExclusive)
/*  677 */         return this.whenInRange.compute(context); 
/*  679 */       return this.whenOutOfRange.compute(context);
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  684 */       this.input.fillArray(output, contextProvider);
/*  685 */       for (int i = 0; i < output.length; i++) {
/*  686 */         double v = output[i];
/*  687 */         if (v >= this.minInclusive && v < this.maxExclusive) {
/*  688 */           output[i] = this.whenInRange.compute(contextProvider.forIndex(i));
/*      */         } else {
/*  690 */           output[i] = this.whenOutOfRange.compute(contextProvider.forIndex(i));
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*  697 */       return new RangeChoice(visitor.apply(this.input), this.minInclusive, this.maxExclusive, visitor.apply(this.whenInRange), visitor.apply(this.whenOutOfRange));
/*      */     }
/*      */     
/*      */     public double minValue() {
/*  702 */       return Math.min(this.whenInRange.minValue(), this.whenOutOfRange.minValue());
/*      */     }
/*      */     
/*      */     public double maxValue() {
/*  707 */       return Math.max(this.whenInRange.maxValue(), this.whenOutOfRange.maxValue());
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  712 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class IntervalSelect extends Record implements DensityFunction {
/*      */     private final DensityFunction input;
/*      */     
/*      */     private final DoubleList thresholds;
/*      */     
/*      */     private final List<DensityFunction> functions;
/*      */     
/*      */     private IntervalSelect(DensityFunction input, DoubleList thresholds, List<DensityFunction> functions) {
/*  716 */       this.input = input;
/*  716 */       this.thresholds = thresholds;
/*  716 */       this.functions = functions;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$IntervalSelect;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #716	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$IntervalSelect;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$IntervalSelect;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #716	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$IntervalSelect;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$IntervalSelect;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #716	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$IntervalSelect;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public DensityFunction input() {
/*  716 */       return this.input;
/*      */     }
/*      */     
/*      */     public DoubleList thresholds() {
/*  716 */       return this.thresholds;
/*      */     }
/*      */     
/*      */     public List<DensityFunction> functions() {
/*  716 */       return this.functions;
/*      */     }
/*      */     
/*  717 */     private static final Codec<DoubleList> THRESHOLDS_CODEC = DensityFunctions.NOISE_VALUE_CODEC.listOf().xmap(it.unimi.dsi.fastutil.doubles.DoubleArrayList::new, Function.identity());
/*      */     
/*      */     public static final MapCodec<IntervalSelect> DATA_CODEC;
/*      */     
/*      */     static {
/*  723 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.CODEC.fieldOf("input").forGetter(IntervalSelect::input), (App)THRESHOLDS_CODEC.fieldOf("thresholds").forGetter(IntervalSelect::thresholds), (App)DensityFunction.CODEC.listOf(2, 2147483647).fieldOf("functions").forGetter(IntervalSelect::functions)).apply((Applicative)i, IntervalSelect::new)).validate(IntervalSelect::validate);
/*      */     }
/*      */     
/*  725 */     public static final KeyDispatchDataCodec<IntervalSelect> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */     
/*      */     private DataResult<IntervalSelect> validate() {
/*  728 */       if (this.thresholds.size() != this.functions.size() - 1)
/*  729 */         return DataResult.error(() -> "Expected " + this.functions.size() - 1 + " thresholds for " + this.functions.size() + " functions, but got " + this.thresholds.size()); 
/*  731 */       if (!Comparators.isInOrder((Iterable)this.thresholds, Double::compare))
/*  732 */         return DataResult.error(() -> "Threshold values must be ordered from smallest to largest"); 
/*  734 */       return DataResult.success(this);
/*      */     }
/*      */     
/*      */     private double compute(DensityFunction.FunctionContext context, double input) {
/*  738 */       for (int i = 0; i < this.thresholds.size(); i++) {
/*  739 */         if (input < this.thresholds.getDouble(i))
/*  740 */           return ((DensityFunction)this.functions.get(i)).compute(context); 
/*      */       } 
/*  743 */       return ((DensityFunction)this.functions.getLast()).compute(context);
/*      */     }
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  748 */       return compute(context, this.input.compute(context));
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  753 */       this.input.fillArray(output, contextProvider);
/*  754 */       for (int i = 0; i < output.length; i++)
/*  755 */         output[i] = compute(contextProvider.forIndex(i), output[i]); 
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*  761 */       Objects.requireNonNull(visitor);
/*  761 */       return new IntervalSelect(visitor.apply(this.input), this.thresholds, List.copyOf(Lists.transform(this.functions, visitor::apply)));
/*      */     }
/*      */     
/*      */     public double minValue() {
/*  766 */       double minValue = Double.MAX_VALUE;
/*  767 */       for (DensityFunction function : this.functions)
/*  768 */         minValue = Math.min(function.minValue(), minValue); 
/*  770 */       return minValue;
/*      */     }
/*      */     
/*      */     public double maxValue() {
/*  775 */       double maxValue = -1.7976931348623157E308D;
/*  776 */       for (DensityFunction function : this.functions)
/*  777 */         maxValue = Math.max(function.maxValue(), maxValue); 
/*  779 */       return maxValue;
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<IntervalSelect> codec() {
/*  784 */       return CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   protected static interface ShiftNoise extends DensityFunction {
/*      */     DensityFunction.NoiseHolder offsetNoise();
/*      */     
/*      */     default double minValue() {
/*  793 */       return -maxValue();
/*      */     }
/*      */     
/*      */     default double maxValue() {
/*  798 */       return offsetNoise().maxValue() * 4.0D;
/*      */     }
/*      */     
/*      */     default double compute(double localX, double localY, double localZ) {
/*  802 */       return offsetNoise().getValue(localX * 0.25D, localY * 0.25D, localZ * 0.25D) * 4.0D;
/*      */     }
/*      */     
/*      */     default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  807 */       contextProvider.fillAllDirectly(output, this);
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class ShiftA extends Record implements ShiftNoise {
/*      */     private final DensityFunction.NoiseHolder offsetNoise;
/*      */     
/*      */     protected ShiftA(DensityFunction.NoiseHolder offsetNoise) {
/*  811 */       this.offsetNoise = offsetNoise;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #811	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #811	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #811	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public DensityFunction.NoiseHolder offsetNoise() {
/*  811 */       return this.offsetNoise;
/*      */     }
/*      */     
/*  812 */     private static final KeyDispatchDataCodec<ShiftA> CODEC = DensityFunctions.singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, ShiftA::new, ShiftA::offsetNoise);
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  816 */       return compute(context.blockX(), 0.0D, context.blockZ());
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*  821 */       return new ShiftA(visitor.visitNoise(this.offsetNoise));
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  826 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class ShiftB extends Record implements ShiftNoise {
/*      */     private final DensityFunction.NoiseHolder offsetNoise;
/*      */     
/*      */     protected ShiftB(DensityFunction.NoiseHolder offsetNoise) {
/*  830 */       this.offsetNoise = offsetNoise;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #830	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #830	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #830	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public DensityFunction.NoiseHolder offsetNoise() {
/*  830 */       return this.offsetNoise;
/*      */     }
/*      */     
/*  831 */     private static final KeyDispatchDataCodec<ShiftB> CODEC = DensityFunctions.singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, ShiftB::new, ShiftB::offsetNoise);
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  835 */       return compute(context.blockZ(), context.blockX(), 0.0D);
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*  840 */       return new ShiftB(visitor.visitNoise(this.offsetNoise));
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  845 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class Shift extends Record implements ShiftNoise {
/*      */     private final DensityFunction.NoiseHolder offsetNoise;
/*      */     
/*      */     protected Shift(DensityFunction.NoiseHolder offsetNoise) {
/*  849 */       this.offsetNoise = offsetNoise;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #849	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #849	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #849	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public DensityFunction.NoiseHolder offsetNoise() {
/*  849 */       return this.offsetNoise;
/*      */     }
/*      */     
/*  850 */     private static final KeyDispatchDataCodec<Shift> CODEC = DensityFunctions.singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, Shift::new, Shift::offsetNoise);
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  854 */       return compute(context.blockX(), context.blockY(), context.blockZ());
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*  859 */       return new Shift(visitor.visitNoise(this.offsetNoise));
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  864 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class Clamp extends Record implements PureTransformer {
/*      */     private final DensityFunction input;
/*      */     
/*      */     private final double minValue;
/*      */     
/*      */     private final double maxValue;
/*      */     
/*      */     private static final MapCodec<Clamp> DATA_CODEC;
/*      */     
/*      */     protected Clamp(DensityFunction input, double minValue, double maxValue) {
/*  868 */       this.input = input;
/*  868 */       this.minValue = minValue;
/*  868 */       this.maxValue = maxValue;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #868	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #868	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #868	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public DensityFunction input() {
/*  868 */       return this.input;
/*      */     }
/*      */     
/*      */     public double minValue() {
/*  868 */       return this.minValue;
/*      */     }
/*      */     
/*      */     public double maxValue() {
/*  868 */       return this.maxValue;
/*      */     }
/*      */     
/*      */     static {
/*  869 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.CODEC.fieldOf("input").forGetter(Clamp::input), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("min").forGetter(Clamp::minValue), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("max").forGetter(Clamp::maxValue)).apply((Applicative)i, Clamp::new));
/*      */     }
/*      */     
/*  875 */     public static final KeyDispatchDataCodec<Clamp> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */     
/*      */     public double transform(double input) {
/*  879 */       return Mth.clamp(input, this.minValue, this.maxValue);
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*  884 */       return new Clamp(visitor.apply(this.input), this.minValue, this.maxValue);
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  889 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class Mapped extends Record implements PureTransformer {
/*      */     private final Type type;
/*      */     
/*      */     private final DensityFunction input;
/*      */     
/*      */     private final double minValue;
/*      */     
/*      */     private final double maxValue;
/*      */     
/*      */     protected Mapped(Type type, DensityFunction input, double minValue, double maxValue) {
/*  893 */       this.type = type;
/*  893 */       this.input = input;
/*  893 */       this.minValue = minValue;
/*  893 */       this.maxValue = maxValue;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #893	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #893	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #893	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public Type type() {
/*  893 */       return this.type;
/*      */     }
/*      */     
/*      */     public DensityFunction input() {
/*  893 */       return this.input;
/*      */     }
/*      */     
/*      */     public double minValue() {
/*  893 */       return this.minValue;
/*      */     }
/*      */     
/*      */     public double maxValue() {
/*  893 */       return this.maxValue;
/*      */     }
/*      */     
/*      */     public static Mapped create(Type type, DensityFunction input) {
/*  895 */       double minValue = input.minValue();
/*  896 */       double maxValue = input.maxValue();
/*  897 */       double minImage = transform(type, minValue);
/*  898 */       double maxImage = transform(type, maxValue);
/*  899 */       if (type == Type.INVERT) {
/*  900 */         if (minValue < 0.0D && maxValue > 0.0D)
/*  901 */           return new Mapped(type, input, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY); 
/*  904 */         return new Mapped(type, input, maxImage, minImage);
/*      */       } 
/*  906 */       if (type == Type.ABS || type == Type.SQUARE)
/*  908 */         return new Mapped(type, input, Math.max(0.0D, minValue), Math.max(minImage, maxImage)); 
/*  911 */       return new Mapped(type, input, minImage, maxImage);
/*      */     }
/*      */     
/*      */     public enum Type implements StringRepresentable {
/*  915 */       ABS("abs"),
/*  916 */       SQUARE("square"),
/*  917 */       CUBE("cube"),
/*  918 */       HALF_NEGATIVE("half_negative"),
/*  919 */       QUARTER_NEGATIVE("quarter_negative"),
/*  920 */       INVERT("invert"),
/*  921 */       SQUEEZE("squeeze");
/*      */       
/*      */       private final String name;
/*      */       
/*      */       private final KeyDispatchDataCodec<DensityFunctions.Mapped> codec;
/*      */       
/*      */       Type(String name) {
/*  926 */         this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> DensityFunctions.Mapped.create(this, input), DensityFunctions.Mapped::input);
/*  929 */         this.name = name;
/*      */       }
/*      */       
/*      */       public String getSerializedName() {
/*  934 */         return this.name;
/*      */       }
/*      */     }
/*      */     
/*      */     private static double transform(Type type, double input) {
/*  939 */       switch (type.ordinal()) {
/*      */         default:
/*  939 */           throw new MatchException(null, null);
/*      */         case 0:
/*      */         
/*      */         case 1:
/*      */         
/*      */         case 2:
/*      */         
/*      */         case 3:
/*  939 */           return 
/*      */             
/*  943 */             (input > 0.0D) ? input : (input * 0.5D);
/*      */         case 4:
/*  944 */           return (input > 0.0D) ? input : (input * 0.25D);
/*      */         case 5:
/*      */         
/*      */         case 6:
/*      */           break;
/*      */       } 
/*  948 */       double c = Mth.clamp(input, -1.0D, 1.0D);
/*      */     }
/*      */     
/*      */     public double transform(double input) {
/*  956 */       return transform(this.type, input);
/*      */     }
/*      */     
/*      */     public Mapped mapChildren(DensityFunction.Visitor visitor) {
/*  961 */       return create(this.type, visitor.apply(this.input));
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  966 */       return (KeyDispatchDataCodec)this.type.codec;
/*      */     }
/*      */   }
/*      */   
/*      */   public enum Type implements StringRepresentable {
/*      */     ABS("abs"),
/*      */     SQUARE("square"),
/*      */     CUBE("cube"),
/*      */     HALF_NEGATIVE("half_negative"),
/*      */     QUARTER_NEGATIVE("quarter_negative"),
/*      */     INVERT("invert"),
/*      */     SQUEEZE("squeeze");
/*      */     
/*      */     private final String name;
/*      */     
/*      */     private final KeyDispatchDataCodec<DensityFunctions.Mapped> codec;
/*      */     
/*      */     Type(String name) {
/*      */       this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> DensityFunctions.Mapped.create(this, input), DensityFunctions.Mapped::input);
/*      */       this.name = name;
/*      */     }
/*      */     
/*      */     public String getSerializedName() {
/*      */       return this.name;
/*      */     }
/*      */   }
/*      */   
/*      */   public static interface TwoArgumentSimpleFunction extends DensityFunction {
/*  971 */     public static final Logger LOGGER = LogUtils.getLogger();
/*      */     
/*      */     static TwoArgumentSimpleFunction create(Type type, DensityFunction argument1, DensityFunction argument2) {
/*  974 */       double min1 = argument1.minValue();
/*  975 */       double min2 = argument2.minValue();
/*  977 */       double max1 = argument1.maxValue();
/*  978 */       double max2 = argument2.maxValue();
/*  980 */       if (type == Type.MIN || type == Type.MAX) {
/*  981 */         boolean firstAlwaysBiggerThanSecond = (min1 >= max2);
/*  982 */         boolean secondAlwaysBiggerThanFirst = (min2 >= max1);
/*  983 */         if (firstAlwaysBiggerThanSecond || secondAlwaysBiggerThanFirst)
/*  984 */           LOGGER.warn("Creating a {} function between two non-overlapping inputs: {} and {}", new Object[] { type, argument1, argument2 }); 
/*      */       } 
/*  994 */       switch (type.ordinal()) {
/*      */         default:
/*  994 */           throw new MatchException(null, null);
/*      */         case 0:
/*      */         
/*      */         case 3:
/*      */         
/*      */         case 2:
/*      */         
/*      */         case 1:
/*  999 */           if (min1 > 0.0D && min2 > 0.0D);
/* 1002 */           if (max1 < 0.0D && max2 < 0.0D);
/*      */           break;
/*      */       } 
/* 1005 */       double minValue = Math.min(min1 * max2, max1 * min2);
/* 1009 */       switch (type.ordinal()) {
/*      */         default:
/* 1009 */           throw new MatchException(null, null);
/*      */         case 0:
/*      */         
/*      */         case 3:
/*      */         
/*      */         case 2:
/*      */         
/*      */         case 1:
/* 1014 */           if (min1 > 0.0D && min2 > 0.0D);
/* 1017 */           if (max1 < 0.0D && max2 < 0.0D);
/*      */           break;
/*      */       } 
/* 1020 */       double maxValue = Math.max(min1 * min2, max1 * max2);
/* 1024 */       if (type == Type.MUL || type == Type.ADD) {
/* 1025 */         if (argument1 instanceof DensityFunctions.Constant) {
/* 1025 */           DensityFunctions.Constant constant = (DensityFunctions.Constant)argument1;
/* 1026 */           return new DensityFunctions.MulOrAdd((type == Type.ADD) ? DensityFunctions.MulOrAdd.Type.ADD : DensityFunctions.MulOrAdd.Type.MUL, argument2, minValue, maxValue, constant.value);
/*      */         } 
/* 1028 */         if (argument2 instanceof DensityFunctions.Constant) {
/* 1028 */           DensityFunctions.Constant constant = (DensityFunctions.Constant)argument2;
/* 1029 */           return new DensityFunctions.MulOrAdd((type == Type.ADD) ? DensityFunctions.MulOrAdd.Type.ADD : DensityFunctions.MulOrAdd.Type.MUL, argument1, minValue, maxValue, constant.value);
/*      */         } 
/*      */       } 
/* 1033 */       return new DensityFunctions.Ap2(type, argument1, argument2, minValue, maxValue);
/*      */     }
/*      */     
/*      */     Type type();
/*      */     
/*      */     DensityFunction argument1();
/*      */     
/*      */     DensityFunction argument2();
/*      */     
/*      */     public enum Type implements StringRepresentable {
/* 1037 */       ADD("add"),
/* 1038 */       MUL("mul"),
/* 1039 */       MIN("min"),
/* 1040 */       MAX("max");
/*      */       
/*      */       private final KeyDispatchDataCodec<DensityFunctions.TwoArgumentSimpleFunction> codec;
/*      */       
/*      */       private final String name;
/*      */       
/*      */       Type(String name) {
/* 1043 */         this.codec = DensityFunctions.doubleFunctionArgumentCodec((argument1, argument2) -> DensityFunctions.TwoArgumentSimpleFunction.create(this, argument1, argument2), DensityFunctions.TwoArgumentSimpleFunction::argument1, DensityFunctions.TwoArgumentSimpleFunction::argument2);
/* 1052 */         this.name = name;
/*      */       }
/*      */       
/*      */       public String getSerializedName() {
/* 1057 */         return this.name;
/*      */       }
/*      */     }
/*      */     
/*      */     default KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1069 */       return (KeyDispatchDataCodec)(type()).codec;
/*      */     }
/*      */   }
/*      */   
/*      */   public enum Type implements StringRepresentable {
/*      */     ADD("add"),
/*      */     MUL("mul"),
/*      */     MIN("min"),
/*      */     MAX("max");
/*      */     
/*      */     private final KeyDispatchDataCodec<DensityFunctions.TwoArgumentSimpleFunction> codec;
/*      */     
/*      */     private final String name;
/*      */     
/*      */     Type(String name) {
/*      */       this.codec = DensityFunctions.doubleFunctionArgumentCodec((argument1, argument2) -> DensityFunctions.TwoArgumentSimpleFunction.create(this, argument1, argument2), DensityFunctions.TwoArgumentSimpleFunction::argument1, DensityFunctions.TwoArgumentSimpleFunction::argument2);
/*      */       this.name = name;
/*      */     }
/*      */     
/*      */     public String getSerializedName() {
/*      */       return this.name;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class MulOrAdd extends Record implements TwoArgumentSimpleFunction, PureTransformer {
/*      */     private final Type specificType;
/*      */     
/*      */     private final DensityFunction input;
/*      */     
/*      */     private final double minValue;
/*      */     
/*      */     private final double maxValue;
/*      */     
/*      */     private final double argument;
/*      */     
/*      */     private MulOrAdd(Type specificType, DensityFunction input, double minValue, double maxValue, double argument) {
/* 1073 */       this.specificType = specificType;
/* 1073 */       this.input = input;
/* 1073 */       this.minValue = minValue;
/* 1073 */       this.maxValue = maxValue;
/* 1073 */       this.argument = argument;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1073	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1073	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1073	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public Type specificType() {
/* 1073 */       return this.specificType;
/*      */     }
/*      */     
/*      */     public DensityFunction input() {
/* 1073 */       return this.input;
/*      */     }
/*      */     
/*      */     public double minValue() {
/* 1073 */       return this.minValue;
/*      */     }
/*      */     
/*      */     public double maxValue() {
/* 1073 */       return this.maxValue;
/*      */     }
/*      */     
/*      */     public double argument() {
/* 1073 */       return this.argument;
/*      */     }
/*      */     
/*      */     public enum Type {
/* 1075 */       MUL, ADD;
/*      */     }
/*      */     
/*      */     public DensityFunctions.TwoArgumentSimpleFunction.Type type() {
/* 1081 */       return (this.specificType == Type.MUL) ? DensityFunctions.TwoArgumentSimpleFunction.Type.MUL : DensityFunctions.TwoArgumentSimpleFunction.Type.ADD;
/*      */     }
/*      */     
/*      */     public DensityFunction argument1() {
/* 1086 */       return DensityFunctions.constant(this.argument);
/*      */     }
/*      */     
/*      */     public DensityFunction argument2() {
/* 1091 */       return this.input;
/*      */     }
/*      */     
/*      */     public double transform(double input) {
/* 1096 */       switch (this.specificType.ordinal()) {
/*      */         default:
/* 1096 */           throw new MatchException(null, null);
/*      */         case 0:
/*      */         
/*      */         case 1:
/*      */           break;
/*      */       } 
/* 1096 */       return 
/*      */         
/* 1098 */         input + this.argument;
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*      */       double minValue, maxValue;
/* 1104 */       DensityFunction function = visitor.apply(this.input);
/* 1105 */       double min = function.minValue();
/* 1106 */       double max = function.maxValue();
/* 1109 */       if (this.specificType == Type.ADD) {
/* 1110 */         minValue = min + this.argument;
/* 1111 */         maxValue = max + this.argument;
/* 1112 */       } else if (this.argument >= 0.0D) {
/* 1113 */         minValue = min * this.argument;
/* 1114 */         maxValue = max * this.argument;
/*      */       } else {
/* 1116 */         minValue = max * this.argument;
/* 1117 */         maxValue = min * this.argument;
/*      */       } 
/* 1119 */       return new MulOrAdd(this.specificType, function, minValue, maxValue, this.argument);
/*      */     }
/*      */   }
/*      */   
/*      */   public enum Type {
/*      */     MUL, ADD;
/*      */   }
/*      */   
/*      */   private static final class Ap2 extends Record implements TwoArgumentSimpleFunction {
/*      */     private final DensityFunctions.TwoArgumentSimpleFunction.Type type;
/*      */     
/*      */     private final DensityFunction argument1;
/*      */     
/*      */     private final DensityFunction argument2;
/*      */     
/*      */     private final double minValue;
/*      */     
/*      */     private final double maxValue;
/*      */     
/*      */     private Ap2(DensityFunctions.TwoArgumentSimpleFunction.Type type, DensityFunction argument1, DensityFunction argument2, double minValue, double maxValue) {
/* 1123 */       this.type = type;
/* 1123 */       this.argument1 = argument1;
/* 1123 */       this.argument2 = argument2;
/* 1123 */       this.minValue = minValue;
/* 1123 */       this.maxValue = maxValue;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1123	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1123	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1123	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public DensityFunctions.TwoArgumentSimpleFunction.Type type() {
/* 1123 */       return this.type;
/*      */     }
/*      */     
/*      */     public DensityFunction argument1() {
/* 1123 */       return this.argument1;
/*      */     }
/*      */     
/*      */     public DensityFunction argument2() {
/* 1123 */       return this.argument2;
/*      */     }
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/* 1126 */       double v1 = this.argument1.compute(context);
/* 1127 */       switch (this.type.ordinal()) {
/*      */         default:
/* 1127 */           throw new MatchException(null, null);
/*      */         case 0:
/*      */         
/*      */         case 1:
/* 1127 */           return 
/*      */             
/* 1130 */             (v1 == 0.0D) ? 
/* 1131 */             0.0D : (
/*      */             
/* 1133 */             v1 * this.argument2.compute(context));
/*      */         case 2:
/* 1136 */           return (v1 < this.argument2.minValue()) ? 
/* 1137 */             v1 : 
/*      */             
/* 1139 */             Math.min(v1, this.argument2.compute(context));
/*      */         case 3:
/*      */           break;
/*      */       } 
/* 1142 */       return (v1 > this.argument2.maxValue()) ? 
/* 1143 */         v1 : 
/*      */         
/* 1145 */         Math.max(v1, this.argument2.compute(context));
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*      */       double[] v2;
/*      */       int i;
/*      */       double min, max;
/*      */       int j, k;
/* 1152 */       this.argument1.fillArray(output, contextProvider);
/* 1153 */       switch (this.type.ordinal()) {
/*      */         case 0:
/* 1155 */           v2 = new double[output.length];
/* 1156 */           this.argument2.fillArray(v2, contextProvider);
/* 1157 */           for (j = 0; j < output.length; j++)
/* 1158 */             output[j] = output[j] + v2[j]; 
/*      */           break;
/*      */         case 1:
/* 1162 */           for (i = 0; i < output.length; i++) {
/* 1163 */             double v = output[i];
/* 1164 */             output[i] = (v == 0.0D) ? 0.0D : (v * this.argument2.compute(contextProvider.forIndex(i)));
/*      */           } 
/*      */           break;
/*      */         case 2:
/* 1168 */           min = this.argument2.minValue();
/* 1169 */           for (k = 0; k < output.length; k++) {
/* 1170 */             double v = output[k];
/* 1171 */             output[k] = (v < min) ? v : Math.min(v, this.argument2.compute(contextProvider.forIndex(k)));
/*      */           } 
/*      */           break;
/*      */         case 3:
/* 1175 */           max = this.argument2.maxValue();
/* 1176 */           for (k = 0; k < output.length; k++) {
/* 1177 */             double v = output[k];
/* 1178 */             output[k] = (v > max) ? v : Math.max(v, this.argument2.compute(contextProvider.forIndex(k)));
/*      */           } 
/*      */           break;
/*      */       } 
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 1186 */       return DensityFunctions.TwoArgumentSimpleFunction.create(this.type, visitor.apply(this.argument1), visitor.apply(this.argument2));
/*      */     }
/*      */     
/*      */     public double minValue() {
/* 1191 */       return this.minValue;
/*      */     }
/*      */     
/*      */     public double maxValue() {
/* 1196 */       return this.maxValue;
/*      */     }
/*      */   }
/*      */   
/*      */   public static final class Spline implements DensityFunction {
/* 1201 */     private static final Codec<CubicSpline<Coordinate>> SPLINE_CODEC = CubicSpline.codec(Coordinate.CODEC);
/*      */     
/* 1202 */     private static final MapCodec<Spline> DATA_CODEC = SPLINE_CODEC.fieldOf("spline").xmap(Spline::new, Spline::spline);
/*      */     
/* 1204 */     public static final KeyDispatchDataCodec<Spline> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */     
/*      */     private final CubicSpline<Coordinate> spline;
/*      */     
/*      */     private final BoundedFloatFunction<Point> sampler;
/*      */     
/*      */     public Spline(CubicSpline<Coordinate> spline) {
/* 1210 */       this.spline = spline;
/* 1211 */       this.sampler = CubicSpline.asSampler(spline);
/*      */     }
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/* 1216 */       return this.sampler.apply(new Point(context));
/*      */     }
/*      */     
/*      */     public double minValue() {
/* 1221 */       return this.spline.minValue();
/*      */     }
/*      */     
/*      */     public double maxValue() {
/* 1226 */       return this.spline.maxValue();
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 1231 */       contextProvider.fillAllDirectly(output, this);
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 1236 */       return new Spline(this.spline.mapCoordinates(c -> c.mapChildren(visitor)));
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1241 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */     
/*      */     public CubicSpline<Coordinate> spline() {
/* 1245 */       return this.spline;
/*      */     }
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1250 */       if (obj == this)
/* 1251 */         return true; 
/* 1253 */       if (obj instanceof Spline) {
/* 1253 */         Spline splineFunction = (Spline)obj;
/* 1253 */         if (this.spline.equals(splineFunction.spline));
/*      */       } 
/* 1253 */       return false;
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1258 */       return this.spline.hashCode();
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1263 */       return this.spline.toString();
/*      */     }
/*      */     
/*      */     public static final class Coordinate extends Record implements BoundedFloatFunction<Point> {
/*      */       private final DensityFunction function;
/*      */       
/*      */       public Coordinate(DensityFunction function) {
/* 1266 */         this.function = function;
/*      */       }
/*      */       
/*      */       public final String toString() {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;)Ljava/lang/String;
/*      */         //   6: areturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1266	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/*      */       }
/*      */       
/*      */       public final int hashCode() {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;)I
/*      */         //   6: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1266	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/*      */       }
/*      */       
/*      */       public final boolean equals(Object o) {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: aload_1
/*      */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;Ljava/lang/Object;)Z
/*      */         //   7: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1266	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/*      */         //   0	8	1	o	Ljava/lang/Object;
/*      */       }
/*      */       
/*      */       public DensityFunction function() {
/* 1266 */         return this.function;
/*      */       }
/*      */       
/* 1267 */       public static final Codec<Coordinate> CODEC = DensityFunction.CODEC.xmap(Coordinate::new, Coordinate::function);
/*      */       
/*      */       public float apply(DensityFunctions.Spline.Point point) {
/* 1271 */         return (float)this.function.compute(point.context());
/*      */       }
/*      */       
/*      */       public float minValue() {
/* 1276 */         return (float)this.function.minValue();
/*      */       }
/*      */       
/*      */       public float maxValue() {
/* 1281 */         return (float)this.function.maxValue();
/*      */       }
/*      */       
/*      */       public Coordinate mapChildren(DensityFunction.Visitor visitor) {
/* 1285 */         return new Coordinate(visitor.apply(this.function));
/*      */       }
/*      */     }
/*      */     
/*      */     public static final class Point extends Record {
/*      */       private final DensityFunction.FunctionContext context;
/*      */       
/*      */       public Point(DensityFunction.FunctionContext context) {
/* 1289 */         this.context = context;
/*      */       }
/*      */       
/*      */       public final String toString() {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;)Ljava/lang/String;
/*      */         //   6: areturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1289	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;
/*      */       }
/*      */       
/*      */       public final int hashCode() {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;)I
/*      */         //   6: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1289	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;
/*      */       }
/*      */       
/*      */       public final boolean equals(Object o) {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: aload_1
/*      */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;Ljava/lang/Object;)Z
/*      */         //   7: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1289	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;
/*      */         //   0	8	1	o	Ljava/lang/Object;
/*      */       }
/*      */       
/*      */       public DensityFunction.FunctionContext context() {
/* 1289 */         return this.context;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static final class Coordinate extends Record implements BoundedFloatFunction<Spline.Point> {
/*      */     private final DensityFunction function;
/*      */     
/*      */     public Coordinate(DensityFunction function) {
/*      */       this.function = function;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1266	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1266	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1266	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public DensityFunction function() {
/*      */       return this.function;
/*      */     }
/*      */     
/*      */     public static final Codec<Coordinate> CODEC = DensityFunction.CODEC.xmap(Coordinate::new, Coordinate::function);
/*      */     
/*      */     public float apply(DensityFunctions.Spline.Point point) {
/*      */       return (float)this.function.compute(point.context());
/*      */     }
/*      */     
/*      */     public float minValue() {
/*      */       return (float)this.function.minValue();
/*      */     }
/*      */     
/*      */     public float maxValue() {
/*      */       return (float)this.function.maxValue();
/*      */     }
/*      */     
/*      */     public Coordinate mapChildren(DensityFunction.Visitor visitor) {
/*      */       return new Coordinate(visitor.apply(this.function));
/*      */     }
/*      */   }
/*      */   
/*      */   public static final class Point extends Record {
/*      */     private final DensityFunction.FunctionContext context;
/*      */     
/*      */     public Point(DensityFunction.FunctionContext context) {
/* 1289 */       this.context = context;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1289	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1289	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1289	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public DensityFunction.FunctionContext context() {
/* 1289 */       return this.context;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class Constant extends Record implements DensityFunction.SimpleFunction {
/*      */     private final double value;
/*      */     
/*      */     private Constant(double value) {
/* 1292 */       this.value = value;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1292	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1292	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1292	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public double value() {
/* 1292 */       return this.value;
/*      */     }
/*      */     
/* 1293 */     private static final KeyDispatchDataCodec<Constant> CODEC = DensityFunctions.singleArgumentCodec(DensityFunctions.NOISE_VALUE_CODEC, Constant::new, Constant::value);
/*      */     
/* 1294 */     private static final Constant ZERO = new Constant(0.0D);
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/* 1298 */       return this.value;
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 1303 */       Arrays.fill(output, this.value);
/*      */     }
/*      */     
/*      */     public double minValue() {
/* 1308 */       return this.value;
/*      */     }
/*      */     
/*      */     public double maxValue() {
/* 1313 */       return this.value;
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1318 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class YClampedGradient extends Record implements DensityFunction.SimpleFunction {
/*      */     private final int fromY;
/*      */     
/*      */     private final int toY;
/*      */     
/*      */     private final double fromValue;
/*      */     
/*      */     private final double toValue;
/*      */     
/*      */     private static final MapCodec<YClampedGradient> DATA_CODEC;
/*      */     
/*      */     private YClampedGradient(int fromY, int toY, double fromValue, double toValue) {
/* 1322 */       this.fromY = fromY;
/* 1322 */       this.toY = toY;
/* 1322 */       this.fromValue = fromValue;
/* 1322 */       this.toValue = toValue;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1322	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1322	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1322	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public int fromY() {
/* 1322 */       return this.fromY;
/*      */     }
/*      */     
/*      */     public int toY() {
/* 1322 */       return this.toY;
/*      */     }
/*      */     
/*      */     public double fromValue() {
/* 1322 */       return this.fromValue;
/*      */     }
/*      */     
/*      */     public double toValue() {
/* 1322 */       return this.toValue;
/*      */     }
/*      */     
/*      */     static {
/* 1323 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("from_y").forGetter(YClampedGradient::fromY), (App)Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("to_y").forGetter(YClampedGradient::toY), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("from_value").forGetter(YClampedGradient::fromValue), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("to_value").forGetter(YClampedGradient::toValue)).apply((Applicative)i, YClampedGradient::new));
/*      */     }
/*      */     
/* 1330 */     public static final KeyDispatchDataCodec<YClampedGradient> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/* 1334 */       return Mth.clampedMap(context.blockY(), this.fromY, this.toY, this.fromValue, this.toValue);
/*      */     }
/*      */     
/*      */     public double minValue() {
/* 1339 */       return Math.min(this.fromValue, this.toValue);
/*      */     }
/*      */     
/*      */     public double maxValue() {
/* 1344 */       return Math.max(this.fromValue, this.toValue);
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1349 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class FindTopSurface extends Record implements DensityFunction {
/*      */     private final DensityFunction density;
/*      */     
/*      */     private final DensityFunction upperBound;
/*      */     
/*      */     private final int lowerBound;
/*      */     
/*      */     private final int cellHeight;
/*      */     
/*      */     private static final MapCodec<FindTopSurface> DATA_CODEC;
/*      */     
/*      */     private FindTopSurface(DensityFunction density, DensityFunction upperBound, int lowerBound, int cellHeight) {
/* 1353 */       this.density = density;
/* 1353 */       this.upperBound = upperBound;
/* 1353 */       this.lowerBound = lowerBound;
/* 1353 */       this.cellHeight = cellHeight;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1353	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1353	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1353	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public DensityFunction density() {
/* 1353 */       return this.density;
/*      */     }
/*      */     
/*      */     public DensityFunction upperBound() {
/* 1353 */       return this.upperBound;
/*      */     }
/*      */     
/*      */     public int lowerBound() {
/* 1353 */       return this.lowerBound;
/*      */     }
/*      */     
/*      */     public int cellHeight() {
/* 1353 */       return this.cellHeight;
/*      */     }
/*      */     
/*      */     static {
/* 1354 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.CODEC.fieldOf("density").forGetter(FindTopSurface::density), (App)DensityFunction.CODEC.fieldOf("upper_bound").forGetter(FindTopSurface::upperBound), (App)Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("lower_bound").forGetter(FindTopSurface::lowerBound), (App)ExtraCodecs.POSITIVE_INT.fieldOf("cell_height").forGetter(FindTopSurface::cellHeight)).apply((Applicative)i, FindTopSurface::new));
/*      */     }
/*      */     
/* 1361 */     public static final KeyDispatchDataCodec<FindTopSurface> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/* 1365 */       int topY = Mth.floor(this.upperBound.compute(context) / this.cellHeight) * this.cellHeight;
/* 1366 */       if (topY <= this.lowerBound)
/* 1367 */         return this.lowerBound; 
/* 1370 */       for (int blockY = topY; blockY >= this.lowerBound; blockY -= this.cellHeight) {
/* 1371 */         if (this.density.compute(new DensityFunction.SinglePointContext(context.blockX(), blockY, context.blockZ())) > 0.0D)
/* 1372 */           return blockY; 
/*      */       } 
/* 1375 */       return this.lowerBound;
/*      */     }
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 1380 */       contextProvider.fillAllDirectly(output, this);
/*      */     }
/*      */     
/*      */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 1385 */       return new FindTopSurface(visitor
/* 1386 */           .apply(this.density), visitor
/* 1387 */           .apply(this.upperBound), this.lowerBound, this.cellHeight);
/*      */     }
/*      */     
/*      */     public double minValue() {
/* 1395 */       return this.lowerBound;
/*      */     }
/*      */     
/*      */     public double maxValue() {
/* 1400 */       return Math.max(this.lowerBound, this.upperBound.maxValue());
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1405 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */