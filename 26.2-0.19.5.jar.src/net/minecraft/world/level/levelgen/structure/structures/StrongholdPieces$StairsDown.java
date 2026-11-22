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
/*     */ public class StairsDown extends StrongholdPieces.StrongholdPiece {
/*     */   private static final int WIDTH = 5;
/*     */   
/*     */   private static final int HEIGHT = 11;
/*     */   
/*     */   private static final int DEPTH = 5;
/*     */   
/*     */   private final boolean isSource;
/*     */   
/*     */   public StairsDown(StructurePieceType type, int genDepth, int west, int north, Direction direction) {
/* 451 */     super(type, genDepth, makeBoundingBox(west, 64, north, direction, 5, 11, 5));
/* 453 */     this.isSource = true;
/* 454 */     setOrientation(direction);
/* 455 */     this.entryDoor = StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING;
/*     */   }
/*     */   
/*     */   public StairsDown(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 459 */     super(StructurePieceType.STRONGHOLD_STAIRS_DOWN, genDepth, boundingBox);
/* 461 */     this.isSource = false;
/* 462 */     setOrientation(direction);
/* 463 */     this.entryDoor = randomSmallDoor(random);
/*     */   }
/*     */   
/*     */   public StairsDown(StructurePieceType type, CompoundTag tag) {
/* 467 */     super(type, tag);
/* 468 */     this.isSource = tag.getBooleanOr("Source", false);
/*     */   }
/*     */   
/*     */   public StairsDown(CompoundTag tag) {
/* 472 */     this(StructurePieceType.STRONGHOLD_STAIRS_DOWN, tag);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 477 */     super.addAdditionalSaveData(context, tag);
/* 478 */     tag.putBoolean("Source", this.isSource);
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 483 */     if (this.isSource)
/* 485 */       StrongholdPieces.imposedPiece = (Class)StrongholdPieces.FiveCrossing.class; 
/* 487 */     generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*     */   }
/*     */   
/*     */   public static StairsDown createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 491 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -7, 0, 5, 11, 5, direction);
/* 493 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 494 */       return null; 
/* 497 */     return new StairsDown(genDepth, random, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 503 */     generateBox(level, chunkBB, 0, 0, 0, 4, 10, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 505 */     generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 7, 0);
/* 507 */     generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 4);
/* 510 */     placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 6, 1, chunkBB);
/* 511 */     placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 1, chunkBB);
/* 512 */     placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 6, 1, chunkBB);
/* 513 */     placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 2, chunkBB);
/* 514 */     placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, 3, chunkBB);
/* 515 */     placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 5, 3, chunkBB);
/* 516 */     placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, 3, chunkBB);
/* 517 */     placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 3, chunkBB);
/* 518 */     placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 4, 3, chunkBB);
/* 519 */     placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 2, chunkBB);
/* 520 */     placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 2, 1, chunkBB);
/* 521 */     placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 3, 1, chunkBB);
/* 522 */     placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 2, 1, chunkBB);
/* 523 */     placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 1, chunkBB);
/* 524 */     placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 2, 1, chunkBB);
/* 525 */     placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 2, chunkBB);
/* 526 */     placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 1, 3, chunkBB);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$StairsDown.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */