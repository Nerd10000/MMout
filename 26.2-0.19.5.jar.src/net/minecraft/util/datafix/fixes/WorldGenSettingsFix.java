/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicLike;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.lang3.math.NumberUtils;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class WorldGenSettingsFix extends DataFix {
/*     */   private static final String VILLAGE = "minecraft:village";
/*     */   
/*     */   private static final String DESERT_PYRAMID = "minecraft:desert_pyramid";
/*     */   
/*     */   private static final String IGLOO = "minecraft:igloo";
/*     */   
/*     */   private static final String JUNGLE_TEMPLE = "minecraft:jungle_pyramid";
/*     */   
/*     */   private static final String SWAMP_HUT = "minecraft:swamp_hut";
/*     */   
/*     */   private static final String PILLAGER_OUTPOST = "minecraft:pillager_outpost";
/*     */   
/*     */   private static final String END_CITY = "minecraft:endcity";
/*     */   
/*     */   private static final String WOODLAND_MANSION = "minecraft:mansion";
/*     */   
/*     */   private static final String OCEAN_MONUMENT = "minecraft:monument";
/*     */   
/*     */   public WorldGenSettingsFix(Schema parent) {
/*  28 */     super(parent, true);
/*     */   }
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  33 */     return fixTypeEverywhereTyped("WorldGenSettings building", getInputSchema().getType(References.WORLD_GEN_SETTINGS), settings -> settings.update(DSL.remainderFinder(), WorldGenSettingsFix::fix));
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> noise(long seed, DynamicLike<T> input, Dynamic<T> noiseGeneratorSettings, Dynamic<T> biomeSource) {
/*  37 */     return input.createMap((Map)ImmutableMap.of(input
/*  38 */           .createString("type"), input.createString("minecraft:noise"), input
/*  39 */           .createString("biome_source"), biomeSource, input
/*  40 */           .createString("seed"), input.createLong(seed), input
/*  41 */           .createString("settings"), noiseGeneratorSettings));
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> vanillaBiomeSource(Dynamic<T> input, long seed, boolean legacyBiomeInitLayer, boolean largeBiomes) {
/*  49 */     ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> builder = ImmutableMap.builder().put(input.createString("type"), input.createString("minecraft:vanilla_layered")).put(input.createString("seed"), input.createLong(seed)).put(input.createString("large_biomes"), input.createBoolean(largeBiomes));
/*  51 */     if (legacyBiomeInitLayer)
/*  52 */       builder.put(input.createString("legacy_biome_init_layer"), input.createBoolean(legacyBiomeInitLayer)); 
/*  55 */     return input.createMap((Map)builder.build());
/*     */   }
/*     */   
/*  68 */   private static final ImmutableMap<String, StructureFeatureConfiguration> DEFAULTS = ImmutableMap.builder()
/*  69 */     .put("minecraft:village", new StructureFeatureConfiguration(32, 8, 10387312))
/*  70 */     .put("minecraft:desert_pyramid", new StructureFeatureConfiguration(32, 8, 14357617))
/*  71 */     .put("minecraft:igloo", new StructureFeatureConfiguration(32, 8, 14357618))
/*  72 */     .put("minecraft:jungle_pyramid", new StructureFeatureConfiguration(32, 8, 14357619))
/*  73 */     .put("minecraft:swamp_hut", new StructureFeatureConfiguration(32, 8, 14357620))
/*  74 */     .put("minecraft:pillager_outpost", new StructureFeatureConfiguration(32, 8, 165745296))
/*  75 */     .put("minecraft:monument", new StructureFeatureConfiguration(32, 5, 10387313))
/*  76 */     .put("minecraft:endcity", new StructureFeatureConfiguration(20, 11, 10387313))
/*  77 */     .put("minecraft:mansion", new StructureFeatureConfiguration(80, 20, 10387319))
/*  78 */     .build();
/*     */   
/*     */   private static final class StructureFeatureConfiguration {
/*     */     public static final Codec<StructureFeatureConfiguration> CODEC;
/*     */     
/*     */     private final int spacing;
/*     */     
/*     */     private final int separation;
/*     */     
/*     */     private final int salt;
/*     */     
/*     */     static {
/*  81 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("spacing").forGetter(()), (App)Codec.INT.fieldOf("separation").forGetter(()), (App)Codec.INT.fieldOf("salt").forGetter(())).apply((Applicative)i, StructureFeatureConfiguration::new));
/*     */     }
/*     */     
/*     */     public StructureFeatureConfiguration(int spacing, int separation, int salt) {
/*  92 */       this.spacing = spacing;
/*  93 */       this.separation = separation;
/*  94 */       this.salt = salt;
/*     */     }
/*     */     
/*     */     public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
/*  98 */       return new Dynamic(ops, CODEC.encodeStart(ops, this).result().orElse(ops.emptyMap()));
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fix(Dynamic<T> input) {
/*     */     Dynamic<T> generator;
/* 103 */     DynamicOps<T> ops = input.getOps();
/* 105 */     long seed = input.get("RandomSeed").asLong(0L);
/* 107 */     Optional<String> name = input.get("generatorName").asString().map(n -> n.toLowerCase(Locale.ROOT)).result();
/* 109 */     Optional<String> legacyCustomOptions = input.get("legacy_custom_options").asString().result().map(Optional::of).orElseGet(() -> name.equals(Optional.of("customized")) ? input.get("generatorOptions").asString().result() : Optional.empty());
/* 116 */     boolean caves = false;
/* 117 */     if (name.equals(Optional.of("customized"))) {
/* 118 */       generator = defaultOverworld(input, seed);
/* 119 */     } else if (name.isEmpty()) {
/* 120 */       generator = defaultOverworld(input, seed);
/*     */     } else {
/*     */       String str;
/*     */       boolean generateMapFeatures;
/*     */       byte b;
/*     */       boolean generateBonusChest;
/*     */       OptionalDynamic<T> flatSettings;
/*     */       ImmutableMap.Builder<T, T> builder3;
/*     */       ImmutableMap.Builder builder2;
/*     */       OptionalDynamic<T> settings;
/*     */       ImmutableMap.Builder builder1;
/*     */       Map<Dynamic<T>, Dynamic<T>> structureBuilder;
/*     */       OptionalDynamic<?> chunkGeneratorObject;
/*     */       Optional<String> type;
/*     */       Dynamic<T> noiseGeneratorSettings, biomeSource, fixedSource;
/* 122 */       switch ((String)name.get()) {
/*     */         case "flat":
/* 124 */           flatSettings = input.get("generatorOptions");
/* 125 */           structureBuilder = fixFlatStructures(ops, flatSettings);
/* 127 */           generator = input.createMap((Map)ImmutableMap.of(input
/* 128 */                 .createString("type"), input.createString("minecraft:flat"), input
/* 129 */                 .createString("settings"), input.createMap((Map)ImmutableMap.of(input
/* 130 */                     .createString("structures"), input.createMap(structureBuilder), input
/* 131 */                     .createString("layers"), flatSettings.get("layers").result().orElseGet(() -> input.createList(Stream.of(new Dynamic[] { input.createMap((Map)ImmutableMap.of(input.createString("height"), input.createInt(1), input.createString("block"), input.createString("minecraft:bedrock"))), input.createMap((Map)ImmutableMap.of(input.createString("height"), input.createInt(2), input.createString("block"), input.createString("minecraft:dirt"))), input.createMap((Map)ImmutableMap.of(input.createString("height"), input.createInt(1), input.createString("block"), input.createString("minecraft:grass_block"))) }))), input
/*     */                     
/* 145 */                     .createString("biome"), input.createString(flatSettings.get("biome").asString("minecraft:plains"))))));
/* 199 */           generateMapFeatures = input.get("MapFeatures").asBoolean(true);
/* 200 */           generateBonusChest = input.get("BonusChest").asBoolean(false);
/* 202 */           builder3 = ImmutableMap.builder();
/* 203 */           builder3.put(ops.createString("seed"), ops.createLong(seed));
/* 204 */           builder3.put(ops.createString("generate_features"), ops.createBoolean(generateMapFeatures));
/* 205 */           builder3.put(ops.createString("bonus_chest"), ops.createBoolean(generateBonusChest));
/* 206 */           builder3.put(ops.createString("dimensions"), vanillaLevels(input, seed, generator, caves));
/* 207 */           legacyCustomOptions.ifPresent(o -> builder.put(ops.createString("legacy_custom_options"), ops.createString(o)));
/* 209 */           return new Dynamic(ops, ops.createMap((Map)builder3.build()));
/*     */         case "debug_all_block_states":
/*     */           generator = input.createMap((Map)ImmutableMap.of(input.createString("type"), input.createString("minecraft:debug")));
/*     */           generateMapFeatures = input.get("MapFeatures").asBoolean(true);
/*     */           generateBonusChest = input.get("BonusChest").asBoolean(false);
/*     */           builder2 = ImmutableMap.builder();
/*     */           builder2.put(ops.createString("seed"), ops.createLong(seed));
/*     */           builder2.put(ops.createString("generate_features"), ops.createBoolean(generateMapFeatures));
/*     */           builder2.put(ops.createString("bonus_chest"), ops.createBoolean(generateBonusChest));
/*     */           builder2.put(ops.createString("dimensions"), vanillaLevels(input, seed, generator, caves));
/*     */           legacyCustomOptions.ifPresent(o -> builder.put(ops.createString("legacy_custom_options"), ops.createString(o)));
/* 209 */           return new Dynamic(ops, ops.createMap((Map)builder2.build()));
/*     */         case "buffet":
/*     */           settings = input.get("generatorOptions");
/*     */           chunkGeneratorObject = settings.get("chunk_generator");
/*     */           type = chunkGeneratorObject.get("type").asString().result();
/*     */           if (Objects.equals(type, Optional.of("minecraft:caves"))) {
/*     */             noiseGeneratorSettings = input.createString("minecraft:caves");
/*     */             caves = true;
/*     */           } else if (Objects.equals(type, Optional.of("minecraft:floating_islands"))) {
/*     */             noiseGeneratorSettings = input.createString("minecraft:floating_islands");
/*     */           } else {
/*     */             noiseGeneratorSettings = input.createString("minecraft:overworld");
/*     */           } 
/*     */           biomeSource = settings.get("biome_source").result().orElseGet(() -> input.createMap((Map)ImmutableMap.of(input.createString("type"), input.createString("minecraft:fixed"))));
/*     */           if (biomeSource.get("type").asString().result().equals(Optional.of("minecraft:fixed"))) {
/*     */             String biome = biomeSource.get("options").get("biomes").asStream().findFirst().flatMap(b -> b.asString().result()).orElse("minecraft:ocean");
/*     */             fixedSource = biomeSource.remove("options").set("biome", input.createString(biome));
/*     */           } else {
/*     */             fixedSource = biomeSource;
/*     */           } 
/*     */           generator = noise(seed, (DynamicLike<T>)input, noiseGeneratorSettings, fixedSource);
/*     */           generateMapFeatures = input.get("MapFeatures").asBoolean(true);
/*     */           generateBonusChest = input.get("BonusChest").asBoolean(false);
/*     */           builder1 = ImmutableMap.builder();
/*     */           builder1.put(ops.createString("seed"), ops.createLong(seed));
/*     */           builder1.put(ops.createString("generate_features"), ops.createBoolean(generateMapFeatures));
/*     */           builder1.put(ops.createString("bonus_chest"), ops.createBoolean(generateBonusChest));
/*     */           builder1.put(ops.createString("dimensions"), vanillaLevels(input, seed, generator, caves));
/*     */           legacyCustomOptions.ifPresent(o -> builder.put(ops.createString("legacy_custom_options"), ops.createString(o)));
/* 209 */           return new Dynamic(ops, ops.createMap((Map)builder1.build()));
/*     */       } 
/*     */       boolean bool3 = ((String)name.get()).equals("default");
/*     */       boolean legacyBiomeInitLayer = (((String)name.get()).equals("default_1_1") || (bool3 && input.get("generatorVersion").asInt(0) == 0));
/*     */       boolean isAmplified = ((String)name.get()).equals("amplified");
/*     */       boolean largeBiomes = ((String)name.get()).equals("largebiomes");
/*     */       generator = noise(seed, (DynamicLike<T>)input, input.createString(isAmplified ? "minecraft:amplified" : "minecraft:overworld"), vanillaBiomeSource(input, seed, legacyBiomeInitLayer, largeBiomes));
/*     */     } 
/*     */     boolean bool1 = input.get("MapFeatures").asBoolean(true);
/*     */     boolean bool2 = input.get("BonusChest").asBoolean(false);
/*     */     ImmutableMap.Builder builder = ImmutableMap.builder();
/*     */     builder.put(ops.createString("seed"), ops.createLong(seed));
/*     */     builder.put(ops.createString("generate_features"), ops.createBoolean(bool1));
/*     */     builder.put(ops.createString("bonus_chest"), ops.createBoolean(bool2));
/*     */     builder.put(ops.createString("dimensions"), vanillaLevels(input, seed, generator, caves));
/*     */     legacyCustomOptions.ifPresent(o -> builder.put(ops.createString("legacy_custom_options"), ops.createString(o)));
/* 209 */     return new Dynamic(ops, ops.createMap((Map)builder.build()));
/*     */   }
/*     */   
/*     */   protected static <T> Dynamic<T> defaultOverworld(Dynamic<T> input, long seed) {
/* 213 */     return noise(seed, (DynamicLike<T>)input, input.createString("minecraft:overworld"), vanillaBiomeSource(input, seed, false, false));
/*     */   }
/*     */   
/*     */   protected static <T> T vanillaLevels(Dynamic<T> input, long seed, Dynamic<T> overworldGenerator, boolean caves) {
/* 217 */     DynamicOps<T> ops = input.getOps();
/* 218 */     return (T)ops.createMap((Map)ImmutableMap.of(ops
/* 219 */           .createString("minecraft:overworld"), ops.createMap((Map)ImmutableMap.of(ops
/* 220 */               .createString("type"), ops.createString("minecraft:overworld" + (caves ? "_caves" : "")), ops
/* 221 */               .createString("generator"), overworldGenerator.getValue())), ops
/*     */           
/* 223 */           .createString("minecraft:the_nether"), ops.createMap((Map)ImmutableMap.of(ops
/* 224 */               .createString("type"), ops.createString("minecraft:the_nether"), ops
/* 225 */               .createString("generator"), noise(seed, (DynamicLike<T>)input, input.createString("minecraft:nether"), input.createMap((Map)ImmutableMap.of(input
/* 226 */                     .createString("type"), input.createString("minecraft:multi_noise"), input
/* 227 */                     .createString("seed"), input.createLong(seed), input
/* 228 */                     .createString("preset"), input.createString("minecraft:nether"))))
/* 229 */               .getValue())), ops
/*     */           
/* 231 */           .createString("minecraft:the_end"), ops.createMap((Map)ImmutableMap.of(ops
/* 232 */               .createString("type"), ops.createString("minecraft:the_end"), ops
/* 233 */               .createString("generator"), noise(seed, (DynamicLike<T>)input, input.createString("minecraft:end"), input.createMap((Map)ImmutableMap.of(input
/* 234 */                     .createString("type"), input.createString("minecraft:the_end"), input
/* 235 */                     .createString("seed"), input.createLong(seed))))
/* 236 */               .getValue()))));
/*     */   }
/*     */   
/*     */   private static <T> Map<Dynamic<T>, Dynamic<T>> fixFlatStructures(DynamicOps<T> ops, OptionalDynamic<T> settings) {
/* 242 */     MutableInt strongholdDistance = new MutableInt(32);
/* 243 */     MutableInt strongholdSpread = new MutableInt(3);
/* 244 */     MutableInt strongholdCount = new MutableInt(128);
/* 245 */     MutableBoolean hasStronghold = new MutableBoolean(false);
/* 246 */     Map<String, StructureFeatureConfiguration> structureConfig = Maps.newHashMap();
/* 248 */     if (settings.result().isEmpty()) {
/* 249 */       hasStronghold.setTrue();
/* 250 */       structureConfig.put("minecraft:village", (StructureFeatureConfiguration)DEFAULTS.get("minecraft:village"));
/*     */     } 
/* 253 */     settings.get("structures").flatMap(Dynamic::getMapValues).ifSuccess(map -> map.forEach(()));
/* 324 */     ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> structureBuilder = ImmutableMap.builder();
/* 325 */     structureBuilder.put(settings.createString("structures"), settings.createMap((Map)structureConfig.entrySet().stream().collect(Collectors.toMap(e -> settings.createString((String)e.getKey()), e -> ((StructureFeatureConfiguration)e.getValue()).serialize(ops)))));
/* 329 */     if (hasStronghold.isTrue())
/* 330 */       structureBuilder.put(settings.createString("stronghold"), settings.createMap((Map)ImmutableMap.of(settings
/* 331 */               .createString("distance"), settings.createInt(strongholdDistance.intValue()), settings
/* 332 */               .createString("spread"), settings.createInt(strongholdSpread.intValue()), settings
/* 333 */               .createString("count"), settings.createInt(strongholdCount.intValue())))); 
/* 336 */     return (Map<Dynamic<T>, Dynamic<T>>)structureBuilder.build();
/*     */   }
/*     */   
/*     */   private static int getInt(String input, int def) {
/* 340 */     return NumberUtils.toInt(input, def);
/*     */   }
/*     */   
/*     */   private static int getInt(String input, int def, int min) {
/* 344 */     return Math.max(min, getInt(input, def));
/*     */   }
/*     */   
/*     */   private static void setSpacing(Map<String, StructureFeatureConfiguration> structureConfig, String structure, String optionValue, int min) {
/* 348 */     StructureFeatureConfiguration config = structureConfig.getOrDefault(structure, (StructureFeatureConfiguration)DEFAULTS.get(structure));
/* 349 */     int spacing = getInt(optionValue, config.spacing, min);
/* 350 */     structureConfig.put(structure, new StructureFeatureConfiguration(spacing, config.separation, config.salt));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\WorldGenSettingsFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */