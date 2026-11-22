/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ final class Marker extends Record implements DensityFunctions.MarkerOrMarked {
/*     */   private final Type type;
/*     */   
/*     */   private final DensityFunction wrapped;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #444	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #444	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #444	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Type type() {
/* 444 */     return this.type;
/*     */   }
/*     */   
/*     */   public DensityFunction wrapped() {
/* 444 */     return this.wrapped;
/*     */   }
/*     */   
/*     */   Marker(Type type, DensityFunction wrapped) {
/* 445 */     this.type = type;
/* 445 */     this.wrapped = wrapped;
/*     */   }
/*     */   
/*     */   public enum Type implements StringRepresentable {
/* 447 */     Interpolated("interpolated"),
/* 448 */     FlatCache("flat_cache"),
/* 449 */     Cache2D("cache_2d"),
/* 450 */     CacheOnce("cache_once"),
/* 451 */     CacheAllInCell("cache_all_in_cell"),
/* 452 */     BlendDensity("blend_density");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final KeyDispatchDataCodec<DensityFunctions.MarkerOrMarked> codec;
/*     */     
/*     */     Type(String name) {
/* 457 */       this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> new DensityFunctions.Marker(this, input), DensityFunctions.MarkerOrMarked::wrapped);
/* 460 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 465 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 471 */     return this.wrapped.compute(context);
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 476 */     this.wrapped.fillArray(output, contextProvider);
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 481 */     if (this.type == Type.BlendDensity)
/* 483 */       return Double.NEGATIVE_INFINITY; 
/* 485 */     return this.wrapped.minValue();
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 490 */     if (this.type == Type.BlendDensity)
/* 492 */       return Double.POSITIVE_INFINITY; 
/* 494 */     return this.wrapped.maxValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$Marker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */