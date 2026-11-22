/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderOwner;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.RegistrySynchronization;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.gametest.framework.GameTestInstance;
/*     */ import net.minecraft.gametest.framework.TestEnvironmentDefinition;
/*     */ import net.minecraft.network.chat.ChatType;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.tags.TagNetworkSerialization;
/*     */ import net.minecraft.world.clock.WorldClock;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.SulfurCubeArchetype;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenSoundVariant;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariant;
/*     */ import net.minecraft.world.entity.animal.cow.CowSoundVariant;
/*     */ import net.minecraft.world.entity.animal.cow.CowVariant;
/*     */ import net.minecraft.world.entity.animal.feline.CatSoundVariant;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariant;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
/*     */ import net.minecraft.world.entity.animal.pig.PigSoundVariant;
/*     */ import net.minecraft.world.entity.animal.pig.PigVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariant;
/*     */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*     */ import net.minecraft.world.item.Instrument;
/*     */ import net.minecraft.world.item.JukeboxSong;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.item.trading.TradeSet;
/*     */ import net.minecraft.world.item.trading.VillagerTrade;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.levelgen.DensityFunctions;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RegistryDataLoader {
/*  81 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  83 */   private static final Comparator<ResourceKey<?>> ERROR_KEY_COMPARATOR = Comparator.comparing(ResourceKey::registry).thenComparing(ResourceKey::identifier);
/*     */   
/*     */   public static final class RegistryData<T> extends Record {
/*     */     private final ResourceKey<? extends Registry<T>> key;
/*     */     
/*     */     private final Codec<T> elementCodec;
/*     */     
/*     */     private final RegistryValidator<T> validator;
/*     */     
/*     */     public RegistryData(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec, RegistryValidator<T> validator) {
/*  85 */       this.key = key;
/*  85 */       this.elementCodec = elementCodec;
/*  85 */       this.validator = validator;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryDataLoader$RegistryData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryDataLoader$RegistryData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryDataLoader$RegistryData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$RegistryData<TT;>;
/*     */     }
/*     */     
/*     */     public ResourceKey<? extends Registry<T>> key() {
/*  85 */       return this.key;
/*     */     }
/*     */     
/*     */     public Codec<T> elementCodec() {
/*  85 */       return this.elementCodec;
/*     */     }
/*     */     
/*     */     public RegistryValidator<T> validator() {
/*  85 */       return this.validator;
/*     */     }
/*     */     
/*     */     private RegistryData(ResourceKey<? extends Registry<T>> key, Codec<T> elementCodec) {
/*  87 */       this(key, elementCodec, RegistryValidator.none());
/*     */     }
/*     */     
/*     */     public void runWithArguments(BiConsumer<ResourceKey<? extends Registry<T>>, Codec<T>> output) {
/*  91 */       output.accept(this.key, this.elementCodec);
/*     */     }
/*     */   }
/*     */   
/*  95 */   public static final List<RegistryData<?>> WORLDGEN_REGISTRIES = List.of((RegistryData<?>[])new RegistryData[] { 
/*  95 */         new RegistryData(Registries.DIMENSION_TYPE, DimensionType.DIRECT_CODEC), new RegistryData(Registries.BIOME, Biome.DIRECT_CODEC), new RegistryData(Registries.CHAT_TYPE, ChatType.DIRECT_CODEC), new RegistryData(Registries.CONFIGURED_CARVER, ConfiguredWorldCarver.DIRECT_CODEC), new RegistryData(Registries.CONFIGURED_FEATURE, ConfiguredFeature.DIRECT_CODEC), new RegistryData(Registries.PLACED_FEATURE, PlacedFeature.DIRECT_CODEC), new RegistryData(Registries.STRUCTURE, Structure.DIRECT_CODEC), new RegistryData(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC), new RegistryData(Registries.PROCESSOR_LIST, StructureProcessorType.DIRECT_CODEC), new RegistryData(Registries.TEMPLATE_POOL, StructureTemplatePool.DIRECT_CODEC), 
/*  95 */         new RegistryData(Registries.NOISE_SETTINGS, NoiseGeneratorSettings.DIRECT_CODEC), new RegistryData(Registries.NOISE, NormalNoise.NoiseParameters.DIRECT_CODEC), new RegistryData(Registries.DENSITY_FUNCTION, DensityFunctions.DIRECT_CODEC), new RegistryData(Registries.WORLD_PRESET, WorldPreset.DIRECT_CODEC), new RegistryData(Registries.FLAT_LEVEL_GENERATOR_PRESET, FlatLevelGeneratorPreset.DIRECT_CODEC), new RegistryData(Registries.TRIM_PATTERN, TrimPattern.DIRECT_CODEC), new RegistryData(Registries.TRIM_MATERIAL, TrimMaterial.DIRECT_CODEC), new RegistryData(Registries.TRIAL_SPAWNER_CONFIG, TrialSpawnerConfig.DIRECT_CODEC), new RegistryData(Registries.WOLF_VARIANT, WolfVariant.DIRECT_CODEC, 
/*     */           
/* 114 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.WOLF_SOUND_VARIANT, WolfSoundVariant.DIRECT_CODEC, 
/* 115 */           RegistryValidator.nonEmpty()), 
/* 115 */         new RegistryData(Registries.PIG_VARIANT, PigVariant.DIRECT_CODEC, 
/* 116 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.PIG_SOUND_VARIANT, PigSoundVariant.DIRECT_CODEC, 
/* 117 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.FROG_VARIANT, FrogVariant.DIRECT_CODEC, 
/* 118 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.CAT_VARIANT, CatVariant.DIRECT_CODEC, 
/* 119 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.CAT_SOUND_VARIANT, CatSoundVariant.DIRECT_CODEC, 
/* 120 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.COW_VARIANT, CowVariant.DIRECT_CODEC, 
/* 121 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.COW_SOUND_VARIANT, CowSoundVariant.DIRECT_CODEC, 
/* 122 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.SULFUR_CUBE_ARCHETYPE, SulfurCubeArchetype.DIRECT_CODEC), new RegistryData(Registries.CHICKEN_VARIANT, ChickenVariant.DIRECT_CODEC, 
/*     */           
/* 124 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.CHICKEN_SOUND_VARIANT, ChickenSoundVariant.DIRECT_CODEC, 
/* 125 */           RegistryValidator.nonEmpty()), 
/* 125 */         new RegistryData(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.DIRECT_CODEC, 
/* 126 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.PAINTING_VARIANT, PaintingVariant.DIRECT_CODEC, 
/* 127 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.DAMAGE_TYPE, DamageType.DIRECT_CODEC), new RegistryData(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, MultiNoiseBiomeSourceParameterList.DIRECT_CODEC), new RegistryData(Registries.BANNER_PATTERN, BannerPattern.DIRECT_CODEC), new RegistryData(Registries.ENCHANTMENT, Enchantment.DIRECT_CODEC), new RegistryData(Registries.ENCHANTMENT_PROVIDER, EnchantmentProvider.DIRECT_CODEC), new RegistryData(Registries.JUKEBOX_SONG, JukeboxSong.DIRECT_CODEC), new RegistryData(Registries.INSTRUMENT, Instrument.DIRECT_CODEC), new RegistryData(Registries.TEST_ENVIRONMENT, TestEnvironmentDefinition.DIRECT_CODEC), 
/* 127 */         new RegistryData(Registries.TEST_INSTANCE, GameTestInstance.DIRECT_CODEC), new RegistryData(Registries.DIALOG, Dialog.DIRECT_CODEC), new RegistryData(Registries.WORLD_CLOCK, WorldClock.DIRECT_CODEC), new RegistryData(Registries.TIMELINE, Timeline.DIRECT_CODEC, Timeline::validateRegistry), new RegistryData(Registries.VILLAGER_TRADE, VillagerTrade.CODEC), new RegistryData(Registries.TRADE_SET, TradeSet.CODEC) });
/*     */   
/* 144 */   public static final List<RegistryData<?>> DIMENSION_REGISTRIES = List.of(new RegistryData(Registries.LEVEL_STEM, LevelStem.CODEC));
/*     */   
/* 148 */   public static final List<RegistryData<?>> SYNCHRONIZED_REGISTRIES = List.of((RegistryData<?>[])new RegistryData[] { 
/* 148 */         new RegistryData(Registries.BIOME, Biome.NETWORK_CODEC), new RegistryData(Registries.CHAT_TYPE, ChatType.DIRECT_CODEC), new RegistryData(Registries.TRIM_PATTERN, TrimPattern.DIRECT_CODEC), new RegistryData(Registries.TRIM_MATERIAL, TrimMaterial.DIRECT_CODEC), new RegistryData(Registries.WOLF_VARIANT, WolfVariant.NETWORK_CODEC, 
/*     */           
/* 153 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.WOLF_SOUND_VARIANT, WolfSoundVariant.NETWORK_CODEC, 
/* 154 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.PIG_VARIANT, PigVariant.NETWORK_CODEC, 
/* 155 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.PIG_SOUND_VARIANT, PigSoundVariant.NETWORK_CODEC, 
/* 156 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.FROG_VARIANT, FrogVariant.NETWORK_CODEC, 
/* 157 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.CAT_VARIANT, CatVariant.NETWORK_CODEC, 
/* 158 */           RegistryValidator.nonEmpty()), 
/* 158 */         new RegistryData(Registries.CAT_SOUND_VARIANT, CatSoundVariant.NETWORK_CODEC, 
/* 159 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.COW_SOUND_VARIANT, CowSoundVariant.DIRECT_CODEC, 
/* 160 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.COW_VARIANT, CowVariant.NETWORK_CODEC, 
/* 161 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.CHICKEN_SOUND_VARIANT, ChickenSoundVariant.DIRECT_CODEC, 
/* 162 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.CHICKEN_VARIANT, ChickenVariant.NETWORK_CODEC, 
/* 163 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.NETWORK_CODEC, 
/* 164 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.PAINTING_VARIANT, PaintingVariant.DIRECT_CODEC, 
/* 165 */           RegistryValidator.nonEmpty()), new RegistryData(Registries.SULFUR_CUBE_ARCHETYPE, SulfurCubeArchetype.DIRECT_CODEC), new RegistryData(Registries.DIMENSION_TYPE, DimensionType.NETWORK_CODEC), new RegistryData(Registries.DAMAGE_TYPE, DamageType.DIRECT_CODEC), 
/* 165 */         new RegistryData(Registries.BANNER_PATTERN, BannerPattern.DIRECT_CODEC), new RegistryData(Registries.ENCHANTMENT, Enchantment.DIRECT_CODEC), new RegistryData(Registries.JUKEBOX_SONG, JukeboxSong.DIRECT_CODEC), new RegistryData(Registries.INSTRUMENT, Instrument.DIRECT_CODEC), new RegistryData(Registries.TEST_ENVIRONMENT, TestEnvironmentDefinition.DIRECT_CODEC), new RegistryData(Registries.TEST_INSTANCE, GameTestInstance.DIRECT_CODEC), new RegistryData(Registries.DIALOG, Dialog.DIRECT_CODEC), new RegistryData(Registries.WORLD_CLOCK, WorldClock.DIRECT_CODEC), new RegistryData(Registries.TIMELINE, Timeline.NETWORK_CODEC) });
/*     */   
/*     */   public static CompletableFuture<RegistryAccess.Frozen> load(final ResourceManager resourceManager, List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryData<?>> registriesToLoad, Executor executor) {
/* 186 */     LoaderFactory loaderFactory = new LoaderFactory() {
/*     */         public <T> RegistryLoadTask<T> create(RegistryDataLoader.RegistryData<T> data, Map<ResourceKey<?>, Exception> loadingErrors) {
/* 189 */           return new ResourceManagerRegistryLoadTask<>(data, Lifecycle.stable(), loadingErrors, resourceManager);
/*     */         }
/*     */       };
/* 192 */     return load(loaderFactory, contextRegistries, registriesToLoad, executor);
/*     */   }
/*     */   
/*     */   public static final class NetworkedRegistryData extends Record {
/*     */     private final List<RegistrySynchronization.PackedRegistryEntry> elements;
/*     */     
/*     */     private final TagNetworkSerialization.NetworkPayload tags;
/*     */     
/*     */     public NetworkedRegistryData(List<RegistrySynchronization.PackedRegistryEntry> elements, TagNetworkSerialization.NetworkPayload tags) {
/* 195 */       this.elements = elements;
/* 195 */       this.tags = tags;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #195	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #195	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #195	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<RegistrySynchronization.PackedRegistryEntry> elements() {
/* 195 */       return this.elements;
/*     */     }
/*     */     
/*     */     public TagNetworkSerialization.NetworkPayload tags() {
/* 195 */       return this.tags;
/*     */     }
/*     */   }
/*     */   
/*     */   public static CompletableFuture<RegistryAccess.Frozen> load(final Map<ResourceKey<? extends Registry<?>>, NetworkedRegistryData> entries, final ResourceProvider knownDataSource, List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryData<?>> registriesToLoad, Executor executor) {
/* 202 */     LoaderFactory loaderFactory = new LoaderFactory() {
/*     */         public <T> RegistryLoadTask<T> create(RegistryDataLoader.RegistryData<T> data, Map<ResourceKey<?>, Exception> loadingErrors) {
/* 205 */           return new NetworkRegistryLoadTask<>(data, Lifecycle.stable(), loadingErrors, entries, knownDataSource);
/*     */         }
/*     */       };
/* 208 */     return load(loaderFactory, contextRegistries, registriesToLoad, executor);
/*     */   }
/*     */   
/*     */   private static CompletableFuture<RegistryAccess.Frozen> load(LoaderFactory loaderFactory, List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryData<?>> registriesToLoad, Executor executor) {
/* 212 */     return CompletableFuture.supplyAsync(() -> {
/*     */           Map<ResourceKey<?>, Exception> loadingErrors = new ConcurrentHashMap<>();
/*     */           List<RegistryLoadTask<?>> loadTasks = (List<RegistryLoadTask<?>>)registriesToLoad.stream().map(()).collect(Collectors.toUnmodifiableList());
/*     */           RegistryOps.RegistryInfoLookup contextAndNewRegistries = createContext(contextRegistries, loadTasks);
/*     */           int taskCount = loadTasks.size();
/*     */           CompletableFuture[] arrayOfCompletableFuture = new CompletableFuture[taskCount];
/*     */           for (int i = 0; i < taskCount; i++)
/*     */             arrayOfCompletableFuture[i] = ((RegistryLoadTask)loadTasks.get(i)).load(contextAndNewRegistries, executor); 
/*     */           return CompletableFuture.allOf((CompletableFuture<?>[])arrayOfCompletableFuture).thenApplyAsync((), executor);
/* 212 */         }executor)
/*     */       
/* 239 */       .thenCompose(c -> c);
/*     */   }
/*     */   
/*     */   private static RegistryOps.RegistryInfoLookup createContext(List<HolderLookup.RegistryLookup<?>> contextRegistries, List<RegistryLoadTask<?>> newRegistriesAndLoaders) {
/* 243 */     final Map<ResourceKey<? extends Registry<?>>, RegistryOps.RegistryInfo<?>> result = new HashMap<>();
/* 245 */     contextRegistries.forEach(e -> result.put(e.key(), createInfoForContextRegistry(e)));
/* 246 */     newRegistriesAndLoaders.forEach(e -> result.put(e.registryKey(), e.createRegistryInfo()));
/* 249 */     return new RegistryOps.RegistryInfoLookup() {
/*     */         public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 253 */           return Optional.ofNullable((RegistryOps.RegistryInfo<T>)result.get(key));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static <T> RegistryOps.RegistryInfo<T> createInfoForContextRegistry(HolderLookup.RegistryLookup<T> lookup) {
/* 259 */     return new RegistryOps.RegistryInfo<>((HolderOwner<T>)lookup, (HolderGetter<T>)lookup, lookup.registryLifecycle());
/*     */   }
/*     */   
/*     */   private static ReportedException logErrors(Map<ResourceKey<?>, Exception> loadingErrors) {
/* 264 */     printFullDetailsToLog(loadingErrors);
/* 265 */     return createReportWithBriefInfo(loadingErrors);
/*     */   }
/*     */   
/*     */   private static void printFullDetailsToLog(Map<ResourceKey<?>, Exception> loadingErrors) {
/* 269 */     StringWriter collectedErrors = new StringWriter();
/* 270 */     PrintWriter errorPrinter = new PrintWriter(collectedErrors);
/* 271 */     Map<Identifier, Map<Identifier, Exception>> errorsByRegistry = (Map<Identifier, Map<Identifier, Exception>>)loadingErrors.entrySet().stream().collect(Collectors.groupingBy(e -> ((ResourceKey)e.getKey()).registry(), Collectors.toMap(e -> ((ResourceKey)e.getKey()).identifier(), Map.Entry::getValue)));
/* 272 */     errorsByRegistry.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(registryEntry -> {
/*     */           errorPrinter.printf(Locale.ROOT, "> Errors in registry %s:%n", new Object[] { registryEntry.getKey() });
/*     */           ((Map)registryEntry.getValue()).entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(());
/*     */         });
/* 279 */     errorPrinter.flush();
/* 280 */     LOGGER.error("Registry loading errors:\n{}", collectedErrors);
/*     */   }
/*     */   
/*     */   private static ReportedException createReportWithBriefInfo(Map<ResourceKey<?>, Exception> loadingErrors) {
/* 284 */     CrashReport report = CrashReport.forThrowable(new IllegalStateException("Failed to load registries due to errors"), "Registry Loading");
/* 285 */     CrashReportCategory errors = report.addCategory("Loading info");
/* 286 */     errors.setDetail("Errors", () -> {
/*     */           StringBuilder briefDetails = new StringBuilder();
/*     */           loadingErrors.entrySet().stream().sorted(Map.Entry.comparingByKey(ERROR_KEY_COMPARATOR)).forEach(());
/*     */           return briefDetails.toString();
/*     */         });
/* 300 */     return new ReportedException(report);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface LoaderFactory {
/*     */     <T> RegistryLoadTask<T> create(RegistryDataLoader.RegistryData<T> param1RegistryData, Map<ResourceKey<?>, Exception> param1Map);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryDataLoader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */