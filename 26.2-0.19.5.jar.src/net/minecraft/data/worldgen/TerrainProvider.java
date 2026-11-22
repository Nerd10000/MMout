/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
/*     */ import net.minecraft.util.BoundedFloatFunction;
/*     */ import net.minecraft.util.CubicSpline;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class TerrainProvider {
/*     */   private static final float DEEP_OCEAN_CONTINENTALNESS = -0.51F;
/*     */   
/*     */   private static final float OCEAN_CONTINENTALNESS = -0.4F;
/*     */   
/*     */   private static final float PLAINS_CONTINENTALNESS = 0.1F;
/*     */   
/*     */   private static final float BEACH_CONTINENTALNESS = -0.15F;
/*     */   
/*  23 */   private static final Float2FloatFunction NO_TRANSFORM = Float2FloatFunction.identity();
/*     */   
/*     */   private static final Float2FloatFunction AMPLIFIED_OFFSET;
/*     */   
/*     */   private static final Float2FloatFunction AMPLIFIED_FACTOR;
/*     */   
/*     */   private static final Float2FloatFunction AMPLIFIED_JAGGEDNESS;
/*     */   
/*     */   static {
/*  25 */     AMPLIFIED_OFFSET = (offset -> (offset < 0.0F) ? offset : (offset * 2.0F));
/*  26 */     AMPLIFIED_FACTOR = (factor -> 1.25F - 6.25F / (factor + 5.0F));
/*  27 */     AMPLIFIED_JAGGEDNESS = (jaggedness -> jaggedness * 2.0F);
/*     */   }
/*     */   
/*     */   public static <I extends BoundedFloatFunction<?>> CubicSpline<I> overworldOffset(I continents, I erosion, I ridges, boolean amplified) {
/*  30 */     Float2FloatFunction offsetTransformer = amplified ? AMPLIFIED_OFFSET : NO_TRANSFORM;
/*  33 */     CubicSpline<I> beachSpline = buildErosionOffsetSpline(erosion, ridges, -0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, offsetTransformer);
/*  34 */     CubicSpline<I> lowSpline = buildErosionOffsetSpline(erosion, ridges, -0.1F, 0.03F, 0.1F, 0.1F, 0.01F, -0.03F, false, false, offsetTransformer);
/*  35 */     CubicSpline<I> midSpline = buildErosionOffsetSpline(erosion, ridges, -0.1F, 0.03F, 0.1F, 0.7F, 0.01F, -0.03F, true, true, offsetTransformer);
/*  36 */     CubicSpline<I> highSpline = buildErosionOffsetSpline(erosion, ridges, -0.05F, 0.03F, 0.1F, 1.0F, 0.01F, 0.01F, true, true, offsetTransformer);
/*  38 */     return CubicSpline.builder((BoundedFloatFunction)continents, offsetTransformer)
/*  39 */       .addPoint(-1.1F, 0.044F)
/*  40 */       .addPoint(-1.02F, -0.2222F)
/*  41 */       .addPoint(-0.51F, -0.2222F)
/*  42 */       .addPoint(-0.44F, -0.12F)
/*  43 */       .addPoint(-0.18F, -0.12F)
/*  44 */       .addPoint(-0.16F, beachSpline)
/*  45 */       .addPoint(-0.15F, beachSpline)
/*  46 */       .addPoint(-0.1F, lowSpline)
/*  47 */       .addPoint(0.25F, midSpline)
/*  48 */       .addPoint(1.0F, highSpline)
/*  49 */       .build();
/*     */   }
/*     */   
/*     */   public static <I extends BoundedFloatFunction<?>> CubicSpline<I> overworldFactor(I continents, I erosion, I weirdness, I ridges, boolean amplified) {
/*  53 */     Float2FloatFunction factorTransformer = amplified ? AMPLIFIED_FACTOR : NO_TRANSFORM;
/*  75 */     return CubicSpline.builder((BoundedFloatFunction)continents, NO_TRANSFORM)
/*     */       
/*  78 */       .addPoint(-0.19F, 3.95F)
/*     */       
/*  84 */       .addPoint(-0.15F, getErosionFactor(erosion, weirdness, ridges, 6.25F, true, NO_TRANSFORM))
/*     */       
/*  87 */       .addPoint(-0.1F, getErosionFactor(erosion, weirdness, ridges, 5.47F, true, factorTransformer))
/*  88 */       .addPoint(0.03F, getErosionFactor(erosion, weirdness, ridges, 5.08F, true, factorTransformer))
/*  89 */       .addPoint(0.06F, getErosionFactor(erosion, weirdness, ridges, 4.69F, false, factorTransformer))
/*  90 */       .build();
/*     */   }
/*     */   
/*     */   public static <I extends BoundedFloatFunction<?>> CubicSpline<I> overworldJaggedness(I continents, I erosion, I weirdness, I ridges, boolean amplified) {
/*  94 */     Float2FloatFunction jaggednessTransformer = amplified ? AMPLIFIED_JAGGEDNESS : NO_TRANSFORM;
/* 102 */     float farInlandMiddle = 0.65F;
/* 103 */     return CubicSpline.builder((BoundedFloatFunction)continents, jaggednessTransformer)
/* 104 */       .addPoint(-0.11F, 0.0F)
/* 105 */       .addPoint(0.03F, buildErosionJaggednessSpline(erosion, weirdness, ridges, 1.0F, 0.5F, 0.0F, 0.0F, jaggednessTransformer))
/* 106 */       .addPoint(0.65F, buildErosionJaggednessSpline(erosion, weirdness, ridges, 1.0F, 1.0F, 1.0F, 0.0F, jaggednessTransformer))
/* 107 */       .build();
/*     */   }
/*     */   
/*     */   private static <I extends BoundedFloatFunction<?>> CubicSpline<I> buildErosionJaggednessSpline(I erosion, I weirdness, I ridges, float jaggednessFactorAtPeakRidgeAndErosionIndex0, float jaggednessFactorAtPeakRidgeAndErosionIndex1, float jaggednessFactorAtHighRidgeAndErosionIndex0, float jaggednessFactorAtHighRidgeAndErosionIndex1, Float2FloatFunction jaggednessTransformer) {
/* 111 */     float erosionIndex1Middle = -0.5775F;
/* 113 */     CubicSpline<I> ridgeJaggednessSplineAtErosion0 = buildRidgeJaggednessSpline(weirdness, ridges, jaggednessFactorAtPeakRidgeAndErosionIndex0, jaggednessFactorAtHighRidgeAndErosionIndex0, jaggednessTransformer);
/* 114 */     CubicSpline<I> ridgeJaggednessSplineAtErosion1 = buildRidgeJaggednessSpline(weirdness, ridges, jaggednessFactorAtPeakRidgeAndErosionIndex1, jaggednessFactorAtHighRidgeAndErosionIndex1, jaggednessTransformer);
/* 116 */     return CubicSpline.builder((BoundedFloatFunction)erosion, jaggednessTransformer)
/* 117 */       .addPoint(-1.0F, ridgeJaggednessSplineAtErosion0)
/* 118 */       .addPoint(-0.78F, ridgeJaggednessSplineAtErosion1)
/* 119 */       .addPoint(-0.5775F, ridgeJaggednessSplineAtErosion1)
/* 120 */       .addPoint(-0.375F, 0.0F)
/* 121 */       .build();
/*     */   }
/*     */   
/*     */   public static float peaksAndValleys(float weirdness) {
/* 126 */     return -(Math.abs(Math.abs(weirdness) - 0.6666667F) - 0.33333334F) * 3.0F;
/*     */   }
/*     */   
/*     */   private static <I extends BoundedFloatFunction<?>> CubicSpline<I> buildRidgeJaggednessSpline(I weirdness, I ridges, float jaggednessFactorAtPeakRidge, float jaggednessFactorAtHighRidge, Float2FloatFunction jaggednessTransformer) {
/* 130 */     float highSliceStart = peaksAndValleys(0.4F);
/* 131 */     float highSliceEnd = peaksAndValleys(0.56666666F);
/* 133 */     float highSliceMiddle = (highSliceStart + highSliceEnd) / 2.0F;
/* 135 */     CubicSpline.Builder<I> ridgeSpline = CubicSpline.builder((BoundedFloatFunction)ridges, jaggednessTransformer);
/* 137 */     ridgeSpline.addPoint(highSliceStart, 0.0F);
/* 139 */     if (jaggednessFactorAtHighRidge > 0.0F) {
/* 140 */       ridgeSpline.addPoint(highSliceMiddle, buildWeirdnessJaggednessSpline(weirdness, jaggednessFactorAtHighRidge, jaggednessTransformer));
/*     */     } else {
/* 142 */       ridgeSpline.addPoint(highSliceMiddle, 0.0F);
/*     */     } 
/* 145 */     if (jaggednessFactorAtPeakRidge > 0.0F) {
/* 146 */       ridgeSpline.addPoint(1.0F, buildWeirdnessJaggednessSpline(weirdness, jaggednessFactorAtPeakRidge, jaggednessTransformer));
/*     */     } else {
/* 148 */       ridgeSpline.addPoint(1.0F, 0.0F);
/*     */     } 
/* 150 */     return ridgeSpline.build();
/*     */   }
/*     */   
/*     */   private static <I extends BoundedFloatFunction<?>> CubicSpline<I> buildWeirdnessJaggednessSpline(I weirdness, float jaggednessFactor, Float2FloatFunction jaggednessTransformer) {
/* 156 */     float maxJaggednessAtNegativeWeirdness = 0.63F * jaggednessFactor;
/* 157 */     float maxJaggednessAtPositiveWeirdness = 0.3F * jaggednessFactor;
/* 159 */     return CubicSpline.builder((BoundedFloatFunction)weirdness, jaggednessTransformer)
/* 160 */       .addPoint(-0.01F, maxJaggednessAtNegativeWeirdness)
/* 161 */       .addPoint(0.01F, maxJaggednessAtPositiveWeirdness)
/* 162 */       .build();
/*     */   }
/*     */   
/*     */   private static <I extends BoundedFloatFunction<?>> CubicSpline<I> getErosionFactor(I erosion, I weirdness, I ridges, float baseValue, boolean shatteredTerrain, Float2FloatFunction factorTransformer) {
/* 172 */     CubicSpline<I> baseSpline = CubicSpline.builder((BoundedFloatFunction)weirdness, factorTransformer).addPoint(-0.2F, 6.3F).addPoint(0.2F, baseValue).build();
/* 189 */     CubicSpline.Builder<I> erosionPoints = CubicSpline.builder((BoundedFloatFunction)erosion, factorTransformer).addPoint(-0.6F, baseSpline).addPoint(-0.5F, CubicSpline.builder((BoundedFloatFunction)weirdness, factorTransformer).addPoint(-0.05F, 6.3F).addPoint(0.05F, 2.67F).build()).addPoint(-0.35F, baseSpline).addPoint(-0.25F, baseSpline).addPoint(-0.1F, CubicSpline.builder((BoundedFloatFunction)weirdness, factorTransformer).addPoint(-0.05F, 2.67F).addPoint(0.05F, 6.3F).build()).addPoint(0.03F, baseSpline);
/* 197 */     if (shatteredTerrain) {
/* 201 */       CubicSpline<I> weirdnessShattered = CubicSpline.builder((BoundedFloatFunction)weirdness, factorTransformer).addPoint(0.0F, baseValue).addPoint(0.1F, 0.625F).build();
/* 206 */       CubicSpline<I> ridgesShattered = CubicSpline.builder((BoundedFloatFunction)ridges, factorTransformer).addPoint(-0.9F, baseValue).addPoint(-0.69F, weirdnessShattered).build();
/* 208 */       erosionPoints
/* 209 */         .addPoint(0.35F, baseValue)
/* 210 */         .addPoint(0.45F, ridgesShattered)
/* 211 */         .addPoint(0.55F, ridgesShattered)
/* 212 */         .addPoint(0.62F, baseValue);
/*     */     } else {
/* 217 */       CubicSpline<I> extremeHillsTerrainFromMidSliceAndUp = CubicSpline.builder((BoundedFloatFunction)ridges, factorTransformer).addPoint(-0.7F, baseSpline).addPoint(-0.15F, 1.37F).build();
/* 222 */       CubicSpline<I> extra3dNoiseOnPeaksOnly = CubicSpline.builder((BoundedFloatFunction)ridges, factorTransformer).addPoint(0.45F, baseSpline).addPoint(0.7F, 1.56F).build();
/* 224 */       erosionPoints
/* 225 */         .addPoint(0.05F, extra3dNoiseOnPeaksOnly)
/* 226 */         .addPoint(0.4F, extra3dNoiseOnPeaksOnly)
/* 227 */         .addPoint(0.45F, extremeHillsTerrainFromMidSliceAndUp)
/* 228 */         .addPoint(0.55F, extremeHillsTerrainFromMidSliceAndUp)
/* 229 */         .addPoint(0.58F, baseValue);
/*     */     } 
/* 231 */     return erosionPoints.build();
/*     */   }
/*     */   
/*     */   private static float calculateSlope(float y1, float y2, float x1, float x2) {
/* 235 */     return (y2 - y1) / (x2 - x1);
/*     */   }
/*     */   
/*     */   private static <I extends BoundedFloatFunction<?>> CubicSpline<I> buildMountainRidgeSplineWithPoints(I ridges, float modulation, boolean saddle, Float2FloatFunction offsetTransformer) {
/* 240 */     CubicSpline.Builder<I> build = CubicSpline.builder((BoundedFloatFunction)ridges, offsetTransformer);
/* 242 */     float allowRiversBelow = -0.7F;
/* 243 */     float minPoint = -1.0F;
/* 244 */     float minPointContinentalness = mountainContinentalness(-1.0F, modulation, -0.7F);
/* 245 */     float maxPoint = 1.0F;
/* 246 */     float maxPointContinentalness = mountainContinentalness(1.0F, modulation, -0.7F);
/* 248 */     float ridgeZeroPoint = calculateMountainRidgeZeroContinentalnessPoint(modulation);
/* 250 */     float afterRiverPoint = -0.65F;
/* 252 */     if (-0.65F < ridgeZeroPoint && ridgeZeroPoint < 1.0F) {
/* 256 */       float afterRiverThresholdContinentalness = mountainContinentalness(-0.65F, modulation, -0.7F);
/* 257 */       float beforeRiverPoint = -0.75F;
/* 258 */       float beforeRiverThresholdContinentalness = mountainContinentalness(-0.75F, modulation, -0.7F);
/* 261 */       float minPointDerivative = calculateSlope(minPointContinentalness, beforeRiverThresholdContinentalness, -1.0F, -0.75F);
/* 262 */       build.addPoint(-1.0F, minPointContinentalness, minPointDerivative);
/* 265 */       build.addPoint(-0.75F, beforeRiverThresholdContinentalness);
/* 266 */       build.addPoint(-0.65F, afterRiverThresholdContinentalness);
/* 269 */       float ridgeZeroPointContinentalness = mountainContinentalness(ridgeZeroPoint, modulation, -0.7F);
/* 270 */       float maxPointDerivative = calculateSlope(ridgeZeroPointContinentalness, maxPointContinentalness, ridgeZeroPoint, 1.0F);
/* 271 */       float smallOffset = 0.01F;
/* 272 */       build.addPoint(ridgeZeroPoint - 0.01F, ridgeZeroPointContinentalness);
/* 273 */       build.addPoint(ridgeZeroPoint, ridgeZeroPointContinentalness, maxPointDerivative);
/* 274 */       build.addPoint(1.0F, maxPointContinentalness, maxPointDerivative);
/*     */     } else {
/* 276 */       float simpleDerivative = calculateSlope(minPointContinentalness, maxPointContinentalness, -1.0F, 1.0F);
/* 278 */       if (saddle) {
/* 280 */         build.addPoint(-1.0F, Math.max(0.2F, minPointContinentalness));
/* 281 */         build.addPoint(0.0F, Mth.lerp(0.5F, minPointContinentalness, maxPointContinentalness), simpleDerivative);
/*     */       } else {
/* 285 */         build.addPoint(-1.0F, minPointContinentalness, simpleDerivative);
/*     */       } 
/* 287 */       build.addPoint(1.0F, maxPointContinentalness, simpleDerivative);
/*     */     } 
/* 290 */     return build.build();
/*     */   }
/*     */   
/*     */   private static float mountainContinentalness(float ridge, float modulation, float allowRiversBelow) {
/* 298 */     float ridgeOffset = 1.17F;
/* 299 */     float ridgeAmplitude = 0.46082947F;
/* 300 */     float ridgeSlope = 1.0F - (1.0F - modulation) * 0.5F;
/* 301 */     float ridgeIntersect = 0.5F * (1.0F - modulation);
/* 303 */     float adjustedRidgeHeight = (ridge + 1.17F) * 0.46082947F;
/* 304 */     float continentalness = adjustedRidgeHeight * ridgeSlope - ridgeIntersect;
/* 306 */     if (ridge < allowRiversBelow)
/* 309 */       return Math.max(continentalness, -0.2222F); 
/* 312 */     return Math.max(continentalness, 0.0F);
/*     */   }
/*     */   
/*     */   private static float calculateMountainRidgeZeroContinentalnessPoint(float modulation) {
/* 316 */     float ridgeOffset = 1.17F;
/* 317 */     float ridgeAmplitude = 0.46082947F;
/* 318 */     float ridgeSlope = 1.0F - (1.0F - modulation) * 0.5F;
/* 319 */     float ridgeIntersect = 0.5F * (1.0F - modulation);
/* 321 */     return ridgeIntersect / 0.46082947F * ridgeSlope - 1.17F;
/*     */   }
/*     */   
/*     */   public static <I extends BoundedFloatFunction<?>> CubicSpline<I> buildErosionOffsetSpline(I erosion, I ridges, float lowValley, float hill, float tallHill, float mountainFactor, float plain, float swamp, boolean includeExtremeHills, boolean saddle, Float2FloatFunction offsetTransformer) {
/* 325 */     float lowPeaks = 0.6F;
/* 327 */     float valleyPlateau = 0.5F;
/* 328 */     float plateau = 0.5F;
/* 338 */     CubicSpline<I> veryLowErosionMountains = buildMountainRidgeSplineWithPoints(ridges, Mth.lerp(mountainFactor, 0.6F, 1.5F), saddle, offsetTransformer);
/* 340 */     CubicSpline<I> lowErosionMountains = buildMountainRidgeSplineWithPoints(ridges, Mth.lerp(mountainFactor, 0.6F, 1.0F), saddle, offsetTransformer);
/* 341 */     CubicSpline<I> mountains = buildMountainRidgeSplineWithPoints(ridges, mountainFactor, saddle, offsetTransformer);
/* 343 */     CubicSpline<I> widePlateau = ridgeSpline(ridges, lowValley - 0.15F, 0.5F * mountainFactor, 
/*     */         
/* 346 */         Mth.lerp(0.5F, 0.5F, 0.5F) * mountainFactor, 0.5F * mountainFactor, 0.6F * mountainFactor, 0.5F, offsetTransformer);
/* 350 */     CubicSpline<I> narrowPlateau = ridgeSpline(ridges, lowValley, plain * mountainFactor, hill * mountainFactor, 0.5F * mountainFactor, 0.6F * mountainFactor, 0.5F, offsetTransformer);
/* 357 */     CubicSpline<I> plains = ridgeSpline(ridges, lowValley, plain, plain, hill, tallHill, 0.5F, offsetTransformer);
/* 364 */     CubicSpline<I> plainsFarInland = ridgeSpline(ridges, lowValley, plain, plain, hill, tallHill, 0.5F, offsetTransformer);
/* 376 */     CubicSpline<I> extremeHills = CubicSpline.builder((BoundedFloatFunction)ridges, offsetTransformer).addPoint(-1.0F, lowValley).addPoint(-0.4F, plains).addPoint(0.0F, tallHill + 0.07F).build();
/* 378 */     CubicSpline<I> swamps = ridgeSpline(ridges, -0.02F, swamp, swamp, hill, tallHill, 0.0F, offsetTransformer);
/* 393 */     CubicSpline.Builder<I> builder = CubicSpline.builder((BoundedFloatFunction)erosion, offsetTransformer).addPoint(-0.85F, veryLowErosionMountains).addPoint(-0.7F, lowErosionMountains).addPoint(-0.4F, mountains).addPoint(-0.35F, widePlateau).addPoint(-0.1F, narrowPlateau).addPoint(0.2F, plains);
/* 395 */     if (includeExtremeHills)
/* 398 */       builder
/* 399 */         .addPoint(0.4F, plainsFarInland)
/* 400 */         .addPoint(0.45F, extremeHills)
/* 401 */         .addPoint(0.55F, extremeHills)
/* 402 */         .addPoint(0.58F, plainsFarInland); 
/* 404 */     builder
/* 405 */       .addPoint(0.7F, swamps);
/* 407 */     return builder.build();
/*     */   }
/*     */   
/*     */   private static <I extends BoundedFloatFunction<?>> CubicSpline<I> ridgeSpline(I ridges, float valley, float low, float mid, float high, float peaks, float minValleySteepness, Float2FloatFunction offsetTransformer) {
/* 412 */     float d1 = Math.max(0.5F * (low - valley), minValleySteepness);
/* 413 */     float d2 = 5.0F * (mid - low);
/* 414 */     return CubicSpline.builder((BoundedFloatFunction)ridges, offsetTransformer)
/* 415 */       .addPoint(-1.0F, valley, d1)
/* 416 */       .addPoint(-0.4F, low, Math.min(d1, d2))
/* 417 */       .addPoint(0.0F, mid, d2)
/* 418 */       .addPoint(0.4F, high, 2.0F * (high - mid))
/* 419 */       .addPoint(1.0F, peaks, 0.7F * (peaks - high))
/* 420 */       .build();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\TerrainProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */