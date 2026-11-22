/*     */ package net.minecraft.world.level.levelgen.flat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
/*     */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FlatLevelGeneratorSettings {
/*  37 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final Codec<FlatLevelGeneratorSettings> CODEC;
/*     */   
/*     */   private final Optional<HolderSet<StructureSet>> structureOverrides;
/*     */   
/*     */   static {
/*  48 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.STRUCTURE_SET).lenientOptionalFieldOf("structure_overrides").forGetter(()), (App)FlatLayerInfo.CODEC.listOf().fieldOf("layers").forGetter(FlatLevelGeneratorSettings::getLayersInfo), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "lakes", Boolean.valueOf(false)).forGetter(()), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "features", Boolean.valueOf(false)).forGetter(()), (App)Biome.CODEC.lenientOptionalFieldOf("biome").orElseGet(Optional::empty).forGetter(()), (App)RegistryOps.retrieveElement(Biomes.PLAINS), (App)RegistryOps.retrieveElement(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND), (App)RegistryOps.retrieveElement(MiscOverworldPlacements.LAKE_LAVA_SURFACE)).apply((Applicative)i, FlatLevelGeneratorSettings::new)).comapFlatMap(FlatLevelGeneratorSettings::validateHeight, Function.identity()).stable();
/*     */   }
/*     */   
/*     */   private static DataResult<FlatLevelGeneratorSettings> validateHeight(FlatLevelGeneratorSettings settings) {
/*  51 */     int totalHeight = settings.layersInfo.stream().mapToInt(FlatLayerInfo::getHeight).sum();
/*  53 */     if (totalHeight > DimensionType.Y_SIZE)
/*  54 */       return DataResult.error(() -> "Sum of layer heights is > " + DimensionType.Y_SIZE, settings); 
/*  56 */     return DataResult.success(settings);
/*     */   }
/*     */   
/*  60 */   private final List<FlatLayerInfo> layersInfo = Lists.newArrayList();
/*     */   
/*     */   private final Holder<Biome> biome;
/*     */   
/*     */   private final List<BlockState> layers;
/*     */   
/*     */   private boolean voidGen;
/*     */   
/*     */   private boolean decoration;
/*     */   
/*     */   private boolean addLakes;
/*     */   
/*     */   private final List<Holder<PlacedFeature>> lakes;
/*     */   
/*     */   private FlatLevelGeneratorSettings(Optional<HolderSet<StructureSet>> structureOverrides, List<FlatLayerInfo> layers, boolean lakes, boolean features, Optional<Holder<Biome>> biome, Holder.Reference<Biome> fallbackBiome, Holder<PlacedFeature> lavaUnderground, Holder<PlacedFeature> lavaSurface) {
/*  69 */     this(structureOverrides, getBiome(biome, (Holder<Biome>)fallbackBiome), List.of(lavaUnderground, lavaSurface));
/*  70 */     if (lakes)
/*  71 */       setAddLakes(); 
/*  73 */     if (features)
/*  74 */       setDecoration(); 
/*  76 */     this.layersInfo.addAll(layers);
/*  77 */     updateLayers();
/*     */   }
/*     */   
/*     */   private static Holder<Biome> getBiome(Optional<? extends Holder<Biome>> biome, Holder<Biome> fallbackBiome) {
/*  81 */     if (biome.isEmpty()) {
/*  82 */       LOGGER.error("Unknown biome, defaulting to plains");
/*  83 */       return fallbackBiome;
/*     */     } 
/*  85 */     return biome.get();
/*     */   }
/*     */   
/*     */   public FlatLevelGeneratorSettings(Optional<HolderSet<StructureSet>> structureOverrides, Holder<Biome> biome, List<Holder<PlacedFeature>> lakes) {
/*  89 */     this.structureOverrides = structureOverrides;
/*  90 */     this.biome = biome;
/*  91 */     this.layers = Lists.newArrayList();
/*  92 */     this.lakes = lakes;
/*     */   }
/*     */   
/*     */   public FlatLevelGeneratorSettings withBiomeAndLayers(List<FlatLayerInfo> layers, Optional<HolderSet<StructureSet>> structureOverrides, Holder<Biome> biome) {
/*  96 */     FlatLevelGeneratorSettings settings = new FlatLevelGeneratorSettings(structureOverrides, biome, this.lakes);
/*  97 */     for (FlatLayerInfo layerInfo : layers) {
/*  98 */       settings.layersInfo.add(layerInfo);
/*  99 */       settings.updateLayers();
/*     */     } 
/* 101 */     if (this.decoration)
/* 102 */       settings.setDecoration(); 
/* 104 */     if (this.addLakes)
/* 105 */       settings.setAddLakes(); 
/* 107 */     return settings;
/*     */   }
/*     */   
/*     */   public void setDecoration() {
/* 111 */     this.decoration = true;
/*     */   }
/*     */   
/*     */   public void setAddLakes() {
/* 115 */     this.addLakes = true;
/*     */   }
/*     */   
/*     */   public BiomeGenerationSettings adjustGenerationSettings(Holder<Biome> sourceBiome) {
/* 119 */     if (!sourceBiome.equals(this.biome))
/* 120 */       return ((Biome)sourceBiome.value()).getGenerationSettings(); 
/* 122 */     BiomeGenerationSettings biomeGenerationSettings = ((Biome)getBiome().value()).getGenerationSettings();
/* 124 */     BiomeGenerationSettings.PlainBuilder newGenerationSettings = new BiomeGenerationSettings.PlainBuilder();
/* 126 */     if (this.addLakes)
/* 127 */       for (Holder<PlacedFeature> lake : this.lakes)
/* 128 */         newGenerationSettings.addFeature(GenerationStep.Decoration.LAKES, lake);  
/* 132 */     boolean biomeDecoration = ((!this.voidGen || sourceBiome.is(Biomes.THE_VOID)) && this.decoration);
/* 134 */     if (biomeDecoration) {
/* 135 */       List<HolderSet<PlacedFeature>> features = biomeGenerationSettings.features();
/* 136 */       for (int stepIndex = 0; stepIndex < features.size(); stepIndex++) {
/* 137 */         if (stepIndex != GenerationStep.Decoration.UNDERGROUND_STRUCTURES.ordinal() && stepIndex != GenerationStep.Decoration.SURFACE_STRUCTURES
/* 138 */           .ordinal() && (!this.addLakes || stepIndex != GenerationStep.Decoration.LAKES
/* 139 */           .ordinal())) {
/* 143 */           HolderSet<PlacedFeature> featureList = features.get(stepIndex);
/* 144 */           for (Holder<PlacedFeature> feature : featureList)
/* 145 */             newGenerationSettings.addFeature(stepIndex, feature); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 150 */     List<BlockState> layers = getLayers();
/* 151 */     for (int i = 0; i < layers.size(); i++) {
/* 152 */       BlockState layer = layers.get(i);
/* 155 */       if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(layer)) {
/* 156 */         layers.set(i, null);
/* 157 */         newGenerationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(Feature.FILL_LAYER, (FeatureConfiguration)new LayerConfiguration(i, layer), new net.minecraft.world.level.levelgen.placement.PlacementModifier[0]));
/*     */       } 
/*     */     } 
/* 161 */     return newGenerationSettings.build();
/*     */   }
/*     */   
/*     */   public Optional<HolderSet<StructureSet>> structureOverrides() {
/* 165 */     return this.structureOverrides;
/*     */   }
/*     */   
/*     */   public Holder<Biome> getBiome() {
/* 169 */     return this.biome;
/*     */   }
/*     */   
/*     */   public List<FlatLayerInfo> getLayersInfo() {
/* 173 */     return this.layersInfo;
/*     */   }
/*     */   
/*     */   public List<BlockState> getLayers() {
/* 177 */     return this.layers;
/*     */   }
/*     */   
/*     */   public void updateLayers() {
/* 181 */     this.layers.clear();
/* 183 */     for (FlatLayerInfo layer : this.layersInfo) {
/* 184 */       for (int y = 0; y < layer.getHeight(); y++)
/* 185 */         this.layers.add(layer.getBlockState()); 
/*     */     } 
/* 189 */     this.voidGen = this.layers.stream().allMatch(s -> s.is(Blocks.AIR));
/*     */   }
/*     */   
/*     */   public static FlatLevelGeneratorSettings getDefault(HolderGetter<Biome> biomes, HolderGetter<StructureSet> structureSets, HolderGetter<PlacedFeature> placedFeatures) {
/* 193 */     HolderSet.Direct direct = HolderSet.direct(new Holder[] { (Holder)structureSets
/* 194 */           .getOrThrow(BuiltinStructureSets.STRONGHOLDS), (Holder)structureSets
/* 195 */           .getOrThrow(BuiltinStructureSets.VILLAGES) });
/* 198 */     FlatLevelGeneratorSettings result = new FlatLevelGeneratorSettings((Optional)Optional.of(direct), getDefaultBiome(biomes), createLakesList(placedFeatures));
/* 199 */     result.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
/* 200 */     result.getLayersInfo().add(new FlatLayerInfo(2, Blocks.DIRT));
/* 201 */     result.getLayersInfo().add(new FlatLayerInfo(1, Blocks.GRASS_BLOCK));
/* 202 */     result.updateLayers();
/* 204 */     return result;
/*     */   }
/*     */   
/*     */   public static Holder<Biome> getDefaultBiome(HolderGetter<Biome> biomes) {
/* 208 */     return (Holder<Biome>)biomes.getOrThrow(Biomes.PLAINS);
/*     */   }
/*     */   
/*     */   public static List<Holder<PlacedFeature>> createLakesList(HolderGetter<PlacedFeature> placedFeatures) {
/* 212 */     return (List)List.of(placedFeatures
/* 213 */         .getOrThrow(MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND), placedFeatures
/* 214 */         .getOrThrow(MiscOverworldPlacements.LAKE_LAVA_SURFACE));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\flat\FlatLevelGeneratorSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */