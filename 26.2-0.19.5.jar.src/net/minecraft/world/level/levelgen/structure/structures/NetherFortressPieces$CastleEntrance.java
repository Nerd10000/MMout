/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FenceBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class CastleEntrance extends NetherFortressPieces.NetherBridgePiece {
/*     */   private static final int WIDTH = 13;
/*     */   
/*     */   private static final int HEIGHT = 14;
/*     */   
/*     */   private static final int DEPTH = 13;
/*     */   
/*     */   public CastleEntrance(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 765 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, genDepth, boundingBox);
/* 767 */     setOrientation(direction);
/*     */   }
/*     */   
/*     */   public CastleEntrance(CompoundTag tag) {
/* 771 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, tag);
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 776 */     generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 5, 3, true);
/*     */   }
/*     */   
/*     */   public static CastleEntrance createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 780 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -5, -3, 0, 13, 14, 13, direction);
/* 782 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 783 */       return null; 
/* 786 */     return new CastleEntrance(genDepth, random, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 792 */     generateBox(level, chunkBB, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 794 */     generateBox(level, chunkBB, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 797 */     generateBox(level, chunkBB, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 798 */     generateBox(level, chunkBB, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 799 */     generateBox(level, chunkBB, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 800 */     generateBox(level, chunkBB, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 801 */     generateBox(level, chunkBB, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 802 */     generateBox(level, chunkBB, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 803 */     generateBox(level, chunkBB, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 804 */     generateBox(level, chunkBB, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 807 */     generateBox(level, chunkBB, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 810 */     generateBox(level, chunkBB, 5, 8, 0, 7, 8, 0, Blocks.NETHER_BRICK_FENCE.defaultBlockState(), Blocks.NETHER_BRICK_FENCE.defaultBlockState(), false);
/* 812 */     BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 813 */     BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 816 */     for (int i = 1; i <= 11; i += 2) {
/* 817 */       generateBox(level, chunkBB, i, 10, 0, i, 11, 0, weFence, weFence, false);
/* 818 */       generateBox(level, chunkBB, i, 10, 12, i, 11, 12, weFence, weFence, false);
/* 819 */       generateBox(level, chunkBB, 0, 10, i, 0, 11, i, nsFence, nsFence, false);
/* 820 */       generateBox(level, chunkBB, 12, 10, i, 12, 11, i, nsFence, nsFence, false);
/* 821 */       placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 0, chunkBB);
/* 822 */       placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 12, chunkBB);
/* 823 */       placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, i, chunkBB);
/* 824 */       placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, i, chunkBB);
/* 825 */       if (i != 11) {
/* 826 */         placeBlock(level, weFence, i + 1, 13, 0, chunkBB);
/* 827 */         placeBlock(level, weFence, i + 1, 13, 12, chunkBB);
/* 828 */         placeBlock(level, nsFence, 0, 13, i + 1, chunkBB);
/* 829 */         placeBlock(level, nsFence, 12, 13, i + 1, chunkBB);
/*     */       } 
/*     */     } 
/* 832 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 0, chunkBB);
/* 833 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 12, chunkBB);
/* 834 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 12, chunkBB);
/* 835 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 0, chunkBB);
/* 838 */     for (int z = 3; z <= 9; z += 2) {
/* 839 */       generateBox(level, chunkBB, 1, 7, z, 1, 8, z, (BlockState)nsFence.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), (BlockState)nsFence.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), false);
/* 840 */       generateBox(level, chunkBB, 11, 7, z, 11, 8, z, (BlockState)nsFence.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), (BlockState)nsFence.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), false);
/*     */     } 
/* 844 */     generateBox(level, chunkBB, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 845 */     generateBox(level, chunkBB, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 847 */     generateBox(level, chunkBB, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 848 */     generateBox(level, chunkBB, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 849 */     generateBox(level, chunkBB, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 850 */     generateBox(level, chunkBB, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*     */     int x;
/* 852 */     for (x = 4; x <= 8; x++) {
/* 853 */       for (int j = 0; j <= 2; j++) {
/* 854 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, j, chunkBB);
/* 855 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, 12 - j, chunkBB);
/*     */       } 
/*     */     } 
/* 858 */     for (x = 0; x <= 2; x++) {
/* 859 */       for (int j = 4; j <= 8; j++) {
/* 860 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, j, chunkBB);
/* 861 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - x, -1, j, chunkBB);
/*     */       } 
/*     */     } 
/* 866 */     generateBox(level, chunkBB, 5, 5, 5, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 867 */     generateBox(level, chunkBB, 6, 1, 6, 6, 4, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 868 */     placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 6, 0, 6, chunkBB);
/* 869 */     placeBlock(level, Blocks.LAVA.defaultBlockState(), 6, 5, 6, chunkBB);
/* 871 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(6, 5, 6);
/* 872 */     if (chunkBB.isInside((Vec3i)mutableBlockPos))
/* 873 */       level.scheduleTick((BlockPos)mutableBlockPos, (Fluid)Fluids.LAVA, 0); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$CastleEntrance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */