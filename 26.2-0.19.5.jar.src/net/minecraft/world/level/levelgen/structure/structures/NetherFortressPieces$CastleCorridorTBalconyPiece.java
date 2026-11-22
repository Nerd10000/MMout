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
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ 
/*      */ public class CastleCorridorTBalconyPiece extends NetherFortressPieces.NetherBridgePiece {
/*      */   private static final int WIDTH = 9;
/*      */   
/*      */   private static final int HEIGHT = 7;
/*      */   
/*      */   private static final int DEPTH = 9;
/*      */   
/*      */   public CastleCorridorTBalconyPiece(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1387 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, genDepth, boundingBox);
/* 1389 */     setOrientation(direction);
/*      */   }
/*      */   
/*      */   public CastleCorridorTBalconyPiece(CompoundTag tag) {
/* 1393 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, tag);
/*      */   }
/*      */   
/*      */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1398 */     int zOff = 1;
/* 1400 */     Direction orientation = getOrientation();
/* 1401 */     if (orientation == Direction.WEST || orientation == Direction.NORTH)
/* 1402 */       zOff = 5; 
/* 1405 */     generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, zOff, (random.nextInt(8) > 0));
/* 1406 */     generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, zOff, (random.nextInt(8) > 0));
/*      */   }
/*      */   
/*      */   public static CastleCorridorTBalconyPiece createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1410 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -3, 0, 0, 9, 7, 9, direction);
/* 1412 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1413 */       return null; 
/* 1416 */     return new CastleCorridorTBalconyPiece(genDepth, box, direction);
/*      */   }
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1421 */     BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1422 */     BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1425 */     generateBox(level, chunkBB, 0, 0, 0, 8, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1427 */     generateBox(level, chunkBB, 0, 2, 0, 8, 5, 8, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1429 */     generateBox(level, chunkBB, 0, 6, 0, 8, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1432 */     generateBox(level, chunkBB, 0, 2, 0, 2, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1433 */     generateBox(level, chunkBB, 6, 2, 0, 8, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1434 */     generateBox(level, chunkBB, 1, 3, 0, 1, 4, 0, weFence, weFence, false);
/* 1435 */     generateBox(level, chunkBB, 7, 3, 0, 7, 4, 0, weFence, weFence, false);
/* 1438 */     generateBox(level, chunkBB, 0, 2, 4, 8, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1439 */     generateBox(level, chunkBB, 1, 1, 4, 2, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1440 */     generateBox(level, chunkBB, 6, 1, 4, 7, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1443 */     generateBox(level, chunkBB, 1, 3, 8, 7, 3, 8, weFence, weFence, false);
/* 1444 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 0, 3, 8, chunkBB);
/* 1445 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 8, 3, 8, chunkBB);
/* 1446 */     generateBox(level, chunkBB, 0, 3, 6, 0, 3, 7, nsFence, nsFence, false);
/* 1447 */     generateBox(level, chunkBB, 8, 3, 6, 8, 3, 7, nsFence, nsFence, false);
/* 1450 */     generateBox(level, chunkBB, 0, 3, 4, 0, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1451 */     generateBox(level, chunkBB, 8, 3, 4, 8, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1452 */     generateBox(level, chunkBB, 1, 3, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1453 */     generateBox(level, chunkBB, 6, 3, 5, 7, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1454 */     generateBox(level, chunkBB, 1, 4, 5, 1, 5, 5, weFence, weFence, false);
/* 1455 */     generateBox(level, chunkBB, 7, 4, 5, 7, 5, 5, weFence, weFence, false);
/* 1458 */     for (int z = 0; z <= 5; z++) {
/* 1459 */       for (int x = 0; x <= 8; x++)
/* 1460 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$CastleCorridorTBalconyPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */