/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.DoorBlock;
/*      */ import net.minecraft.world.level.block.IronBarsBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ 
/*      */ public class PrisonHall extends StrongholdPieces.StrongholdPiece {
/*      */   protected static final int WIDTH = 9;
/*      */   
/*      */   protected static final int HEIGHT = 5;
/*      */   
/*      */   protected static final int DEPTH = 11;
/*      */   
/*      */   public PrisonHall(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1004 */     super(StructurePieceType.STRONGHOLD_PRISON_HALL, genDepth, boundingBox);
/* 1006 */     setOrientation(direction);
/* 1007 */     this.entryDoor = randomSmallDoor(random);
/*      */   }
/*      */   
/*      */   public PrisonHall(CompoundTag tag) {
/* 1011 */     super(StructurePieceType.STRONGHOLD_PRISON_HALL, tag);
/*      */   }
/*      */   
/*      */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1016 */     generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */   }
/*      */   
/*      */   public static PrisonHall createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1020 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 9, 5, 11, direction);
/* 1022 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1023 */       return null; 
/* 1026 */     return new PrisonHall(genDepth, random, box, direction);
/*      */   }
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1032 */     generateBox(level, chunkBB, 0, 0, 0, 8, 4, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1034 */     generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/* 1036 */     generateBox(level, chunkBB, 1, 1, 10, 3, 3, 10, CAVE_AIR, CAVE_AIR, false);
/* 1039 */     generateBox(level, chunkBB, 4, 1, 1, 4, 3, 1, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1040 */     generateBox(level, chunkBB, 4, 1, 3, 4, 3, 3, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1041 */     generateBox(level, chunkBB, 4, 1, 7, 4, 3, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1042 */     generateBox(level, chunkBB, 4, 1, 9, 4, 3, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1045 */     for (int y = 1; y <= 3; y++) {
/* 1046 */       placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, y, 4, chunkBB);
/* 1047 */       placeBlock(level, (BlockState)((BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), 4, y, 5, chunkBB);
/* 1048 */       placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, y, 6, chunkBB);
/* 1050 */       placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), 5, y, 5, chunkBB);
/* 1051 */       placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), 6, y, 5, chunkBB);
/* 1052 */       placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), 7, y, 5, chunkBB);
/*      */     } 
/* 1056 */     placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, 3, 2, chunkBB);
/* 1057 */     placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, 3, 8, chunkBB);
/* 1058 */     BlockState doorBottom = (BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue((Property)DoorBlock.FACING, (Comparable)Direction.WEST);
/* 1059 */     BlockState doorTop = (BlockState)((BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue((Property)DoorBlock.FACING, (Comparable)Direction.WEST)).setValue((Property)DoorBlock.HALF, (Comparable)DoubleBlockHalf.UPPER);
/* 1060 */     placeBlock(level, doorBottom, 4, 1, 2, chunkBB);
/* 1061 */     placeBlock(level, doorTop, 4, 2, 2, chunkBB);
/* 1062 */     placeBlock(level, doorBottom, 4, 1, 8, chunkBB);
/* 1063 */     placeBlock(level, doorTop, 4, 2, 8, chunkBB);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$PrisonHall.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */