/*    */ package net.minecraft.world.level.levelgen.structure.pieces;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.QuartPos;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.RandomState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface PieceGeneratorSupplier<C extends FeatureConfiguration> {
/*    */   static <C extends FeatureConfiguration> PieceGeneratorSupplier<C> simple(Predicate<Context<C>> check, PieceGenerator<C> generator) {
/* 24 */     Optional<PieceGenerator<C>> result = Optional.of(generator);
/* 25 */     return context -> check.test(context) ? result : Optional.empty();
/*    */   }
/*    */   
/*    */   static <C extends FeatureConfiguration> Predicate<Context<C>> checkForBiomeOnTop(Heightmap.Types type) {
/* 29 */     return context -> context.validBiomeOnTop(type);
/*    */   }
/*    */   
/*    */   Optional<PieceGenerator<C>> createGenerator(Context<C> paramContext);
/*    */   
/*    */   public static final class Context<C extends FeatureConfiguration> extends Record {
/*    */     private final ChunkGenerator chunkGenerator;
/*    */     
/*    */     private final BiomeSource biomeSource;
/*    */     
/*    */     private final RandomState randomState;
/*    */     
/*    */     private final long seed;
/*    */     
/*    */     private final ChunkPos chunkPos;
/*    */     
/*    */     private final C config;
/*    */     
/*    */     private final LevelHeightAccessor heightAccessor;
/*    */     
/*    */     private final Predicate<Holder<Biome>> validBiome;
/*    */     
/*    */     private final StructureTemplateManager structureTemplateManager;
/*    */     
/*    */     private final RegistryAccess registryAccess;
/*    */     
/*    */     public Context(ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, long seed, ChunkPos chunkPos, C config, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureTemplateManager structureTemplateManager, RegistryAccess registryAccess) {
/* 32 */       this.chunkGenerator = chunkGenerator;
/* 32 */       this.biomeSource = biomeSource;
/* 32 */       this.randomState = randomState;
/* 32 */       this.seed = seed;
/* 32 */       this.chunkPos = chunkPos;
/* 32 */       this.config = config;
/* 32 */       this.heightAccessor = heightAccessor;
/* 32 */       this.validBiome = validBiome;
/* 32 */       this.structureTemplateManager = structureTemplateManager;
/* 32 */       this.registryAccess = registryAccess;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context<TC;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context<TC;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #32	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context<TC;>;
/*    */     }
/*    */     
/*    */     public ChunkGenerator chunkGenerator() {
/* 32 */       return this.chunkGenerator;
/*    */     }
/*    */     
/*    */     public BiomeSource biomeSource() {
/* 32 */       return this.biomeSource;
/*    */     }
/*    */     
/*    */     public RandomState randomState() {
/* 32 */       return this.randomState;
/*    */     }
/*    */     
/*    */     public long seed() {
/* 32 */       return this.seed;
/*    */     }
/*    */     
/*    */     public ChunkPos chunkPos() {
/* 32 */       return this.chunkPos;
/*    */     }
/*    */     
/*    */     public C config() {
/* 32 */       return this.config;
/*    */     }
/*    */     
/*    */     public LevelHeightAccessor heightAccessor() {
/* 32 */       return this.heightAccessor;
/*    */     }
/*    */     
/*    */     public Predicate<Holder<Biome>> validBiome() {
/* 32 */       return this.validBiome;
/*    */     }
/*    */     
/*    */     public StructureTemplateManager structureTemplateManager() {
/* 32 */       return this.structureTemplateManager;
/*    */     }
/*    */     
/*    */     public RegistryAccess registryAccess() {
/* 32 */       return this.registryAccess;
/*    */     }
/*    */     
/*    */     public boolean validBiomeOnTop(Heightmap.Types type) {
/* 47 */       int blockX = this.chunkPos.getMiddleBlockX();
/* 48 */       int blockZ = this.chunkPos.getMiddleBlockZ();
/* 49 */       int blockY = this.chunkGenerator.getFirstOccupiedHeight(blockX, blockZ, type, this.heightAccessor, this.randomState);
/* 50 */       Holder<Biome> biome = this.chunkGenerator.getBiomeSource().getNoiseBiome(QuartPos.fromBlock(blockX), QuartPos.fromBlock(blockY), QuartPos.fromBlock(blockZ), this.randomState.sampler());
/* 51 */       return this.validBiome.test(biome);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pieces\PieceGeneratorSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */