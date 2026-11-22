/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ 
/*     */ final class Point<I extends BoundedFloatFunction<?>> extends Record {
/*     */   private final float location;
/*     */   
/*     */   private final CubicSpline<I> value;
/*     */   
/*     */   private final float derivative;
/*     */   
/*     */   private Point(float location, CubicSpline<I> value, float derivative) {
/* 277 */     this.location = location;
/* 277 */     this.value = value;
/* 277 */     this.derivative = derivative;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/CubicSpline$Multipoint$Point;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #277	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point<TI;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/CubicSpline$Multipoint$Point;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #277	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point<TI;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/CubicSpline$Multipoint$Point;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #277	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Multipoint$Point<TI;>;
/*     */   }
/*     */   
/*     */   public float location() {
/* 277 */     return this.location;
/*     */   }
/*     */   
/*     */   public CubicSpline<I> value() {
/* 277 */     return this.value;
/*     */   }
/*     */   
/*     */   public float derivative() {
/* 277 */     return this.derivative;
/*     */   }
/*     */   
/*     */   public static <I extends BoundedFloatFunction<?>> Codec<Point<I>> codec(Codec<CubicSpline<I>> subSplineCodec) {
/* 279 */     return RecordCodecBuilder.create(i -> i.group((App)Codec.FLOAT.fieldOf("location").forGetter(Point::location), (App)subSplineCodec.fieldOf("value").forGetter(Point::value), (App)Codec.FLOAT.fieldOf("derivative").forGetter(Point::derivative)).apply((Applicative)i, Point::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\CubicSpline$Multipoint$Point.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */