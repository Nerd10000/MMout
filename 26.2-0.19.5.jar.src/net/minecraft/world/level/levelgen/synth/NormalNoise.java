/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class NormalNoise {
/*     */   private static final double INPUT_FACTOR = 1.0181268882175227D;
/*     */   
/*     */   private static final double TARGET_DEVIATION = 0.3333333333333333D;
/*     */   
/*     */   private final double valueFactor;
/*     */   
/*     */   private final PerlinNoise first;
/*     */   
/*     */   private final PerlinNoise second;
/*     */   
/*     */   private final double maxValue;
/*     */   
/*     */   private final NoiseParameters parameters;
/*     */   
/*     */   @Deprecated
/*     */   public static NormalNoise createLegacyNetherBiome(RandomSource random, NoiseParameters parameters) {
/*  36 */     return new NormalNoise(random, parameters, false);
/*     */   }
/*     */   
/*     */   public static NormalNoise create(RandomSource random, int firstOctave, double... amplitudes) {
/*  40 */     return create(random, new NoiseParameters(firstOctave, (DoubleList)new DoubleArrayList(amplitudes)));
/*     */   }
/*     */   
/*     */   public static NormalNoise create(RandomSource random, NoiseParameters parameters) {
/*  44 */     return new NormalNoise(random, parameters, true);
/*     */   }
/*     */   
/*     */   private NormalNoise(RandomSource random, NoiseParameters parameters, boolean useNewInitialization) {
/*  48 */     int firstOctave = parameters.firstOctave;
/*  49 */     DoubleList amplitudes = parameters.amplitudes;
/*  51 */     this.parameters = parameters;
/*  53 */     if (useNewInitialization) {
/*  54 */       this.first = PerlinNoise.create(random, firstOctave, amplitudes);
/*  55 */       this.second = PerlinNoise.create(random, firstOctave, amplitudes);
/*     */     } else {
/*  57 */       this.first = PerlinNoise.createLegacyForLegacyNetherBiome(random, firstOctave, amplitudes);
/*  58 */       this.second = PerlinNoise.createLegacyForLegacyNetherBiome(random, firstOctave, amplitudes);
/*     */     } 
/*  61 */     int minOctave = Integer.MAX_VALUE;
/*  62 */     int maxOctave = Integer.MIN_VALUE;
/*  64 */     DoubleListIterator iterator = amplitudes.iterator();
/*  65 */     while (iterator.hasNext()) {
/*  66 */       int i = iterator.nextIndex();
/*  67 */       double amplitude = iterator.nextDouble();
/*  68 */       if (amplitude != 0.0D) {
/*  69 */         minOctave = Math.min(minOctave, i);
/*  70 */         maxOctave = Math.max(maxOctave, i);
/*     */       } 
/*     */     } 
/*  74 */     this.valueFactor = 0.16666666666666666D / expectedDeviation(maxOctave - minOctave);
/*  76 */     this.maxValue = (this.first.maxValue() + this.second.maxValue()) * this.valueFactor;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/*  80 */     return this.maxValue;
/*     */   }
/*     */   
/*     */   private static double expectedDeviation(int octaveSpan) {
/*  85 */     return 0.1D * (1.0D + 1.0D / (octaveSpan + 1));
/*     */   }
/*     */   
/*     */   public double getValue(double x, double y, double z) {
/*  89 */     double x2 = x * 1.0181268882175227D;
/*  90 */     double y2 = y * 1.0181268882175227D;
/*  91 */     double z2 = z * 1.0181268882175227D;
/*  92 */     return (this.first.getValue(x, y, z) + this.second.getValue(x2, y2, z2)) * this.valueFactor;
/*     */   }
/*     */   
/*     */   public NoiseParameters parameters() {
/*  96 */     return this.parameters;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void parityConfigString(StringBuilder sb) {
/* 101 */     sb.append("NormalNoise {");
/* 102 */     sb.append("first: ");
/* 103 */     this.first.parityConfigString(sb);
/* 104 */     sb.append(", second: ");
/* 105 */     this.second.parityConfigString(sb);
/* 106 */     sb.append("}");
/*     */   }
/*     */   
/*     */   public static final class NoiseParameters extends Record {
/*     */     private final int firstOctave;
/*     */     
/*     */     private final DoubleList amplitudes;
/*     */     
/*     */     public static final Codec<NoiseParameters> DIRECT_CODEC;
/*     */     
/*     */     public NoiseParameters(int firstOctave, DoubleList amplitudes) {
/* 109 */       this.firstOctave = firstOctave;
/* 109 */       this.amplitudes = amplitudes;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #109	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #109	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #109	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int firstOctave() {
/* 109 */       return this.firstOctave;
/*     */     }
/*     */     
/*     */     public DoubleList amplitudes() {
/* 109 */       return this.amplitudes;
/*     */     }
/*     */     
/*     */     static {
/* 110 */       DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("firstOctave").forGetter(NoiseParameters::firstOctave), (App)Codec.DOUBLE.listOf().fieldOf("amplitudes").forGetter(NoiseParameters::amplitudes)).apply((Applicative)i, NoiseParameters::new));
/*     */     }
/*     */     
/* 115 */     public static final Codec<Holder<NoiseParameters>> CODEC = (Codec<Holder<NoiseParameters>>)RegistryFileCodec.create(Registries.NOISE, DIRECT_CODEC);
/*     */     
/*     */     public NoiseParameters(int firstOctave, List<Double> amplitudes) {
/* 118 */       this(firstOctave, (DoubleList)new DoubleArrayList(amplitudes));
/*     */     }
/*     */     
/*     */     public NoiseParameters(int firstOctave, double firstAmplitude, double... amplitudes) {
/* 122 */       this(firstOctave, (DoubleList)Util.make(new DoubleArrayList(amplitudes), list -> list.add(0, firstAmplitude)));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\synth\NormalNoise.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */