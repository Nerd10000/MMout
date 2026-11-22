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
/*     */ public class RoomCrossing extends NetherFortressPieces.NetherBridgePiece {
/*     */   private static final int WIDTH = 7;
/*     */   
/*     */   private static final int HEIGHT = 9;
/*     */   
/*     */   private static final int DEPTH = 7;
/*     */   
/*     */   public RoomCrossing(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 517 */     super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, genDepth, boundingBox);
/* 519 */     setOrientation(direction);
/*     */   }
/*     */   
/*     */   public RoomCrossing(CompoundTag tag) {
/* 523 */     super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, tag);
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 528 */     generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 2, 0, false);
/* 529 */     generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 2, false);
/* 530 */     generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 2, false);
/*     */   }
/*     */   
/*     */   public static RoomCrossing createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 534 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -2, 0, 0, 7, 9, 7, direction);
/* 536 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 537 */       return null; 
/* 540 */     return new RoomCrossing(genDepth, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 546 */     generateBox(level, chunkBB, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 548 */     generateBox(level, chunkBB, 0, 2, 0, 6, 7, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 551 */     generateBox(level, chunkBB, 0, 2, 0, 1, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 552 */     generateBox(level, chunkBB, 0, 2, 6, 1, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 553 */     generateBox(level, chunkBB, 5, 2, 0, 6, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 554 */     generateBox(level, chunkBB, 5, 2, 6, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 555 */     generateBox(level, chunkBB, 0, 2, 0, 0, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 556 */     generateBox(level, chunkBB, 0, 2, 5, 0, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 557 */     generateBox(level, chunkBB, 6, 2, 0, 6, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 558 */     generateBox(level, chunkBB, 6, 2, 5, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 561 */     BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 562 */     BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 564 */     generateBox(level, chunkBB, 2, 6, 0, 4, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 565 */     generateBox(level, chunkBB, 2, 5, 0, 4, 5, 0, weFence, weFence, false);
/* 566 */     generateBox(level, chunkBB, 2, 6, 6, 4, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 567 */     generateBox(level, chunkBB, 2, 5, 6, 4, 5, 6, weFence, weFence, false);
/* 568 */     generateBox(level, chunkBB, 0, 6, 2, 0, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 569 */     generateBox(level, chunkBB, 0, 5, 2, 0, 5, 4, nsFence, nsFence, false);
/* 570 */     generateBox(level, chunkBB, 6, 6, 2, 6, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 571 */     generateBox(level, chunkBB, 6, 5, 2, 6, 5, 4, nsFence, nsFence, false);
/* 574 */     for (int x = 0; x <= 6; x++) {
/* 575 */       for (int z = 0; z <= 6; z++)
/* 576 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$RoomCrossing.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */