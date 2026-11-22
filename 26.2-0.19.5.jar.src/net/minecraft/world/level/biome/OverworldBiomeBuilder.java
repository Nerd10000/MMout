/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.RegistrySetBuilder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.NoiseData;
/*     */ import net.minecraft.data.worldgen.TerrainProvider;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.BoundedFloatFunction;
/*     */ import net.minecraft.util.CubicSpline;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.DensityFunctions;
/*     */ import net.minecraft.world.level.levelgen.NoiseRouterData;
/*     */ 
/*     */ public final class OverworldBiomeBuilder {
/*     */   private static final float VALLEY_SIZE = 0.05F;
/*     */   
/*     */   private static final float LOW_START = 0.26666668F;
/*     */   
/*     */   public static final float HIGH_START = 0.4F;
/*     */   
/*     */   private static final float HIGH_END = 0.93333334F;
/*     */   
/*     */   private static final float PEAK_SIZE = 0.1F;
/*     */   
/*     */   public static final float PEAK_START = 0.56666666F;
/*     */   
/*     */   private static final float PEAK_END = 0.7666667F;
/*     */   
/*     */   public static final float NEAR_INLAND_START = -0.11F;
/*     */   
/*     */   public static final float MID_INLAND_START = 0.03F;
/*     */   
/*     */   public static final float FAR_INLAND_START = 0.3F;
/*     */   
/*     */   public static final float EROSION_INDEX_1_START = -0.78F;
/*     */   
/*     */   public static final float EROSION_INDEX_2_START = -0.375F;
/*     */   
/*     */   private static final float EROSION_DEEP_DARK_DRYNESS_THRESHOLD = -0.225F;
/*     */   
/*     */   private static final float DEPTH_DEEP_DARK_DRYNESS_THRESHOLD = 0.9F;
/*     */   
/*  59 */   private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
/*     */   
/*  61 */   private final Climate.Parameter[] temperatures = new Climate.Parameter[] { Climate.Parameter.span(-1.0F, -0.45F), 
/*  63 */       Climate.Parameter.span(-0.45F, -0.15F), 
/*  64 */       Climate.Parameter.span(-0.15F, 0.2F), 
/*  65 */       Climate.Parameter.span(0.2F, 0.55F), 
/*  66 */       Climate.Parameter.span(0.55F, 1.0F) };
/*     */   
/*  68 */   private final Climate.Parameter[] humidities = new Climate.Parameter[] { Climate.Parameter.span(-1.0F, -0.35F), 
/*  70 */       Climate.Parameter.span(-0.35F, -0.1F), 
/*  71 */       Climate.Parameter.span(-0.1F, 0.1F), 
/*  72 */       Climate.Parameter.span(0.1F, 0.3F), 
/*  73 */       Climate.Parameter.span(0.3F, 1.0F) };
/*     */   
/*  76 */   private final Climate.Parameter[] erosions = new Climate.Parameter[] { Climate.Parameter.span(-1.0F, -0.78F), 
/*  78 */       Climate.Parameter.span(-0.78F, -0.375F), 
/*  79 */       Climate.Parameter.span(-0.375F, -0.2225F), 
/*  80 */       Climate.Parameter.span(-0.2225F, 0.05F), 
/*  81 */       Climate.Parameter.span(0.05F, 0.45F), 
/*  82 */       Climate.Parameter.span(0.45F, 0.55F), 
/*  83 */       Climate.Parameter.span(0.55F, 1.0F) };
/*     */   
/*  86 */   private final Climate.Parameter FROZEN_RANGE = this.temperatures[0];
/*     */   
/*  87 */   private final Climate.Parameter UNFROZEN_RANGE = Climate.Parameter.span(this.temperatures[1], this.temperatures[4]);
/*     */   
/*  89 */   private final Climate.Parameter mushroomFieldsContinentalness = Climate.Parameter.span(-1.2F, -1.05F);
/*     */   
/*  90 */   private final Climate.Parameter deepOceanContinentalness = Climate.Parameter.span(-1.05F, -0.455F);
/*     */   
/*  91 */   private final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-0.455F, -0.19F);
/*     */   
/*  92 */   private final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, -0.11F);
/*     */   
/*  93 */   private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.11F, 0.55F);
/*     */   
/*  95 */   private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.11F, 0.03F);
/*     */   
/*  96 */   private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
/*     */   
/*  97 */   private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);
/*     */   
/* 100 */   private final ResourceKey<Biome>[][] OCEANS = new ResourceKey[][] { { Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.WARM_OCEAN }, { Biomes.FROZEN_OCEAN, Biomes.COLD_OCEAN, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN } };
/*     */   
/* 107 */   private final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][] { { Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.TAIGA }, { Biomes.PLAINS, Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA }, { Biomes.FLOWER_FOREST, Biomes.PLAINS, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST }, { Biomes.SAVANNA, Biomes.SAVANNA, Biomes.FOREST, Biomes.JUNGLE, Biomes.JUNGLE }, { Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT } };
/*     */   
/* 118 */   private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][] { { Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null }, { null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA }, { Biomes.SUNFLOWER_PLAINS, null, null, Biomes.OLD_GROWTH_BIRCH_FOREST, null }, { null, null, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE }, { null, null, null, null, null } };
/*     */   
/* 127 */   private final ResourceKey<Biome>[][] PLATEAU_BIOMES = new ResourceKey[][] { { Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA }, { Biomes.MEADOW, Biomes.MEADOW, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA }, { Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.PALE_GARDEN }, { Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA_PLATEAU, Biomes.FOREST, Biomes.FOREST, Biomes.JUNGLE }, { Biomes.BADLANDS, Biomes.BADLANDS, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.WOODED_BADLANDS } };
/*     */   
/* 137 */   private final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT = new ResourceKey[][] { { Biomes.ICE_SPIKES, null, null, null, null }, { Biomes.CHERRY_GROVE, null, Biomes.MEADOW, Biomes.MEADOW, Biomes.OLD_GROWTH_PINE_TAIGA }, { Biomes.CHERRY_GROVE, Biomes.CHERRY_GROVE, Biomes.FOREST, Biomes.BIRCH_FOREST, null }, { null, null, null, null, null }, { Biomes.ERODED_BADLANDS, Biomes.ERODED_BADLANDS, null, null, null } };
/*     */   
/* 149 */   private final ResourceKey<Biome>[][] SHATTERED_BIOMES = new ResourceKey[][] { { Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST }, { Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST }, { Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST }, { null, null, null, null, null }, { null, null, null, null, null } };
/*     */   
/*     */   public List<Climate.ParameterPoint> spawnTarget() {
/* 158 */     Climate.Parameter surfaceDepth = Climate.Parameter.point(0.0F);
/* 159 */     float riverClearance = 0.16F;
/* 160 */     return List.of(new Climate.ParameterPoint(this.FULL_RANGE, this.FULL_RANGE, 
/*     */           
/* 164 */           Climate.Parameter.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, surfaceDepth, 
/*     */           
/* 167 */           Climate.Parameter.span(-1.0F, -0.16F), 0L), new Climate.ParameterPoint(this.FULL_RANGE, this.FULL_RANGE, 
/*     */           
/* 173 */           Climate.Parameter.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, surfaceDepth, 
/*     */           
/* 176 */           Climate.Parameter.span(0.16F, 1.0F), 0L));
/*     */   }
/*     */   
/*     */   void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes) {
/* 184 */     if (SharedConstants.debugGenerateSquareTerrainWithoutNoise) {
/* 185 */       addDebugBiomes(biomes);
/*     */       return;
/*     */     } 
/* 189 */     addOffCoastBiomes(biomes);
/* 190 */     addInlandBiomes(biomes);
/* 191 */     addUndergroundBiomes(biomes);
/*     */   }
/*     */   
/*     */   private void addDebugBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes) {
/* 198 */     HolderLookup.Provider builtIns = (new RegistrySetBuilder()).add(Registries.DENSITY_FUNCTION, NoiseRouterData::bootstrap).add(Registries.NOISE, NoiseData::bootstrap).build((RegistryAccess)RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
/* 200 */     HolderLookup.RegistryLookup registryLookup = builtIns.lookupOrThrow(Registries.DENSITY_FUNCTION);
/* 201 */     DensityFunctions.Spline.Coordinate continents = new DensityFunctions.Spline.Coordinate((DensityFunction)new DensityFunctions.HolderHolder((Holder)registryLookup.getOrThrow(NoiseRouterData.CONTINENTS)));
/* 202 */     DensityFunctions.Spline.Coordinate erosion = new DensityFunctions.Spline.Coordinate((DensityFunction)new DensityFunctions.HolderHolder((Holder)registryLookup.getOrThrow(NoiseRouterData.EROSION)));
/* 203 */     DensityFunctions.Spline.Coordinate ridges = new DensityFunctions.Spline.Coordinate((DensityFunction)new DensityFunctions.HolderHolder((Holder)registryLookup.getOrThrow(NoiseRouterData.RIDGES_FOLDED)));
/* 205 */     biomes.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.point(0.0F), this.FULL_RANGE, 0.01F), Biomes.PLAINS));
/* 207 */     CubicSpline<?> erosionOffsetSpline = TerrainProvider.buildErosionOffsetSpline((BoundedFloatFunction)erosion, (BoundedFloatFunction)ridges, -0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, Float2FloatFunction.identity());
/* 208 */     if (erosionOffsetSpline instanceof CubicSpline.Multipoint) {
/* 208 */       CubicSpline.Multipoint<?> multipoint = (CubicSpline.Multipoint)erosionOffsetSpline;
/* 209 */       ResourceKey<Biome> biome = Biomes.DESERT;
/* 210 */       for (float location : multipoint.locations()) {
/* 211 */         biomes.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.point(location), Climate.Parameter.point(0.0F), this.FULL_RANGE, 0.0F), biome));
/* 212 */         biome = (biome == Biomes.DESERT) ? Biomes.BADLANDS : Biomes.DESERT;
/*     */       } 
/*     */     } 
/* 216 */     CubicSpline<?> overworldOffset = TerrainProvider.overworldOffset((BoundedFloatFunction)continents, (BoundedFloatFunction)erosion, (BoundedFloatFunction)ridges, false);
/* 217 */     if (overworldOffset instanceof CubicSpline.Multipoint) {
/* 217 */       CubicSpline.Multipoint<?> multipoint = (CubicSpline.Multipoint)overworldOffset;
/* 218 */       for (float location : multipoint.locations())
/* 219 */         biomes.accept(Pair.of(Climate.parameters(this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.point(location), this.FULL_RANGE, Climate.Parameter.point(0.0F), this.FULL_RANGE, 0.0F), Biomes.SNOWY_TAIGA)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addOffCoastBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes) {
/* 226 */     addSurfaceBiome(biomes, this.FULL_RANGE, this.FULL_RANGE, this.mushroomFieldsContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.MUSHROOM_FIELDS);
/* 227 */     for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; temperatureIndex++) {
/* 228 */       Climate.Parameter temperature = this.temperatures[temperatureIndex];
/* 230 */       addSurfaceBiome(biomes, temperature, this.FULL_RANGE, this.deepOceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS[0][temperatureIndex]);
/* 231 */       addSurfaceBiome(biomes, temperature, this.FULL_RANGE, this.oceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS[1][temperatureIndex]);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes) {
/* 251 */     addMidSlice(biomes, Climate.Parameter.span(-1.0F, -0.93333334F));
/* 254 */     addHighSlice(biomes, Climate.Parameter.span(-0.93333334F, -0.7666667F));
/* 255 */     addPeaks(biomes, Climate.Parameter.span(-0.7666667F, -0.56666666F));
/* 256 */     addHighSlice(biomes, Climate.Parameter.span(-0.56666666F, -0.4F));
/* 259 */     addMidSlice(biomes, Climate.Parameter.span(-0.4F, -0.26666668F));
/* 262 */     addLowSlice(biomes, Climate.Parameter.span(-0.26666668F, -0.05F));
/* 263 */     addValleys(biomes, Climate.Parameter.span(-0.05F, 0.05F));
/* 264 */     addLowSlice(biomes, Climate.Parameter.span(0.05F, 0.26666668F));
/* 267 */     addMidSlice(biomes, Climate.Parameter.span(0.26666668F, 0.4F));
/* 270 */     addHighSlice(biomes, Climate.Parameter.span(0.4F, 0.56666666F));
/* 271 */     addPeaks(biomes, Climate.Parameter.span(0.56666666F, 0.7666667F));
/* 272 */     addHighSlice(biomes, Climate.Parameter.span(0.7666667F, 0.93333334F));
/* 275 */     addMidSlice(biomes, Climate.Parameter.span(0.93333334F, 1.0F));
/*     */   }
/*     */   
/*     */   private void addPeaks(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter weirdness) {
/* 281 */     for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; temperatureIndex++) {
/* 282 */       Climate.Parameter temperature = this.temperatures[temperatureIndex];
/* 283 */       for (int humidityIndex = 0; humidityIndex < this.humidities.length; humidityIndex++) {
/* 284 */         Climate.Parameter humidity = this.humidities[humidityIndex];
/* 286 */         ResourceKey<Biome> middleBiome = pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
/* 287 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
/* 288 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
/* 289 */         ResourceKey<Biome> plateauBiome = pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
/* 290 */         ResourceKey<Biome> shatteredBiome = pickShatteredBiome(temperatureIndex, humidityIndex, weirdness);
/* 291 */         ResourceKey<Biome> shatteredBiomeOrWindsweptSavanna = maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, shatteredBiome);
/* 292 */         ResourceKey<Biome> peakBiome = pickPeakBiome(temperatureIndex, humidityIndex, weirdness);
/* 294 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, peakBiome);
/* 296 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[1], weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
/* 298 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdness, 0.0F, peakBiome);
/* 300 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
/* 301 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], weirdness, 0.0F, plateauBiome);
/* 303 */         addSurfaceBiome(biomes, temperature, humidity, this.midInlandContinentalness, this.erosions[3], weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/* 304 */         addSurfaceBiome(biomes, temperature, humidity, this.farInlandContinentalness, this.erosions[3], weirdness, 0.0F, plateauBiome);
/* 306 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
/* 308 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[5], weirdness, 0.0F, shatteredBiomeOrWindsweptSavanna);
/* 309 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, shatteredBiome);
/* 311 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addHighSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter weirdness) {
/* 320 */     for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; temperatureIndex++) {
/* 321 */       Climate.Parameter temperature = this.temperatures[temperatureIndex];
/* 322 */       for (int humidityIndex = 0; humidityIndex < this.humidities.length; humidityIndex++) {
/* 323 */         Climate.Parameter humidity = this.humidities[humidityIndex];
/* 325 */         ResourceKey<Biome> middleBiome = pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
/* 326 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
/* 327 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
/* 328 */         ResourceKey<Biome> plateauBiome = pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
/* 329 */         ResourceKey<Biome> shatteredBiome = pickShatteredBiome(temperatureIndex, humidityIndex, weirdness);
/* 330 */         ResourceKey<Biome> middleBiomeOrWindsweptSavanna = maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, middleBiome);
/* 331 */         ResourceKey<Biome> slopeBiome = pickSlopeBiome(temperatureIndex, humidityIndex, weirdness);
/* 332 */         ResourceKey<Biome> peakBiome = pickPeakBiome(temperatureIndex, humidityIndex, weirdness);
/* 334 */         addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiome);
/* 335 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, this.erosions[0], weirdness, 0.0F, slopeBiome);
/* 336 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, peakBiome);
/* 338 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, this.erosions[1], weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
/* 339 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdness, 0.0F, slopeBiome);
/* 341 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
/* 342 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], weirdness, 0.0F, plateauBiome);
/* 344 */         addSurfaceBiome(biomes, temperature, humidity, this.midInlandContinentalness, this.erosions[3], weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/* 345 */         addSurfaceBiome(biomes, temperature, humidity, this.farInlandContinentalness, this.erosions[3], weirdness, 0.0F, plateauBiome);
/* 347 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
/* 349 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[5], weirdness, 0.0F, middleBiomeOrWindsweptSavanna);
/* 350 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, shatteredBiome);
/* 352 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addMidSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter weirdness) {
/* 360 */     addSurfaceBiome(biomes, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), weirdness, 0.0F, Biomes.STONY_SHORE);
/* 361 */     addSurfaceBiome(biomes, Climate.Parameter.span(this.temperatures[1], this.temperatures[2]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.SWAMP);
/* 362 */     addSurfaceBiome(biomes, Climate.Parameter.span(this.temperatures[3], this.temperatures[4]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.MANGROVE_SWAMP);
/* 364 */     for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; temperatureIndex++) {
/* 365 */       Climate.Parameter temperature = this.temperatures[temperatureIndex];
/* 366 */       for (int humidityIndex = 0; humidityIndex < this.humidities.length; humidityIndex++) {
/* 367 */         Climate.Parameter humidity = this.humidities[humidityIndex];
/* 369 */         ResourceKey<Biome> middleBiome = pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
/* 370 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
/* 371 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
/* 372 */         ResourceKey<Biome> shatteredBiome = pickShatteredBiome(temperatureIndex, humidityIndex, weirdness);
/* 373 */         ResourceKey<Biome> plateauBiome = pickPlateauBiome(temperatureIndex, humidityIndex, weirdness);
/* 374 */         ResourceKey<Biome> beachBiome = pickBeachBiome(temperatureIndex, humidityIndex);
/* 375 */         ResourceKey<Biome> middleBiomeOrWindsweptSavanna = maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, middleBiome);
/* 376 */         ResourceKey<Biome> shatteredCoastBiome = pickShatteredCoastBiome(temperatureIndex, humidityIndex, weirdness);
/* 377 */         ResourceKey<Biome> slopeBiome = pickSlopeBiome(temperatureIndex, humidityIndex, weirdness);
/* 379 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, slopeBiome);
/* 381 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.midInlandContinentalness), this.erosions[1], weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
/* 382 */         addSurfaceBiome(biomes, temperature, humidity, this.farInlandContinentalness, this.erosions[1], weirdness, 0.0F, (temperatureIndex == 0) ? slopeBiome : plateauBiome);
/* 384 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, this.erosions[2], weirdness, 0.0F, middleBiome);
/* 385 */         addSurfaceBiome(biomes, temperature, humidity, this.midInlandContinentalness, this.erosions[2], weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/* 386 */         addSurfaceBiome(biomes, temperature, humidity, this.farInlandContinentalness, this.erosions[2], weirdness, 0.0F, plateauBiome);
/* 388 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[3], weirdness, 0.0F, middleBiome);
/* 389 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[3], weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/* 391 */         if (weirdness.max() < 0L) {
/* 392 */           addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, this.erosions[4], weirdness, 0.0F, beachBiome);
/* 393 */           addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
/*     */         } else {
/* 395 */           addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
/*     */         } 
/* 398 */         addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, this.erosions[5], weirdness, 0.0F, shatteredCoastBiome);
/* 399 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, this.erosions[5], weirdness, 0.0F, middleBiomeOrWindsweptSavanna);
/* 400 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, shatteredBiome);
/* 402 */         if (weirdness.max() < 0L) {
/* 403 */           addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, beachBiome);
/*     */         } else {
/* 405 */           addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, middleBiome);
/*     */         } 
/* 409 */         if (temperatureIndex == 0)
/* 410 */           addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addLowSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter weirdness) {
/* 419 */     addSurfaceBiome(biomes, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), weirdness, 0.0F, Biomes.STONY_SHORE);
/* 420 */     addSurfaceBiome(biomes, Climate.Parameter.span(this.temperatures[1], this.temperatures[2]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.SWAMP);
/* 421 */     addSurfaceBiome(biomes, Climate.Parameter.span(this.temperatures[3], this.temperatures[4]), this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.MANGROVE_SWAMP);
/* 423 */     for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; temperatureIndex++) {
/* 424 */       Climate.Parameter temperature = this.temperatures[temperatureIndex];
/* 425 */       for (int humidityIndex = 0; humidityIndex < this.humidities.length; humidityIndex++) {
/* 426 */         Climate.Parameter humidity = this.humidities[humidityIndex];
/* 428 */         ResourceKey<Biome> middleBiome = pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
/* 429 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
/* 430 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHotOrSlopeIfCold = pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdness);
/* 431 */         ResourceKey<Biome> beachBiome = pickBeachBiome(temperatureIndex, humidityIndex);
/* 432 */         ResourceKey<Biome> middleBiomeOrWindsweptSavanna = maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, middleBiome);
/* 433 */         ResourceKey<Biome> shatteredCoastBiome = pickShatteredCoastBiome(temperatureIndex, humidityIndex, weirdness);
/* 435 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/* 436 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiomeOrBadlandsIfHotOrSlopeIfCold);
/* 438 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
/* 439 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/* 441 */         addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, Climate.Parameter.span(this.erosions[3], this.erosions[4]), weirdness, 0.0F, beachBiome);
/* 443 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
/* 445 */         addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, this.erosions[5], weirdness, 0.0F, shatteredCoastBiome);
/* 446 */         addSurfaceBiome(biomes, temperature, humidity, this.nearInlandContinentalness, this.erosions[5], weirdness, 0.0F, middleBiomeOrWindsweptSavanna);
/* 447 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, middleBiome);
/* 449 */         addSurfaceBiome(biomes, temperature, humidity, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, beachBiome);
/* 452 */         if (temperatureIndex == 0)
/* 453 */           addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter weirdness) {
/* 462 */     addSurfaceBiome(biomes, this.FROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, (weirdness.max() < 0L) ? Biomes.STONY_SHORE : Biomes.FROZEN_RIVER);
/* 463 */     addSurfaceBiome(biomes, this.UNFROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, (weirdness.max() < 0L) ? Biomes.STONY_SHORE : Biomes.RIVER);
/* 465 */     addSurfaceBiome(biomes, this.FROZEN_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, Biomes.FROZEN_RIVER);
/* 466 */     addSurfaceBiome(biomes, this.UNFROZEN_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, Biomes.RIVER);
/* 468 */     addSurfaceBiome(biomes, this.FROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[5]), weirdness, 0.0F, Biomes.FROZEN_RIVER);
/* 469 */     addSurfaceBiome(biomes, this.UNFROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[5]), weirdness, 0.0F, Biomes.RIVER);
/* 471 */     addSurfaceBiome(biomes, this.FROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, Biomes.FROZEN_RIVER);
/* 472 */     addSurfaceBiome(biomes, this.UNFROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, Biomes.RIVER);
/* 474 */     addSurfaceBiome(biomes, Climate.Parameter.span(this.temperatures[1], this.temperatures[2]), this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.SWAMP);
/* 475 */     addSurfaceBiome(biomes, Climate.Parameter.span(this.temperatures[3], this.temperatures[4]), this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.MANGROVE_SWAMP);
/* 476 */     addSurfaceBiome(biomes, this.FROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, Biomes.FROZEN_RIVER);
/* 478 */     for (int temperatureIndex = 0; temperatureIndex < this.temperatures.length; temperatureIndex++) {
/* 479 */       Climate.Parameter temperature = this.temperatures[temperatureIndex];
/* 480 */       for (int humidityIndex = 0; humidityIndex < this.humidities.length; humidityIndex++) {
/* 481 */         Climate.Parameter humidity = this.humidities[humidityIndex];
/* 482 */         ResourceKey<Biome> middleBiomeOrBadlandsIfHot = pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
/* 484 */         addSurfaceBiome(biomes, temperature, humidity, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiomeOrBadlandsIfHot);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes) {
/* 491 */     addUndergroundBiome(biomes, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(0.8F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.DRIPSTONE_CAVES);
/* 493 */     addUndergroundBiome(biomes, this.FULL_RANGE, Climate.Parameter.span(0.7F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.LUSH_CAVES);
/* 495 */     addUndergroundBiome(biomes, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.inlandContinentalness), Climate.Parameter.span(this.erosions[5], this.erosions[6]), Climate.Parameter.span(-1.1F, -0.85F), 0.0F, Biomes.SULFUR_CAVES);
/* 497 */     addBottomBiome(biomes, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.erosions[0], this.erosions[1]), this.FULL_RANGE, 0.0F, Biomes.DEEP_DARK);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickMiddleBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 501 */     if (weirdness.max() < 0L)
/* 502 */       return this.MIDDLE_BIOMES[temperatureIndex][humidityIndex]; 
/* 504 */     ResourceKey<Biome> variant = this.MIDDLE_BIOMES_VARIANT[temperatureIndex][humidityIndex];
/* 505 */     return (variant == null) ? this.MIDDLE_BIOMES[temperatureIndex][humidityIndex] : variant;
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickMiddleBiomeOrBadlandsIfHot(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 510 */     return (temperatureIndex == 4) ? pickBadlandsBiome(humidityIndex, weirdness) : pickMiddleBiome(temperatureIndex, humidityIndex, weirdness);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 514 */     return (temperatureIndex == 0) ? pickSlopeBiome(temperatureIndex, humidityIndex, weirdness) : pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdness);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> maybePickWindsweptSavannaBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness, ResourceKey<Biome> underlyingBiome) {
/* 518 */     if (temperatureIndex > 1 && humidityIndex < 4 && weirdness.max() >= 0L)
/* 519 */       return Biomes.WINDSWEPT_SAVANNA; 
/* 521 */     return underlyingBiome;
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickShatteredCoastBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 525 */     ResourceKey<Biome> beachOrMiddleBiome = (weirdness.max() >= 0L) ? pickMiddleBiome(temperatureIndex, humidityIndex, weirdness) : pickBeachBiome(temperatureIndex, humidityIndex);
/* 526 */     return maybePickWindsweptSavannaBiome(temperatureIndex, humidityIndex, weirdness, beachOrMiddleBiome);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickBeachBiome(int temperatureIndex, int humidityIndex) {
/* 530 */     if (temperatureIndex == 0)
/* 531 */       return Biomes.SNOWY_BEACH; 
/* 533 */     if (temperatureIndex == 4)
/* 535 */       return Biomes.DESERT; 
/* 537 */     return Biomes.BEACH;
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickBadlandsBiome(int humidityIndex, Climate.Parameter weirdness) {
/* 541 */     if (humidityIndex < 2)
/* 542 */       return (weirdness.max() < 0L) ? Biomes.BADLANDS : Biomes.ERODED_BADLANDS; 
/* 543 */     if (humidityIndex < 3)
/* 544 */       return Biomes.BADLANDS; 
/* 546 */     return Biomes.WOODED_BADLANDS;
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickPlateauBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 551 */     if (weirdness.max() >= 0L) {
/* 552 */       ResourceKey<Biome> variant = this.PLATEAU_BIOMES_VARIANT[temperatureIndex][humidityIndex];
/* 553 */       if (variant != null)
/* 554 */         return variant; 
/*     */     } 
/* 557 */     return this.PLATEAU_BIOMES[temperatureIndex][humidityIndex];
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickPeakBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 561 */     if (temperatureIndex <= 2)
/* 563 */       return (weirdness.max() < 0L) ? Biomes.JAGGED_PEAKS : Biomes.FROZEN_PEAKS; 
/* 565 */     if (temperatureIndex == 3)
/* 566 */       return Biomes.STONY_PEAKS; 
/* 568 */     return pickBadlandsBiome(humidityIndex, weirdness);
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickSlopeBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 572 */     if (temperatureIndex >= 3)
/* 573 */       return pickPlateauBiome(temperatureIndex, humidityIndex, weirdness); 
/* 575 */     if (humidityIndex <= 1)
/* 576 */       return Biomes.SNOWY_SLOPES; 
/* 578 */     return Biomes.GROVE;
/*     */   }
/*     */   
/*     */   private ResourceKey<Biome> pickShatteredBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdness) {
/* 582 */     ResourceKey<Biome> biome = this.SHATTERED_BIOMES[temperatureIndex][humidityIndex];
/* 583 */     return (biome == null) ? pickMiddleBiome(temperatureIndex, humidityIndex, weirdness) : biome;
/*     */   }
/*     */   
/*     */   private void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> second) {
/* 588 */     biomes.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(0.0F), weirdness, offset), second));
/* 590 */     biomes.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1.0F), weirdness, offset), second));
/*     */   }
/*     */   
/*     */   private void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
/* 594 */     biomes.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.span(0.2F, 0.9F), weirdness, offset), biome));
/*     */   }
/*     */   
/*     */   private void addBottomBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
/* 598 */     biomes.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, Climate.Parameter.point(1.1F), weirdness, offset), biome));
/*     */   }
/*     */   
/*     */   public static boolean isDeepDarkRegion(DensityFunction erosion, DensityFunction depth, DensityFunction.FunctionContext context) {
/* 602 */     return (erosion.compute(context) < -0.22499999403953552D && depth.compute(context) > 0.8999999761581421D);
/*     */   }
/*     */   
/*     */   public static String getDebugStringForPeaksAndValleys(double peaksAndValleys) {
/* 606 */     if (peaksAndValleys < NoiseRouterData.peaksAndValleys(0.05F))
/* 607 */       return "Valley"; 
/* 608 */     if (peaksAndValleys < NoiseRouterData.peaksAndValleys(0.26666668F))
/* 609 */       return "Low"; 
/* 610 */     if (peaksAndValleys < NoiseRouterData.peaksAndValleys(0.4F))
/* 611 */       return "Mid"; 
/* 612 */     if (peaksAndValleys < NoiseRouterData.peaksAndValleys(0.56666666F))
/* 613 */       return "High"; 
/* 615 */     return "Peak";
/*     */   }
/*     */   
/*     */   public String getDebugStringForContinentalness(double continentalness) {
/* 620 */     double continentalnessQuantized = Climate.quantizeCoord((float)continentalness);
/* 621 */     if (continentalnessQuantized < this.mushroomFieldsContinentalness.max())
/* 622 */       return "Mushroom fields"; 
/* 623 */     if (continentalnessQuantized < this.deepOceanContinentalness.max())
/* 624 */       return "Deep ocean"; 
/* 625 */     if (continentalnessQuantized < this.oceanContinentalness.max())
/* 626 */       return "Ocean"; 
/* 627 */     if (continentalnessQuantized < this.coastContinentalness.max())
/* 628 */       return "Coast"; 
/* 629 */     if (continentalnessQuantized < this.nearInlandContinentalness.max())
/* 630 */       return "Near inland"; 
/* 631 */     if (continentalnessQuantized < this.midInlandContinentalness.max())
/* 632 */       return "Mid inland"; 
/* 634 */     return "Far inland";
/*     */   }
/*     */   
/*     */   public String getDebugStringForErosion(double erosion) {
/* 639 */     return getDebugStringForNoiseValue(erosion, this.erosions);
/*     */   }
/*     */   
/*     */   public String getDebugStringForTemperature(double temperature) {
/* 643 */     return getDebugStringForNoiseValue(temperature, this.temperatures);
/*     */   }
/*     */   
/*     */   public String getDebugStringForHumidity(double humidity) {
/* 647 */     return getDebugStringForNoiseValue(humidity, this.humidities);
/*     */   }
/*     */   
/*     */   private static String getDebugStringForNoiseValue(double noiseValue, Climate.Parameter[] array) {
/* 651 */     double noiseValueQuantized = Climate.quantizeCoord((float)noiseValue);
/* 652 */     for (int i = 0; i < array.length; i++) {
/* 653 */       if (noiseValueQuantized < array[i].max())
/* 654 */         return "" + i; 
/*     */     } 
/* 657 */     return "?";
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getTemperatureThresholds() {
/* 662 */     return this.temperatures;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getHumidityThresholds() {
/* 667 */     return this.humidities;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getErosionThresholds() {
/* 672 */     return this.erosions;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getContinentalnessThresholds() {
/* 677 */     return new Climate.Parameter[] { this.mushroomFieldsContinentalness, this.deepOceanContinentalness, this.oceanContinentalness, this.coastContinentalness, this.nearInlandContinentalness, this.midInlandContinentalness, this.farInlandContinentalness };
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getPeaksAndValleysThresholds() {
/* 690 */     return new Climate.Parameter[] { Climate.Parameter.span(-2.0F, NoiseRouterData.peaksAndValleys(0.05F)), 
/* 692 */         Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.05F), NoiseRouterData.peaksAndValleys(0.26666668F)), 
/* 693 */         Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.26666668F), NoiseRouterData.peaksAndValleys(0.4F)), 
/* 694 */         Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.4F), NoiseRouterData.peaksAndValleys(0.56666666F)), 
/* 695 */         Climate.Parameter.span(NoiseRouterData.peaksAndValleys(0.56666666F), 2.0F) };
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Climate.Parameter[] getWeirdnessThresholds() {
/* 701 */     return new Climate.Parameter[] { Climate.Parameter.span(-2.0F, 0.0F), 
/* 703 */         Climate.Parameter.span(0.0F, 2.0F) };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\OverworldBiomeBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */