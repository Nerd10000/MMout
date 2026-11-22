/*     */ package net.minecraft.world.level.levelgen.flat;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ 
/*     */ class Bootstrap {
/*     */   private final BootstrapContext<FlatLevelGeneratorPreset> context;
/*     */   
/*     */   private Bootstrap(BootstrapContext<FlatLevelGeneratorPreset> context) {
/*  39 */     this.context = context;
/*     */   }
/*     */   
/*     */   private void register(ResourceKey<FlatLevelGeneratorPreset> key, ItemLike icon, ResourceKey<Biome> biome, Set<ResourceKey<StructureSet>> structures, boolean decoration, boolean addLakes, FlatLayerInfo... layers) {
/*  43 */     HolderGetter<StructureSet> structureSets = this.context.lookup(Registries.STRUCTURE_SET);
/*  44 */     HolderGetter<PlacedFeature> placedFeatures = this.context.lookup(Registries.PLACED_FEATURE);
/*  45 */     HolderGetter<Biome> biomes = this.context.lookup(Registries.BIOME);
/*  47 */     Objects.requireNonNull(structureSets);
/*  47 */     HolderSet.Direct<StructureSet> structuresHolder = HolderSet.direct((List)structures.stream().map(structureSets::getOrThrow).collect(Collectors.toList()));
/*  48 */     FlatLevelGeneratorSettings generator = new FlatLevelGeneratorSettings((Optional)Optional.of(structuresHolder), (Holder<Biome>)biomes.getOrThrow(biome), FlatLevelGeneratorSettings.createLakesList(placedFeatures));
/*  49 */     if (decoration)
/*  50 */       generator.setDecoration(); 
/*  53 */     if (addLakes)
/*  54 */       generator.setAddLakes(); 
/*  57 */     for (int i = layers.length - 1; i >= 0; i--)
/*  58 */       generator.getLayersInfo().add(layers[i]); 
/*  61 */     this.context.register(key, new FlatLevelGeneratorPreset((Holder<Item>)icon
/*  62 */           .asItem().builtInRegistryHolder(), generator));
/*     */   }
/*     */   
/*     */   public void run() {
/*  68 */     register(FlatLevelGeneratorPresets.CLASSIC_FLAT, (ItemLike)Blocks.GRASS_BLOCK, Biomes.PLAINS, 
/*     */         
/*  71 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES), false, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(2, Blocks.DIRT), new FlatLayerInfo(1, Blocks.BEDROCK) });
/*  81 */     register(FlatLevelGeneratorPresets.TUNNELERS_DREAM, (ItemLike)Blocks.STONE, Biomes.WINDSWEPT_HILLS, 
/*     */         
/*  84 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.MINESHAFTS, BuiltinStructureSets.STRONGHOLDS), true, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(230, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
/*  96 */     register(FlatLevelGeneratorPresets.WATER_WORLD, (ItemLike)Items.WATER_BUCKET, Biomes.DEEP_OCEAN, 
/*     */         
/*  99 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.OCEAN_RUINS, BuiltinStructureSets.SHIPWRECKS, BuiltinStructureSets.OCEAN_MONUMENTS), false, false, new FlatLayerInfo[] { new FlatLayerInfo(90, Blocks.WATER), new FlatLayerInfo(5, Blocks.GRAVEL), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(5, Blocks.STONE), new FlatLayerInfo(64, Blocks.DEEPSLATE), new FlatLayerInfo(1, Blocks.BEDROCK) });
/* 114 */     register(FlatLevelGeneratorPresets.OVERWORLD, (ItemLike)Blocks.SHORT_GRASS, Biomes.PLAINS, 
/*     */         
/* 117 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES, BuiltinStructureSets.MINESHAFTS, BuiltinStructureSets.PILLAGER_OUTPOSTS, BuiltinStructureSets.RUINED_PORTALS, BuiltinStructureSets.STRONGHOLDS), true, true, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
/* 132 */     register(FlatLevelGeneratorPresets.SNOWY_KINGDOM, (ItemLike)Blocks.SNOW, Biomes.SNOWY_PLAINS, 
/*     */         
/* 135 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES, BuiltinStructureSets.IGLOOS), false, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.SNOW), new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
/* 148 */     register(FlatLevelGeneratorPresets.BOTTOMLESS_PIT, (ItemLike)Items.FEATHER, Biomes.PLAINS, 
/*     */         
/* 151 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES), false, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(2, Blocks.COBBLESTONE) });
/* 161 */     register(FlatLevelGeneratorPresets.DESERT, (ItemLike)Blocks.SAND, Biomes.DESERT, 
/*     */         
/* 164 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(BuiltinStructureSets.VILLAGES, BuiltinStructureSets.DESERT_PYRAMIDS, BuiltinStructureSets.MINESHAFTS, BuiltinStructureSets.STRONGHOLDS), true, false, new FlatLayerInfo[] { new FlatLayerInfo(8, Blocks.SAND), new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
/* 178 */     register(FlatLevelGeneratorPresets.REDSTONE_READY, (ItemLike)Items.REDSTONE, Biomes.DESERT, 
/*     */         
/* 181 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(), false, false, new FlatLayerInfo[] { new FlatLayerInfo(116, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
/* 189 */     register(FlatLevelGeneratorPresets.THE_VOID, (ItemLike)Blocks.BARRIER, Biomes.THE_VOID, 
/*     */         
/* 192 */         (Set<ResourceKey<StructureSet>>)ImmutableSet.of(), true, false, new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.AIR) });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\flat\FlatLevelGeneratorPresets$Bootstrap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */