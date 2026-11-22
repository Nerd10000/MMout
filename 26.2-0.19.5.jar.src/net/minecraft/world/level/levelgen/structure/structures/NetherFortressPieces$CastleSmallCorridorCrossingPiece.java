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
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ 
/*      */ public class CastleSmallCorridorCrossingPiece extends NetherFortressPieces.NetherBridgePiece {
/*      */   private static final int WIDTH = 5;
/*      */   
/*      */   private static final int HEIGHT = 7;
/*      */   
/*      */   private static final int DEPTH = 5;
/*      */   
/*      */   public CastleSmallCorridorCrossingPiece(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1098 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, genDepth, boundingBox);
/* 1100 */     setOrientation(direction);
/*      */   }
/*      */   
/*      */   public CastleSmallCorridorCrossingPiece(CompoundTag tag) {
/* 1104 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, tag);
/*      */   }
/*      */   
/*      */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1109 */     generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 0, true);
/* 1110 */     generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 1, true);
/* 1111 */     generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 1, true);
/*      */   }
/*      */   
/*      */   public static CastleSmallCorridorCrossingPiece createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1115 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
/* 1117 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1118 */       return null; 
/* 1121 */     return new CastleSmallCorridorCrossingPiece(genDepth, box, direction);
/*      */   }
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1127 */     generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1129 */     generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1132 */     generateBox(level, chunkBB, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1133 */     generateBox(level, chunkBB, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1134 */     generateBox(level, chunkBB, 0, 2, 4, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1135 */     generateBox(level, chunkBB, 4, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1138 */     generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1141 */     for (int x = 0; x <= 4; x++) {
/* 1142 */       for (int z = 0; z <= 4; z++)
/* 1143 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$CastleSmallCorridorCrossingPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */