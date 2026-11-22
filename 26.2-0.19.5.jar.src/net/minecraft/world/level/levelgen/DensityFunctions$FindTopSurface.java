/*      */ package net.minecraft.world.level.levelgen;
/*      */ 
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.kinds.Applicative;
/*      */ import com.mojang.datafixers.util.Function4;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import net.minecraft.util.ExtraCodecs;
/*      */ import net.minecraft.util.KeyDispatchDataCodec;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ 
/*      */ final class FindTopSurface extends Record implements DensityFunction {
/*      */   private final DensityFunction density;
/*      */   
/*      */   private final DensityFunction upperBound;
/*      */   
/*      */   private final int lowerBound;
/*      */   
/*      */   private final int cellHeight;
/*      */   
/*      */   private static final MapCodec<FindTopSurface> DATA_CODEC;
/*      */   
/*      */   private FindTopSurface(DensityFunction density, DensityFunction upperBound, int lowerBound, int cellHeight) {
/* 1353 */     this.density = density;
/* 1353 */     this.upperBound = upperBound;
/* 1353 */     this.lowerBound = lowerBound;
/* 1353 */     this.cellHeight = cellHeight;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1353	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1353	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1353	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public DensityFunction density() {
/* 1353 */     return this.density;
/*      */   }
/*      */   
/*      */   public DensityFunction upperBound() {
/* 1353 */     return this.upperBound;
/*      */   }
/*      */   
/*      */   public int lowerBound() {
/* 1353 */     return this.lowerBound;
/*      */   }
/*      */   
/*      */   public int cellHeight() {
/* 1353 */     return this.cellHeight;
/*      */   }
/*      */   
/*      */   static {
/* 1354 */     DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.CODEC.fieldOf("density").forGetter(FindTopSurface::density), (App)DensityFunction.CODEC.fieldOf("upper_bound").forGetter(FindTopSurface::upperBound), (App)Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("lower_bound").forGetter(FindTopSurface::lowerBound), (App)ExtraCodecs.POSITIVE_INT.fieldOf("cell_height").forGetter(FindTopSurface::cellHeight)).apply((Applicative)i, FindTopSurface::new));
/*      */   }
/*      */   
/* 1361 */   public static final KeyDispatchDataCodec<FindTopSurface> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */   
/*      */   public double compute(DensityFunction.FunctionContext context) {
/* 1365 */     int topY = Mth.floor(this.upperBound.compute(context) / this.cellHeight) * this.cellHeight;
/* 1366 */     if (topY <= this.lowerBound)
/* 1367 */       return this.lowerBound; 
/* 1370 */     for (int blockY = topY; blockY >= this.lowerBound; blockY -= this.cellHeight) {
/* 1371 */       if (this.density.compute(new DensityFunction.SinglePointContext(context.blockX(), blockY, context.blockZ())) > 0.0D)
/* 1372 */         return blockY; 
/*      */     } 
/* 1375 */     return this.lowerBound;
/*      */   }
/*      */   
/*      */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 1380 */     contextProvider.fillAllDirectly(output, this);
/*      */   }
/*      */   
/*      */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 1385 */     return new FindTopSurface(visitor
/* 1386 */         .apply(this.density), visitor
/* 1387 */         .apply(this.upperBound), this.lowerBound, this.cellHeight);
/*      */   }
/*      */   
/*      */   public double minValue() {
/* 1395 */     return this.lowerBound;
/*      */   }
/*      */   
/*      */   public double maxValue() {
/* 1400 */     return Math.max(this.lowerBound, this.upperBound.maxValue());
/*      */   }
/*      */   
/*      */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1405 */     return (KeyDispatchDataCodec)CODEC;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$FindTopSurface.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */