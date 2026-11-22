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
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public class BridgeCrossing extends NetherFortressPieces.NetherBridgePiece {
/*     */   private static final int WIDTH = 19;
/*     */   
/*     */   private static final int HEIGHT = 10;
/*     */   
/*     */   private static final int DEPTH = 19;
/*     */   
/*     */   public BridgeCrossing(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 432 */     super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, genDepth, boundingBox);
/* 434 */     setOrientation(direction);
/*     */   }
/*     */   
/*     */   protected BridgeCrossing(int west, int north, Direction direction) {
/* 438 */     super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, 0, StructurePiece.makeBoundingBox(west, 64, north, direction, 19, 10, 19));
/* 440 */     setOrientation(direction);
/*     */   }
/*     */   
/*     */   protected BridgeCrossing(StructurePieceType type, CompoundTag tag) {
/* 444 */     super(type, tag);
/*     */   }
/*     */   
/*     */   public BridgeCrossing(CompoundTag tag) {
/* 448 */     this(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, tag);
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 453 */     generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 8, 3, false);
/* 454 */     generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 3, 8, false);
/* 455 */     generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 3, 8, false);
/*     */   }
/*     */   
/*     */   public static BridgeCrossing createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 459 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -8, -3, 0, 19, 10, 19, direction);
/* 461 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 462 */       return null; 
/* 465 */     return new BridgeCrossing(genDepth, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 471 */     generateBox(level, chunkBB, 7, 3, 0, 11, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 472 */     generateBox(level, chunkBB, 0, 3, 7, 18, 4, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 474 */     generateBox(level, chunkBB, 8, 5, 0, 10, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 475 */     generateBox(level, chunkBB, 0, 5, 8, 18, 7, 10, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 477 */     generateBox(level, chunkBB, 7, 5, 0, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 478 */     generateBox(level, chunkBB, 7, 5, 11, 7, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 479 */     generateBox(level, chunkBB, 11, 5, 0, 11, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 480 */     generateBox(level, chunkBB, 11, 5, 11, 11, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 481 */     generateBox(level, chunkBB, 0, 5, 7, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 482 */     generateBox(level, chunkBB, 11, 5, 7, 18, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 483 */     generateBox(level, chunkBB, 0, 5, 11, 7, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 484 */     generateBox(level, chunkBB, 11, 5, 11, 18, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 487 */     generateBox(level, chunkBB, 7, 2, 0, 11, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 488 */     generateBox(level, chunkBB, 7, 2, 13, 11, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 489 */     generateBox(level, chunkBB, 7, 0, 0, 11, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 490 */     generateBox(level, chunkBB, 7, 0, 15, 11, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*     */     int x;
/* 491 */     for (x = 7; x <= 11; x++) {
/* 492 */       for (int z = 0; z <= 2; z++) {
/* 493 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/* 494 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, 18 - z, chunkBB);
/*     */       } 
/*     */     } 
/* 498 */     generateBox(level, chunkBB, 0, 2, 7, 5, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 499 */     generateBox(level, chunkBB, 13, 2, 7, 18, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 500 */     generateBox(level, chunkBB, 0, 0, 7, 3, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 501 */     generateBox(level, chunkBB, 15, 0, 7, 18, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 502 */     for (x = 0; x <= 2; x++) {
/* 503 */       for (int z = 7; z <= 11; z++) {
/* 504 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/* 505 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), 18 - x, -1, z, chunkBB);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$BridgeCrossing.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */