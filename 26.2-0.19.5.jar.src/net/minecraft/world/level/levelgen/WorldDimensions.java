/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.RegistrationInfo;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ 
/*     */ public final class WorldDimensions extends Record {
/*     */   private final Map<ResourceKey<LevelStem>, LevelStem> dimensions;
/*     */   
/*     */   public static final MapCodec<WorldDimensions> CODEC;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/WorldDimensions;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #37	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/WorldDimensions;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #37	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/WorldDimensions;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #37	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Map<ResourceKey<LevelStem>, LevelStem> dimensions() {
/*  37 */     return this.dimensions;
/*     */   }
/*     */   
/*     */   static {
/*  41 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.unboundedMap(ResourceKey.codec(Registries.LEVEL_STEM), LevelStem.CODEC).fieldOf("dimensions").forGetter(WorldDimensions::dimensions)).apply((Applicative)i, i.stable(WorldDimensions::new)));
/*     */   }
/*     */   
/*  47 */   private static final Set<ResourceKey<LevelStem>> BUILTIN_ORDER = (Set<ResourceKey<LevelStem>>)ImmutableSet.of(LevelStem.OVERWORLD, LevelStem.NETHER, LevelStem.END);
/*     */   
/*     */   public WorldDimensions(Map<ResourceKey<LevelStem>, LevelStem> dimensions) {
/*  54 */     LevelStem overworld = dimensions.get(LevelStem.OVERWORLD);
/*  55 */     if (overworld == null)
/*  56 */       throw new IllegalStateException("Overworld settings missing"); 
/*     */     this.dimensions = dimensions;
/*     */   }
/*     */   
/*     */   public WorldDimensions(Registry<LevelStem> registry) {
/*  61 */     this((Map<ResourceKey<LevelStem>, LevelStem>)registry
/*  62 */         .listElements().collect(Collectors.toMap(Holder.Reference::key, Holder.Reference::value)));
/*     */   }
/*     */   
/*     */   public static Stream<ResourceKey<LevelStem>> keysInOrder(Set<ResourceKey<LevelStem>> knownKeys) {
/*  68 */     Objects.requireNonNull(knownKeys);
/*  68 */     return Stream.concat(BUILTIN_ORDER.stream().filter(knownKeys::contains), knownKeys
/*  69 */         .stream().filter(k -> !BUILTIN_ORDER.contains(k)));
/*     */   }
/*     */   
/*     */   public WorldDimensions replaceOverworldGenerator(HolderLookup.Provider registries, ChunkGenerator generator) {
/*  74 */     HolderLookup.RegistryLookup registryLookup = registries.lookupOrThrow(Registries.DIMENSION_TYPE);
/*  75 */     Map<ResourceKey<LevelStem>, LevelStem> newDimensions = withOverworld((HolderLookup<DimensionType>)registryLookup, this.dimensions, generator);
/*  76 */     return new WorldDimensions(newDimensions);
/*     */   }
/*     */   
/*     */   public static Map<ResourceKey<LevelStem>, LevelStem> withOverworld(HolderLookup<DimensionType> dimensionTypes, Map<ResourceKey<LevelStem>, LevelStem> dimensions, ChunkGenerator generator) {
/*  80 */     LevelStem stem = dimensions.get(LevelStem.OVERWORLD);
/*  81 */     Holder<DimensionType> type = (stem == null) ? (Holder<DimensionType>)dimensionTypes.getOrThrow(BuiltinDimensionTypes.OVERWORLD) : stem.type();
/*  83 */     return withOverworld(dimensions, type, generator);
/*     */   }
/*     */   
/*     */   public static Map<ResourceKey<LevelStem>, LevelStem> withOverworld(Map<ResourceKey<LevelStem>, LevelStem> dimensions, Holder<DimensionType> type, ChunkGenerator generator) {
/*  87 */     ImmutableMap.Builder<ResourceKey<LevelStem>, LevelStem> builder = ImmutableMap.builder();
/*  88 */     builder.putAll(dimensions);
/*  89 */     builder.put(LevelStem.OVERWORLD, new LevelStem(type, generator));
/*  90 */     return (Map<ResourceKey<LevelStem>, LevelStem>)builder.buildKeepingLast();
/*     */   }
/*     */   
/*     */   public ChunkGenerator overworld() {
/*  94 */     LevelStem stem = this.dimensions.get(LevelStem.OVERWORLD);
/*  95 */     if (stem == null)
/*  96 */       throw new IllegalStateException("Overworld settings missing"); 
/*  98 */     return stem.generator();
/*     */   }
/*     */   
/*     */   public Optional<LevelStem> get(ResourceKey<LevelStem> key) {
/* 102 */     return Optional.ofNullable(this.dimensions.get(key));
/*     */   }
/*     */   
/*     */   public ImmutableSet<ResourceKey<Level>> levels() {
/* 106 */     return (ImmutableSet<ResourceKey<Level>>)dimensions().keySet().stream().map(Registries::levelStemToLevel).collect(ImmutableSet.toImmutableSet());
/*     */   }
/*     */   
/*     */   public boolean isDebug() {
/* 110 */     return overworld() instanceof DebugLevelSource;
/*     */   }
/*     */   
/*     */   private static PrimaryLevelData.SpecialWorldProperty specialWorldProperty(Registry<LevelStem> registry) {
/* 114 */     return registry.getOptional(LevelStem.OVERWORLD).map(overworld -> {
/*     */           ChunkGenerator generator = overworld.generator();
/*     */           return (generator instanceof DebugLevelSource) ? PrimaryLevelData.SpecialWorldProperty.DEBUG : ((generator instanceof FlatLevelSource) ? PrimaryLevelData.SpecialWorldProperty.FLAT : PrimaryLevelData.SpecialWorldProperty.NONE);
/* 123 */         }).orElse(PrimaryLevelData.SpecialWorldProperty.NONE);
/*     */   }
/*     */   
/*     */   private static Lifecycle checkStability(ResourceKey<LevelStem> key, LevelStem dimension) {
/* 127 */     return isVanillaLike(key, dimension) ? Lifecycle.stable() : Lifecycle.experimental();
/*     */   }
/*     */   
/*     */   private static boolean isVanillaLike(ResourceKey<LevelStem> key, LevelStem dimension) {
/* 131 */     if (key == LevelStem.OVERWORLD)
/* 132 */       return isStableOverworld(dimension); 
/* 134 */     if (key == LevelStem.NETHER)
/* 135 */       return isStableNether(dimension); 
/* 137 */     if (key == LevelStem.END)
/* 138 */       return isStableEnd(dimension); 
/* 140 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isStableOverworld(LevelStem dimension) {
/* 144 */     Holder<DimensionType> dimensionType = dimension.type();
/* 145 */     if (!dimensionType.is(BuiltinDimensionTypes.OVERWORLD) && !dimensionType.is(BuiltinDimensionTypes.OVERWORLD_CAVES))
/* 146 */       return false; 
/* 148 */     BiomeSource biomeSource = dimension.generator().getBiomeSource();
/* 148 */     if (biomeSource instanceof MultiNoiseBiomeSource) {
/* 148 */       MultiNoiseBiomeSource multiNoiseBiomeSource = (MultiNoiseBiomeSource)biomeSource;
/* 149 */       if (!multiNoiseBiomeSource.stable(MultiNoiseBiomeSourceParameterLists.OVERWORLD))
/* 150 */         return false; 
/*     */     } 
/* 153 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isStableNether(LevelStem dimension) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual type : ()Lnet/minecraft/core/Holder;
/*     */     //   4: getstatic net/minecraft/world/level/dimension/BuiltinDimensionTypes.NETHER : Lnet/minecraft/resources/ResourceKey;
/*     */     //   7: invokeinterface is : (Lnet/minecraft/resources/ResourceKey;)Z
/*     */     //   12: ifeq -> 73
/*     */     //   15: aload_0
/*     */     //   16: invokevirtual generator : ()Lnet/minecraft/world/level/chunk/ChunkGenerator;
/*     */     //   19: astore_3
/*     */     //   20: aload_3
/*     */     //   21: instanceof net/minecraft/world/level/levelgen/NoiseBasedChunkGenerator
/*     */     //   24: ifeq -> 73
/*     */     //   27: aload_3
/*     */     //   28: checkcast net/minecraft/world/level/levelgen/NoiseBasedChunkGenerator
/*     */     //   31: astore_2
/*     */     //   32: aload_2
/*     */     //   33: getstatic net/minecraft/world/level/levelgen/NoiseGeneratorSettings.NETHER : Lnet/minecraft/resources/ResourceKey;
/*     */     //   36: invokevirtual stable : (Lnet/minecraft/resources/ResourceKey;)Z
/*     */     //   39: ifeq -> 73
/*     */     //   42: aload_2
/*     */     //   43: invokevirtual getBiomeSource : ()Lnet/minecraft/world/level/biome/BiomeSource;
/*     */     //   46: astore_3
/*     */     //   47: aload_3
/*     */     //   48: instanceof net/minecraft/world/level/biome/MultiNoiseBiomeSource
/*     */     //   51: ifeq -> 73
/*     */     //   54: aload_3
/*     */     //   55: checkcast net/minecraft/world/level/biome/MultiNoiseBiomeSource
/*     */     //   58: astore_1
/*     */     //   59: aload_1
/*     */     //   60: getstatic net/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterLists.NETHER : Lnet/minecraft/resources/ResourceKey;
/*     */     //   63: invokevirtual stable : (Lnet/minecraft/resources/ResourceKey;)Z
/*     */     //   66: ifeq -> 73
/*     */     //   69: iconst_1
/*     */     //   70: goto -> 74
/*     */     //   73: iconst_0
/*     */     //   74: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #161	-> 0
/*     */     //   #157	-> 1
/*     */     //   #158	-> 15
/*     */     //   #159	-> 36
/*     */     //   #160	-> 42
/*     */     //   #161	-> 63
/*     */     //   #157	-> 74
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   59	14	1	biomeSource	Lnet/minecraft/world/level/biome/MultiNoiseBiomeSource;
/*     */     //   32	41	2	generator	Lnet/minecraft/world/level/levelgen/NoiseBasedChunkGenerator;
/*     */     //   0	75	0	dimension	Lnet/minecraft/world/level/dimension/LevelStem;
/*     */   }
/*     */   
/*     */   private static boolean isStableEnd(LevelStem dimension) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual type : ()Lnet/minecraft/core/Holder;
/*     */     //   4: getstatic net/minecraft/world/level/dimension/BuiltinDimensionTypes.END : Lnet/minecraft/resources/ResourceKey;
/*     */     //   7: invokeinterface is : (Lnet/minecraft/resources/ResourceKey;)Z
/*     */     //   12: ifeq -> 56
/*     */     //   15: aload_0
/*     */     //   16: invokevirtual generator : ()Lnet/minecraft/world/level/chunk/ChunkGenerator;
/*     */     //   19: astore_2
/*     */     //   20: aload_2
/*     */     //   21: instanceof net/minecraft/world/level/levelgen/NoiseBasedChunkGenerator
/*     */     //   24: ifeq -> 56
/*     */     //   27: aload_2
/*     */     //   28: checkcast net/minecraft/world/level/levelgen/NoiseBasedChunkGenerator
/*     */     //   31: astore_1
/*     */     //   32: aload_1
/*     */     //   33: getstatic net/minecraft/world/level/levelgen/NoiseGeneratorSettings.END : Lnet/minecraft/resources/ResourceKey;
/*     */     //   36: invokevirtual stable : (Lnet/minecraft/resources/ResourceKey;)Z
/*     */     //   39: ifeq -> 56
/*     */     //   42: aload_1
/*     */     //   43: invokevirtual getBiomeSource : ()Lnet/minecraft/world/level/biome/BiomeSource;
/*     */     //   46: instanceof net/minecraft/world/level/biome/TheEndBiomeSource
/*     */     //   49: ifeq -> 56
/*     */     //   52: iconst_1
/*     */     //   53: goto -> 57
/*     */     //   56: iconst_0
/*     */     //   57: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #168	-> 0
/*     */     //   #165	-> 1
/*     */     //   #166	-> 15
/*     */     //   #167	-> 36
/*     */     //   #168	-> 43
/*     */     //   #165	-> 57
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   32	24	1	generator	Lnet/minecraft/world/level/levelgen/NoiseBasedChunkGenerator;
/*     */     //   0	58	0	dimension	Lnet/minecraft/world/level/dimension/LevelStem;
/*     */   }
/*     */   
/*     */   public Complete bake(Registry<LevelStem> baseDimensions) {
/* 178 */     Sets.SetView setView = Sets.union(baseDimensions.registryKeySet(), this.dimensions.keySet());
/* 180 */     List<Entry> results = new ArrayList<>();
/* 181 */     keysInOrder((Set<ResourceKey<LevelStem>>)setView).forEach(key -> baseDimensions.getOptional(key).or(()).ifPresent(()));
/* 189 */     Lifecycle initialStability = setView.containsAll(BUILTIN_ORDER) ? Lifecycle.stable() : Lifecycle.experimental();
/* 190 */     MappedRegistry mappedRegistry = new MappedRegistry(Registries.LEVEL_STEM, initialStability);
/* 191 */     results.forEach(entry -> writableDimensions.register(entry.key, entry.value, entry.registrationInfo()));
/* 192 */     Registry<LevelStem> newDimensions = mappedRegistry.freeze();
/* 194 */     PrimaryLevelData.SpecialWorldProperty specialWorldProperty = specialWorldProperty(newDimensions);
/* 195 */     return new Complete(newDimensions.freeze(), specialWorldProperty);
/*     */   }
/*     */   
/*     */   public static final class Complete extends Record {
/*     */     private final Registry<LevelStem> dimensions;
/*     */     
/*     */     private final PrimaryLevelData.SpecialWorldProperty specialWorldProperty;
/*     */     
/*     */     public Complete(Registry<LevelStem> dimensions, PrimaryLevelData.SpecialWorldProperty specialWorldProperty) {
/* 198 */       this.dimensions = dimensions;
/* 198 */       this.specialWorldProperty = specialWorldProperty;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #198	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #198	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #198	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Registry<LevelStem> dimensions() {
/* 198 */       return this.dimensions;
/*     */     }
/*     */     
/*     */     public PrimaryLevelData.SpecialWorldProperty specialWorldProperty() {
/* 198 */       return this.specialWorldProperty;
/*     */     }
/*     */     
/*     */     public Lifecycle lifecycle() {
/* 203 */       return this.dimensions.registryLifecycle();
/*     */     }
/*     */     
/*     */     public RegistryAccess.Frozen dimensionsRegistryAccess() {
/* 207 */       return (new RegistryAccess.ImmutableRegistryAccess(List.of(this.dimensions))).freeze();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\WorldDimensions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */