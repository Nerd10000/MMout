/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.levelgen.Noises;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ public class NoiseData {
/*     */   @Deprecated
/*  10 */   public static final NormalNoise.NoiseParameters DEFAULT_SHIFT = new NormalNoise.NoiseParameters(-3, 1.0D, new double[] { 1.0D, 1.0D, 0.0D });
/*     */   
/*     */   public static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> context) {
/*  13 */     registerBiomeNoises(context, 0, Noises.TEMPERATURE, Noises.VEGETATION, Noises.CONTINENTALNESS, Noises.EROSION);
/*  14 */     registerBiomeNoises(context, -2, Noises.TEMPERATURE_LARGE, Noises.VEGETATION_LARGE, Noises.CONTINENTALNESS_LARGE, Noises.EROSION_LARGE);
/*  16 */     register(context, Noises.TEMPERATURE_NETHER, -7, 1.0D, new double[] { 1.0D });
/*  17 */     register(context, Noises.VEGETATION_NETHER, -7, 1.0D, new double[] { 1.0D });
/*  19 */     register(context, Noises.RIDGE, -7, 1.0D, new double[] { 2.0D, 1.0D, 0.0D, 0.0D, 0.0D });
/*  20 */     context.register(Noises.SHIFT, DEFAULT_SHIFT);
/*  22 */     register(context, Noises.AQUIFER_BARRIER, -3, 1.0D, new double[0]);
/*  23 */     register(context, Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS, -7, 1.0D, new double[0]);
/*  24 */     register(context, Noises.AQUIFER_LAVA, -1, 1.0D, new double[0]);
/*  25 */     register(context, Noises.AQUIFER_FLUID_LEVEL_SPREAD, -5, 1.0D, new double[0]);
/*  27 */     register(context, Noises.PILLAR, -7, 1.0D, new double[] { 1.0D });
/*  28 */     register(context, Noises.PILLAR_RARENESS, -8, 1.0D, new double[0]);
/*  29 */     register(context, Noises.PILLAR_THICKNESS, -8, 1.0D, new double[0]);
/*  31 */     register(context, Noises.SPAGHETTI_2D, -7, 1.0D, new double[0]);
/*  32 */     register(context, Noises.SPAGHETTI_2D_ELEVATION, -8, 1.0D, new double[0]);
/*  33 */     register(context, Noises.SPAGHETTI_2D_MODULATOR, -11, 1.0D, new double[0]);
/*  34 */     register(context, Noises.SPAGHETTI_2D_THICKNESS, -11, 1.0D, new double[0]);
/*  36 */     register(context, Noises.SPAGHETTI_3D_1, -7, 1.0D, new double[0]);
/*  37 */     register(context, Noises.SPAGHETTI_3D_2, -7, 1.0D, new double[0]);
/*  38 */     register(context, Noises.SPAGHETTI_3D_RARITY, -11, 1.0D, new double[0]);
/*  39 */     register(context, Noises.SPAGHETTI_3D_THICKNESS, -8, 1.0D, new double[0]);
/*  41 */     register(context, Noises.SPAGHETTI_ROUGHNESS, -5, 1.0D, new double[0]);
/*  42 */     register(context, Noises.SPAGHETTI_ROUGHNESS_MODULATOR, -8, 1.0D, new double[0]);
/*  44 */     register(context, Noises.CAVE_ENTRANCE, -7, 0.4D, new double[] { 0.5D, 1.0D });
/*  45 */     register(context, Noises.CAVE_LAYER, -8, 1.0D, new double[0]);
/*  47 */     register(context, Noises.CAVE_CHEESE, -8, 0.5D, new double[] { 1.0D, 2.0D, 1.0D, 2.0D, 1.0D, 0.0D, 2.0D, 0.0D });
/*  49 */     register(context, Noises.ORE_VEININESS, -8, 1.0D, new double[0]);
/*  50 */     register(context, Noises.ORE_VEIN_A, -7, 1.0D, new double[0]);
/*  51 */     register(context, Noises.ORE_VEIN_B, -7, 1.0D, new double[0]);
/*  52 */     register(context, Noises.ORE_GAP, -5, 1.0D, new double[0]);
/*  54 */     register(context, Noises.NOODLE, -8, 1.0D, new double[0]);
/*  55 */     register(context, Noises.NOODLE_THICKNESS, -8, 1.0D, new double[0]);
/*  56 */     register(context, Noises.NOODLE_RIDGE_A, -7, 1.0D, new double[0]);
/*  57 */     register(context, Noises.NOODLE_RIDGE_B, -7, 1.0D, new double[0]);
/*  59 */     register(context, Noises.JAGGED, -16, 1.0D, new double[] { 
/*  59 */           1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 
/*  59 */           1.0D, 1.0D, 1.0D, 1.0D, 1.0D });
/*  66 */     register(context, Noises.SURFACE, -6, 1.0D, new double[] { 1.0D, 1.0D });
/*  67 */     register(context, Noises.SURFACE_SECONDARY, -6, 1.0D, new double[] { 1.0D, 0.0D, 1.0D });
/*  69 */     register(context, Noises.CLAY_BANDS_OFFSET, -8, 1.0D, new double[0]);
/*  70 */     register(context, Noises.BADLANDS_PILLAR, -2, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  71 */     register(context, Noises.BADLANDS_PILLAR_ROOF, -8, 1.0D, new double[0]);
/*  72 */     register(context, Noises.BADLANDS_SURFACE, -6, 1.0D, new double[] { 1.0D, 1.0D });
/*  74 */     register(context, Noises.ICEBERG_PILLAR, -6, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  75 */     register(context, Noises.ICEBERG_PILLAR_ROOF, -3, 1.0D, new double[0]);
/*  76 */     register(context, Noises.ICEBERG_SURFACE, -6, 1.0D, new double[] { 1.0D, 1.0D });
/*  78 */     register(context, Noises.SULFUR_CAVE_GRADIENT, -5, 1.0D, new double[] { 0.0D, 1.0D });
/*  80 */     register(context, Noises.SWAMP, -2, 1.0D, new double[0]);
/*  82 */     register(context, Noises.CALCITE, -9, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  83 */     register(context, Noises.GRAVEL, -8, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  84 */     register(context, Noises.POWDER_SNOW, -6, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  85 */     register(context, Noises.PACKED_ICE, -7, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  86 */     register(context, Noises.ICE, -4, 1.0D, new double[] { 1.0D, 1.0D, 1.0D });
/*  88 */     register(context, Noises.SOUL_SAND_LAYER, -8, 1.0D, new double[] { 1.0D, 1.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.013333333333333334D });
/*  89 */     register(context, Noises.GRAVEL_LAYER, -8, 1.0D, new double[] { 1.0D, 1.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.013333333333333334D });
/*  90 */     register(context, Noises.PATCH, -5, 1.0D, new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 0.013333333333333334D });
/*  91 */     register(context, Noises.NETHERRACK, -3, 1.0D, new double[] { 0.0D, 0.0D, 0.35D });
/*  92 */     register(context, Noises.NETHER_WART, -3, 1.0D, new double[] { 0.0D, 0.0D, 0.9D });
/*  93 */     register(context, Noises.NETHER_STATE_SELECTOR, -4, 1.0D, new double[0]);
/*     */   }
/*     */   
/*     */   private static void registerBiomeNoises(BootstrapContext<NormalNoise.NoiseParameters> context, int octaveOffset, ResourceKey<NormalNoise.NoiseParameters> temperature, ResourceKey<NormalNoise.NoiseParameters> vegetation, ResourceKey<NormalNoise.NoiseParameters> continentalness, ResourceKey<NormalNoise.NoiseParameters> erosion) {
/*  97 */     register(context, temperature, -10 + octaveOffset, 1.5D, new double[] { 0.0D, 1.0D, 0.0D, 0.0D, 0.0D });
/*  98 */     register(context, vegetation, -8 + octaveOffset, 1.0D, new double[] { 1.0D, 0.0D, 0.0D, 0.0D, 0.0D });
/*  99 */     register(context, continentalness, -9 + octaveOffset, 1.0D, new double[] { 1.0D, 2.0D, 2.0D, 2.0D, 1.0D, 1.0D, 1.0D, 1.0D });
/* 100 */     register(context, erosion, -9 + octaveOffset, 1.0D, new double[] { 1.0D, 0.0D, 1.0D, 1.0D });
/*     */   }
/*     */   
/*     */   private static void register(BootstrapContext<NormalNoise.NoiseParameters> context, ResourceKey<NormalNoise.NoiseParameters> key, int firstOctave, double firstAmplitude, double... amplitudes) {
/* 104 */     context.register(key, new NormalNoise.NoiseParameters(firstOctave, firstAmplitude, amplitudes));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\NoiseData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */