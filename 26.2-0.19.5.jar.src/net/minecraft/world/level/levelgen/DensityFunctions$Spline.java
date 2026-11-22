/*      */ package net.minecraft.world.level.levelgen;
/*      */ 
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import java.util.function.Function;
/*      */ import net.minecraft.util.BoundedFloatFunction;
/*      */ import net.minecraft.util.CubicSpline;
/*      */ import net.minecraft.util.KeyDispatchDataCodec;
/*      */ 
/*      */ public final class Spline implements DensityFunction {
/* 1201 */   private static final Codec<CubicSpline<Coordinate>> SPLINE_CODEC = CubicSpline.codec(Coordinate.CODEC);
/*      */   
/* 1202 */   private static final MapCodec<Spline> DATA_CODEC = SPLINE_CODEC.fieldOf("spline").xmap(Spline::new, Spline::spline);
/*      */   
/* 1204 */   public static final KeyDispatchDataCodec<Spline> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */   
/*      */   private final CubicSpline<Coordinate> spline;
/*      */   
/*      */   private final BoundedFloatFunction<Point> sampler;
/*      */   
/*      */   public Spline(CubicSpline<Coordinate> spline) {
/* 1210 */     this.spline = spline;
/* 1211 */     this.sampler = CubicSpline.asSampler(spline);
/*      */   }
/*      */   
/*      */   public double compute(DensityFunction.FunctionContext context) {
/* 1216 */     return this.sampler.apply(new Point(context));
/*      */   }
/*      */   
/*      */   public double minValue() {
/* 1221 */     return this.spline.minValue();
/*      */   }
/*      */   
/*      */   public double maxValue() {
/* 1226 */     return this.spline.maxValue();
/*      */   }
/*      */   
/*      */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 1231 */     contextProvider.fillAllDirectly(output, this);
/*      */   }
/*      */   
/*      */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 1236 */     return new Spline(this.spline.mapCoordinates(c -> c.mapChildren(visitor)));
/*      */   }
/*      */   
/*      */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1241 */     return (KeyDispatchDataCodec)CODEC;
/*      */   }
/*      */   
/*      */   public CubicSpline<Coordinate> spline() {
/* 1245 */     return this.spline;
/*      */   }
/*      */   
/*      */   public boolean equals(Object obj) {
/* 1250 */     if (obj == this)
/* 1251 */       return true; 
/* 1253 */     if (obj instanceof Spline) {
/* 1253 */       Spline splineFunction = (Spline)obj;
/* 1253 */       if (this.spline.equals(splineFunction.spline));
/*      */     } 
/* 1253 */     return false;
/*      */   }
/*      */   
/*      */   public int hashCode() {
/* 1258 */     return this.spline.hashCode();
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1263 */     return this.spline.toString();
/*      */   }
/*      */   
/*      */   public static final class Coordinate extends Record implements BoundedFloatFunction<Point> {
/*      */     private final DensityFunction function;
/*      */     
/*      */     public Coordinate(DensityFunction function) {
/* 1266 */       this.function = function;
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
/* 1266 */       return this.function;
/*      */     }
/*      */     
/* 1267 */     public static final Codec<Coordinate> CODEC = DensityFunction.CODEC.xmap(Coordinate::new, Coordinate::function);
/*      */     
/*      */     public float apply(DensityFunctions.Spline.Point point) {
/* 1271 */       return (float)this.function.compute(point.context());
/*      */     }
/*      */     
/*      */     public float minValue() {
/* 1276 */       return (float)this.function.minValue();
/*      */     }
/*      */     
/*      */     public float maxValue() {
/* 1281 */       return (float)this.function.maxValue();
/*      */     }
/*      */     
/*      */     public Coordinate mapChildren(DensityFunction.Visitor visitor) {
/* 1285 */       return new Coordinate(visitor.apply(this.function));
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
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$Spline.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */