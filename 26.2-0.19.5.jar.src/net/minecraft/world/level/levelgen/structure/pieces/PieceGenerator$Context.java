/*    */ package net.minecraft.world.level.levelgen.structure.pieces;
/*    */ 
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public final class Context<C extends FeatureConfiguration> extends Record {
/*    */   private final C config;
/*    */   
/*    */   private final ChunkGenerator chunkGenerator;
/*    */   
/*    */   private final StructureTemplateManager structureTemplateManager;
/*    */   
/*    */   private final ChunkPos chunkPos;
/*    */   
/*    */   private final LevelHeightAccessor heightAccessor;
/*    */   
/*    */   private final WorldgenRandom random;
/*    */   
/*    */   private final long seed;
/*    */   
/*    */   public Context(C config, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, WorldgenRandom random, long seed) {
/* 14 */     this.config = config;
/* 14 */     this.chunkGenerator = chunkGenerator;
/* 14 */     this.structureTemplateManager = structureTemplateManager;
/* 14 */     this.chunkPos = chunkPos;
/* 14 */     this.heightAccessor = heightAccessor;
/* 14 */     this.random = random;
/* 14 */     this.seed = seed;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context<TC;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context<TC;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGenerator$Context<TC;>;
/*    */   }
/*    */   
/*    */   public C config() {
/* 14 */     return this.config;
/*    */   }
/*    */   
/*    */   public ChunkGenerator chunkGenerator() {
/* 14 */     return this.chunkGenerator;
/*    */   }
/*    */   
/*    */   public StructureTemplateManager structureTemplateManager() {
/* 14 */     return this.structureTemplateManager;
/*    */   }
/*    */   
/*    */   public ChunkPos chunkPos() {
/* 14 */     return this.chunkPos;
/*    */   }
/*    */   
/*    */   public LevelHeightAccessor heightAccessor() {
/* 14 */     return this.heightAccessor;
/*    */   }
/*    */   
/*    */   public WorldgenRandom random() {
/* 14 */     return this.random;
/*    */   }
/*    */   
/*    */   public long seed() {
/* 14 */     return this.seed;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pieces\PieceGenerator$Context.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */