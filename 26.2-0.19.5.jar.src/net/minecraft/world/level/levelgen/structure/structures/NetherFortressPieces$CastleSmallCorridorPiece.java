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
/*      */ public class CastleSmallCorridorPiece extends NetherFortressPieces.NetherBridgePiece {
/*      */   private static final int WIDTH = 5;
/*      */   
/*      */   private static final int HEIGHT = 7;
/*      */   
/*      */   private static final int DEPTH = 5;
/*      */   
/*      */   public CastleSmallCorridorPiece(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1039 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, genDepth, boundingBox);
/* 1041 */     setOrientation(direction);
/*      */   }
/*      */   
/*      */   public CastleSmallCorridorPiece(CompoundTag tag) {
/* 1045 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, tag);
/*      */   }
/*      */   
/*      */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1050 */     generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 0, true);
/*      */   }
/*      */   
/*      */   public static CastleSmallCorridorPiece createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1054 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
/* 1056 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1057 */       return null; 
/* 1060 */     return new CastleSmallCorridorPiece(genDepth, box, direction);
/*      */   }
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1066 */     generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1068 */     generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1070 */     BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1073 */     generateBox(level, chunkBB, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1074 */     generateBox(level, chunkBB, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1075 */     generateBox(level, chunkBB, 0, 3, 1, 0, 4, 1, nsFence, nsFence, false);
/* 1076 */     generateBox(level, chunkBB, 0, 3, 3, 0, 4, 3, nsFence, nsFence, false);
/* 1077 */     generateBox(level, chunkBB, 4, 3, 1, 4, 4, 1, nsFence, nsFence, false);
/* 1078 */     generateBox(level, chunkBB, 4, 3, 3, 4, 4, 3, nsFence, nsFence, false);
/* 1081 */     generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1084 */     for (int x = 0; x <= 4; x++) {
/* 1085 */       for (int z = 0; z <= 4; z++)
/* 1086 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$CastleSmallCorridorPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */