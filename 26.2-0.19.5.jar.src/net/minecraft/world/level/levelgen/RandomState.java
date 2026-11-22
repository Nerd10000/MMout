/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.levelgen.synth.BlendedNoise;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ public final class RandomState {
/*     */   private final PositionalRandomFactory random;
/*     */   
/*     */   private final HolderGetter<NormalNoise.NoiseParameters> noises;
/*     */   
/*     */   private final NoiseRouter router;
/*     */   
/*     */   private final Climate.Sampler sampler;
/*     */   
/*     */   private final SurfaceSystem surfaceSystem;
/*     */   
/*     */   private final PositionalRandomFactory aquiferRandom;
/*     */   
/*     */   private final PositionalRandomFactory oreRandom;
/*     */   
/*     */   private final Map<ResourceKey<NormalNoise.NoiseParameters>, NormalNoise> noiseIntances;
/*     */   
/*     */   private final Map<Identifier, PositionalRandomFactory> positionalRandoms;
/*     */   
/*     */   public static RandomState create(HolderGetter.Provider holders, ResourceKey<NoiseGeneratorSettings> noiseSettings, long seed) {
/*  29 */     return create((NoiseGeneratorSettings)holders.lookupOrThrow(Registries.NOISE_SETTINGS).getOrThrow(noiseSettings).value(), holders.lookupOrThrow(Registries.NOISE), seed);
/*     */   }
/*     */   
/*     */   public static RandomState create(NoiseGeneratorSettings settings, HolderGetter<NormalNoise.NoiseParameters> noises, long seed) {
/*  33 */     return new RandomState(settings, noises, seed);
/*     */   }
/*     */   
/*     */   private RandomState(NoiseGeneratorSettings settings, HolderGetter<NormalNoise.NoiseParameters> noises, final long seed) {
/*  37 */     this.random = settings.getRandomSource().newInstance(seed).forkPositional();
/*  38 */     this.noises = noises;
/*  39 */     this.aquiferRandom = this.random.fromHashOf(Identifier.withDefaultNamespace("aquifer")).forkPositional();
/*  40 */     this.oreRandom = this.random.fromHashOf(Identifier.withDefaultNamespace("ore")).forkPositional();
/*  41 */     this.noiseIntances = new ConcurrentHashMap<>();
/*  42 */     this.positionalRandoms = new ConcurrentHashMap<>();
/*  44 */     this.surfaceSystem = new SurfaceSystem(this, settings.defaultBlock(), settings.seaLevel(), this.random);
/*  46 */     final boolean useLegacyInit = settings.useLegacyRandomSource();
/*     */     class NoiseWiringHelper implements DensityFunction.Visitor {
/*  48 */       private final Map<DensityFunction, DensityFunction> wrapped = new HashMap<>();
/*     */       
/*     */       private RandomSource newLegacyInstance(long seedOffset) {
/*  51 */         return new LegacyRandomSource(seed + seedOffset);
/*     */       }
/*     */       
/*     */       public DensityFunction.NoiseHolder visitNoise(DensityFunction.NoiseHolder noise) {
/*  56 */         Holder<NormalNoise.NoiseParameters> noiseData = noise.noiseData();
/*  57 */         if (noiseData.is(Noises.TEMPERATURE_NETHER)) {
/*  58 */           NormalNoise newNoise = NormalNoise.createLegacyNetherBiome(newLegacyInstance(0L), (NormalNoise.NoiseParameters)noiseData.value());
/*  59 */           return new DensityFunction.NoiseHolder(noiseData, newNoise);
/*     */         } 
/*  61 */         if (noiseData.is(Noises.VEGETATION_NETHER)) {
/*  62 */           NormalNoise newNoise = NormalNoise.createLegacyNetherBiome(newLegacyInstance(1L), (NormalNoise.NoiseParameters)noiseData.value());
/*  63 */           return new DensityFunction.NoiseHolder(noiseData, newNoise);
/*     */         } 
/*  65 */         NormalNoise instantiate = RandomState.this.getOrCreateNoise(noiseData.unwrapKey().orElseThrow());
/*  66 */         return new DensityFunction.NoiseHolder(noiseData, instantiate);
/*     */       }
/*     */       
/*     */       private DensityFunction wrapNew(DensityFunction function) {
/*  70 */         if (function instanceof BlendedNoise) {
/*  70 */           BlendedNoise noise = (BlendedNoise)function;
/*  71 */           RandomSource terrainRandom = useLegacyInit ? newLegacyInstance(0L) : RandomState.this.random.fromHashOf(Identifier.withDefaultNamespace("terrain"));
/*  72 */           return (DensityFunction)noise.withNewRandom(terrainRandom);
/*     */         } 
/*  74 */         if (function instanceof DensityFunctions.EndIslandDensityFunction)
/*  75 */           return new DensityFunctions.EndIslandDensityFunction(seed); 
/*  77 */         return function;
/*     */       }
/*     */       
/*     */       public DensityFunction apply(DensityFunction function) {
/*  82 */         return this.wrapped.computeIfAbsent(function, this::wrapNew);
/*     */       }
/*     */     };
/*  86 */     this.router = settings.noiseRouter().mapAll(new NoiseWiringHelper());
/*  88 */     DensityFunction.Visitor noiseFlattener = new DensityFunction.Visitor(this) {
/*  89 */         private final Map<DensityFunction, DensityFunction> wrapped = new HashMap<>();
/*     */         
/*     */         private DensityFunction wrapNew(DensityFunction function) {
/*  92 */           if (function instanceof DensityFunctions.HolderHolder) {
/*  92 */             DensityFunctions.HolderHolder holder = (DensityFunctions.HolderHolder)function;
/*  93 */             return (DensityFunction)holder.function().value();
/*     */           } 
/*  95 */           if (function instanceof DensityFunctions.Marker) {
/*  95 */             DensityFunctions.Marker marker = (DensityFunctions.Marker)function;
/*  96 */             return marker.wrapped();
/*     */           } 
/*  98 */           return function;
/*     */         }
/*     */         
/*     */         public DensityFunction apply(DensityFunction input) {
/* 103 */           return this.wrapped.computeIfAbsent(input, this::wrapNew);
/*     */         }
/*     */       };
/* 107 */     this
/*     */       
/* 114 */       .sampler = new Climate.Sampler(this.router.temperature().mapAll(noiseFlattener), this.router.vegetation().mapAll(noiseFlattener), this.router.continents().mapAll(noiseFlattener), this.router.erosion().mapAll(noiseFlattener), this.router.depth().mapAll(noiseFlattener), this.router.ridges().mapAll(noiseFlattener), settings.spawnTarget());
/*     */   }
/*     */   
/*     */   public NormalNoise getOrCreateNoise(ResourceKey<NormalNoise.NoiseParameters> noise) {
/* 119 */     return this.noiseIntances.computeIfAbsent(noise, key -> Noises.instantiate(this.noises, this.random, noise));
/*     */   }
/*     */   
/*     */   public PositionalRandomFactory getOrCreateRandomFactory(Identifier name) {
/* 123 */     return this.positionalRandoms.computeIfAbsent(name, key -> this.random.fromHashOf(name).forkPositional());
/*     */   }
/*     */   
/*     */   public NoiseRouter router() {
/* 127 */     return this.router;
/*     */   }
/*     */   
/*     */   public Climate.Sampler sampler() {
/* 131 */     return this.sampler;
/*     */   }
/*     */   
/*     */   public SurfaceSystem surfaceSystem() {
/* 135 */     return this.surfaceSystem;
/*     */   }
/*     */   
/*     */   public PositionalRandomFactory aquiferRandom() {
/* 139 */     return this.aquiferRandom;
/*     */   }
/*     */   
/*     */   public PositionalRandomFactory oreRandom() {
/* 143 */     return this.oreRandom;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\RandomState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */