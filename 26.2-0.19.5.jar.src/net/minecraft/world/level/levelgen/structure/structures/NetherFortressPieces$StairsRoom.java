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
/*     */ import net.minecraft.world.level.block.FenceBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public class StairsRoom extends NetherFortressPieces.NetherBridgePiece {
/*     */   private static final int WIDTH = 7;
/*     */   
/*     */   private static final int HEIGHT = 11;
/*     */   
/*     */   private static final int DEPTH = 7;
/*     */   
/*     */   public StairsRoom(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 588 */     super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, genDepth, boundingBox);
/* 590 */     setOrientation(direction);
/*     */   }
/*     */   
/*     */   public StairsRoom(CompoundTag tag) {
/* 594 */     super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, tag);
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 599 */     generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 6, 2, false);
/*     */   }
/*     */   
/*     */   public static StairsRoom createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, int genDepth, Direction direction) {
/* 603 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -2, 0, 0, 7, 11, 7, direction);
/* 605 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 606 */       return null; 
/* 609 */     return new StairsRoom(genDepth, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 615 */     generateBox(level, chunkBB, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 617 */     generateBox(level, chunkBB, 0, 2, 0, 6, 10, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 620 */     generateBox(level, chunkBB, 0, 2, 0, 1, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 621 */     generateBox(level, chunkBB, 5, 2, 0, 6, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 622 */     generateBox(level, chunkBB, 0, 2, 1, 0, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 623 */     generateBox(level, chunkBB, 6, 2, 1, 6, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 624 */     generateBox(level, chunkBB, 1, 2, 6, 5, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 627 */     BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 628 */     BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 630 */     generateBox(level, chunkBB, 0, 3, 2, 0, 5, 4, nsFence, nsFence, false);
/* 631 */     generateBox(level, chunkBB, 6, 3, 2, 6, 5, 2, nsFence, nsFence, false);
/* 632 */     generateBox(level, chunkBB, 6, 3, 4, 6, 5, 4, nsFence, nsFence, false);
/* 635 */     placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 5, 2, 5, chunkBB);
/* 636 */     generateBox(level, chunkBB, 4, 2, 5, 4, 3, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 637 */     generateBox(level, chunkBB, 3, 2, 5, 3, 4, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 638 */     generateBox(level, chunkBB, 2, 2, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 639 */     generateBox(level, chunkBB, 1, 2, 5, 1, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 642 */     generateBox(level, chunkBB, 1, 7, 1, 5, 7, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 643 */     generateBox(level, chunkBB, 6, 8, 2, 6, 8, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 646 */     generateBox(level, chunkBB, 2, 6, 0, 4, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 647 */     generateBox(level, chunkBB, 2, 5, 0, 4, 5, 0, weFence, weFence, false);
/* 649 */     for (int x = 0; x <= 6; x++) {
/* 650 */       for (int z = 0; z <= 6; z++)
/* 651 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$StairsRoom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */