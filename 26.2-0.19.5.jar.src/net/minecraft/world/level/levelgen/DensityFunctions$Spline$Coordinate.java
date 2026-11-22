/*      */ package net.minecraft.world.level.levelgen;
/*      */ 
/*      */ import com.mojang.serialization.Codec;
/*      */ import java.util.function.Function;
/*      */ import net.minecraft.util.BoundedFloatFunction;
/*      */ 
/*      */ public final class Coordinate extends Record implements BoundedFloatFunction<DensityFunctions.Spline.Point> {
/*      */   private final DensityFunction function;
/*      */   
/*      */   public Coordinate(DensityFunction function) {
/* 1266 */     this.function = function;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1266	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1266	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1266	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public DensityFunction function() {
/* 1266 */     return this.function;
/*      */   }
/*      */   
/* 1267 */   public static final Codec<Coordinate> CODEC = DensityFunction.CODEC.xmap(Coordinate::new, Coordinate::function);
/*      */   
/*      */   public float apply(DensityFunctions.Spline.Point point) {
/* 1271 */     return (float)this.function.compute(point.context());
/*      */   }
/*      */   
/*      */   public float minValue() {
/* 1276 */     return (float)this.function.minValue();
/*      */   }
/*      */   
/*      */   public float maxValue() {
/* 1281 */     return (float)this.function.maxValue();
/*      */   }
/*      */   
/*      */   public Coordinate mapChildren(DensityFunction.Visitor visitor) {
/* 1285 */     return new Coordinate(visitor.apply(this.function));
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$Spline$Coordinate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */