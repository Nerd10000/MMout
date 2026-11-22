/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
/*     */ import it.unimi.dsi.fastutil.floats.FloatArrayList;
/*     */ import it.unimi.dsi.fastutil.floats.FloatList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ 
/*     */ public interface CubicSpline<I> {
/*     */   static <C, I extends BoundedFloatFunction<C>> float sample(CubicSpline<I> spline, C coordinate) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_2
/*     */     //   7: iconst_0
/*     */     //   8: istore_3
/*     */     //   9: aload_2
/*     */     //   10: iload_3
/*     */     //   11: <illegal opcode> typeSwitch : (Lnet/minecraft/util/CubicSpline;I)I
/*     */     //   16: lookupswitch default -> 44, 0 -> 54, 1 -> 69
/*     */     //   44: new java/lang/MatchException
/*     */     //   47: dup
/*     */     //   48: aconst_null
/*     */     //   49: aconst_null
/*     */     //   50: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   53: athrow
/*     */     //   54: aload_2
/*     */     //   55: checkcast net/minecraft/util/CubicSpline$Multipoint
/*     */     //   58: astore #4
/*     */     //   60: aload #4
/*     */     //   62: aload_1
/*     */     //   63: invokestatic sample : (Lnet/minecraft/util/CubicSpline$Multipoint;Ljava/lang/Object;)F
/*     */     //   66: goto -> 80
/*     */     //   69: aload_2
/*     */     //   70: checkcast net/minecraft/util/CubicSpline$Constant
/*     */     //   73: astore #5
/*     */     //   75: aload #5
/*     */     //   77: invokevirtual value : ()F
/*     */     //   80: freturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     //   #33	-> 54
/*     */     //   #34	-> 69
/*     */     //   #32	-> 80
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   60	9	4	multipoint	Lnet/minecraft/util/CubicSpline$Multipoint;
/*     */     //   75	5	5	constant	Lnet/minecraft/util/CubicSpline$Constant;
/*     */     //   0	81	0	spline	Lnet/minecraft/util/CubicSpline;
/*     */     //   0	81	1	coordinate	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   60	9	4	multipoint	Lnet/minecraft/util/CubicSpline$Multipoint<TI;>;
/*     */     //   75	5	5	constant	Lnet/minecraft/util/CubicSpline$Constant<TI;>;
/*     */     //   0	81	0	spline	Lnet/minecraft/util/CubicSpline<TI;>;
/*     */     //   0	81	1	coordinate	TC;
/*     */   }
/*     */   
/*     */   static <C, I extends BoundedFloatFunction<C>> BoundedFloatFunction<C> asSampler(CubicSpline<I> spline) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_1
/*     */     //   7: iconst_0
/*     */     //   8: istore_2
/*     */     //   9: aload_1
/*     */     //   10: iload_2
/*     */     //   11: <illegal opcode> typeSwitch : (Lnet/minecraft/util/CubicSpline;I)I
/*     */     //   16: lookupswitch default -> 44, 0 -> 54, 1 -> 70
/*     */     //   44: new java/lang/MatchException
/*     */     //   47: dup
/*     */     //   48: aconst_null
/*     */     //   49: aconst_null
/*     */     //   50: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   53: athrow
/*     */     //   54: aload_1
/*     */     //   55: checkcast net/minecraft/util/CubicSpline$Multipoint
/*     */     //   58: astore_3
/*     */     //   59: new net/minecraft/util/CubicSpline$1
/*     */     //   62: dup
/*     */     //   63: aload_3
/*     */     //   64: invokespecial <init> : (Lnet/minecraft/util/CubicSpline$Multipoint;)V
/*     */     //   67: goto -> 84
/*     */     //   70: aload_1
/*     */     //   71: checkcast net/minecraft/util/CubicSpline$Constant
/*     */     //   74: astore #4
/*     */     //   76: aload #4
/*     */     //   78: invokevirtual value : ()F
/*     */     //   81: invokestatic constant : (F)Lnet/minecraft/util/BoundedFloatFunction;
/*     */     //   84: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #39	-> 0
/*     */     //   #40	-> 54
/*     */     //   #56	-> 70
/*     */     //   #39	-> 84
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   59	11	3	multipoint	Lnet/minecraft/util/CubicSpline$Multipoint;
/*     */     //   76	8	4	constant	Lnet/minecraft/util/CubicSpline$Constant;
/*     */     //   0	85	0	spline	Lnet/minecraft/util/CubicSpline;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   59	11	3	multipoint	Lnet/minecraft/util/CubicSpline$Multipoint<TI;>;
/*     */     //   76	8	4	constant	Lnet/minecraft/util/CubicSpline$Constant<TI;>;
/*     */     //   0	85	0	spline	Lnet/minecraft/util/CubicSpline<TI;>;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public static final class Multipoint<I extends BoundedFloatFunction<?>> extends Record implements CubicSpline<I> {
/*     */     private final I coordinate;
/*     */     
/*     */     private final float[] locations;
/*     */     
/*     */     private final List<CubicSpline<I>> values;
/*     */     
/*     */     private final float[] derivatives;
/*     */     
/*     */     private final float minValue;
/*     */     
/*     */     private final float maxValue;
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/CubicSpline$Multipoint;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint<TI;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/CubicSpline$Multipoint;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint<TI;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/CubicSpline$Multipoint;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Multipoint;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Multipoint<TI;>;
/*     */     }
/*     */     
/*     */     public I coordinate() {
/*  64 */       return this.coordinate;
/*     */     }
/*     */     
/*     */     public float[] locations() {
/*  64 */       return this.locations;
/*     */     }
/*     */     
/*     */     public List<CubicSpline<I>> values() {
/*  64 */       return this.values;
/*     */     }
/*     */     
/*     */     public float[] derivatives() {
/*  64 */       return this.derivatives;
/*     */     }
/*     */     
/*     */     public float minValue() {
/*  64 */       return this.minValue;
/*     */     }
/*     */     
/*     */     public float maxValue() {
/*  64 */       return this.maxValue;
/*     */     }
/*     */     
/*     */     public Multipoint(I coordinate, float[] locations, List<CubicSpline<I>> values, float[] derivatives, float minValue, float maxValue) {
/*  74 */       validateSizes(locations, values, derivatives);
/*     */       this.coordinate = coordinate;
/*     */       this.locations = locations;
/*     */       this.values = values;
/*     */       this.derivatives = derivatives;
/*     */       this.minValue = minValue;
/*     */       this.maxValue = maxValue;
/*     */     }
/*     */     
/*     */     public Multipoint(I coordinate, float[] locations, List<CubicSpline<I>> values, float[] derivatives) {
/* 163 */       this(coordinate, locations, values, derivatives, minValue, maxValue);
/*     */     }
/*     */     
/*     */     private static float linearExtend(float input, float[] locations, float value, float[] derivatives, int index) {
/* 167 */       float derivative = derivatives[index];
/* 168 */       if (derivative == 0.0F)
/* 170 */         return value; 
/* 172 */       return value + derivative * (input - locations[index]);
/*     */     }
/*     */     
/*     */     private static <I> void validateSizes(float[] locations, List<CubicSpline<I>> values, float[] derivatives) {
/* 176 */       if (locations.length != values.size() || locations.length != derivatives.length)
/* 177 */         throw new IllegalArgumentException("All lengths must be equal, got: " + locations.length + " " + values.size() + " " + derivatives.length); 
/* 179 */       if (locations.length == 0)
/* 180 */         throw new IllegalArgumentException("Cannot create a multipoint spline with no points"); 
/*     */     }
/*     */     
/*     */     public static <C, I extends BoundedFloatFunction<C>> float sample(Multipoint<I> sampler, C c) {
/* 185 */       return sample(sampler.coordinate, sampler.derivatives, sampler.locations, sampler.values, c);
/*     */     }
/*     */     
/*     */     private static <C, I extends BoundedFloatFunction<C>> float sample(I coordinate, float[] derivatives, float[] locations, List<CubicSpline<I>> values, C c) {
/* 189 */       float input = coordinate.apply(c);
/* 190 */       int start = findIntervalStart(locations, input);
/* 192 */       int lastIndex = locations.length - 1;
/* 195 */       if (start < 0)
/* 196 */         return linearExtend(input, locations, CubicSpline.sample((CubicSpline<BoundedFloatFunction<C>>)values.getFirst(), c), derivatives, 0); 
/* 198 */       if (start == lastIndex)
/* 199 */         return linearExtend(input, locations, CubicSpline.sample((CubicSpline<BoundedFloatFunction<C>>)values.get(lastIndex), c), derivatives, lastIndex); 
/* 201 */       float x1 = locations[start];
/* 202 */       float x2 = locations[start + 1];
/* 203 */       float t = (input - x1) / (x2 - x1);
/* 205 */       CubicSpline<I> f1 = values.get(start);
/* 206 */       CubicSpline<I> f2 = values.get(start + 1);
/* 207 */       float d1 = derivatives[start];
/* 208 */       float d2 = derivatives[start + 1];
/* 210 */       float y1 = CubicSpline.sample(f1, c);
/* 211 */       float y2 = CubicSpline.sample(f2, c);
/* 213 */       float a = d1 * (x2 - x1) - y2 - y1;
/* 214 */       float b = -d2 * (x2 - x1) + y2 - y1;
/* 217 */       float offset = Mth.lerp(t, y1, y2) + t * (1.0F - t) * Mth.lerp(t, a, b);
/* 218 */       return offset;
/*     */     }
/*     */     
/*     */     private static int findIntervalStart(float[] locations, float input) {
/* 224 */       return Mth.binarySearch(0, locations.length, i -> (input < locations[i])) - 1;
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     public String parityString() {
/* 230 */       return "Spline{coordinate=" + String.valueOf(this.coordinate) + ", locations=" + toString(this.locations) + ", derivatives=" + toString(this.derivatives) + ", values=" + (String)this.values.stream().map(CubicSpline::parityString).collect(Collectors.joining(", ", "[", "]")) + "}";
/*     */     }
/*     */     
/*     */     private static String toString(float[] arr) {
/* 234 */       return "[" + (String)IntStream.range(0, arr.length).mapToDouble(i -> arr[i]).<CharSequence>mapToObj(f -> String.format(Locale.ROOT, "%.3f", new Object[] { Double.valueOf(f) })).collect(Collectors.joining(", ")) + "]";
/*     */     }
/*     */     
/*     */     public CubicSpline<I> mapCoordinates(UnaryOperator<I> mapper) {
/* 239 */       return new Multipoint(mapper
/* 240 */           .apply(this.coordinate), this.locations, this.values
/*     */           
/* 242 */           .stream().map(v -> v.mapCoordinates(mapper)).toList(), this.derivatives);
/*     */     }
/*     */     
/*     */     public static <I extends BoundedFloatFunction<?>> Codec<Multipoint<I>> codec(Codec<I> coordinateCodec, Codec<CubicSpline<I>> subSplineCodec) {
/* 248 */       return RecordCodecBuilder.create(i -> i.group((App)coordinateCodec.fieldOf("coordinate").forGetter(Multipoint::coordinate), (App)ExtraCodecs.<T>nonEmptyList(Point.<BoundedFloatFunction<?>>codec(subSplineCodec).listOf()).fieldOf("points").forGetter(Multipoint::packToPoints)).apply((Applicative)i, Multipoint::createFromPoints));
/*     */     }
/*     */     
/*     */     private List<Point<I>> packToPoints() {
/* 255 */       int pointCount = this.locations.length;
/* 256 */       List<Point<I>> list = new ArrayList<>(pointCount);
/* 257 */       for (int p = 0; p < pointCount; p++)
/* 258 */         list.add(new Point<>(this.locations[p], this.values.get(p), this.derivatives[p])); 
/* 260 */       return list;
/*     */     }
/*     */     
/*     */     private static <I extends BoundedFloatFunction<?>> Multipoint<I> createFromPoints(I coordinate, List<Point<I>> points) {
/* 264 */       int pointCount = points.size();
/* 265 */       float[] locations = new float[pointCount];
/* 266 */       ImmutableList.Builder<CubicSpline<I>> values = ImmutableList.builderWithExpectedSize(pointCount);
/* 267 */       float[] derivatives = new float[pointCount];
/* 268 */       for (int p = 0; p < pointCount; p++) {
/* 269 */         Point<I> point = points.get(p);
/* 270 */         locations[p] = point.location();
/* 271 */         values.add(point.value());
/* 272 */         derivatives[p] = point.derivative();
/*     */       } 
/* 274 */       return new Multipoint<>(coordinate, locations, (List<CubicSpline<I>>)values.build(), derivatives);
/*     */     }
/*     */     
/*     */     private static final class Point<I extends BoundedFloatFunction<?>> extends Record {
/*     */       private final float location;
/*     */       
/*     */       private final CubicSpline<I> value;
/*     */       
/*     */       private final float derivative;
/*     */       
/*     */       private Point(float location, CubicSpline<I> value, float derivative) {
/* 277 */         this.location = location;
/* 277 */         this.value = value;
/* 277 */         this.derivative = derivative;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/util/CubicSpline$Multipoint$Point;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #277	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point<TI;>;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/CubicSpline$Multipoint$Point;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #277	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point<TI;>;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/util/CubicSpline$Multipoint$Point;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #277	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point<TI;>;
/*     */       }
/*     */       
/*     */       public float location() {
/* 277 */         return this.location;
/*     */       }
/*     */       
/*     */       public CubicSpline<I> value() {
/* 277 */         return this.value;
/*     */       }
/*     */       
/*     */       public float derivative() {
/* 277 */         return this.derivative;
/*     */       }
/*     */       
/*     */       public static <I extends BoundedFloatFunction<?>> Codec<Point<I>> codec(Codec<CubicSpline<I>> subSplineCodec) {
/* 279 */         return RecordCodecBuilder.create(i -> i.group((App)Codec.FLOAT.fieldOf("location").forGetter(Point::location), (App)subSplineCodec.fieldOf("value").forGetter(Point::value), (App)Codec.FLOAT.fieldOf("derivative").forGetter(Point::derivative)).apply((Applicative)i, Point::new));
/*     */       }
/*     */     }
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
/*     */       this.location = location;
/*     */       this.value = value;
/*     */       this.derivative = derivative;
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
/*     */       return this.location;
/*     */     }
/*     */     
/*     */     public CubicSpline<I> value() {
/*     */       return this.value;
/*     */     }
/*     */     
/*     */     public float derivative() {
/*     */       return this.derivative;
/*     */     }
/*     */     
/*     */     public static <I extends BoundedFloatFunction<?>> Codec<Point<I>> codec(Codec<CubicSpline<I>> subSplineCodec) {
/* 279 */       return RecordCodecBuilder.create(i -> i.group((App)Codec.FLOAT.fieldOf("location").forGetter(Point::location), (App)subSplineCodec.fieldOf("value").forGetter(Point::value), (App)Codec.FLOAT.fieldOf("derivative").forGetter(Point::derivative)).apply((Applicative)i, Point::new));
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public static final class Constant<I> extends Record implements CubicSpline<I> {
/*     */     private final float value;
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/CubicSpline$Constant;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Constant;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Constant<TI;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/CubicSpline$Constant;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Constant;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Constant<TI;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/CubicSpline$Constant;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Constant;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Constant<TI;>;
/*     */     }
/*     */     
/*     */     public float value() {
/* 288 */       return this.value;
/*     */     }
/*     */     
/*     */     public Constant(float value) {
/* 289 */       this.value = value;
/*     */     }
/*     */     
/*     */     public String parityString() {
/* 292 */       return String.format(Locale.ROOT, "k=%.3f", new Object[] { Float.valueOf(this.value) });
/*     */     }
/*     */     
/*     */     public float minValue() {
/* 297 */       return this.value;
/*     */     }
/*     */     
/*     */     public float maxValue() {
/* 302 */       return this.value;
/*     */     }
/*     */     
/*     */     public CubicSpline<I> mapCoordinates(UnaryOperator<I> mapper) {
/* 307 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   static <I extends BoundedFloatFunction<?>> Codec<CubicSpline<I>> codec(Codec<I> coordinateCodec) {
/* 312 */     return Codec.recursive("CubicSpline", subSplineCodec -> Codec.either((Codec)Codec.FLOAT, Multipoint.codec(coordinateCodec, subSplineCodec)).xmap((), ()));
/*     */   }
/*     */   
/*     */   static <I> CubicSpline<I> constant(float value) {
/* 327 */     return new Constant<>(value);
/*     */   }
/*     */   
/*     */   static <I extends BoundedFloatFunction<?>> Builder<I> builder(I coordinate) {
/* 331 */     return new Builder<>(coordinate);
/*     */   }
/*     */   
/*     */   static <I extends BoundedFloatFunction<?>> Builder<I> builder(I coordinate, Float2FloatFunction valueTransformer) {
/* 335 */     return new Builder<>(coordinate, valueTransformer);
/*     */   }
/*     */   
/*     */   CubicSpline<I> mapCoordinates(UnaryOperator<I> paramUnaryOperator);
/*     */   
/*     */   float minValue();
/*     */   
/*     */   float maxValue();
/*     */   
/*     */   @VisibleForDebug
/*     */   String parityString();
/*     */   
/*     */   public static final class Builder<I extends BoundedFloatFunction<?>> {
/*     */     private final I coordinate;
/*     */     
/*     */     private final Float2FloatFunction valueTransformer;
/*     */     
/* 341 */     private final FloatList locations = (FloatList)new FloatArrayList();
/*     */     
/* 342 */     private final List<CubicSpline<I>> values = Lists.newArrayList();
/*     */     
/* 343 */     private final FloatList derivatives = (FloatList)new FloatArrayList();
/*     */     
/*     */     private Builder(I coordinate) {
/* 346 */       this(coordinate, Float2FloatFunction.identity());
/*     */     }
/*     */     
/*     */     private Builder(I coordinate, Float2FloatFunction valueTransformer) {
/* 350 */       this.coordinate = coordinate;
/* 351 */       this.valueTransformer = valueTransformer;
/*     */     }
/*     */     
/*     */     public Builder<I> addPoint(float location, float value) {
/* 355 */       return addPoint(location, new CubicSpline.Constant<>(((Float)this.valueTransformer.apply(Float.valueOf(value))).floatValue()), 0.0F);
/*     */     }
/*     */     
/*     */     public Builder<I> addPoint(float location, float value, float derivative) {
/* 359 */       return addPoint(location, new CubicSpline.Constant<>(((Float)this.valueTransformer.apply(Float.valueOf(value))).floatValue()), derivative);
/*     */     }
/*     */     
/*     */     public Builder<I> addPoint(float location, CubicSpline<I> sampler) {
/* 363 */       return addPoint(location, sampler, 0.0F);
/*     */     }
/*     */     
/*     */     private Builder<I> addPoint(float location, CubicSpline<I> sampler, float derivative) {
/* 367 */       if (!this.locations.isEmpty() && location <= this.locations.getFloat(this.locations.size() - 1))
/* 368 */         throw new IllegalArgumentException("Please register points in ascending order"); 
/* 370 */       this.locations.add(location);
/* 371 */       this.values.add(sampler);
/* 372 */       this.derivatives.add(derivative);
/* 373 */       return this;
/*     */     }
/*     */     
/*     */     public CubicSpline<I> build() {
/* 377 */       if (this.locations.isEmpty())
/* 378 */         throw new IllegalStateException("No elements added"); 
/* 380 */       return new CubicSpline.Multipoint<>(this.coordinate, this.locations.toFloatArray(), List.copyOf(this.values), this.derivatives.toFloatArray());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\CubicSpline.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */