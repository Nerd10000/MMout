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
/*      */ import net.minecraft.world.level.block.FenceBlock;
/*      */ import net.minecraft.world.level.block.StairBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ 
/*      */ public class CastleCorridorStairsPiece extends NetherFortressPieces.NetherBridgePiece {
/*      */   private static final int WIDTH = 5;
/*      */   
/*      */   private static final int HEIGHT = 14;
/*      */   
/*      */   private static final int DEPTH = 10;
/*      */   
/*      */   public CastleCorridorStairsPiece(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1319 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, genDepth, boundingBox);
/* 1321 */     setOrientation(direction);
/*      */   }
/*      */   
/*      */   public CastleCorridorStairsPiece(CompoundTag tag) {
/* 1325 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, tag);
/*      */   }
/*      */   
/*      */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1330 */     generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 0, true);
/*      */   }
/*      */   
/*      */   public static CastleCorridorStairsPiece createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1334 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -7, 0, 5, 14, 10, direction);
/* 1336 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1337 */       return null; 
/* 1340 */     return new CastleCorridorStairsPiece(genDepth, box, direction);
/*      */   }
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1346 */     BlockState stairs = (BlockState)Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/* 1347 */     BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1349 */     for (int step = 0; step <= 9; step++) {
/* 1350 */       int floor = Math.max(1, 7 - step);
/* 1351 */       int roof = Math.min(Math.max(floor + 5, 14 - step), 13);
/* 1352 */       int z = step;
/* 1355 */       generateBox(level, chunkBB, 0, 0, z, 4, floor, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1357 */       generateBox(level, chunkBB, 1, floor + 1, z, 3, roof - 1, z, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1358 */       if (step <= 6) {
/* 1359 */         placeBlock(level, stairs, 1, floor + 1, z, chunkBB);
/* 1360 */         placeBlock(level, stairs, 2, floor + 1, z, chunkBB);
/* 1361 */         placeBlock(level, stairs, 3, floor + 1, z, chunkBB);
/*      */       } 
/* 1364 */       generateBox(level, chunkBB, 0, roof, z, 4, roof, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1366 */       generateBox(level, chunkBB, 0, floor + 1, z, 0, roof - 1, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1367 */       generateBox(level, chunkBB, 4, floor + 1, z, 4, roof - 1, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1368 */       if ((step & 0x1) == 0) {
/* 1369 */         generateBox(level, chunkBB, 0, floor + 2, z, 0, floor + 3, z, nsFence, nsFence, false);
/* 1370 */         generateBox(level, chunkBB, 4, floor + 2, z, 4, floor + 3, z, nsFence, nsFence, false);
/*      */       } 
/* 1374 */       for (int x = 0; x <= 4; x++)
/* 1375 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$CastleCorridorStairsPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */