/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public final class GenerationContext extends Record {
/*     */   private final RegistryAccess registryAccess;
/*     */   
/*     */   private final ChunkGenerator chunkGenerator;
/*     */   
/*     */   private final BiomeSource biomeSource;
/*     */   
/*     */   private final RandomState randomState;
/*     */   
/*     */   private final StructureTemplateManager structureTemplateManager;
/*     */   
/*     */   private final WorldgenRandom random;
/*     */   
/*     */   private final long seed;
/*     */   
/*     */   private final ChunkPos chunkPos;
/*     */   
/*     */   private final LevelHeightAccessor heightAccessor;
/*     */   
/*     */   private final Predicate<Holder<Biome>> validBiome;
/*     */   
/*     */   public GenerationContext(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, WorldgenRandom random, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome) {
/* 138 */     this.registryAccess = registryAccess;
/* 138 */     this.chunkGenerator = chunkGenerator;
/* 138 */     this.biomeSource = biomeSource;
/* 138 */     this.randomState = randomState;
/* 138 */     this.structureTemplateManager = structureTemplateManager;
/* 138 */     this.random = random;
/* 138 */     this.seed = seed;
/* 138 */     this.chunkPos = chunkPos;
/* 138 */     this.heightAccessor = heightAccessor;
/* 138 */     this.validBiome = validBiome;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public RegistryAccess registryAccess() {
/* 138 */     return this.registryAccess;
/*     */   }
/*     */   
/*     */   public ChunkGenerator chunkGenerator() {
/* 138 */     return this.chunkGenerator;
/*     */   }
/*     */   
/*     */   public BiomeSource biomeSource() {
/* 138 */     return this.biomeSource;
/*     */   }
/*     */   
/*     */   public RandomState randomState() {
/* 138 */     return this.randomState;
/*     */   }
/*     */   
/*     */   public StructureTemplateManager structureTemplateManager() {
/* 138 */     return this.structureTemplateManager;
/*     */   }
/*     */   
/*     */   public WorldgenRandom random() {
/* 138 */     return this.random;
/*     */   }
/*     */   
/*     */   public long seed() {
/* 138 */     return this.seed;
/*     */   }
/*     */   
/*     */   public ChunkPos chunkPos() {
/* 138 */     return this.chunkPos;
/*     */   }
/*     */   
/*     */   public LevelHeightAccessor heightAccessor() {
/* 138 */     return this.heightAccessor;
/*     */   }
/*     */   
/*     */   public Predicate<Holder<Biome>> validBiome() {
/* 138 */     return this.validBiome;
/*     */   }
/*     */   
/*     */   public GenerationContext(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome) {
/* 140 */     this(registryAccess, chunkGenerator, biomeSource, randomState, structureTemplateManager, makeRandom(seed, chunkPos), seed, chunkPos, heightAccessor, validBiome);
/*     */   }
/*     */   
/*     */   private static WorldgenRandom makeRandom(long seed, ChunkPos chunkPos) {
/* 144 */     WorldgenRandom random = new WorldgenRandom((RandomSource)new LegacyRandomSource(0L));
/* 145 */     random.setLargeFeatureSeed(seed, chunkPos.x(), chunkPos.z());
/* 146 */     return random;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\Structure$GenerationContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */