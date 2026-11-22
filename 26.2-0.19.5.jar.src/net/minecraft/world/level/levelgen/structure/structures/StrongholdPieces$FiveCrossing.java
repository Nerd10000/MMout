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
/*      */ import net.minecraft.world.level.block.SlabBlock;
/*      */ import net.minecraft.world.level.block.WallTorchBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.block.state.properties.SlabType;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ 
/*      */ public class FiveCrossing extends StrongholdPieces.StrongholdPiece {
/*      */   protected static final int WIDTH = 10;
/*      */   
/*      */   protected static final int HEIGHT = 9;
/*      */   
/*      */   protected static final int DEPTH = 11;
/*      */   
/*      */   private final boolean leftLow;
/*      */   
/*      */   private final boolean leftHigh;
/*      */   
/*      */   private final boolean rightLow;
/*      */   
/*      */   private final boolean rightHigh;
/*      */   
/*      */   public FiveCrossing(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1255 */     super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, genDepth, boundingBox);
/* 1257 */     setOrientation(direction);
/* 1258 */     this.entryDoor = randomSmallDoor(random);
/* 1260 */     this.leftLow = random.nextBoolean();
/* 1261 */     this.leftHigh = random.nextBoolean();
/* 1262 */     this.rightLow = random.nextBoolean();
/* 1263 */     this.rightHigh = (random.nextInt(3) > 0);
/*      */   }
/*      */   
/*      */   public FiveCrossing(CompoundTag tag) {
/* 1267 */     super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, tag);
/* 1268 */     this.leftLow = tag.getBooleanOr("leftLow", false);
/* 1269 */     this.leftHigh = tag.getBooleanOr("leftHigh", false);
/* 1270 */     this.rightLow = tag.getBooleanOr("rightLow", false);
/* 1271 */     this.rightHigh = tag.getBooleanOr("rightHigh", false);
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1276 */     super.addAdditionalSaveData(context, tag);
/* 1277 */     tag.putBoolean("leftLow", this.leftLow);
/* 1278 */     tag.putBoolean("leftHigh", this.leftHigh);
/* 1279 */     tag.putBoolean("rightLow", this.rightLow);
/* 1280 */     tag.putBoolean("rightHigh", this.rightHigh);
/*      */   }
/*      */   
/*      */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1285 */     int zOffA = 3;
/* 1286 */     int zOffB = 5;
/* 1288 */     Direction orientation = getOrientation();
/* 1289 */     if (orientation == Direction.WEST || orientation == Direction.NORTH) {
/* 1290 */       zOffA = 8 - zOffA;
/* 1291 */       zOffB = 8 - zOffB;
/*      */     } 
/* 1294 */     generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 5, 1);
/* 1295 */     if (this.leftLow)
/* 1296 */       generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, zOffA, 1); 
/* 1298 */     if (this.leftHigh)
/* 1299 */       generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, zOffB, 7); 
/* 1301 */     if (this.rightLow)
/* 1302 */       generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, zOffA, 1); 
/* 1304 */     if (this.rightHigh)
/* 1305 */       generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, zOffB, 7); 
/*      */   }
/*      */   
/*      */   public static FiveCrossing createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1310 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -3, 0, 10, 9, 11, direction);
/* 1312 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1313 */       return null; 
/* 1316 */     return new FiveCrossing(genDepth, random, box, direction);
/*      */   }
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1322 */     generateBox(level, chunkBB, 0, 0, 0, 9, 8, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1324 */     generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 3, 0);
/* 1327 */     if (this.leftLow)
/* 1328 */       generateBox(level, chunkBB, 0, 3, 1, 0, 5, 3, CAVE_AIR, CAVE_AIR, false); 
/* 1330 */     if (this.rightLow)
/* 1331 */       generateBox(level, chunkBB, 9, 3, 1, 9, 5, 3, CAVE_AIR, CAVE_AIR, false); 
/* 1333 */     if (this.leftHigh)
/* 1334 */       generateBox(level, chunkBB, 0, 5, 7, 0, 7, 9, CAVE_AIR, CAVE_AIR, false); 
/* 1336 */     if (this.rightHigh)
/* 1337 */       generateBox(level, chunkBB, 9, 5, 7, 9, 7, 9, CAVE_AIR, CAVE_AIR, false); 
/* 1339 */     generateBox(level, chunkBB, 5, 1, 10, 7, 3, 10, CAVE_AIR, CAVE_AIR, false);
/* 1342 */     generateBox(level, chunkBB, 1, 2, 1, 8, 2, 6, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1344 */     generateBox(level, chunkBB, 4, 1, 5, 4, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1345 */     generateBox(level, chunkBB, 8, 1, 5, 8, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1347 */     generateBox(level, chunkBB, 1, 4, 7, 3, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1350 */     generateBox(level, chunkBB, 1, 3, 5, 3, 3, 6, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1351 */     generateBox(level, chunkBB, 1, 3, 4, 3, 3, 4, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1352 */     generateBox(level, chunkBB, 1, 4, 6, 3, 4, 6, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1355 */     generateBox(level, chunkBB, 5, 1, 7, 7, 1, 8, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1356 */     generateBox(level, chunkBB, 5, 1, 9, 7, 1, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1357 */     generateBox(level, chunkBB, 5, 2, 7, 7, 2, 7, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1360 */     generateBox(level, chunkBB, 4, 5, 7, 4, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1361 */     generateBox(level, chunkBB, 8, 5, 7, 8, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1362 */     generateBox(level, chunkBB, 5, 5, 7, 7, 5, 9, (BlockState)Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue((Property)SlabBlock.TYPE, (Comparable)SlabType.DOUBLE), (BlockState)Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue((Property)SlabBlock.TYPE, (Comparable)SlabType.DOUBLE), false);
/* 1363 */     placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.SOUTH), 6, 5, 6, chunkBB);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$FiveCrossing.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */