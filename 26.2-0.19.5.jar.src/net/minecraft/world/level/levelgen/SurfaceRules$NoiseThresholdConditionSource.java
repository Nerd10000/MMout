/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.DoubleSupplier;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ final class NoiseThresholdConditionSource extends Record implements SurfaceRules.ConditionSource {
/*     */   private final ResourceKey<NormalNoise.NoiseParameters> noise;
/*     */   
/*     */   private final double minThreshold;
/*     */   
/*     */   private final double maxThreshold;
/*     */   
/*     */   private final boolean is3d;
/*     */   
/*     */   private static final MapCodec<NoiseThresholdConditionSource> CODEC;
/*     */   
/*     */   private NoiseThresholdConditionSource(ResourceKey<NormalNoise.NoiseParameters> noise, double minThreshold, double maxThreshold, boolean is3d) {
/* 712 */     this.noise = noise;
/* 712 */     this.minThreshold = minThreshold;
/* 712 */     this.maxThreshold = maxThreshold;
/* 712 */     this.is3d = is3d;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #712	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #712	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #712	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ResourceKey<NormalNoise.NoiseParameters> noise() {
/* 712 */     return this.noise;
/*     */   }
/*     */   
/*     */   public double minThreshold() {
/* 712 */     return this.minThreshold;
/*     */   }
/*     */   
/*     */   public double maxThreshold() {
/* 712 */     return this.maxThreshold;
/*     */   }
/*     */   
/*     */   public boolean is3d() {
/* 712 */     return this.is3d;
/*     */   }
/*     */   
/*     */   static {
/* 713 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.NOISE).fieldOf("noise").forGetter(NoiseThresholdConditionSource::noise), (App)Codec.DOUBLE.fieldOf("min_threshold").forGetter(NoiseThresholdConditionSource::minThreshold), (App)Codec.DOUBLE.fieldOf("max_threshold").forGetter(NoiseThresholdConditionSource::maxThreshold), (App)Codec.BOOL.optionalFieldOf("is_3d", Boolean.valueOf(false)).forGetter(NoiseThresholdConditionSource::is3d)).apply((Applicative)i, NoiseThresholdConditionSource::new));
/*     */   }
/*     */   
/*     */   public MapCodec<NoiseThresholdConditionSource> codec() {
/* 723 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.Condition apply(SurfaceRules.Context ruleContext) {
/* 728 */     final DoubleSupplier noise = ruleContext.getNoiseSampler(this.noise, this.is3d);
/*     */     class NoiseThresholdCondition implements SurfaceRules.Condition {
/*     */       public boolean test() {
/* 732 */         double value = noise.getAsDouble();
/* 733 */         return (value >= SurfaceRules.NoiseThresholdConditionSource.this.minThreshold && value <= SurfaceRules.NoiseThresholdConditionSource.this.maxThreshold);
/*     */       }
/*     */     };
/* 737 */     return new NoiseThresholdCondition();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$NoiseThresholdConditionSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */