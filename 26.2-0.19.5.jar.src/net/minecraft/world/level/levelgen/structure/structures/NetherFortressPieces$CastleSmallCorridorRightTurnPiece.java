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
/*      */ public class CastleSmallCorridorRightTurnPiece extends NetherFortressPieces.NetherBridgePiece {
/*      */   private static final int WIDTH = 5;
/*      */   
/*      */   private static final int HEIGHT = 7;
/*      */   
/*      */   private static final int DEPTH = 5;
/*      */   
/*      */   private boolean isNeedingChest;
/*      */   
/*      */   public CastleSmallCorridorRightTurnPiece(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1157 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, genDepth, boundingBox);
/* 1159 */     setOrientation(direction);
/* 1161 */     this.isNeedingChest = (random.nextInt(3) == 0);
/*      */   }
/*      */   
/*      */   public CastleSmallCorridorRightTurnPiece(CompoundTag tag) {
/* 1165 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, tag);
/* 1166 */     this.isNeedingChest = tag.getBooleanOr("Chest", false);
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1171 */     super.addAdditionalSaveData(context, tag);
/* 1173 */     tag.putBoolean("Chest", this.isNeedingChest);
/*      */   }
/*      */   
/*      */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1178 */     generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 1, true);
/*      */   }
/*      */   
/*      */   public static CastleSmallCorridorRightTurnPiece createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1182 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
/* 1184 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1185 */       return null; 
/* 1188 */     return new CastleSmallCorridorRightTurnPiece(genDepth, random, box, direction);
/*      */   }
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1194 */     generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1196 */     generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1198 */     BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1199 */     BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1202 */     generateBox(level, chunkBB, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1203 */     generateBox(level, chunkBB, 0, 3, 1, 0, 4, 1, nsFence, nsFence, false);
/* 1204 */     generateBox(level, chunkBB, 0, 3, 3, 0, 4, 3, nsFence, nsFence, false);
/* 1206 */     generateBox(level, chunkBB, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1208 */     generateBox(level, chunkBB, 1, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1209 */     generateBox(level, chunkBB, 1, 3, 4, 1, 4, 4, weFence, weFence, false);
/* 1210 */     generateBox(level, chunkBB, 3, 3, 4, 3, 4, 4, weFence, weFence, false);
/* 1212 */     if (this.isNeedingChest && 
/* 1213 */       chunkBB.isInside((Vec3i)getWorldPos(1, 2, 3))) {
/* 1214 */       this.isNeedingChest = false;
/* 1215 */       createChest(level, chunkBB, random, 1, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
/*      */     } 
/* 1220 */     generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1223 */     for (int x = 0; x <= 4; x++) {
/* 1224 */       for (int z = 0; z <= 4; z++)
/* 1225 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$CastleSmallCorridorRightTurnPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */