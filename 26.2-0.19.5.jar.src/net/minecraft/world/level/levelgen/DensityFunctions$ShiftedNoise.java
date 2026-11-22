/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ 
/*     */ public final class ShiftedNoise extends Record implements DensityFunction {
/*     */   private final DensityFunction shiftX;
/*     */   
/*     */   private final DensityFunction shiftY;
/*     */   
/*     */   private final DensityFunction shiftZ;
/*     */   
/*     */   private final double xzScale;
/*     */   
/*     */   private final double yScale;
/*     */   
/*     */   private final DensityFunction.NoiseHolder noise;
/*     */   
/*     */   private static final MapCodec<ShiftedNoise> DATA_CODEC;
/*     */   
/*     */   protected ShiftedNoise(DensityFunction shiftX, DensityFunction shiftY, DensityFunction shiftZ, double xzScale, double yScale, DensityFunction.NoiseHolder noise) {
/* 602 */     this.shiftX = shiftX;
/* 602 */     this.shiftY = shiftY;
/* 602 */     this.shiftZ = shiftZ;
/* 602 */     this.xzScale = xzScale;
/* 602 */     this.yScale = yScale;
/* 602 */     this.noise = noise;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #602	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #602	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #602	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DensityFunction shiftX() {
/* 602 */     return this.shiftX;
/*     */   }
/*     */   
/*     */   public DensityFunction shiftY() {
/* 602 */     return this.shiftY;
/*     */   }
/*     */   
/*     */   public DensityFunction shiftZ() {
/* 602 */     return this.shiftZ;
/*     */   }
/*     */   
/*     */   public double xzScale() {
/* 602 */     return this.xzScale;
/*     */   }
/*     */   
/*     */   public double yScale() {
/* 602 */     return this.yScale;
/*     */   }
/*     */   
/*     */   public DensityFunction.NoiseHolder noise() {
/* 602 */     return this.noise;
/*     */   }
/*     */   
/*     */   static {
/* 610 */     DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.CODEC.fieldOf("shift_x").forGetter(ShiftedNoise::shiftX), (App)DensityFunction.CODEC.fieldOf("shift_y").forGetter(ShiftedNoise::shiftY), (App)DensityFunction.CODEC.fieldOf("shift_z").forGetter(ShiftedNoise::shiftZ), (App)Codec.DOUBLE.fieldOf("xz_scale").forGetter(ShiftedNoise::xzScale), (App)Codec.DOUBLE.fieldOf("y_scale").forGetter(ShiftedNoise::yScale), (App)DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(ShiftedNoise::noise)).apply((Applicative)i, ShiftedNoise::new));
/*     */   }
/*     */   
/* 619 */   public static final KeyDispatchDataCodec<ShiftedNoise> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 623 */     double x = context.blockX() * this.xzScale + this.shiftX.compute(context);
/* 624 */     double y = context.blockY() * this.yScale + this.shiftY.compute(context);
/* 625 */     double z = context.blockZ() * this.xzScale + this.shiftZ.compute(context);
/* 626 */     return this.noise.getValue(x, y, z);
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 631 */     contextProvider.fillAllDirectly(output, this);
/*     */   }
/*     */   
/*     */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 636 */     return new ShiftedNoise(visitor
/* 637 */         .apply(this.shiftX), visitor
/* 638 */         .apply(this.shiftY), visitor
/* 639 */         .apply(this.shiftZ), this.xzScale, this.yScale, visitor
/*     */         
/* 642 */         .visitNoise(this.noise));
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 648 */     return -maxValue();
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 653 */     return this.noise.maxValue();
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 658 */     return (KeyDispatchDataCodec)CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$ShiftedNoise.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */