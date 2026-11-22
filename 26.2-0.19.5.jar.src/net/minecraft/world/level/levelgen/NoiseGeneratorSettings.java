/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.SurfaceRuleData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.biome.OverworldBiomeBuilder;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public final class NoiseGeneratorSettings extends Record {
/*     */   private final NoiseSettings noiseSettings;
/*     */   
/*     */   private final BlockState defaultBlock;
/*     */   
/*     */   private final BlockState defaultFluid;
/*     */   
/*     */   private final NoiseRouter noiseRouter;
/*     */   
/*     */   private final SurfaceRules.RuleSource surfaceRule;
/*     */   
/*     */   private final List<Climate.ParameterPoint> spawnTarget;
/*     */   
/*     */   private final int seaLevel;
/*     */   
/*     */   private final boolean disableMobGeneration;
/*     */   
/*     */   private final boolean aquifersEnabled;
/*     */   
/*     */   private final boolean oreVeinsEnabled;
/*     */   
/*     */   private final boolean useLegacyRandomSource;
/*     */   
/*     */   public static final Codec<NoiseGeneratorSettings> DIRECT_CODEC;
/*     */   
/*     */   public NoiseGeneratorSettings(NoiseSettings noiseSettings, BlockState defaultBlock, BlockState defaultFluid, NoiseRouter noiseRouter, SurfaceRules.RuleSource surfaceRule, List<Climate.ParameterPoint> spawnTarget, int seaLevel, boolean disableMobGeneration, boolean aquifersEnabled, boolean oreVeinsEnabled, boolean useLegacyRandomSource) {
/*  21 */     this.noiseSettings = noiseSettings;
/*  21 */     this.defaultBlock = defaultBlock;
/*  21 */     this.defaultFluid = defaultFluid;
/*  21 */     this.noiseRouter = noiseRouter;
/*  21 */     this.surfaceRule = surfaceRule;
/*  21 */     this.spawnTarget = spawnTarget;
/*  21 */     this.seaLevel = seaLevel;
/*  21 */     this.disableMobGeneration = disableMobGeneration;
/*  21 */     this.aquifersEnabled = aquifersEnabled;
/*  21 */     this.oreVeinsEnabled = oreVeinsEnabled;
/*  21 */     this.useLegacyRandomSource = useLegacyRandomSource;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public NoiseSettings noiseSettings() {
/*  21 */     return this.noiseSettings;
/*     */   }
/*     */   
/*     */   public BlockState defaultBlock() {
/*  21 */     return this.defaultBlock;
/*     */   }
/*     */   
/*     */   public BlockState defaultFluid() {
/*  21 */     return this.defaultFluid;
/*     */   }
/*     */   
/*     */   public NoiseRouter noiseRouter() {
/*  21 */     return this.noiseRouter;
/*     */   }
/*     */   
/*     */   public SurfaceRules.RuleSource surfaceRule() {
/*  21 */     return this.surfaceRule;
/*     */   }
/*     */   
/*     */   public List<Climate.ParameterPoint> spawnTarget() {
/*  21 */     return this.spawnTarget;
/*     */   }
/*     */   
/*     */   public int seaLevel() {
/*  21 */     return this.seaLevel;
/*     */   }
/*     */   
/*     */   public boolean aquifersEnabled() {
/*  21 */     return this.aquifersEnabled;
/*     */   }
/*     */   
/*     */   public boolean useLegacyRandomSource() {
/*  21 */     return this.useLegacyRandomSource;
/*     */   }
/*     */   
/*     */   static {
/*  34 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)NoiseSettings.CODEC.fieldOf("noise").forGetter(NoiseGeneratorSettings::noiseSettings), (App)BlockState.CODEC.fieldOf("default_block").forGetter(NoiseGeneratorSettings::defaultBlock), (App)BlockState.CODEC.fieldOf("default_fluid").forGetter(NoiseGeneratorSettings::defaultFluid), (App)NoiseRouter.CODEC.fieldOf("noise_router").forGetter(NoiseGeneratorSettings::noiseRouter), (App)SurfaceRules.RuleSource.CODEC.fieldOf("surface_rule").forGetter(NoiseGeneratorSettings::surfaceRule), (App)Climate.ParameterPoint.CODEC.listOf().fieldOf("spawn_target").forGetter(NoiseGeneratorSettings::spawnTarget), (App)Codec.INT.fieldOf("sea_level").forGetter(NoiseGeneratorSettings::seaLevel), (App)Codec.BOOL.fieldOf("disable_mob_generation").forGetter(NoiseGeneratorSettings::disableMobGeneration), (App)Codec.BOOL.fieldOf("aquifers_enabled").forGetter(NoiseGeneratorSettings::isAquifersEnabled), (App)Codec.BOOL.fieldOf("ore_veins_enabled").forGetter(NoiseGeneratorSettings::oreVeinsEnabled), (App)Codec.BOOL.fieldOf("legacy_random_source").forGetter(NoiseGeneratorSettings::useLegacyRandomSource)).apply((Applicative)i, NoiseGeneratorSettings::new));
/*     */   }
/*     */   
/*  48 */   public static final Codec<Holder<NoiseGeneratorSettings>> CODEC = (Codec<Holder<NoiseGeneratorSettings>>)RegistryFileCodec.create(Registries.NOISE_SETTINGS, DIRECT_CODEC);
/*     */   
/*     */   @Deprecated
/*     */   public boolean disableMobGeneration() {
/*  53 */     return this.disableMobGeneration;
/*     */   }
/*     */   
/*     */   public boolean isAquifersEnabled() {
/*  57 */     return (this.aquifersEnabled && !SharedConstants.DEBUG_DISABLE_AQUIFERS);
/*     */   }
/*     */   
/*     */   public boolean oreVeinsEnabled() {
/*  62 */     return (this.oreVeinsEnabled && !SharedConstants.DEBUG_DISABLE_ORE_VEINS);
/*     */   }
/*     */   
/*     */   public WorldgenRandom.Algorithm getRandomSource() {
/*  66 */     return this.useLegacyRandomSource ? WorldgenRandom.Algorithm.LEGACY : WorldgenRandom.Algorithm.XOROSHIRO;
/*     */   }
/*     */   
/*  69 */   public static final ResourceKey<NoiseGeneratorSettings> OVERWORLD = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("overworld"));
/*     */   
/*  70 */   public static final ResourceKey<NoiseGeneratorSettings> LARGE_BIOMES = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("large_biomes"));
/*     */   
/*  71 */   public static final ResourceKey<NoiseGeneratorSettings> AMPLIFIED = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("amplified"));
/*     */   
/*  72 */   public static final ResourceKey<NoiseGeneratorSettings> NETHER = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("nether"));
/*     */   
/*  73 */   public static final ResourceKey<NoiseGeneratorSettings> END = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("end"));
/*     */   
/*  74 */   public static final ResourceKey<NoiseGeneratorSettings> CAVES = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("caves"));
/*     */   
/*  75 */   public static final ResourceKey<NoiseGeneratorSettings> FLOATING_ISLANDS = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("floating_islands"));
/*     */   
/*     */   public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
/*  78 */     context.register(OVERWORLD, overworld(context, false, false));
/*  79 */     context.register(LARGE_BIOMES, overworld(context, false, true));
/*  80 */     context.register(AMPLIFIED, overworld(context, true, false));
/*  81 */     context.register(NETHER, nether(context));
/*  82 */     context.register(END, end(context));
/*  83 */     context.register(CAVES, caves(context));
/*  84 */     context.register(FLOATING_ISLANDS, floatingIslands(context));
/*     */   }
/*     */   
/*     */   private static NoiseGeneratorSettings end(BootstrapContext<?> context) {
/*  88 */     return new NoiseGeneratorSettings(NoiseSettings.END_NOISE_SETTINGS, Blocks.END_STONE
/*     */         
/*  90 */         .defaultBlockState(), Blocks.AIR
/*  91 */         .defaultBlockState(), 
/*  92 */         NoiseRouterData.end(context.lookup(Registries.DENSITY_FUNCTION)), 
/*  93 */         SurfaceRuleData.end(), 
/*  94 */         List.of(), 0, true, false, false, true);
/*     */   }
/*     */   
/*     */   private static NoiseGeneratorSettings nether(BootstrapContext<?> context) {
/* 104 */     return new NoiseGeneratorSettings(NoiseSettings.NETHER_NOISE_SETTINGS, Blocks.NETHERRACK
/*     */         
/* 106 */         .defaultBlockState(), Blocks.LAVA
/* 107 */         .defaultBlockState(), 
/* 108 */         NoiseRouterData.nether(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE)), 
/* 109 */         SurfaceRuleData.nether(context.lookup(Registries.BIOME)), 
/* 110 */         List.of(), 32, false, false, false, true);
/*     */   }
/*     */   
/*     */   private static NoiseGeneratorSettings overworld(BootstrapContext<?> context, boolean isAmplified, boolean largeBiomes) {
/* 120 */     return new NoiseGeneratorSettings(NoiseSettings.OVERWORLD_NOISE_SETTINGS, Blocks.STONE
/*     */         
/* 122 */         .defaultBlockState(), Blocks.WATER
/* 123 */         .defaultBlockState(), 
/* 124 */         NoiseRouterData.overworld(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE), largeBiomes, isAmplified), 
/* 125 */         SurfaceRuleData.overworld(context.lookup(Registries.BIOME)), (new OverworldBiomeBuilder())
/* 126 */         .spawnTarget(), 63, false, true, true, false);
/*     */   }
/*     */   
/*     */   private static NoiseGeneratorSettings caves(BootstrapContext<?> context) {
/* 136 */     return new NoiseGeneratorSettings(NoiseSettings.CAVES_NOISE_SETTINGS, Blocks.STONE
/*     */         
/* 138 */         .defaultBlockState(), Blocks.WATER
/* 139 */         .defaultBlockState(), 
/* 140 */         NoiseRouterData.caves(context.lookup(Registries.DENSITY_FUNCTION)), 
/* 141 */         SurfaceRuleData.overworldLike(context.lookup(Registries.BIOME), false, true, true), 
/* 142 */         List.of(), 32, false, false, false, true);
/*     */   }
/*     */   
/*     */   private static NoiseGeneratorSettings floatingIslands(BootstrapContext<?> context) {
/* 152 */     return new NoiseGeneratorSettings(NoiseSettings.FLOATING_ISLANDS_NOISE_SETTINGS, Blocks.STONE
/*     */         
/* 154 */         .defaultBlockState(), Blocks.WATER
/* 155 */         .defaultBlockState(), 
/* 156 */         NoiseRouterData.floatingIslands(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE)), 
/* 157 */         SurfaceRuleData.overworldLike(context.lookup(Registries.BIOME), false, false, false), 
/* 158 */         List.of(), -64, false, false, false, true);
/*     */   }
/*     */   
/*     */   public static NoiseGeneratorSettings dummy() {
/* 168 */     return new NoiseGeneratorSettings(NoiseSettings.OVERWORLD_NOISE_SETTINGS, Blocks.STONE
/*     */         
/* 170 */         .defaultBlockState(), Blocks.AIR
/* 171 */         .defaultBlockState(), 
/* 172 */         NoiseRouterData.none(), 
/* 173 */         SurfaceRuleData.air(), 
/* 174 */         List.of(), 63, true, false, false, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseGeneratorSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */