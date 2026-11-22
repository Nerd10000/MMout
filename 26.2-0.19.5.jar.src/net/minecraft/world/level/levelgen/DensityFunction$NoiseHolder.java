/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ public final class NoiseHolder extends Record {
/*    */   private final Holder<NormalNoise.NoiseParameters> noiseData;
/*    */   
/*    */   private final NormalNoise noise;
/*    */   
/*    */   public static final Codec<NoiseHolder> CODEC;
/*    */   
/*    */   public NoiseHolder(Holder<NormalNoise.NoiseParameters> noiseData, NormalNoise noise) {
/* 59 */     this.noiseData = noiseData;
/* 59 */     this.noise = noise;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #59	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #59	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #59	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<NormalNoise.NoiseParameters> noiseData() {
/* 59 */     return this.noiseData;
/*    */   }
/*    */   
/*    */   public NormalNoise noise() {
/* 59 */     return this.noise;
/*    */   }
/*    */   
/*    */   public NoiseHolder(Holder<NormalNoise.NoiseParameters> noiseData) {
/* 64 */     this(noiseData, null);
/*    */   }
/*    */   
/*    */   static {
/* 67 */     CODEC = NormalNoise.NoiseParameters.CODEC.xmap(data -> new NoiseHolder(data, null), NoiseHolder::noiseData);
/*    */   }
/*    */   
/*    */   public double getValue(double x, double y, double z) {
/* 70 */     return (this.noise == null) ? 0.0D : this.noise.getValue(x, y, z);
/*    */   }
/*    */   
/*    */   public double maxValue() {
/* 74 */     return (this.noise == null) ? 2.0D : this.noise.maxValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunction$NoiseHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */