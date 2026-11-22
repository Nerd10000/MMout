/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ 
/*     */ public final class Noise extends Record implements DensityFunction {
/*     */   private final DensityFunction.NoiseHolder noise;
/*     */   
/*     */   @Deprecated
/*     */   private final double xzScale;
/*     */   
/*     */   private final double yScale;
/*     */   
/*     */   public static final MapCodec<Noise> DATA_CODEC;
/*     */   
/*     */   protected Noise(DensityFunction.NoiseHolder noise, @Deprecated double xzScale, double yScale) {
/* 498 */     this.noise = noise;
/* 498 */     this.xzScale = xzScale;
/* 498 */     this.yScale = yScale;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #498	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #498	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #498	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DensityFunction.NoiseHolder noise() {
/* 498 */     return this.noise;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public double xzScale() {
/* 498 */     return this.xzScale;
/*     */   }
/*     */   
/*     */   public double yScale() {
/* 498 */     return this.yScale;
/*     */   }
/*     */   
/*     */   static {
/* 499 */     DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(Noise::noise), (App)Codec.DOUBLE.fieldOf("xz_scale").forGetter(Noise::xzScale), (App)Codec.DOUBLE.fieldOf("y_scale").forGetter(Noise::yScale)).apply((Applicative)i, Noise::new));
/*     */   }
/*     */   
/* 505 */   public static final KeyDispatchDataCodec<Noise> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 509 */     return this.noise.getValue(context.blockX() * this.xzScale, context.blockY() * this.yScale, context.blockZ() * this.xzScale);
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 514 */     contextProvider.fillAllDirectly(output, this);
/*     */   }
/*     */   
/*     */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 519 */     return new Noise(visitor.visitNoise(this.noise), this.xzScale, this.yScale);
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 524 */     return -maxValue();
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 529 */     return this.noise.maxValue();
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 534 */     return (KeyDispatchDataCodec)CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$Noise.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */