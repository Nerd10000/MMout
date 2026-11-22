/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*     */ import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public abstract class Structure {
/*  48 */   public static final Codec<Structure> DIRECT_CODEC = BuiltInRegistries.STRUCTURE_TYPE.byNameCodec().dispatch(Structure::type, StructureType::codec);
/*     */   
/*  49 */   public static final Codec<Holder<Structure>> CODEC = (Codec<Holder<Structure>>)RegistryFileCodec.create(Registries.STRUCTURE, DIRECT_CODEC);
/*     */   
/*     */   protected final StructureSettings settings;
/*     */   
/*     */   public static final class StructureSettings extends Record {
/*     */     private final HolderSet<Biome> biomes;
/*     */     
/*     */     private final Map<MobCategory, StructureSpawnOverride> spawnOverrides;
/*     */     
/*     */     private final GenerationStep.Decoration step;
/*     */     
/*     */     private final TerrainAdjustment terrainAdaptation;
/*     */     
/*     */     public StructureSettings(HolderSet<Biome> biomes, Map<MobCategory, StructureSpawnOverride> spawnOverrides, GenerationStep.Decoration step, TerrainAdjustment terrainAdaptation) {
/*  51 */       this.biomes = biomes;
/*  51 */       this.spawnOverrides = spawnOverrides;
/*  51 */       this.step = step;
/*  51 */       this.terrainAdaptation = terrainAdaptation;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #51	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #51	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #51	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public HolderSet<Biome> biomes() {
/*  51 */       return this.biomes;
/*     */     }
/*     */     
/*     */     public Map<MobCategory, StructureSpawnOverride> spawnOverrides() {
/*  51 */       return this.spawnOverrides;
/*     */     }
/*     */     
/*     */     public GenerationStep.Decoration step() {
/*  51 */       return this.step;
/*     */     }
/*     */     
/*     */     public TerrainAdjustment terrainAdaptation() {
/*  51 */       return this.terrainAdaptation;
/*     */     }
/*     */     
/*  57 */     private static final StructureSettings DEFAULT = new StructureSettings(HolderSet.empty(), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE);
/*     */     
/*     */     public static final MapCodec<StructureSettings> CODEC;
/*     */     
/*     */     static {
/*  58 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(StructureSettings::biomes), (App)Codec.simpleMap(MobCategory.CODEC, StructureSpawnOverride.CODEC, StringRepresentable.keys((StringRepresentable[])MobCategory.values())).fieldOf("spawn_overrides").forGetter(StructureSettings::spawnOverrides), (App)GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(StructureSettings::step), (App)TerrainAdjustment.CODEC.optionalFieldOf("terrain_adaptation", DEFAULT.terrainAdaptation).forGetter(StructureSettings::terrainAdaptation)).apply((Applicative)i, StructureSettings::new));
/*     */     }
/*     */     
/*     */     public StructureSettings(HolderSet<Biome> biomes) {
/*  67 */       this(biomes, DEFAULT.spawnOverrides, DEFAULT.step, DEFAULT.terrainAdaptation);
/*     */     }
/*     */     
/*     */     public static class Builder {
/*     */       private final HolderSet<Biome> biomes;
/*     */       
/*  72 */       private Map<MobCategory, StructureSpawnOverride> spawnOverrides = Structure.StructureSettings.DEFAULT.spawnOverrides;
/*     */       
/*  73 */       private GenerationStep.Decoration step = Structure.StructureSettings.DEFAULT.step;
/*     */       
/*  74 */       private TerrainAdjustment terrainAdaption = Structure.StructureSettings.DEFAULT.terrainAdaptation;
/*     */       
/*     */       public Builder(HolderSet<Biome> biomes) {
/*  77 */         this.biomes = biomes;
/*     */       }
/*     */       
/*     */       public Builder spawnOverrides(Map<MobCategory, StructureSpawnOverride> spawnOverrides) {
/*  81 */         this.spawnOverrides = spawnOverrides;
/*  82 */         return this;
/*     */       }
/*     */       
/*     */       public Builder generationStep(GenerationStep.Decoration step) {
/*  86 */         this.step = step;
/*  87 */         return this;
/*     */       }
/*     */       
/*     */       public Builder terrainAdapation(TerrainAdjustment terrainAdaption) {
/*  91 */         this.terrainAdaption = terrainAdaption;
/*  92 */         return this;
/*     */       }
/*     */       
/*     */       public Structure.StructureSettings build() {
/*  96 */         return new Structure.StructureSettings(this.biomes, this.spawnOverrides, this.step, this.terrainAdaption);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final HolderSet<Biome> biomes;
/*     */     
/*     */     private Map<MobCategory, StructureSpawnOverride> spawnOverrides = Structure.StructureSettings.DEFAULT.spawnOverrides;
/*     */     
/*     */     private GenerationStep.Decoration step = Structure.StructureSettings.DEFAULT.step;
/*     */     
/*     */     private TerrainAdjustment terrainAdaption = Structure.StructureSettings.DEFAULT.terrainAdaptation;
/*     */     
/*     */     public Builder(HolderSet<Biome> biomes) {
/*     */       this.biomes = biomes;
/*     */     }
/*     */     
/*     */     public Builder spawnOverrides(Map<MobCategory, StructureSpawnOverride> spawnOverrides) {
/*     */       this.spawnOverrides = spawnOverrides;
/*     */       return this;
/*     */     }
/*     */     
/*     */     public Builder generationStep(GenerationStep.Decoration step) {
/*     */       this.step = step;
/*     */       return this;
/*     */     }
/*     */     
/*     */     public Builder terrainAdapation(TerrainAdjustment terrainAdaption) {
/*     */       this.terrainAdaption = terrainAdaption;
/*     */       return this;
/*     */     }
/*     */     
/*     */     public Structure.StructureSettings build() {
/*  96 */       return new Structure.StructureSettings(this.biomes, this.spawnOverrides, this.step, this.terrainAdaption);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <S extends Structure> RecordCodecBuilder<S, StructureSettings> settingsCodec(RecordCodecBuilder.Instance<S> i) {
/* 102 */     return StructureSettings.CODEC.forGetter(e -> e.settings);
/*     */   }
/*     */   
/*     */   public static <S extends Structure> MapCodec<S> simpleCodec(Function<StructureSettings, S> constructor) {
/* 106 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)settingsCodec(i)).apply((Applicative)i, constructor));
/*     */   }
/*     */   
/*     */   protected Structure(StructureSettings settings) {
/* 112 */     this.settings = settings;
/*     */   }
/*     */   
/*     */   public HolderSet<Biome> biomes() {
/* 116 */     return this.settings.biomes;
/*     */   }
/*     */   
/*     */   public Map<MobCategory, StructureSpawnOverride> spawnOverrides() {
/* 120 */     return this.settings.spawnOverrides;
/*     */   }
/*     */   
/*     */   public GenerationStep.Decoration step() {
/* 124 */     return this.settings.step;
/*     */   }
/*     */   
/*     */   public TerrainAdjustment terrainAdaptation() {
/* 128 */     return this.settings.terrainAdaptation;
/*     */   }
/*     */   
/*     */   public BoundingBox adjustBoundingBox(BoundingBox boundingBox) {
/* 132 */     if (terrainAdaptation() != TerrainAdjustment.NONE)
/* 133 */       return boundingBox.inflatedBy(12); 
/* 135 */     return boundingBox;
/*     */   }
/*     */   
/*     */   public static final class GenerationContext extends Record {
/*     */     private final RegistryAccess registryAccess;
/*     */     
/*     */     private final ChunkGenerator chunkGenerator;
/*     */     
/*     */     private final BiomeSource biomeSource;
/*     */     
/*     */     private final RandomState randomState;
/*     */     
/*     */     private final StructureTemplateManager structureTemplateManager;
/*     */     
/*     */     private final WorldgenRandom random;
/*     */     
/*     */     private final long seed;
/*     */     
/*     */     private final ChunkPos chunkPos;
/*     */     
/*     */     private final LevelHeightAccessor heightAccessor;
/*     */     
/*     */     private final Predicate<Holder<Biome>> validBiome;
/*     */     
/*     */     public GenerationContext(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, WorldgenRandom random, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome) {
/* 138 */       this.registryAccess = registryAccess;
/* 138 */       this.chunkGenerator = chunkGenerator;
/* 138 */       this.biomeSource = biomeSource;
/* 138 */       this.randomState = randomState;
/* 138 */       this.structureTemplateManager = structureTemplateManager;
/* 138 */       this.random = random;
/* 138 */       this.seed = seed;
/* 138 */       this.chunkPos = chunkPos;
/* 138 */       this.heightAccessor = heightAccessor;
/* 138 */       this.validBiome = validBiome;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public RegistryAccess registryAccess() {
/* 138 */       return this.registryAccess;
/*     */     }
/*     */     
/*     */     public ChunkGenerator chunkGenerator() {
/* 138 */       return this.chunkGenerator;
/*     */     }
/*     */     
/*     */     public BiomeSource biomeSource() {
/* 138 */       return this.biomeSource;
/*     */     }
/*     */     
/*     */     public RandomState randomState() {
/* 138 */       return this.randomState;
/*     */     }
/*     */     
/*     */     public StructureTemplateManager structureTemplateManager() {
/* 138 */       return this.structureTemplateManager;
/*     */     }
/*     */     
/*     */     public WorldgenRandom random() {
/* 138 */       return this.random;
/*     */     }
/*     */     
/*     */     public long seed() {
/* 138 */       return this.seed;
/*     */     }
/*     */     
/*     */     public ChunkPos chunkPos() {
/* 138 */       return this.chunkPos;
/*     */     }
/*     */     
/*     */     public LevelHeightAccessor heightAccessor() {
/* 138 */       return this.heightAccessor;
/*     */     }
/*     */     
/*     */     public Predicate<Holder<Biome>> validBiome() {
/* 138 */       return this.validBiome;
/*     */     }
/*     */     
/*     */     public GenerationContext(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome) {
/* 140 */       this(registryAccess, chunkGenerator, biomeSource, randomState, structureTemplateManager, makeRandom(seed, chunkPos), seed, chunkPos, heightAccessor, validBiome);
/*     */     }
/*     */     
/*     */     private static WorldgenRandom makeRandom(long seed, ChunkPos chunkPos) {
/* 144 */       WorldgenRandom random = new WorldgenRandom((RandomSource)new LegacyRandomSource(0L));
/* 145 */       random.setLargeFeatureSeed(seed, chunkPos.x(), chunkPos.z());
/* 146 */       return random;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class GenerationStub extends Record {
/*     */     private final BlockPos position;
/*     */     
/*     */     private final Either<Consumer<StructurePiecesBuilder>, StructurePiecesBuilder> generator;
/*     */     
/*     */     public GenerationStub(BlockPos position, Either<Consumer<StructurePiecesBuilder>, StructurePiecesBuilder> generator) {
/* 150 */       this.position = position;
/* 150 */       this.generator = generator;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #150	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #150	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #150	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockPos position() {
/* 150 */       return this.position;
/*     */     }
/*     */     
/*     */     public Either<Consumer<StructurePiecesBuilder>, StructurePiecesBuilder> generator() {
/* 150 */       return this.generator;
/*     */     }
/*     */     
/*     */     public GenerationStub(BlockPos position, Consumer<StructurePiecesBuilder> generator) {
/* 152 */       this(position, Either.left(generator));
/*     */     }
/*     */     
/*     */     public StructurePiecesBuilder getPiecesBuilder() {
/* 156 */       return (StructurePiecesBuilder)this.generator.map(pieceGenerator -> {
/*     */             StructurePiecesBuilder newBuilder = new StructurePiecesBuilder();
/*     */             pieceGenerator.accept(newBuilder);
/*     */             return newBuilder;
/*     */           }previousBuilder -> previousBuilder);
/*     */     }
/*     */   }
/*     */   
/*     */   public StructureStart generate(Holder<Structure> selected, ResourceKey<Level> dimension, RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, long seed, ChunkPos sourceChunkPos, int references, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome) {
/* 165 */     ProfiledDuration profiled = JvmProfiler.INSTANCE.onStructureGenerate(sourceChunkPos, dimension, selected);
/* 167 */     GenerationContext context = new GenerationContext(registryAccess, chunkGenerator, biomeSource, randomState, structureTemplateManager, seed, sourceChunkPos, heightAccessor, validBiome);
/* 168 */     Optional<GenerationStub> generation = findValidGenerationPoint(context);
/* 169 */     if (generation.isPresent()) {
/* 170 */       StructurePiecesBuilder builder = ((GenerationStub)generation.get()).getPiecesBuilder();
/* 172 */       StructureStart testStart = new StructureStart(this, sourceChunkPos, references, builder.build());
/* 173 */       if (testStart.isValid()) {
/* 174 */         if (profiled != null)
/* 175 */           profiled.finish(true); 
/* 177 */         return testStart;
/*     */       } 
/*     */     } 
/* 181 */     if (profiled != null)
/* 182 */       profiled.finish(false); 
/* 184 */     return StructureStart.INVALID_START;
/*     */   }
/*     */   
/*     */   protected static Optional<GenerationStub> onTopOfChunkCenter(GenerationContext context, Heightmap.Types heightmap, Consumer<StructurePiecesBuilder> generator) {
/* 188 */     ChunkPos chunkPos = context.chunkPos();
/* 189 */     int blockX = chunkPos.getMiddleBlockX();
/* 190 */     int blockZ = chunkPos.getMiddleBlockZ();
/* 191 */     int blockY = context.chunkGenerator().getFirstOccupiedHeight(blockX, blockZ, heightmap, context.heightAccessor(), context.randomState());
/* 192 */     return Optional.of(new GenerationStub(new BlockPos(blockX, blockY, blockZ), generator));
/*     */   }
/*     */   
/*     */   private static boolean isValidBiome(GenerationStub stub, GenerationContext context) {
/* 196 */     BlockPos startPos = stub.position();
/* 197 */     return context.validBiome.test(context.chunkGenerator.getBiomeSource().getNoiseBiome(QuartPos.fromBlock(startPos.getX()), QuartPos.fromBlock(startPos.getY()), QuartPos.fromBlock(startPos.getZ()), context.randomState.sampler()));
/*     */   }
/*     */   
/*     */   public void afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, PiecesContainer pieces) {}
/*     */   
/*     */   private static int[] getCornerHeights(GenerationContext context, int minX, int sizeX, int minZ, int sizeZ) {
/* 204 */     ChunkGenerator chunkGenerator = context.chunkGenerator();
/* 205 */     LevelHeightAccessor heightAccessor = context.heightAccessor();
/* 206 */     RandomState randomState = context.randomState();
/* 207 */     return new int[] { chunkGenerator
/* 208 */         .getFirstOccupiedHeight(minX, minZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState), chunkGenerator
/* 209 */         .getFirstOccupiedHeight(minX, minZ + sizeZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState), chunkGenerator
/* 210 */         .getFirstOccupiedHeight(minX + sizeX, minZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState), chunkGenerator
/* 211 */         .getFirstOccupiedHeight(minX + sizeX, minZ + sizeZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState) };
/*     */   }
/*     */   
/*     */   public static int getMeanFirstOccupiedHeight(GenerationContext context, int minX, int sizeX, int minZ, int sizeZ) {
/* 216 */     int[] cornerHeights = getCornerHeights(context, minX, sizeX, minZ, sizeZ);
/* 217 */     return (cornerHeights[0] + cornerHeights[1] + cornerHeights[2] + cornerHeights[3]) / 4;
/*     */   }
/*     */   
/*     */   protected static int getLowestY(GenerationContext context, int sizeX, int sizeZ) {
/* 221 */     ChunkPos chunkPos = context.chunkPos();
/* 222 */     int minX = chunkPos.getMinBlockX();
/* 223 */     int minZ = chunkPos.getMinBlockZ();
/* 224 */     return getLowestY(context, minX, minZ, sizeX, sizeZ);
/*     */   }
/*     */   
/*     */   protected static int getLowestY(GenerationContext context, int minX, int minZ, int sizeX, int sizeZ) {
/* 228 */     int[] cornerHeights = getCornerHeights(context, minX, sizeX, minZ, sizeZ);
/* 229 */     return Math.min(Math.min(cornerHeights[0], cornerHeights[1]), Math.min(cornerHeights[2], cornerHeights[3]));
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected BlockPos getLowestYIn5by5BoxOffset7Blocks(GenerationContext context, Rotation rotation) {
/* 234 */     int offsetX = 5;
/* 235 */     int offsetZ = 5;
/* 236 */     if (rotation == Rotation.CLOCKWISE_90) {
/* 237 */       offsetX = -5;
/* 238 */     } else if (rotation == Rotation.CLOCKWISE_180) {
/* 239 */       offsetX = -5;
/* 240 */       offsetZ = -5;
/* 241 */     } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
/* 242 */       offsetZ = -5;
/*     */     } 
/* 245 */     ChunkPos chunkPos = context.chunkPos();
/* 246 */     int blockX = chunkPos.getBlockX(7);
/* 247 */     int blockZ = chunkPos.getBlockZ(7);
/* 248 */     return new BlockPos(blockX, getLowestY(context, blockX, blockZ, offsetX, offsetZ), blockZ);
/*     */   }
/*     */   
/*     */   public Optional<GenerationStub> findValidGenerationPoint(GenerationContext context) {
/* 254 */     return findGenerationPoint(context).filter(generation -> isValidBiome(generation, context));
/*     */   }
/*     */   
/*     */   protected abstract Optional<GenerationStub> findGenerationPoint(GenerationContext paramGenerationContext);
/*     */   
/*     */   public abstract StructureType<?> type();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\Structure.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */