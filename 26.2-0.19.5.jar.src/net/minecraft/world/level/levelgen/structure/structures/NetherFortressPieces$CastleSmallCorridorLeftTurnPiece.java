/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Vec3i;
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
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ public class CastleSmallCorridorLeftTurnPiece extends NetherFortressPieces.NetherBridgePiece {
/*      */   private static final int WIDTH = 5;
/*      */   
/*      */   private static final int HEIGHT = 7;
/*      */   
/*      */   private static final int DEPTH = 5;
/*      */   
/*      */   private boolean isNeedingChest;
/*      */   
/*      */   public CastleSmallCorridorLeftTurnPiece(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1239 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, genDepth, boundingBox);
/* 1241 */     setOrientation(direction);
/* 1243 */     this.isNeedingChest = (random.nextInt(3) == 0);
/*      */   }
/*      */   
/*      */   public CastleSmallCorridorLeftTurnPiece(CompoundTag tag) {
/* 1247 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, tag);
/* 1248 */     this.isNeedingChest = tag.getBooleanOr("Chest", false);
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1253 */     super.addAdditionalSaveData(context, tag);
/* 1255 */     tag.putBoolean("Chest", this.isNeedingChest);
/*      */   }
/*      */   
/*      */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1260 */     generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 1, true);
/*      */   }
/*      */   
/*      */   public static CastleSmallCorridorLeftTurnPiece createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1264 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
/* 1266 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1267 */       return null; 
/* 1270 */     return new CastleSmallCorridorLeftTurnPiece(genDepth, random, box, direction);
/*      */   }
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1276 */     generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1278 */     generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1280 */     BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1281 */     BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1284 */     generateBox(level, chunkBB, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1285 */     generateBox(level, chunkBB, 4, 3, 1, 4, 4, 1, nsFence, nsFence, false);
/* 1286 */     generateBox(level, chunkBB, 4, 3, 3, 4, 4, 3, nsFence, nsFence, false);
/* 1288 */     generateBox(level, chunkBB, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1290 */     generateBox(level, chunkBB, 0, 2, 4, 3, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1291 */     generateBox(level, chunkBB, 1, 3, 4, 1, 4, 4, weFence, weFence, false);
/* 1292 */     generateBox(level, chunkBB, 3, 3, 4, 3, 4, 4, weFence, weFence, false);
/* 1294 */     if (this.isNeedingChest && 
/* 1295 */       chunkBB.isInside((Vec3i)getWorldPos(3, 2, 3))) {
/* 1296 */       this.isNeedingChest = false;
/* 1297 */       createChest(level, chunkBB, random, 3, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
/*      */     } 
/* 1302 */     generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1305 */     for (int x = 0; x <= 4; x++) {
/* 1306 */       for (int z = 0; z <= 4; z++)
/* 1307 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$CastleSmallCorridorLeftTurnPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */