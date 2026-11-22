/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public class FillerCorridor extends StrongholdPieces.StrongholdPiece {
/*     */   private final int steps;
/*     */   
/*     */   public FillerCorridor(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 372 */     super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, genDepth, boundingBox);
/* 374 */     setOrientation(direction);
/* 375 */     this.steps = (direction == Direction.NORTH || direction == Direction.SOUTH) ? boundingBox.getZSpan() : boundingBox.getXSpan();
/*     */   }
/*     */   
/*     */   public FillerCorridor(CompoundTag tag) {
/* 379 */     super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, tag);
/* 380 */     this.steps = tag.getIntOr("Steps", 0);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 385 */     super.addAdditionalSaveData(context, tag);
/* 386 */     tag.putInt("Steps", this.steps);
/*     */   }
/*     */   
/*     */   public static BoundingBox findPieceBox(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction) {
/* 390 */     int maxLength = 3;
/* 392 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 4, direction);
/* 394 */     StructurePiece collisionPiece = structurePieceAccessor.findCollisionPiece(box);
/* 395 */     if (collisionPiece == null)
/* 397 */       return null; 
/* 400 */     if (collisionPiece.getBoundingBox().minY() == box.minY())
/* 402 */       for (int depth = 2; depth >= 1; depth--) {
/* 403 */         box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, depth, direction);
/* 404 */         if (!collisionPiece.getBoundingBox().intersects(box))
/* 407 */           return BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, depth + 1, direction); 
/*     */       }  
/* 412 */     return null;
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 418 */     for (int i = 0; i < this.steps; i++) {
/* 420 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, 0, i, chunkBB);
/* 421 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 0, i, chunkBB);
/* 422 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 0, i, chunkBB);
/* 423 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 0, i, chunkBB);
/* 424 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, 0, i, chunkBB);
/* 426 */       for (int y = 1; y <= 3; y++) {
/* 427 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, y, i, chunkBB);
/* 428 */         placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 1, y, i, chunkBB);
/* 429 */         placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 2, y, i, chunkBB);
/* 430 */         placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 3, y, i, chunkBB);
/* 431 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, y, i, chunkBB);
/*     */       } 
/* 434 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, 4, i, chunkBB);
/* 435 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, i, chunkBB);
/* 436 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, i, chunkBB);
/* 437 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 4, i, chunkBB);
/* 438 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, 4, i, chunkBB);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$FillerCorridor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */