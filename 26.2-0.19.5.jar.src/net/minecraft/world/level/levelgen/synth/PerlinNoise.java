/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
/*     */ import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSortedSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.levelgen.PositionalRandomFactory;
/*     */ 
/*     */ public class PerlinNoise {
/*     */   private static final int ROUND_OFF = 33554432;
/*     */   
/*     */   private final ImprovedNoise[] noiseLevels;
/*     */   
/*     */   private final int firstOctave;
/*     */   
/*     */   private final DoubleList amplitudes;
/*     */   
/*     */   private final double lowestFreqValueFactor;
/*     */   
/*     */   private final double lowestFreqInputFactor;
/*     */   
/*     */   private final double maxValue;
/*     */   
/*     */   @Deprecated
/*     */   public static PerlinNoise createLegacyForBlendedNoise(RandomSource random, IntStream octaves) {
/*  35 */     return new PerlinNoise(random, makeAmplitudes((IntSortedSet)new IntRBTreeSet(octaves.boxed().collect(ImmutableList.toImmutableList()))), false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static PerlinNoise createLegacyForLegacyNetherBiome(RandomSource random, int firstOctave, DoubleList amplitudes) {
/*  40 */     return new PerlinNoise(random, Pair.of(Integer.valueOf(firstOctave), amplitudes), false);
/*     */   }
/*     */   
/*     */   public static PerlinNoise create(RandomSource random, IntStream octaves) {
/*  44 */     return create(random, octaves.boxed().collect(ImmutableList.toImmutableList()));
/*     */   }
/*     */   
/*     */   public static PerlinNoise create(RandomSource random, List<Integer> octaveSet) {
/*  48 */     return new PerlinNoise(random, makeAmplitudes((IntSortedSet)new IntRBTreeSet(octaveSet)), true);
/*     */   }
/*     */   
/*     */   public static PerlinNoise create(RandomSource random, int firstOctave, double firstAmplitude, double... amplitudes) {
/*  52 */     DoubleArrayList amplitudeList = new DoubleArrayList(amplitudes);
/*  53 */     amplitudeList.add(0, firstAmplitude);
/*  54 */     return new PerlinNoise(random, Pair.of(Integer.valueOf(firstOctave), amplitudeList), true);
/*     */   }
/*     */   
/*     */   public static PerlinNoise create(RandomSource random, int firstOctave, DoubleList amplitudes) {
/*  58 */     return new PerlinNoise(random, Pair.of(Integer.valueOf(firstOctave), amplitudes), true);
/*     */   }
/*     */   
/*     */   private static Pair<Integer, DoubleList> makeAmplitudes(IntSortedSet octaveSet) {
/*  62 */     if (octaveSet.isEmpty())
/*  63 */       throw new IllegalArgumentException("Need some octaves!"); 
/*  66 */     int lowFreqOctaves = -octaveSet.firstInt();
/*  67 */     int highFreqOctaves = octaveSet.lastInt();
/*  69 */     int octaves = lowFreqOctaves + highFreqOctaves + 1;
/*  70 */     if (octaves < 1)
/*  71 */       throw new IllegalArgumentException("Total number of octaves needs to be >= 1"); 
/*  74 */     DoubleArrayList doubleArrayList = new DoubleArrayList(new double[octaves]);
/*  75 */     IntBidirectionalIterator iterator = octaveSet.iterator();
/*  76 */     while (iterator.hasNext()) {
/*  77 */       int octave = iterator.nextInt();
/*  78 */       doubleArrayList.set(octave + lowFreqOctaves, 1.0D);
/*     */     } 
/*  81 */     return Pair.of(Integer.valueOf(-lowFreqOctaves), doubleArrayList);
/*     */   }
/*     */   
/*     */   protected PerlinNoise(RandomSource random, Pair<Integer, DoubleList> pair, boolean useNewInitialization) {
/*  85 */     this.firstOctave = ((Integer)pair.getFirst()).intValue();
/*  86 */     this.amplitudes = (DoubleList)pair.getSecond();
/*  87 */     int octaves = this.amplitudes.size();
/*  88 */     int zeroOctaveIndex = -this.firstOctave;
/*  90 */     this.noiseLevels = new ImprovedNoise[octaves];
/*  92 */     if (useNewInitialization) {
/*  93 */       PositionalRandomFactory positional = random.forkPositional();
/*  94 */       for (int i = 0; i < octaves; i++) {
/*  95 */         if (this.amplitudes.getDouble(i) != 0.0D) {
/*  96 */           int octave = this.firstOctave + i;
/*  97 */           this.noiseLevels[i] = new ImprovedNoise(positional.fromHashOf("octave_" + octave));
/*     */         } 
/*     */       } 
/*     */     } else {
/* 101 */       ImprovedNoise zeroOctave = new ImprovedNoise(random);
/* 102 */       if (zeroOctaveIndex >= 0 && zeroOctaveIndex < octaves) {
/* 103 */         double zeroOctaveAmplitude = this.amplitudes.getDouble(zeroOctaveIndex);
/* 104 */         if (zeroOctaveAmplitude != 0.0D)
/* 105 */           this.noiseLevels[zeroOctaveIndex] = zeroOctave; 
/*     */       } 
/* 109 */       for (int i = zeroOctaveIndex - 1; i >= 0; i--) {
/* 110 */         if (i < octaves) {
/* 111 */           double amplitude = this.amplitudes.getDouble(i);
/* 112 */           if (amplitude != 0.0D) {
/* 113 */             this.noiseLevels[i] = new ImprovedNoise(random);
/*     */           } else {
/* 115 */             skipOctave(random);
/*     */           } 
/*     */         } else {
/* 118 */           skipOctave(random);
/*     */         } 
/*     */       } 
/* 122 */       if (Arrays.<ImprovedNoise>stream(this.noiseLevels).filter(Objects::nonNull).count() != this.amplitudes.stream().filter(a -> (a.doubleValue() != 0.0D)).count())
/* 123 */         throw new IllegalStateException("Failed to create correct number of noise levels for given non-zero amplitudes"); 
/* 126 */       if (zeroOctaveIndex < octaves - 1)
/* 128 */         throw new IllegalArgumentException("Positive octaves are temporarily disabled"); 
/*     */     } 
/* 132 */     this.lowestFreqInputFactor = Math.pow(2.0D, -zeroOctaveIndex);
/* 133 */     this.lowestFreqValueFactor = Math.pow(2.0D, (octaves - 1)) / (Math.pow(2.0D, octaves) - 1.0D);
/* 136 */     this.maxValue = edgeValue(2.0D);
/*     */   }
/*     */   
/*     */   protected double maxValue() {
/* 140 */     return this.maxValue;
/*     */   }
/*     */   
/*     */   private static void skipOctave(RandomSource random) {
/* 146 */     random.consumeCount(262);
/*     */   }
/*     */   
/*     */   public double getValue(double x, double y, double z) {
/* 150 */     return getValue(x, y, z, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public double getValue(double x, double y, double z, double yScale, double yFudge) {
/* 156 */     double value = 0.0D;
/* 157 */     double factor = this.lowestFreqInputFactor;
/* 158 */     double valueFactor = this.lowestFreqValueFactor;
/* 160 */     for (int i = 0; i < this.noiseLevels.length; i++) {
/* 161 */       ImprovedNoise noise = this.noiseLevels[i];
/* 162 */       if (noise != null) {
/* 163 */         double noiseVal = noise.noise(wrap(x * factor), wrap(y * factor), wrap(z * factor), yScale * factor, yFudge * factor);
/* 164 */         value += this.amplitudes.getDouble(i) * noiseVal * valueFactor;
/*     */       } 
/* 166 */       factor *= 2.0D;
/* 167 */       valueFactor /= 2.0D;
/*     */     } 
/* 170 */     return value;
/*     */   }
/*     */   
/*     */   public double maxBrokenValue(double yScale) {
/* 175 */     return edgeValue(yScale + 2.0D);
/*     */   }
/*     */   
/*     */   private double edgeValue(double noiseValue) {
/* 179 */     double value = 0.0D;
/* 180 */     double valueFactor = this.lowestFreqValueFactor;
/* 182 */     for (int i = 0; i < this.noiseLevels.length; i++) {
/* 183 */       ImprovedNoise noise = this.noiseLevels[i];
/* 184 */       if (noise != null)
/* 185 */         value += this.amplitudes.getDouble(i) * noiseValue * valueFactor; 
/* 187 */       valueFactor /= 2.0D;
/*     */     } 
/* 190 */     return value;
/*     */   }
/*     */   
/*     */   public ImprovedNoise getOctaveNoise(int i) {
/* 194 */     return this.noiseLevels[this.noiseLevels.length - 1 - i];
/*     */   }
/*     */   
/*     */   public static double wrap(double x) {
/* 198 */     return x - Mth.lfloor(x / 3.3554432E7D + 0.5D) * 3.3554432E7D;
/*     */   }
/*     */   
/*     */   protected int firstOctave() {
/* 202 */     return this.firstOctave;
/*     */   }
/*     */   
/*     */   protected DoubleList amplitudes() {
/* 206 */     return this.amplitudes;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void parityConfigString(StringBuilder sb) {
/* 211 */     sb.append("PerlinNoise{");
/* 212 */     List<String> amplitudeStrings = this.amplitudes.stream().map(d -> String.format(Locale.ROOT, "%.2f", new Object[] { d })).toList();
/* 213 */     sb.append("first octave: ").append(this.firstOctave).append(", amplitudes: ").append(amplitudeStrings)
/* 214 */       .append(", noise levels: [");
/* 216 */     for (int i = 0; i < this.noiseLevels.length; i++) {
/* 217 */       sb.append(i).append(": ");
/* 218 */       ImprovedNoise noiseLevel = this.noiseLevels[i];
/* 219 */       if (noiseLevel == null) {
/* 220 */         sb.append("null");
/*     */       } else {
/* 222 */         noiseLevel.parityConfigString(sb);
/*     */       } 
/* 224 */       sb.append(", ");
/*     */     } 
/* 227 */     sb.append("]");
/* 228 */     sb.append("}");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\synth\PerlinNoise.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */