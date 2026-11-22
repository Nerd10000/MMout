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
/*      */ public class CastleStalkRoom extends NetherFortressPieces.NetherBridgePiece {
/*      */   private static final int WIDTH = 13;
/*      */   
/*      */   private static final int HEIGHT = 14;
/*      */   
/*      */   private static final int DEPTH = 13;
/*      */   
/*      */   public CastleStalkRoom(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  884 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, genDepth, boundingBox);
/*  886 */     setOrientation(direction);
/*      */   }
/*      */   
/*      */   public CastleStalkRoom(CompoundTag tag) {
/*  890 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, tag);
/*      */   }
/*      */   
/*      */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  895 */     generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 5, 3, true);
/*  896 */     generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 5, 11, true);
/*      */   }
/*      */   
/*      */   public static CastleStalkRoom createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  900 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -5, -3, 0, 13, 14, 13, direction);
/*  902 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  903 */       return null; 
/*  906 */     return new CastleStalkRoom(genDepth, box, direction);
/*      */   }
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  912 */     generateBox(level, chunkBB, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  914 */     generateBox(level, chunkBB, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  917 */     generateBox(level, chunkBB, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  918 */     generateBox(level, chunkBB, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  919 */     generateBox(level, chunkBB, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  920 */     generateBox(level, chunkBB, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  921 */     generateBox(level, chunkBB, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  922 */     generateBox(level, chunkBB, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  923 */     generateBox(level, chunkBB, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  924 */     generateBox(level, chunkBB, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  927 */     generateBox(level, chunkBB, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  929 */     BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  930 */     BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*  931 */     BlockState nswFence = (BlockState)nsFence.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/*  932 */     BlockState nseFence = (BlockState)nsFence.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  935 */     for (int i = 1; i <= 11; i += 2) {
/*  936 */       generateBox(level, chunkBB, i, 10, 0, i, 11, 0, weFence, weFence, false);
/*  937 */       generateBox(level, chunkBB, i, 10, 12, i, 11, 12, weFence, weFence, false);
/*  938 */       generateBox(level, chunkBB, 0, 10, i, 0, 11, i, nsFence, nsFence, false);
/*  939 */       generateBox(level, chunkBB, 12, 10, i, 12, 11, i, nsFence, nsFence, false);
/*  940 */       placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 0, chunkBB);
/*  941 */       placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 12, chunkBB);
/*  942 */       placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, i, chunkBB);
/*  943 */       placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, i, chunkBB);
/*  944 */       if (i != 11) {
/*  945 */         placeBlock(level, weFence, i + 1, 13, 0, chunkBB);
/*  946 */         placeBlock(level, weFence, i + 1, 13, 12, chunkBB);
/*  947 */         placeBlock(level, nsFence, 0, 13, i + 1, chunkBB);
/*  948 */         placeBlock(level, nsFence, 12, 13, i + 1, chunkBB);
/*      */       } 
/*      */     } 
/*  951 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 0, chunkBB);
/*  952 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 12, chunkBB);
/*  953 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 12, chunkBB);
/*  954 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 0, chunkBB);
/*  957 */     for (int z = 3; z <= 9; z += 2) {
/*  958 */       generateBox(level, chunkBB, 1, 7, z, 1, 8, z, nswFence, nswFence, false);
/*  959 */       generateBox(level, chunkBB, 11, 7, z, 11, 8, z, nseFence, nseFence, false);
/*      */     } 
/*  963 */     BlockState stairs = (BlockState)Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH);
/*  964 */     for (int j = 0; j <= 6; j++) {
/*  965 */       int m = j + 4;
/*  966 */       for (int n = 5; n <= 7; n++)
/*  967 */         placeBlock(level, stairs, n, 5 + j, m, chunkBB); 
/*  969 */       if (m >= 5 && m <= 8) {
/*  970 */         generateBox(level, chunkBB, 5, 5, m, 7, j + 4, m, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  971 */       } else if (m >= 9 && m <= 10) {
/*  972 */         generateBox(level, chunkBB, 5, 8, m, 7, j + 4, m, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       } 
/*  974 */       if (j >= 1)
/*  975 */         generateBox(level, chunkBB, 5, 6 + j, m, 7, 9 + j, m, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false); 
/*      */     } 
/*  978 */     for (int x = 5; x <= 7; x++)
/*  979 */       placeBlock(level, stairs, x, 12, 11, chunkBB); 
/*  981 */     generateBox(level, chunkBB, 5, 6, 7, 5, 7, 7, nseFence, nseFence, false);
/*  982 */     generateBox(level, chunkBB, 7, 6, 7, 7, 7, 7, nswFence, nswFence, false);
/*  983 */     generateBox(level, chunkBB, 5, 13, 12, 7, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  986 */     generateBox(level, chunkBB, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  987 */     generateBox(level, chunkBB, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  988 */     generateBox(level, chunkBB, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  989 */     generateBox(level, chunkBB, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  990 */     generateBox(level, chunkBB, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  991 */     generateBox(level, chunkBB, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  992 */     BlockState eastStairs = (BlockState)stairs.setValue((Property)StairBlock.FACING, (Comparable)Direction.EAST);
/*  993 */     BlockState westStairs = (BlockState)stairs.setValue((Property)StairBlock.FACING, (Comparable)Direction.WEST);
/*  994 */     placeBlock(level, westStairs, 4, 5, 2, chunkBB);
/*  995 */     placeBlock(level, westStairs, 4, 5, 3, chunkBB);
/*  996 */     placeBlock(level, westStairs, 4, 5, 9, chunkBB);
/*  997 */     placeBlock(level, westStairs, 4, 5, 10, chunkBB);
/*  998 */     placeBlock(level, eastStairs, 8, 5, 2, chunkBB);
/*  999 */     placeBlock(level, eastStairs, 8, 5, 3, chunkBB);
/* 1000 */     placeBlock(level, eastStairs, 8, 5, 9, chunkBB);
/* 1001 */     placeBlock(level, eastStairs, 8, 5, 10, chunkBB);
/* 1004 */     generateBox(level, chunkBB, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
/* 1005 */     generateBox(level, chunkBB, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
/* 1006 */     generateBox(level, chunkBB, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
/* 1007 */     generateBox(level, chunkBB, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
/* 1010 */     generateBox(level, chunkBB, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1011 */     generateBox(level, chunkBB, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1013 */     generateBox(level, chunkBB, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1014 */     generateBox(level, chunkBB, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1015 */     generateBox(level, chunkBB, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1016 */     generateBox(level, chunkBB, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */     int k;
/* 1018 */     for (k = 4; k <= 8; k++) {
/* 1019 */       for (int m = 0; m <= 2; m++) {
/* 1020 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), k, -1, m, chunkBB);
/* 1021 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), k, -1, 12 - m, chunkBB);
/*      */       } 
/*      */     } 
/* 1024 */     for (k = 0; k <= 2; k++) {
/* 1025 */       for (int m = 4; m <= 8; m++) {
/* 1026 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), k, -1, m, chunkBB);
/* 1027 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - k, -1, m, chunkBB);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$CastleStalkRoom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */