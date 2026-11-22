/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.List;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.TerrainProvider;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.synth.BlendedNoise;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ public class NoiseRouterData {
/*     */   public static final float GLOBAL_OFFSET = -0.50375F;
/*     */   
/*     */   private static final float ORE_THICKNESS = 0.08F;
/*     */   
/*     */   private static final double VEININESS_FREQUENCY = 1.5D;
/*     */   
/*     */   private static final double NOODLE_SPACING_AND_STRAIGHTNESS = 1.5D;
/*     */   
/*     */   private static final double SURFACE_DENSITY_THRESHOLD = 1.5625D;
/*     */   
/*     */   private static final double CHEESE_NOISE_TARGET = -0.703125D;
/*     */   
/*     */   public static final double NOISE_ZERO = 0.390625D;
/*     */   
/*     */   public static final int ISLAND_CHUNK_DISTANCE = 64;
/*     */   
/*     */   public static final long ISLAND_CHUNK_DISTANCE_SQR = 4096L;
/*     */   
/*     */   private static final int DENSITY_Y_ANCHOR_BOTTOM = -64;
/*     */   
/*     */   private static final int DENSITY_Y_ANCHOR_TOP = 320;
/*     */   
/*     */   private static final double DENSITY_Y_BOTTOM = 1.5D;
/*     */   
/*     */   private static final double DENSITY_Y_TOP = -1.5D;
/*     */   
/*     */   private static final int OVERWORLD_BOTTOM_SLIDE_HEIGHT = 24;
/*     */   
/*     */   private static final double BASE_DENSITY_MULTIPLIER = 4.0D;
/*     */   
/*  92 */   private static final DensityFunction BLENDING_FACTOR = DensityFunctions.constant(10.0D);
/*     */   
/*  93 */   private static final DensityFunction BLENDING_JAGGEDNESS = DensityFunctions.zero();
/*     */   
/*  95 */   private static final ResourceKey<DensityFunction> ZERO = createKey("zero");
/*     */   
/*  96 */   private static final ResourceKey<DensityFunction> Y = createKey("y");
/*     */   
/*  97 */   private static final ResourceKey<DensityFunction> SHIFT_X = createKey("shift_x");
/*     */   
/*  98 */   private static final ResourceKey<DensityFunction> SHIFT_Z = createKey("shift_z");
/*     */   
/* 100 */   private static final ResourceKey<DensityFunction> BASE_3D_NOISE_OVERWORLD = createKey("overworld/base_3d_noise");
/*     */   
/* 101 */   private static final ResourceKey<DensityFunction> BASE_3D_NOISE_NETHER = createKey("nether/base_3d_noise");
/*     */   
/* 102 */   private static final ResourceKey<DensityFunction> BASE_3D_NOISE_END = createKey("end/base_3d_noise");
/*     */   
/* 104 */   public static final ResourceKey<DensityFunction> CONTINENTS = createKey("overworld/continents");
/*     */   
/* 105 */   public static final ResourceKey<DensityFunction> EROSION = createKey("overworld/erosion");
/*     */   
/* 106 */   public static final ResourceKey<DensityFunction> RIDGES = createKey("overworld/ridges");
/*     */   
/* 107 */   public static final ResourceKey<DensityFunction> RIDGES_FOLDED = createKey("overworld/ridges_folded");
/*     */   
/* 109 */   public static final ResourceKey<DensityFunction> OFFSET = createKey("overworld/offset");
/*     */   
/* 110 */   public static final ResourceKey<DensityFunction> FACTOR = createKey("overworld/factor");
/*     */   
/* 111 */   public static final ResourceKey<DensityFunction> JAGGEDNESS = createKey("overworld/jaggedness");
/*     */   
/* 112 */   public static final ResourceKey<DensityFunction> DEPTH = createKey("overworld/depth");
/*     */   
/* 113 */   private static final ResourceKey<DensityFunction> SLOPED_CHEESE = createKey("overworld/sloped_cheese");
/*     */   
/* 115 */   public static final ResourceKey<DensityFunction> CONTINENTS_LARGE = createKey("overworld_large_biomes/continents");
/*     */   
/* 116 */   public static final ResourceKey<DensityFunction> EROSION_LARGE = createKey("overworld_large_biomes/erosion");
/*     */   
/* 118 */   private static final ResourceKey<DensityFunction> OFFSET_LARGE = createKey("overworld_large_biomes/offset");
/*     */   
/* 119 */   private static final ResourceKey<DensityFunction> FACTOR_LARGE = createKey("overworld_large_biomes/factor");
/*     */   
/* 120 */   private static final ResourceKey<DensityFunction> JAGGEDNESS_LARGE = createKey("overworld_large_biomes/jaggedness");
/*     */   
/* 121 */   private static final ResourceKey<DensityFunction> DEPTH_LARGE = createKey("overworld_large_biomes/depth");
/*     */   
/* 122 */   private static final ResourceKey<DensityFunction> SLOPED_CHEESE_LARGE = createKey("overworld_large_biomes/sloped_cheese");
/*     */   
/* 124 */   private static final ResourceKey<DensityFunction> OFFSET_AMPLIFIED = createKey("overworld_amplified/offset");
/*     */   
/* 125 */   private static final ResourceKey<DensityFunction> FACTOR_AMPLIFIED = createKey("overworld_amplified/factor");
/*     */   
/* 126 */   private static final ResourceKey<DensityFunction> JAGGEDNESS_AMPLIFIED = createKey("overworld_amplified/jaggedness");
/*     */   
/* 127 */   private static final ResourceKey<DensityFunction> DEPTH_AMPLIFIED = createKey("overworld_amplified/depth");
/*     */   
/* 128 */   private static final ResourceKey<DensityFunction> SLOPED_CHEESE_AMPLIFIED = createKey("overworld_amplified/sloped_cheese");
/*     */   
/* 130 */   private static final ResourceKey<DensityFunction> SLOPED_CHEESE_END = createKey("end/sloped_cheese");
/*     */   
/* 132 */   private static final ResourceKey<DensityFunction> SPAGHETTI_ROUGHNESS_FUNCTION = createKey("overworld/caves/spaghetti_roughness_function");
/*     */   
/* 133 */   private static final ResourceKey<DensityFunction> ENTRANCES = createKey("overworld/caves/entrances");
/*     */   
/* 134 */   private static final ResourceKey<DensityFunction> NOODLE = createKey("overworld/caves/noodle");
/*     */   
/* 135 */   private static final ResourceKey<DensityFunction> PILLARS = createKey("overworld/caves/pillars");
/*     */   
/* 136 */   private static final ResourceKey<DensityFunction> SPAGHETTI_2D_THICKNESS_MODULATOR = createKey("overworld/caves/spaghetti_2d_thickness_modulator");
/*     */   
/* 137 */   private static final ResourceKey<DensityFunction> SPAGHETTI_2D = createKey("overworld/caves/spaghetti_2d");
/*     */   
/*     */   private static ResourceKey<DensityFunction> createKey(String name) {
/* 140 */     return ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace(name));
/*     */   }
/*     */   
/*     */   public static Holder<? extends DensityFunction> bootstrap(BootstrapContext<DensityFunction> context) {
/* 144 */     HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);
/* 145 */     HolderGetter<DensityFunction> functions = context.lookup(Registries.DENSITY_FUNCTION);
/* 147 */     context.register(ZERO, DensityFunctions.zero());
/* 149 */     int belowBottom = DimensionType.MIN_Y * 2;
/* 150 */     int aboveTop = DimensionType.MAX_Y * 2;
/* 151 */     context.register(Y, DensityFunctions.yClampedGradient(belowBottom, aboveTop, belowBottom, aboveTop));
/* 153 */     DensityFunction shiftX = registerAndWrap(context, SHIFT_X, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftA((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.SHIFT)))));
/* 154 */     DensityFunction shiftZ = registerAndWrap(context, SHIFT_Z, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftB((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.SHIFT)))));
/* 156 */     context.register(BASE_3D_NOISE_OVERWORLD, BlendedNoise.createUnseeded(0.25D, 0.125D, 80.0D, 160.0D, 8.0D));
/* 157 */     context.register(BASE_3D_NOISE_NETHER, BlendedNoise.createUnseeded(0.25D, 0.375D, 80.0D, 60.0D, 8.0D));
/* 158 */     context.register(BASE_3D_NOISE_END, BlendedNoise.createUnseeded(0.25D, 0.25D, 80.0D, 160.0D, 4.0D));
/* 160 */     DensityFunction continents = registerAndWrap(context, CONTINENTS, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, (Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.CONTINENTALNESS))));
/* 161 */     DensityFunction erosion = registerAndWrap(context, EROSION, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, (Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.EROSION))));
/* 162 */     DensityFunction ridge = registerAndWrap(context, RIDGES, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, (Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.RIDGE))));
/* 163 */     context.register(RIDGES_FOLDED, peaksAndValleys(ridge));
/* 165 */     DensityFunction jaggedNoise = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.JAGGED), 1500.0D, 0.0D);
/* 167 */     registerTerrainNoises(context, functions, jaggedNoise, continents, erosion, OFFSET, FACTOR, JAGGEDNESS, DEPTH, SLOPED_CHEESE, false);
/* 169 */     DensityFunction continentsLarge = registerAndWrap(context, CONTINENTS_LARGE, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, (Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.CONTINENTALNESS_LARGE))));
/* 170 */     DensityFunction erosionLarge = registerAndWrap(context, EROSION_LARGE, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, (Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.EROSION_LARGE))));
/* 172 */     registerTerrainNoises(context, functions, jaggedNoise, continentsLarge, erosionLarge, OFFSET_LARGE, FACTOR_LARGE, JAGGEDNESS_LARGE, DEPTH_LARGE, SLOPED_CHEESE_LARGE, false);
/* 173 */     registerTerrainNoises(context, functions, jaggedNoise, continents, erosion, OFFSET_AMPLIFIED, FACTOR_AMPLIFIED, JAGGEDNESS_AMPLIFIED, DEPTH_AMPLIFIED, SLOPED_CHEESE_AMPLIFIED, true);
/* 175 */     context.register(SLOPED_CHEESE_END, DensityFunctions.add(DensityFunctions.endIslands(0L), getFunction(functions, BASE_3D_NOISE_END)));
/* 177 */     context.register(SPAGHETTI_ROUGHNESS_FUNCTION, spaghettiRoughnessFunction(noises));
/* 178 */     context.register(SPAGHETTI_2D_THICKNESS_MODULATOR, DensityFunctions.cacheOnce(DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.SPAGHETTI_2D_THICKNESS), 2.0D, 1.0D, -0.6D, -1.3D)));
/* 179 */     context.register(SPAGHETTI_2D, spaghetti2D(functions, noises));
/* 180 */     context.register(ENTRANCES, entrances(functions, noises));
/* 181 */     context.register(NOODLE, noodle(functions, noises));
/* 182 */     return (Holder<? extends DensityFunction>)context.register(PILLARS, pillars(noises));
/*     */   }
/*     */   
/*     */   private static void registerTerrainNoises(BootstrapContext<DensityFunction> context, HolderGetter<DensityFunction> functions, DensityFunction jaggedNoise, DensityFunction continentsFunction, DensityFunction erosionFunction, ResourceKey<DensityFunction> offsetName, ResourceKey<DensityFunction> factorName, ResourceKey<DensityFunction> jaggednessName, ResourceKey<DensityFunction> depthName, ResourceKey<DensityFunction> slopedCheeseName, boolean amplified) {
/* 198 */     DensityFunctions.Spline.Coordinate continents = new DensityFunctions.Spline.Coordinate(continentsFunction);
/* 199 */     DensityFunctions.Spline.Coordinate erosion = new DensityFunctions.Spline.Coordinate(erosionFunction);
/* 200 */     DensityFunctions.Spline.Coordinate weirdness = new DensityFunctions.Spline.Coordinate(getFunction(functions, RIDGES));
/* 201 */     DensityFunctions.Spline.Coordinate ridges = new DensityFunctions.Spline.Coordinate(getFunction(functions, RIDGES_FOLDED));
/* 204 */     DensityFunction offset = registerAndWrap(context, offsetName, splineWithBlending(
/* 205 */           DensityFunctions.add(DensityFunctions.constant(-0.5037500262260437D), DensityFunctions.spline(TerrainProvider.overworldOffset(continents, erosion, ridges, amplified))), 
/* 206 */           DensityFunctions.blendOffset()));
/* 208 */     DensityFunction factor = registerAndWrap(context, factorName, splineWithBlending(
/* 209 */           DensityFunctions.spline(TerrainProvider.overworldFactor(continents, erosion, weirdness, ridges, amplified)), BLENDING_FACTOR));
/* 212 */     DensityFunction depth = registerAndWrap(context, depthName, offsetToDepth(offset));
/* 213 */     DensityFunction unscaledJaggedness = registerAndWrap(context, jaggednessName, splineWithBlending(
/* 214 */           DensityFunctions.spline(TerrainProvider.overworldJaggedness(continents, erosion, weirdness, ridges, amplified)), BLENDING_JAGGEDNESS));
/* 218 */     DensityFunction jaggedness = DensityFunctions.flatCache(DensityFunctions.mul(unscaledJaggedness, jaggedNoise.halfNegative()));
/* 219 */     DensityFunction initialDensity = noiseGradientDensity(factor, DensityFunctions.add(depth, jaggedness));
/* 220 */     context.register(slopedCheeseName, DensityFunctions.add(initialDensity, getFunction(functions, BASE_3D_NOISE_OVERWORLD)));
/*     */   }
/*     */   
/*     */   private static DensityFunction offsetToDepth(DensityFunction offset) {
/* 224 */     return DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5D, -1.5D), offset);
/*     */   }
/*     */   
/*     */   private static DensityFunction registerAndWrap(BootstrapContext<DensityFunction> context, ResourceKey<DensityFunction> name, DensityFunction value) {
/* 228 */     return new DensityFunctions.HolderHolder((Holder<DensityFunction>)context.register(name, value));
/*     */   }
/*     */   
/*     */   private static DensityFunction getFunction(HolderGetter<DensityFunction> functions, ResourceKey<DensityFunction> name) {
/* 232 */     return new DensityFunctions.HolderHolder((Holder<DensityFunction>)functions.getOrThrow(name));
/*     */   }
/*     */   
/*     */   private static DensityFunction peaksAndValleys(DensityFunction weirdness) {
/* 242 */     return DensityFunctions.mul(DensityFunctions.add(DensityFunctions.add(weirdness.abs(), DensityFunctions.constant(-0.6666666666666666D)).abs(), DensityFunctions.constant(-0.3333333333333333D)), DensityFunctions.constant(-3.0D));
/*     */   }
/*     */   
/*     */   public static float peaksAndValleys(float weirdness) {
/* 246 */     return TerrainProvider.peaksAndValleys(weirdness);
/*     */   }
/*     */   
/*     */   private static DensityFunction spaghettiRoughnessFunction(HolderGetter<NormalNoise.NoiseParameters> noises) {
/* 250 */     DensityFunction spaghettiRoughnessNoise = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.SPAGHETTI_ROUGHNESS));
/* 252 */     DensityFunction spaghettiRoughnessModulator = DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.SPAGHETTI_ROUGHNESS_MODULATOR), 0.0D, -0.1D);
/* 254 */     return DensityFunctions.cacheOnce(DensityFunctions.mul(spaghettiRoughnessModulator, 
/*     */           
/* 256 */           DensityFunctions.add(spaghettiRoughnessNoise.abs(), DensityFunctions.constant(-0.4D))));
/*     */   }
/*     */   
/*     */   private static DensityFunction entrances(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises) {
/* 261 */     DensityFunction spaghetti3DRarityModulator = DensityFunctions.cacheOnce(DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.SPAGHETTI_3D_RARITY), 2.0D, 1.0D));
/* 264 */     DensityFunction spaghetti3DThicknessModulator = DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.SPAGHETTI_3D_THICKNESS), -0.065D, -0.088D);
/* 266 */     DensityFunction spaghetti3DCave1 = QuantizedSpaghettiRarity.wrapRarity3d(spaghetti3DRarityModulator, (Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.SPAGHETTI_3D_1));
/* 267 */     DensityFunction spaghetti3DCave2 = QuantizedSpaghettiRarity.wrapRarity3d(spaghetti3DRarityModulator, (Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.SPAGHETTI_3D_2));
/* 273 */     DensityFunction spaghetti3DFunction = DensityFunctions.add(DensityFunctions.max(spaghetti3DCave1, spaghetti3DCave2), spaghetti3DThicknessModulator).clamp(-1.0D, 1.0D);
/* 275 */     DensityFunction spaghettiRoughnessFunction = getFunction(functions, SPAGHETTI_ROUGHNESS_FUNCTION);
/* 277 */     DensityFunction bigEntranceNoiseSource = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.CAVE_ENTRANCE), 0.75D, 0.5D);
/* 279 */     DensityFunction bigEntrancesFunction = DensityFunctions.add(
/* 280 */         DensityFunctions.add(bigEntranceNoiseSource, DensityFunctions.constant(0.37D)), 
/*     */         
/* 283 */         DensityFunctions.yClampedGradient(-10, 30, 0.3D, 0.0D));
/* 287 */     return DensityFunctions.cacheOnce(DensityFunctions.min(bigEntrancesFunction, DensityFunctions.add(spaghettiRoughnessFunction, spaghetti3DFunction)));
/*     */   }
/*     */   
/*     */   private static DensityFunction noodle(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises) {
/* 291 */     DensityFunction y = getFunction(functions, Y);
/* 293 */     int minBlockY = -64;
/* 296 */     int noodleMinY = -60;
/* 297 */     int noodleMaxY = 320;
/* 299 */     DensityFunction noodleToggle = yLimitedInterpolatable(y, DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.NOODLE), 1.0D, 1.0D), -60, 320, -1);
/* 303 */     DensityFunction noodleThickness = yLimitedInterpolatable(y, DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.NOODLE_THICKNESS), 1.0D, 1.0D, -0.05D, -0.1D), -60, 320, 0);
/* 305 */     double noodleRidgeFrequency = 2.6666666666666665D;
/* 306 */     DensityFunction noodleRidgeA = yLimitedInterpolatable(y, DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.NOODLE_RIDGE_A), 2.6666666666666665D, 2.6666666666666665D), -60, 320, 0);
/* 307 */     DensityFunction noodleRidgeB = yLimitedInterpolatable(y, DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.NOODLE_RIDGE_B), 2.6666666666666665D, 2.6666666666666665D), -60, 320, 0);
/* 309 */     DensityFunction noodleRidged = DensityFunctions.mul(
/* 310 */         DensityFunctions.constant(1.5D), 
/* 311 */         DensityFunctions.max(noodleRidgeA.abs(), noodleRidgeB.abs()));
/* 314 */     return DensityFunctions.rangeChoice(noodleToggle, -1000000.0D, 0.0D, 
/*     */         
/* 318 */         DensityFunctions.constant(64.0D), 
/* 319 */         DensityFunctions.add(noodleThickness, noodleRidged));
/*     */   }
/*     */   
/*     */   private static DensityFunction pillars(HolderGetter<NormalNoise.NoiseParameters> noises) {
/* 327 */     double xzFrequency = 25.0D;
/* 332 */     double yFrequency = 0.3D;
/* 334 */     DensityFunction pillarNoiseSource = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.PILLAR), 25.0D, 0.3D);
/* 338 */     DensityFunction pillarRarenessModulator = DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.PILLAR_RARENESS), 0.0D, -2.0D);
/* 342 */     DensityFunction pillarThicknessModulator = DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.PILLAR_THICKNESS), 0.0D, 1.1D);
/* 344 */     DensityFunction pillarsWithRareness = DensityFunctions.add(
/* 345 */         DensityFunctions.mul(pillarNoiseSource, DensityFunctions.constant(2.0D)), pillarRarenessModulator);
/* 349 */     return DensityFunctions.cacheOnce(DensityFunctions.mul(pillarsWithRareness, pillarThicknessModulator
/*     */           
/* 352 */           .cube()));
/*     */   }
/*     */   
/*     */   private static DensityFunction spaghetti2D(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises) {
/* 357 */     DensityFunction spaghetti2DRarityModulator = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.SPAGHETTI_2D_MODULATOR), 2.0D, 1.0D);
/* 358 */     DensityFunction spaghetti2DCave = QuantizedSpaghettiRarity.wrapRarity2d(spaghetti2DRarityModulator, (Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.SPAGHETTI_2D));
/* 360 */     DensityFunction spaghetti2DElevationModulator = DensityFunctions.mappedNoise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.SPAGHETTI_2D_ELEVATION), 0.0D, Math.floorDiv(-64, 8), 8.0D);
/* 362 */     DensityFunction spaghetti2DThicknessModulator = getFunction(functions, SPAGHETTI_2D_THICKNESS_MODULATOR);
/* 367 */     DensityFunction slopedSpaghetti = DensityFunctions.add(DensityFunctions.flatCache(spaghetti2DElevationModulator), DensityFunctions.yClampedGradient(-64, 320, 8.0D, -40.0D)).abs();
/* 369 */     DensityFunction layerRidged = DensityFunctions.add(slopedSpaghetti, spaghetti2DThicknessModulator).cube();
/* 371 */     double ridgeOffset = 0.083D;
/* 372 */     DensityFunction caveNoise = DensityFunctions.add(spaghetti2DCave, DensityFunctions.mul(DensityFunctions.constant(0.083D), spaghetti2DThicknessModulator));
/* 375 */     return DensityFunctions.max(caveNoise, layerRidged).clamp(-1.0D, 1.0D);
/*     */   }
/*     */   
/*     */   private static DensityFunction underground(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises, DensityFunction slopedCheese) {
/* 379 */     DensityFunction spaghetti2DFunction = getFunction(functions, SPAGHETTI_2D);
/* 381 */     DensityFunction spaghettiRoughnessFunction = getFunction(functions, SPAGHETTI_ROUGHNESS_FUNCTION);
/* 383 */     DensityFunction layerNoiseSource = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.CAVE_LAYER), 8.0D);
/* 385 */     DensityFunction layerizedCavernsFunction = DensityFunctions.mul(DensityFunctions.constant(4.0D), layerNoiseSource.square());
/* 387 */     DensityFunction cheese = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.CAVE_CHEESE), 0.6666666666666666D);
/* 389 */     DensityFunction solidifedCheeseWithTopSlide = DensityFunctions.add(
/*     */         
/* 391 */         DensityFunctions.add(DensityFunctions.constant(0.27D), cheese).clamp(-1.0D, 1.0D), 
/*     */         
/* 393 */         DensityFunctions.add(DensityFunctions.constant(1.5D), DensityFunctions.mul(DensityFunctions.constant(-0.64D), slopedCheese)).clamp(0.0D, 0.5D));
/* 396 */     DensityFunction baseCaveDensity = DensityFunctions.add(layerizedCavernsFunction, solidifedCheeseWithTopSlide);
/* 398 */     DensityFunction undergroundSubtractions = DensityFunctions.min(DensityFunctions.min(baseCaveDensity, getFunction(functions, ENTRANCES)), DensityFunctions.add(spaghetti2DFunction, spaghettiRoughnessFunction));
/* 400 */     DensityFunction pillarsWithoutCutoff = getFunction(functions, PILLARS);
/* 402 */     DensityFunction pillars = DensityFunctions.rangeChoice(pillarsWithoutCutoff, -1000000.0D, 0.03D, DensityFunctions.constant(-1000000.0D), pillarsWithoutCutoff);
/* 404 */     return DensityFunctions.max(undergroundSubtractions, pillars);
/*     */   }
/*     */   
/*     */   private static DensityFunction postProcess(DensityFunction slide) {
/* 409 */     DensityFunction blended = DensityFunctions.blendDensity(slide);
/* 410 */     return DensityFunctions.interpolated(DensityFunctions.mul(blended, DensityFunctions.constant(0.64D))).squeeze();
/*     */   }
/*     */   
/*     */   private static DensityFunction remap(DensityFunction input, double fromMin, double fromMax, double toMin, double toMax) {
/* 414 */     double factor = (toMax - toMin) / (fromMax - fromMin);
/* 415 */     double offset = toMin - fromMin * factor;
/* 416 */     return DensityFunctions.add(DensityFunctions.mul(input, DensityFunctions.constant(factor)), DensityFunctions.constant(offset));
/*     */   }
/*     */   
/*     */   protected static NoiseRouter overworld(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises, boolean largeBiomes, boolean amplified) {
/* 421 */     DensityFunction barrierNoise = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.AQUIFER_BARRIER), 0.5D);
/* 422 */     DensityFunction fluidLevelFloodednessNoise = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67D);
/* 423 */     DensityFunction fluidLevelSpreadNoise = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143D);
/* 424 */     DensityFunction lavaNoise = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.AQUIFER_LAVA));
/* 426 */     DensityFunction shiftX = getFunction(functions, SHIFT_X);
/* 427 */     DensityFunction shiftZ = getFunction(functions, SHIFT_Z);
/* 430 */     DensityFunction temperature = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, (Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(largeBiomes ? Noises.TEMPERATURE_LARGE : Noises.TEMPERATURE));
/* 431 */     DensityFunction vegetation = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, (Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(largeBiomes ? Noises.VEGETATION_LARGE : Noises.VEGETATION));
/* 433 */     DensityFunction offset = getFunction(functions, largeBiomes ? OFFSET_LARGE : (amplified ? OFFSET_AMPLIFIED : OFFSET));
/* 434 */     DensityFunction factor = getFunction(functions, largeBiomes ? FACTOR_LARGE : (amplified ? FACTOR_AMPLIFIED : FACTOR));
/* 435 */     DensityFunction depth = getFunction(functions, largeBiomes ? DEPTH_LARGE : (amplified ? DEPTH_AMPLIFIED : DEPTH));
/* 437 */     DensityFunction preliminarySurfaceLevel = preliminarySurfaceLevel(offset, factor, amplified);
/* 439 */     DensityFunction slopedCheese = DensityFunctions.cacheOnce(getFunction(functions, largeBiomes ? SLOPED_CHEESE_LARGE : (amplified ? SLOPED_CHEESE_AMPLIFIED : SLOPED_CHEESE)));
/* 444 */     DensityFunction surfaceWithEntrances = DensityFunctions.min(slopedCheese, DensityFunctions.mul(DensityFunctions.constant(5.0D), getFunction(functions, ENTRANCES)));
/* 445 */     DensityFunction caves = DensityFunctions.rangeChoice(slopedCheese, -1000000.0D, 1.5625D, surfaceWithEntrances, underground(functions, noises, slopedCheese));
/* 447 */     DensityFunction fullNoise = DensityFunctions.min(postProcess(slideOverworld(amplified, caves)), getFunction(functions, NOODLE));
/* 449 */     DensityFunction y = getFunction(functions, Y);
/* 451 */     int veinMinY = Stream.<OreVeinifier.VeinType>of(OreVeinifier.VeinType.values()).mapToInt(t -> t.minY).min().orElse(-DimensionType.MIN_Y * 2);
/* 452 */     int veinMaxY = Stream.<OreVeinifier.VeinType>of(OreVeinifier.VeinType.values()).mapToInt(t -> t.maxY).max().orElse(-DimensionType.MIN_Y * 2);
/* 455 */     DensityFunction veinToggle = yLimitedInterpolatable(y, DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.ORE_VEININESS), 1.5D, 1.5D), veinMinY, veinMaxY, 0);
/* 457 */     float oreRidgeFrequency = 4.0F;
/* 460 */     DensityFunction veinA = yLimitedInterpolatable(y, DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.ORE_VEIN_A), 4.0D, 4.0D), veinMinY, veinMaxY, 0).abs();
/* 461 */     DensityFunction veinB = yLimitedInterpolatable(y, DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.ORE_VEIN_B), 4.0D, 4.0D), veinMinY, veinMaxY, 0).abs();
/* 463 */     DensityFunction veinRidged = DensityFunctions.add(DensityFunctions.constant(-0.07999999821186066D), DensityFunctions.max(veinA, veinB));
/* 465 */     DensityFunction veinGap = DensityFunctions.noise((Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.ORE_GAP));
/* 469 */     return new NoiseRouter(barrierNoise, fluidLevelFloodednessNoise, fluidLevelSpreadNoise, lavaNoise, temperature, vegetation, 
/*     */         
/* 476 */         getFunction(functions, largeBiomes ? CONTINENTS_LARGE : CONTINENTS), 
/* 477 */         getFunction(functions, largeBiomes ? EROSION_LARGE : EROSION), depth, 
/*     */         
/* 479 */         getFunction(functions, RIDGES), preliminarySurfaceLevel, fullNoise, veinToggle, veinRidged, veinGap);
/*     */   }
/*     */   
/*     */   private static DensityFunction slideOverworld(boolean isAmplified, DensityFunction caves) {
/* 489 */     return slide(caves, -64, 384, 
/*     */         
/* 493 */         isAmplified ? 16 : 80, 
/* 494 */         isAmplified ? 0 : 64, -0.078125D, 0, 24, 
/*     */         
/* 498 */         isAmplified ? 0.4D : 0.1171875D);
/*     */   }
/*     */   
/*     */   private static DensityFunction slideNetherLike(HolderGetter<DensityFunction> functions, int minY, int height) {
/* 503 */     return slide(
/* 504 */         getFunction(functions, BASE_3D_NOISE_NETHER), minY, height, 24, 0, 0.9375D, -8, 24, 2.5D);
/*     */   }
/*     */   
/*     */   private static DensityFunction slideEndLike(DensityFunction caves, int minY, int height) {
/* 517 */     return slide(caves, minY, height, 72, -184, -23.4375D, 4, 32, -0.234375D);
/*     */   }
/*     */   
/*     */   protected static NoiseRouter nether(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises) {
/* 532 */     DensityFunction temperature = DensityFunctions.shiftedNoise2d(DensityFunctions.zero(), DensityFunctions.zero(), 0.25D, (Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.TEMPERATURE_NETHER));
/* 533 */     DensityFunction vegetation = DensityFunctions.shiftedNoise2d(DensityFunctions.zero(), DensityFunctions.zero(), 0.25D, (Holder<NormalNoise.NoiseParameters>)noises.getOrThrow(Noises.VEGETATION_NETHER));
/* 535 */     DensityFunction slide = slideNetherLike(functions, 0, 128);
/* 536 */     DensityFunction fullNoise = postProcess(slide);
/* 538 */     return new NoiseRouter(
/* 539 */         DensityFunctions.zero(), 
/* 540 */         DensityFunctions.zero(), 
/* 541 */         DensityFunctions.zero(), 
/* 542 */         DensityFunctions.zero(), temperature, vegetation, 
/*     */         
/* 545 */         DensityFunctions.zero(), 
/* 546 */         DensityFunctions.zero(), 
/* 547 */         DensityFunctions.zero(), 
/* 548 */         DensityFunctions.zero(), 
/* 549 */         DensityFunctions.zero(), fullNoise, 
/*     */         
/* 551 */         DensityFunctions.zero(), 
/* 552 */         DensityFunctions.zero(), 
/* 553 */         DensityFunctions.zero());
/*     */   }
/*     */   
/*     */   protected static NoiseRouter caves(HolderGetter<DensityFunction> functions) {
/* 558 */     DensityFunction slide = slideNetherLike(functions, -64, 192);
/* 559 */     return simpleRouter(
/* 560 */         postProcess(slide));
/*     */   }
/*     */   
/*     */   protected static NoiseRouter floatingIslands(HolderGetter<DensityFunction> functions, HolderGetter<NormalNoise.NoiseParameters> noises) {
/* 565 */     DensityFunction slide = slideEndLike(getFunction(functions, BASE_3D_NOISE_END), 0, 256);
/* 566 */     return simpleRouter(
/* 567 */         postProcess(slide));
/*     */   }
/*     */   
/*     */   private static DensityFunction slideEnd(DensityFunction caves) {
/* 572 */     return slideEndLike(caves, 0, 128);
/*     */   }
/*     */   
/*     */   protected static NoiseRouter end(HolderGetter<DensityFunction> functions) {
/* 576 */     DensityFunction islands = DensityFunctions.cache2d(DensityFunctions.endIslands(0L));
/* 577 */     DensityFunction fullNoise = postProcess(slideEnd(getFunction(functions, SLOPED_CHEESE_END)));
/* 580 */     return new NoiseRouter(
/* 581 */         DensityFunctions.zero(), 
/* 582 */         DensityFunctions.zero(), 
/* 583 */         DensityFunctions.zero(), 
/* 584 */         DensityFunctions.zero(), 
/* 585 */         DensityFunctions.zero(), 
/* 586 */         DensityFunctions.zero(), 
/* 587 */         DensityFunctions.zero(), islands, 
/*     */         
/* 589 */         DensityFunctions.zero(), 
/* 590 */         DensityFunctions.zero(), 
/* 591 */         DensityFunctions.zero(), fullNoise, 
/*     */         
/* 593 */         DensityFunctions.zero(), 
/* 594 */         DensityFunctions.zero(), 
/* 595 */         DensityFunctions.zero());
/*     */   }
/*     */   
/*     */   private static NoiseRouter simpleRouter(DensityFunction fullNoise) {
/* 600 */     return new NoiseRouter(
/* 601 */         DensityFunctions.zero(), 
/* 602 */         DensityFunctions.zero(), 
/* 603 */         DensityFunctions.zero(), 
/* 604 */         DensityFunctions.zero(), 
/* 605 */         DensityFunctions.zero(), 
/* 606 */         DensityFunctions.zero(), 
/* 607 */         DensityFunctions.zero(), 
/* 608 */         DensityFunctions.zero(), 
/* 609 */         DensityFunctions.zero(), 
/* 610 */         DensityFunctions.zero(), 
/* 611 */         DensityFunctions.zero(), fullNoise, 
/*     */         
/* 613 */         DensityFunctions.zero(), 
/* 614 */         DensityFunctions.zero(), 
/* 615 */         DensityFunctions.zero());
/*     */   }
/*     */   
/*     */   public static NoiseRouter none() {
/* 621 */     return simpleRouter(DensityFunctions.zero());
/*     */   }
/*     */   
/*     */   private static DensityFunction splineWithBlending(DensityFunction spline, DensityFunction blendingTarget) {
/* 625 */     DensityFunction blendedSpline = DensityFunctions.lerp(DensityFunctions.blendAlpha(), blendingTarget, spline);
/* 626 */     return DensityFunctions.flatCache(DensityFunctions.cache2d(blendedSpline));
/*     */   }
/*     */   
/*     */   private static DensityFunction noiseGradientDensity(DensityFunction factor, DensityFunction depthWithJaggedness) {
/* 630 */     DensityFunction gradientUnscaled = DensityFunctions.mul(depthWithJaggedness, factor);
/* 633 */     return DensityFunctions.mul(DensityFunctions.constant(4.0D), gradientUnscaled.quarterNegative());
/*     */   }
/*     */   
/*     */   private static DensityFunction preliminarySurfaceLevel(DensityFunction offset, DensityFunction factor, boolean amplified) {
/* 637 */     DensityFunction cachedFactor = DensityFunctions.cache2d(factor);
/* 638 */     DensityFunction cachedOffset = DensityFunctions.cache2d(offset);
/* 639 */     DensityFunction upperBound = remap(
/* 640 */         DensityFunctions.add(
/* 641 */           DensityFunctions.mul(
/* 642 */             DensityFunctions.constant(0.2734375D), cachedFactor
/* 643 */             .invert()), 
/*     */           
/* 645 */           DensityFunctions.mul(DensityFunctions.constant(-1.0D), cachedOffset)), 1.5D, -1.5D, -64.0D, 320.0D);
/* 650 */     upperBound = upperBound.clamp(-40.0D, 320.0D);
/* 651 */     DensityFunction density = DensityFunctions.add(
/* 652 */         slideOverworld(amplified, 
/*     */           
/* 654 */           DensityFunctions.add(
/* 655 */             noiseGradientDensity(cachedFactor, offsetToDepth(cachedOffset)), 
/* 656 */             DensityFunctions.constant(-0.703125D))
/* 657 */           .clamp(-64.0D, 64.0D)), 
/* 658 */         DensityFunctions.constant(-0.390625D));
/* 660 */     return DensityFunctions.findTopSurface(density, upperBound, -64, NoiseSettings.OVERWORLD_NOISE_SETTINGS.getCellHeight());
/*     */   }
/*     */   
/*     */   private static DensityFunction yLimitedInterpolatable(DensityFunction y, DensityFunction whenInRange, int minYInclusive, int maxYInclusive, int whenOutOfRange) {
/* 664 */     return DensityFunctions.interpolated(DensityFunctions.rangeChoice(y, minYInclusive, (maxYInclusive + 1), whenInRange, DensityFunctions.constant(whenOutOfRange)));
/*     */   }
/*     */   
/*     */   private static DensityFunction slide(DensityFunction caves, int minY, int height, int topStartY, int topEndY, double topTarget, int bottomStartY, int bottomEndY, double bottomTarget) {
/* 668 */     DensityFunction noiseValue = caves;
/* 670 */     DensityFunction topFactor = DensityFunctions.yClampedGradient(minY + height - topStartY, minY + height - topEndY, 1.0D, 0.0D);
/* 676 */     noiseValue = DensityFunctions.lerp(topFactor, topTarget, noiseValue);
/* 678 */     DensityFunction bottomFactor = DensityFunctions.yClampedGradient(minY + bottomStartY, minY + bottomEndY, 0.0D, 1.0D);
/* 684 */     noiseValue = DensityFunctions.lerp(bottomFactor, bottomTarget, noiseValue);
/* 686 */     return noiseValue;
/*     */   }
/*     */   
/*     */   protected static final class QuantizedSpaghettiRarity {
/*     */     public static DensityFunction wrapRarity2d(DensityFunction input, Holder<NormalNoise.NoiseParameters> noise) {
/* 698 */       return DensityFunctions.intervalSelect(input, 
/* 699 */           DoubleList.of(new double[] { -0.75D, -0.5D, 0.5D, 0.75D }, ), List.of(
/* 701 */             noiseFunctionForRarity(noise, 0.5D), 
/* 702 */             noiseFunctionForRarity(noise, 0.75D), 
/* 703 */             noiseFunctionForRarity(noise, 1.0D), 
/* 704 */             noiseFunctionForRarity(noise, 2.0D), 
/* 705 */             noiseFunctionForRarity(noise, 3.0D)))
/*     */         
/* 707 */         .abs();
/*     */     }
/*     */     
/*     */     public static DensityFunction wrapRarity3d(DensityFunction input, Holder<NormalNoise.NoiseParameters> noise) {
/* 711 */       return DensityFunctions.intervalSelect(input, 
/* 712 */           DoubleList.of(-0.5D, 0.0D, 0.5D), 
/* 713 */           List.of(
/* 714 */             noiseFunctionForRarity(noise, 0.75D), 
/* 715 */             noiseFunctionForRarity(noise, 1.0D), 
/* 716 */             noiseFunctionForRarity(noise, 1.5D), 
/* 717 */             noiseFunctionForRarity(noise, 2.0D)))
/*     */         
/* 719 */         .abs();
/*     */     }
/*     */     
/*     */     private static DensityFunction noiseFunctionForRarity(Holder<NormalNoise.NoiseParameters> noise, double rarity) {
/* 723 */       return DensityFunctions.mul(DensityFunctions.constant(rarity), DensityFunctions.noise(noise, 1.0D / rarity, 1.0D / rarity));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseRouterData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */