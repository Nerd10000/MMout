/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Locale;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
/*     */ 
/*     */ public class BlendedNoise implements DensityFunction.SimpleFunction {
/*  22 */   private static final Codec<Double> SCALE_RANGE = Codec.doubleRange(0.001D, 1000.0D);
/*     */   
/*     */   private static final MapCodec<BlendedNoise> DATA_CODEC;
/*     */   
/*     */   static {
/*  24 */     DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SCALE_RANGE.fieldOf("xz_scale").forGetter(()), (App)SCALE_RANGE.fieldOf("y_scale").forGetter(()), (App)SCALE_RANGE.fieldOf("xz_factor").forGetter(()), (App)SCALE_RANGE.fieldOf("y_factor").forGetter(()), (App)Codec.doubleRange(1.0D, 8.0D).fieldOf("smear_scale_multiplier").forGetter(())).apply((Applicative)i, BlendedNoise::createUnseeded));
/*     */   }
/*     */   
/*  32 */   public static final KeyDispatchDataCodec<BlendedNoise> CODEC = KeyDispatchDataCodec.of(DATA_CODEC);
/*     */   
/*     */   private final PerlinNoise minLimitNoise;
/*     */   
/*     */   private final PerlinNoise maxLimitNoise;
/*     */   
/*     */   private final PerlinNoise mainNoise;
/*     */   
/*     */   private final double xzMultiplier;
/*     */   
/*     */   private final double yMultiplier;
/*     */   
/*     */   private final double xzFactor;
/*     */   
/*     */   private final double yFactor;
/*     */   
/*     */   private final double smearScaleMultiplier;
/*     */   
/*     */   private final double maxValue;
/*     */   
/*     */   private final double xzScale;
/*     */   
/*     */   private final double yScale;
/*     */   
/*     */   public static BlendedNoise createUnseeded(double xzScale, double yScale, double xzFactor, double yFactor, double smearScaleMultiplier) {
/*  50 */     return new BlendedNoise((RandomSource)new XoroshiroRandomSource(0L), xzScale, yScale, xzFactor, yFactor, smearScaleMultiplier);
/*     */   }
/*     */   
/*     */   private BlendedNoise(PerlinNoise minLimitNoise, PerlinNoise maxLimitNoise, PerlinNoise mainNoise, double xzScale, double yScale, double xzFactor, double yFactor, double smearScaleMultiplier) {
/*  54 */     this.minLimitNoise = minLimitNoise;
/*  55 */     this.maxLimitNoise = maxLimitNoise;
/*  56 */     this.mainNoise = mainNoise;
/*  58 */     this.xzScale = xzScale;
/*  59 */     this.yScale = yScale;
/*  60 */     this.xzFactor = xzFactor;
/*  61 */     this.yFactor = yFactor;
/*  62 */     this.smearScaleMultiplier = smearScaleMultiplier;
/*  64 */     this.xzMultiplier = 684.412D * this.xzScale;
/*  65 */     this.yMultiplier = 684.412D * this.yScale;
/*  70 */     this.maxValue = minLimitNoise.maxBrokenValue(this.yMultiplier);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public BlendedNoise(RandomSource random, double xzScale, double yScale, double xzFactor, double yFactor, double smearScaleMultiplier) {
/*  75 */     this(
/*  76 */         PerlinNoise.createLegacyForBlendedNoise(random, IntStream.rangeClosed(-15, 0)), 
/*  77 */         PerlinNoise.createLegacyForBlendedNoise(random, IntStream.rangeClosed(-15, 0)), 
/*  78 */         PerlinNoise.createLegacyForBlendedNoise(random, IntStream.rangeClosed(-7, 0)), xzScale, yScale, xzFactor, yFactor, smearScaleMultiplier);
/*     */   }
/*     */   
/*     */   public BlendedNoise withNewRandom(RandomSource terrainRandom) {
/*  84 */     return new BlendedNoise(terrainRandom, this.xzScale, this.yScale, this.xzFactor, this.yFactor, this.smearScaleMultiplier);
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/*  93 */     double limitX = context.blockX() * this.xzMultiplier;
/*  94 */     double limitY = context.blockY() * this.yMultiplier;
/*  95 */     double limitZ = context.blockZ() * this.xzMultiplier;
/*  97 */     double mainX = limitX / this.xzFactor;
/*  98 */     double mainY = limitY / this.yFactor;
/*  99 */     double mainZ = limitZ / this.xzFactor;
/* 101 */     double limitSmear = this.yMultiplier * this.smearScaleMultiplier;
/* 102 */     double mainSmear = limitSmear / this.yFactor;
/* 104 */     double blendMin = 0.0D;
/* 105 */     double blendMax = 0.0D;
/* 106 */     double mainNoiseValue = 0.0D;
/* 108 */     boolean optimizeLoop = true;
/* 116 */     double pow = 1.0D;
/* 119 */     for (int i = 0; i < 8; i++) {
/* 120 */       ImprovedNoise noise = this.mainNoise.getOctaveNoise(i);
/* 121 */       if (noise != null)
/* 122 */         mainNoiseValue += noise.noise(PerlinNoise.wrap(mainX * pow), PerlinNoise.wrap(mainY * pow), PerlinNoise.wrap(mainZ * pow), mainSmear * pow, mainY * pow) / pow; 
/* 124 */       pow /= 2.0D;
/*     */     } 
/* 127 */     double factor = (mainNoiseValue / 10.0D + 1.0D) / 2.0D;
/* 131 */     boolean isMax = (factor >= 1.0D);
/* 132 */     boolean isMin = (factor <= 0.0D);
/* 133 */     pow = 1.0D;
/* 134 */     for (int j = 0; j < 16; j++) {
/* 135 */       double wx = PerlinNoise.wrap(limitX * pow);
/* 136 */       double wy = PerlinNoise.wrap(limitY * pow);
/* 137 */       double wz = PerlinNoise.wrap(limitZ * pow);
/* 138 */       double yScalePow = limitSmear * pow;
/* 139 */       if (!isMax) {
/* 140 */         ImprovedNoise minNoise = this.minLimitNoise.getOctaveNoise(j);
/* 141 */         if (minNoise != null)
/* 142 */           blendMin += minNoise.noise(wx, wy, wz, yScalePow, limitY * pow) / pow; 
/*     */       } 
/* 145 */       if (!isMin) {
/* 146 */         ImprovedNoise maxNoise = this.maxLimitNoise.getOctaveNoise(j);
/* 147 */         if (maxNoise != null)
/* 148 */           blendMax += maxNoise.noise(wx, wy, wz, yScalePow, limitY * pow) / pow; 
/*     */       } 
/* 151 */       pow /= 2.0D;
/*     */     } 
/* 155 */     return Mth.clampedLerp(factor, blendMin / 512.0D, blendMax / 512.0D) / 128.0D;
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 160 */     return -maxValue();
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 165 */     return this.maxValue;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void parityConfigString(StringBuilder sb) {
/* 170 */     sb.append("BlendedNoise{minLimitNoise=");
/* 171 */     this.minLimitNoise.parityConfigString(sb);
/* 172 */     sb.append(", maxLimitNoise=");
/* 173 */     this.maxLimitNoise.parityConfigString(sb);
/* 174 */     sb.append(", mainNoise=");
/* 175 */     this.mainNoise.parityConfigString(sb);
/* 177 */     sb.append(
/* 178 */         String.format(Locale.ROOT, ", xzScale=%.3f, yScale=%.3f, xzMainScale=%.3f, yMainScale=%.3f, cellWidth=4, cellHeight=8", new Object[] { Double.valueOf(684.412D), Double.valueOf(684.412D), Double.valueOf(8.555150000000001D), Double.valueOf(4.277575000000001D) })).append('}');
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 185 */     return (KeyDispatchDataCodec)CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\synth\BlendedNoise.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */