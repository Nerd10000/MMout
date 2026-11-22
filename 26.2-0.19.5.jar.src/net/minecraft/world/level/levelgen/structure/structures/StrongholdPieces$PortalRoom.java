/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.EndPortalFrameBlock;
/*      */ import net.minecraft.world.level.block.IronBarsBlock;
/*      */ import net.minecraft.world.level.block.StairBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ 
/*      */ public class PortalRoom extends StrongholdPieces.StrongholdPiece {
/*      */   protected static final int WIDTH = 11;
/*      */   
/*      */   protected static final int HEIGHT = 8;
/*      */   
/*      */   protected static final int DEPTH = 16;
/*      */   
/*      */   private boolean hasPlacedSpawner;
/*      */   
/*      */   public PortalRoom(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1375 */     super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, genDepth, boundingBox);
/* 1377 */     setOrientation(direction);
/*      */   }
/*      */   
/*      */   public PortalRoom(CompoundTag tag) {
/* 1381 */     super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, tag);
/* 1382 */     this.hasPlacedSpawner = tag.getBooleanOr("Mob", false);
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1387 */     super.addAdditionalSaveData(context, tag);
/* 1388 */     tag.putBoolean("Mob", this.hasPlacedSpawner);
/*      */   }
/*      */   
/*      */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1393 */     if (startPiece != null)
/* 1394 */       ((StrongholdPieces.StartPiece)startPiece).portalRoomPiece = this; 
/*      */   }
/*      */   
/*      */   public static PortalRoom createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1399 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 11, 8, 16, direction);
/* 1401 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1402 */       return null; 
/* 1405 */     return new PortalRoom(genDepth, box, direction);
/*      */   }
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1411 */     generateBox(level, chunkBB, 0, 0, 0, 10, 7, 15, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1413 */     generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.GRATES, 4, 1, 0);
/* 1416 */     int y = 6;
/* 1417 */     generateBox(level, chunkBB, 1, 6, 1, 1, 6, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1418 */     generateBox(level, chunkBB, 9, 6, 1, 9, 6, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1419 */     generateBox(level, chunkBB, 2, 6, 1, 8, 6, 2, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1420 */     generateBox(level, chunkBB, 2, 6, 14, 8, 6, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1423 */     generateBox(level, chunkBB, 1, 1, 1, 2, 1, 4, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1424 */     generateBox(level, chunkBB, 8, 1, 1, 9, 1, 4, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1425 */     generateBox(level, chunkBB, 1, 1, 1, 1, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
/* 1426 */     generateBox(level, chunkBB, 9, 1, 1, 9, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
/* 1429 */     generateBox(level, chunkBB, 3, 1, 8, 7, 1, 12, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1430 */     generateBox(level, chunkBB, 4, 1, 9, 6, 1, 11, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
/* 1433 */     BlockState nsBars = (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true));
/* 1434 */     BlockState weBars = (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true));
/* 1435 */     for (int z = 3; z < 14; z += 2) {
/* 1436 */       generateBox(level, chunkBB, 0, 3, z, 0, 4, z, nsBars, nsBars, false);
/* 1437 */       generateBox(level, chunkBB, 10, 3, z, 10, 4, z, nsBars, nsBars, false);
/*      */     } 
/* 1439 */     for (int x = 2; x < 9; x += 2)
/* 1440 */       generateBox(level, chunkBB, x, 3, 15, x, 4, 15, weBars, weBars, false); 
/* 1444 */     BlockState blockState = (BlockState)Blocks.STONE_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH);
/* 1445 */     generateBox(level, chunkBB, 4, 1, 5, 6, 1, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1446 */     generateBox(level, chunkBB, 4, 2, 6, 6, 2, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1447 */     generateBox(level, chunkBB, 4, 3, 7, 6, 3, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1448 */     for (int j = 4; j <= 6; j++) {
/* 1449 */       placeBlock(level, blockState, j, 1, 4, chunkBB);
/* 1450 */       placeBlock(level, blockState, j, 2, 5, chunkBB);
/* 1451 */       placeBlock(level, blockState, j, 3, 6, chunkBB);
/*      */     } 
/* 1454 */     BlockState northFrame = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue((Property)EndPortalFrameBlock.FACING, (Comparable)Direction.NORTH);
/* 1455 */     BlockState southFrame = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue((Property)EndPortalFrameBlock.FACING, (Comparable)Direction.SOUTH);
/* 1456 */     BlockState eastFrame = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue((Property)EndPortalFrameBlock.FACING, (Comparable)Direction.EAST);
/* 1457 */     BlockState westFrame = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue((Property)EndPortalFrameBlock.FACING, (Comparable)Direction.WEST);
/* 1459 */     boolean allEyes = true;
/* 1460 */     boolean[] eyes = new boolean[12];
/* 1461 */     for (int i = 0; i < eyes.length; i++) {
/* 1462 */       eyes[i] = (random.nextFloat() > 0.9F);
/* 1463 */       allEyes &= eyes[i];
/*      */     } 
/* 1466 */     placeBlock(level, (BlockState)northFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[0])), 4, 3, 8, chunkBB);
/* 1467 */     placeBlock(level, (BlockState)northFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[1])), 5, 3, 8, chunkBB);
/* 1468 */     placeBlock(level, (BlockState)northFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[2])), 6, 3, 8, chunkBB);
/* 1469 */     placeBlock(level, (BlockState)southFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[3])), 4, 3, 12, chunkBB);
/* 1470 */     placeBlock(level, (BlockState)southFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[4])), 5, 3, 12, chunkBB);
/* 1471 */     placeBlock(level, (BlockState)southFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[5])), 6, 3, 12, chunkBB);
/* 1472 */     placeBlock(level, (BlockState)eastFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[6])), 3, 3, 9, chunkBB);
/* 1473 */     placeBlock(level, (BlockState)eastFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[7])), 3, 3, 10, chunkBB);
/* 1474 */     placeBlock(level, (BlockState)eastFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[8])), 3, 3, 11, chunkBB);
/* 1475 */     placeBlock(level, (BlockState)westFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[9])), 7, 3, 9, chunkBB);
/* 1476 */     placeBlock(level, (BlockState)westFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[10])), 7, 3, 10, chunkBB);
/* 1477 */     placeBlock(level, (BlockState)westFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[11])), 7, 3, 11, chunkBB);
/* 1479 */     if (allEyes) {
/* 1480 */       BlockState portal = Blocks.END_PORTAL.defaultBlockState();
/* 1482 */       placeBlock(level, portal, 4, 3, 9, chunkBB);
/* 1483 */       placeBlock(level, portal, 5, 3, 9, chunkBB);
/* 1484 */       placeBlock(level, portal, 6, 3, 9, chunkBB);
/* 1485 */       placeBlock(level, portal, 4, 3, 10, chunkBB);
/* 1486 */       placeBlock(level, portal, 5, 3, 10, chunkBB);
/* 1487 */       placeBlock(level, portal, 6, 3, 10, chunkBB);
/* 1488 */       placeBlock(level, portal, 4, 3, 11, chunkBB);
/* 1489 */       placeBlock(level, portal, 5, 3, 11, chunkBB);
/* 1490 */       placeBlock(level, portal, 6, 3, 11, chunkBB);
/*      */     } 
/* 1493 */     if (!this.hasPlacedSpawner) {
/* 1494 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(5, 3, 6);
/* 1495 */       if (chunkBB.isInside((Vec3i)mutableBlockPos)) {
/* 1496 */         this.hasPlacedSpawner = true;
/* 1497 */         level.setBlock((BlockPos)mutableBlockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/* 1499 */         BlockEntity blockEntity = level.getBlockEntity((BlockPos)mutableBlockPos);
/* 1500 */         if (blockEntity instanceof SpawnerBlockEntity) {
/* 1500 */           SpawnerBlockEntity spawner = (SpawnerBlockEntity)blockEntity;
/* 1501 */           spawner.setEntityId(EntityTypes.SILVERFISH, random);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$PortalRoom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */