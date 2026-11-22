/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface PostPlacementProcessor {
/*    */   public static final PostPlacementProcessor NONE = (level, structureManager, generator, random, chunkBB, chunkPos, pieces) -> {
/*    */     
/*    */     };
/*    */   
/*    */   void afterPlace(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, PiecesContainer paramPiecesContainer);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\PostPlacementProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */