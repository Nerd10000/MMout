/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ 
/*     */ @VisibleForDebug
/*     */ public final class Multipoint<I extends BoundedFloatFunction<?>> extends Record implements CubicSpline<I> {
/*     */   private final I coordinate;
/*     */   
/*     */   private final float[] locations;
/*     */   
/*     */   private final List<CubicSpline<I>> values;
/*     */   
/*     */   private final float[] derivatives;
/*     */   
/*     */   private final float minValue;
/*     */   
/*     */   private final float maxValue;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/CubicSpline$Multipoint;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #64	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint<TI;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/CubicSpline$Multipoint;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #64	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint<TI;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/CubicSpline$Multipoint;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #64	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Multipoint;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Multipoint<TI;>;
/*     */   }
/*     */   
/*     */   public I coordinate() {
/*  64 */     return this.coordinate;
/*     */   }
/*     */   
/*     */   public float[] locations() {
/*  64 */     return this.locations;
/*     */   }
/*     */   
/*     */   public List<CubicSpline<I>> values() {
/*  64 */     return this.values;
/*     */   }
/*     */   
/*     */   public float[] derivatives() {
/*  64 */     return this.derivatives;
/*     */   }
/*     */   
/*     */   public float minValue() {
/*  64 */     return this.minValue;
/*     */   }
/*     */   
/*     */   public float maxValue() {
/*  64 */     return this.maxValue;
/*     */   }
/*     */   
/*     */   public Multipoint(I coordinate, float[] locations, List<CubicSpline<I>> values, float[] derivatives, float minValue, float maxValue) {
/*  74 */     validateSizes(locations, values, derivatives);
/*     */     this.coordinate = coordinate;
/*     */     this.locations = locations;
/*     */     this.values = values;
/*     */     this.derivatives = derivatives;
/*     */     this.minValue = minValue;
/*     */     this.maxValue = maxValue;
/*     */   }
/*     */   
/*     */   public Multipoint(I coordinate, float[] locations, List<CubicSpline<I>> values, float[] derivatives) {
/* 163 */     this(coordinate, locations, values, derivatives, minValue, maxValue);
/*     */   }
/*     */   
/*     */   private static float linearExtend(float input, float[] locations, float value, float[] derivatives, int index) {
/* 167 */     float derivative = derivatives[index];
/* 168 */     if (derivative == 0.0F)
/* 170 */       return value; 
/* 172 */     return value + derivative * (input - locations[index]);
/*     */   }
/*     */   
/*     */   private static <I> void validateSizes(float[] locations, List<CubicSpline<I>> values, float[] derivatives) {
/* 176 */     if (locations.length != values.size() || locations.length != derivatives.length)
/* 177 */       throw new IllegalArgumentException("All lengths must be equal, got: " + locations.length + " " + values.size() + " " + derivatives.length); 
/* 179 */     if (locations.length == 0)
/* 180 */       throw new IllegalArgumentException("Cannot create a multipoint spline with no points"); 
/*     */   }
/*     */   
/*     */   public static <C, I extends BoundedFloatFunction<C>> float sample(Multipoint<I> sampler, C c) {
/* 185 */     return sample(sampler.coordinate, sampler.derivatives, sampler.locations, sampler.values, c);
/*     */   }
/*     */   
/*     */   private static <C, I extends BoundedFloatFunction<C>> float sample(I coordinate, float[] derivatives, float[] locations, List<CubicSpline<I>> values, C c) {
/* 189 */     float input = coordinate.apply(c);
/* 190 */     int start = findIntervalStart(locations, input);
/* 192 */     int lastIndex = locations.length - 1;
/* 195 */     if (start < 0)
/* 196 */       return linearExtend(input, locations, CubicSpline.sample((CubicSpline<BoundedFloatFunction<C>>)values.getFirst(), c), derivatives, 0); 
/* 198 */     if (start == lastIndex)
/* 199 */       return linearExtend(input, locations, CubicSpline.sample((CubicSpline<BoundedFloatFunction<C>>)values.get(lastIndex), c), derivatives, lastIndex); 
/* 201 */     float x1 = locations[start];
/* 202 */     float x2 = locations[start + 1];
/* 203 */     float t = (input - x1) / (x2 - x1);
/* 205 */     CubicSpline<I> f1 = values.get(start);
/* 206 */     CubicSpline<I> f2 = values.get(start + 1);
/* 207 */     float d1 = derivatives[start];
/* 208 */     float d2 = derivatives[start + 1];
/* 210 */     float y1 = CubicSpline.sample(f1, c);
/* 211 */     float y2 = CubicSpline.sample(f2, c);
/* 213 */     float a = d1 * (x2 - x1) - y2 - y1;
/* 214 */     float b = -d2 * (x2 - x1) + y2 - y1;
/* 217 */     float offset = Mth.lerp(t, y1, y2) + t * (1.0F - t) * Mth.lerp(t, a, b);
/* 218 */     return offset;
/*     */   }
/*     */   
/*     */   private static int findIntervalStart(float[] locations, float input) {
/* 224 */     return Mth.binarySearch(0, locations.length, i -> (input < locations[i])) - 1;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public String parityString() {
/* 230 */     return "Spline{coordinate=" + String.valueOf(this.coordinate) + ", locations=" + toString(this.locations) + ", derivatives=" + toString(this.derivatives) + ", values=" + (String)this.values.stream().map(CubicSpline::parityString).collect(Collectors.joining(", ", "[", "]")) + "}";
/*     */   }
/*     */   
/*     */   private static String toString(float[] arr) {
/* 234 */     return "[" + (String)IntStream.range(0, arr.length).mapToDouble(i -> arr[i]).<CharSequence>mapToObj(f -> String.format(Locale.ROOT, "%.3f", new Object[] { Double.valueOf(f) })).collect(Collectors.joining(", ")) + "]";
/*     */   }
/*     */   
/*     */   public CubicSpline<I> mapCoordinates(UnaryOperator<I> mapper) {
/* 239 */     return new Multipoint(mapper
/* 240 */         .apply(this.coordinate), this.locations, this.values
/*     */         
/* 242 */         .stream().map(v -> v.mapCoordinates(mapper)).toList(), this.derivatives);
/*     */   }
/*     */   
/*     */   public static <I extends BoundedFloatFunction<?>> Codec<Multipoint<I>> codec(Codec<I> coordinateCodec, Codec<CubicSpline<I>> subSplineCodec) {
/* 248 */     return RecordCodecBuilder.create(i -> i.group((App)coordinateCodec.fieldOf("coordinate").forGetter(Multipoint::coordinate), (App)ExtraCodecs.<T>nonEmptyList(Point.<BoundedFloatFunction<?>>codec(subSplineCodec).listOf()).fieldOf("points").forGetter(Multipoint::packToPoints)).apply((Applicative)i, Multipoint::createFromPoints));
/*     */   }
/*     */   
/*     */   private List<Point<I>> packToPoints() {
/* 255 */     int pointCount = this.locations.length;
/* 256 */     List<Point<I>> list = new ArrayList<>(pointCount);
/* 257 */     for (int p = 0; p < pointCount; p++)
/* 258 */       list.add(new Point<>(this.locations[p], this.values.get(p), this.derivatives[p])); 
/* 260 */     return list;
/*     */   }
/*     */   
/*     */   private static <I extends BoundedFloatFunction<?>> Multipoint<I> createFromPoints(I coordinate, List<Point<I>> points) {
/* 264 */     int pointCount = points.size();
/* 265 */     float[] locations = new float[pointCount];
/* 266 */     ImmutableList.Builder<CubicSpline<I>> values = ImmutableList.builderWithExpectedSize(pointCount);
/* 267 */     float[] derivatives = new float[pointCount];
/* 268 */     for (int p = 0; p < pointCount; p++) {
/* 269 */       Point<I> point = points.get(p);
/* 270 */       locations[p] = point.location();
/* 271 */       values.add(point.value());
/* 272 */       derivatives[p] = point.derivative();
/*     */     } 
/* 274 */     return new Multipoint<>(coordinate, locations, (List<CubicSpline<I>>)values.build(), derivatives);
/*     */   }
/*     */   
/*     */   private static final class Point<I extends BoundedFloatFunction<?>> extends Record {
/*     */     private final float location;
/*     */     
/*     */     private final CubicSpline<I> value;
/*     */     
/*     */     private final float derivative;
/*     */     
/*     */     private Point(float location, CubicSpline<I> value, float derivative) {
/* 277 */       this.location = location;
/* 277 */       this.value = value;
/* 277 */       this.derivative = derivative;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/CubicSpline$Multipoint$Point;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #277	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point<TI;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/CubicSpline$Multipoint$Point;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #277	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point<TI;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/CubicSpline$Multipoint$Point;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #277	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point<TI;>;
/*     */     }
/*     */     
/*     */     public float location() {
/* 277 */       return this.location;
/*     */     }
/*     */     
/*     */     public CubicSpline<I> value() {
/* 277 */       return this.value;
/*     */     }
/*     */     
/*     */     public float derivative() {
/* 277 */       return this.derivative;
/*     */     }
/*     */     
/*     */     public static <I extends BoundedFloatFunction<?>> Codec<Point<I>> codec(Codec<CubicSpline<I>> subSplineCodec) {
/* 279 */       return RecordCodecBuilder.create(i -> i.group((App)Codec.FLOAT.fieldOf("location").forGetter(Point::location), (App)subSplineCodec.fieldOf("value").forGetter(Point::value), (App)Codec.FLOAT.fieldOf("derivative").forGetter(Point::derivative)).apply((Applicative)i, Point::new));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\CubicSpline$Multipoint.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */