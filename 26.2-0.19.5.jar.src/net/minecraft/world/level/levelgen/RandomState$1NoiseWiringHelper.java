/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.synth.BlendedNoise;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ class NoiseWiringHelper implements DensityFunction.Visitor {
/* 48 */   private final Map<DensityFunction, DensityFunction> wrapped = new HashMap<>();
/*    */   
/*    */   private RandomSource newLegacyInstance(long seedOffset) {
/* 51 */     return new LegacyRandomSource(seed + seedOffset);
/*    */   }
/*    */   
/*    */   public DensityFunction.NoiseHolder visitNoise(DensityFunction.NoiseHolder noise) {
/* 56 */     Holder<NormalNoise.NoiseParameters> noiseData = noise.noiseData();
/* 57 */     if (noiseData.is(Noises.TEMPERATURE_NETHER)) {
/* 58 */       NormalNoise newNoise = NormalNoise.createLegacyNetherBiome(newLegacyInstance(0L), (NormalNoise.NoiseParameters)noiseData.value());
/* 59 */       return new DensityFunction.NoiseHolder(noiseData, newNoise);
/*    */     } 
/* 61 */     if (noiseData.is(Noises.VEGETATION_NETHER)) {
/* 62 */       NormalNoise newNoise = NormalNoise.createLegacyNetherBiome(newLegacyInstance(1L), (NormalNoise.NoiseParameters)noiseData.value());
/* 63 */       return new DensityFunction.NoiseHolder(noiseData, newNoise);
/*    */     } 
/* 65 */     NormalNoise instantiate = RandomState.this.getOrCreateNoise(noiseData.unwrapKey().orElseThrow());
/* 66 */     return new DensityFunction.NoiseHolder(noiseData, instantiate);
/*    */   }
/*    */   
/*    */   private DensityFunction wrapNew(DensityFunction function) {
/* 70 */     if (function instanceof BlendedNoise) {
/* 70 */       BlendedNoise noise = (BlendedNoise)function;
/* 71 */       RandomSource terrainRandom = useLegacyInit ? newLegacyInstance(0L) : RandomState.this.random.fromHashOf(Identifier.withDefaultNamespace("terrain"));
/* 72 */       return (DensityFunction)noise.withNewRandom(terrainRandom);
/*    */     } 
/* 74 */     if (function instanceof DensityFunctions.EndIslandDensityFunction)
/* 75 */       return new DensityFunctions.EndIslandDensityFunction(seed); 
/* 77 */     return function;
/*    */   }
/*    */   
/*    */   public DensityFunction apply(DensityFunction function) {
/* 82 */     return this.wrapped.computeIfAbsent(function, this::wrapNew);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\RandomState$1NoiseWiringHelper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */