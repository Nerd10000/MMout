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
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public class StraightStairsDown extends StrongholdPieces.StrongholdPiece {
/*     */   private static final int WIDTH = 5;
/*     */   
/*     */   private static final int HEIGHT = 11;
/*     */   
/*     */   private static final int DEPTH = 8;
/*     */   
/*     */   public StraightStairsDown(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 706 */     super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, genDepth, boundingBox);
/* 708 */     setOrientation(direction);
/* 709 */     this.entryDoor = randomSmallDoor(random);
/*     */   }
/*     */   
/*     */   public StraightStairsDown(CompoundTag tag) {
/* 713 */     super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, tag);
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 718 */     generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*     */   }
/*     */   
/*     */   public static StraightStairsDown createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 722 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -7, 0, 5, 11, 8, direction);
/* 724 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 725 */       return null; 
/* 728 */     return new StraightStairsDown(genDepth, random, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 734 */     generateBox(level, chunkBB, 0, 0, 0, 4, 10, 7, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 736 */     generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 7, 0);
/* 738 */     generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 7);
/* 741 */     BlockState stairs = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/* 742 */     for (int i = 0; i < 6; i++) {
/* 743 */       placeBlock(level, stairs, 1, 6 - i, 1 + i, chunkBB);
/* 744 */       placeBlock(level, stairs, 2, 6 - i, 1 + i, chunkBB);
/* 745 */       placeBlock(level, stairs, 3, 6 - i, 1 + i, chunkBB);
/* 746 */       if (i < 5) {
/* 747 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5 - i, 1 + i, chunkBB);
/* 748 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 5 - i, 1 + i, chunkBB);
/* 749 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 5 - i, 1 + i, chunkBB);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$StraightStairsDown.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */