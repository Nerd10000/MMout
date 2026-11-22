/*     */ package net.minecraft.world.level.levelgen.presets;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.FixedBiomeSource;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
/*     */ import net.minecraft.world.level.biome.TheEndBiomeSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.levelgen.DebugLevelSource;
/*     */ import net.minecraft.world.level.levelgen.FlatLevelSource;
/*     */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ 
/*     */ class Bootstrap {
/*     */   private final BootstrapContext<WorldPreset> context;
/*     */   
/*     */   private final HolderGetter<NoiseGeneratorSettings> noiseSettings;
/*     */   
/*     */   private final HolderGetter<Biome> biomes;
/*     */   
/*     */   private final HolderGetter<PlacedFeature> placedFeatures;
/*     */   
/*     */   private final HolderGetter<StructureSet> structureSets;
/*     */   
/*     */   private final HolderGetter<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterLists;
/*     */   
/*     */   private final Holder<DimensionType> overworldDimensionType;
/*     */   
/*     */   private final Holder<DimensionType> netherDimensionType;
/*     */   
/*     */   private final Holder<DimensionType> endDimensionType;
/*     */   
/*     */   private final LevelStem netherStem;
/*     */   
/*     */   private final LevelStem endStem;
/*     */   
/*     */   private Bootstrap(BootstrapContext<WorldPreset> context) {
/*  63 */     this.context = context;
/*  65 */     HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
/*  67 */     this.noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
/*  68 */     this.biomes = context.lookup(Registries.BIOME);
/*  69 */     this.placedFeatures = context.lookup(Registries.PLACED_FEATURE);
/*  70 */     this.structureSets = context.lookup(Registries.STRUCTURE_SET);
/*  71 */     this.multiNoiseBiomeSourceParameterLists = context.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST);
/*  73 */     this.overworldDimensionType = (Holder<DimensionType>)dimensionTypes.getOrThrow(BuiltinDimensionTypes.OVERWORLD);
/*  74 */     this.netherDimensionType = (Holder<DimensionType>)dimensionTypes.getOrThrow(BuiltinDimensionTypes.NETHER);
/*  75 */     this.endDimensionType = (Holder<DimensionType>)dimensionTypes.getOrThrow(BuiltinDimensionTypes.END);
/*  77 */     Holder.Reference reference1 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.NETHER);
/*  78 */     Holder.Reference<MultiNoiseBiomeSourceParameterList> netherBiomePreset = this.multiNoiseBiomeSourceParameterLists.getOrThrow(MultiNoiseBiomeSourceParameterLists.NETHER);
/*  80 */     this.netherStem = new LevelStem(this.netherDimensionType, (ChunkGenerator)new NoiseBasedChunkGenerator((BiomeSource)MultiNoiseBiomeSource.createFromPreset((Holder)netherBiomePreset), (Holder)reference1));
/*  82 */     Holder.Reference reference2 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.END);
/*  84 */     this.endStem = new LevelStem(this.endDimensionType, (ChunkGenerator)new NoiseBasedChunkGenerator((BiomeSource)TheEndBiomeSource.create(this.biomes), (Holder)reference2));
/*     */   }
/*     */   
/*     */   private LevelStem makeOverworld(ChunkGenerator generator) {
/*  88 */     return new LevelStem(this.overworldDimensionType, generator);
/*     */   }
/*     */   
/*     */   private LevelStem makeNether(ChunkGenerator generator) {
/*  92 */     return new LevelStem(this.netherDimensionType, generator);
/*     */   }
/*     */   
/*     */   private LevelStem makeEnd(ChunkGenerator generator) {
/*  96 */     return new LevelStem(this.endDimensionType, generator);
/*     */   }
/*     */   
/*     */   private LevelStem makeNoiseBasedOverworld(BiomeSource overworldBiomeSource, Holder<NoiseGeneratorSettings> noiseSettings) {
/* 100 */     return makeOverworld((ChunkGenerator)new NoiseBasedChunkGenerator(overworldBiomeSource, noiseSettings));
/*     */   }
/*     */   
/*     */   private WorldPreset createPresetWithCustomOverworld(LevelStem overworldStem) {
/* 104 */     return new WorldPreset(
/* 105 */         Map.of(LevelStem.OVERWORLD, overworldStem, LevelStem.NETHER, this.netherStem, LevelStem.END, this.endStem));
/*     */   }
/*     */   
/*     */   private void registerCustomOverworldPreset(ResourceKey<WorldPreset> debug, LevelStem overworld) {
/* 114 */     this.context.register(debug, createPresetWithCustomOverworld(overworld));
/*     */   }
/*     */   
/*     */   private void registerOverworlds(BiomeSource biomeSource) {
/* 118 */     Holder.Reference reference1 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
/* 119 */     registerCustomOverworldPreset(WorldPresets.NORMAL, makeNoiseBasedOverworld(biomeSource, (Holder<NoiseGeneratorSettings>)reference1));
/* 121 */     Holder.Reference reference2 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.LARGE_BIOMES);
/* 122 */     registerCustomOverworldPreset(WorldPresets.LARGE_BIOMES, makeNoiseBasedOverworld(biomeSource, (Holder<NoiseGeneratorSettings>)reference2));
/* 124 */     Holder.Reference reference3 = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED);
/* 125 */     registerCustomOverworldPreset(WorldPresets.AMPLIFIED, makeNoiseBasedOverworld(biomeSource, (Holder<NoiseGeneratorSettings>)reference3));
/*     */   }
/*     */   
/*     */   public void bootstrap() {
/* 129 */     Holder.Reference<MultiNoiseBiomeSourceParameterList> overworldPreset = this.multiNoiseBiomeSourceParameterLists.getOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD);
/* 130 */     registerOverworlds((BiomeSource)MultiNoiseBiomeSource.createFromPreset((Holder)overworldPreset));
/* 132 */     Holder.Reference reference = this.noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
/* 133 */     Holder.Reference<Biome> plains = this.biomes.getOrThrow(Biomes.PLAINS);
/* 134 */     registerCustomOverworldPreset(WorldPresets.SINGLE_BIOME_SURFACE, makeNoiseBasedOverworld((BiomeSource)new FixedBiomeSource((Holder)plains), (Holder<NoiseGeneratorSettings>)reference));
/* 136 */     registerCustomOverworldPreset(WorldPresets.FLAT, makeOverworld((ChunkGenerator)new FlatLevelSource(FlatLevelGeneratorSettings.getDefault(this.biomes, this.structureSets, this.placedFeatures))));
/* 137 */     this.context.register(WorldPresets.FLAT_ALL_DIMENSIONS, createFlatAllDimensionsPreset());
/* 139 */     registerCustomOverworldPreset(WorldPresets.DEBUG, makeOverworld((ChunkGenerator)new DebugLevelSource(plains)));
/*     */   }
/*     */   
/*     */   private FlatLevelGeneratorSettings flatSettingsForBiomeAndLayers(ResourceKey<Biome> biomeKey, List<FlatLayerInfo> layers) {
/* 143 */     return 
/* 144 */       FlatLevelGeneratorSettings.getDefault(this.biomes, this.structureSets, this.placedFeatures)
/* 145 */       .withBiomeAndLayers(layers, Optional.empty(), (Holder)this.biomes.getOrThrow(biomeKey));
/*     */   }
/*     */   
/*     */   private WorldPreset createFlatAllDimensionsPreset() {
/* 149 */     LevelStem overworldFlat = makeOverworld((ChunkGenerator)new FlatLevelSource(
/* 150 */           flatSettingsForBiomeAndLayers(Biomes.DESERT, 
/*     */             
/* 152 */             List.of(new FlatLayerInfo(1, Blocks.BEDROCK), new FlatLayerInfo(67, Blocks.SANDSTONE)))));
/* 159 */     LevelStem netherFlat = makeNether((ChunkGenerator)new FlatLevelSource(
/* 160 */           flatSettingsForBiomeAndLayers(Biomes.BASALT_DELTAS, 
/*     */             
/* 162 */             List.of(new FlatLayerInfo(1, Blocks.BEDROCK), new FlatLayerInfo(3, Blocks.BASALT)))));
/* 169 */     LevelStem endFlat = makeEnd((ChunkGenerator)new FlatLevelSource(
/* 170 */           flatSettingsForBiomeAndLayers(Biomes.THE_END, 
/*     */             
/* 172 */             List.of(new FlatLayerInfo(1, Blocks.BEDROCK), new FlatLayerInfo(3, Blocks.END_STONE)))));
/* 179 */     return new WorldPreset(Map.of(LevelStem.OVERWORLD, overworldFlat, LevelStem.NETHER, netherFlat, LevelStem.END, endFlat));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\presets\WorldPresets$Bootstrap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */