/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
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
/*     */ 
/*     */ public class BridgeStraight extends NetherFortressPieces.NetherBridgePiece {
/*     */   private static final int WIDTH = 5;
/*     */   
/*     */   private static final int HEIGHT = 10;
/*     */   
/*     */   private static final int DEPTH = 19;
/*     */   
/*     */   public BridgeStraight(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 292 */     super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, genDepth, boundingBox);
/* 294 */     setOrientation(direction);
/*     */   }
/*     */   
/*     */   public BridgeStraight(CompoundTag tag) {
/* 298 */     super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, tag);
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 303 */     generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 3, false);
/*     */   }
/*     */   
/*     */   public static BridgeStraight createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 307 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -3, 0, 5, 10, 19, direction);
/* 309 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 310 */       return null; 
/* 313 */     return new BridgeStraight(genDepth, random, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 319 */     generateBox(level, chunkBB, 0, 3, 0, 4, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 321 */     generateBox(level, chunkBB, 1, 5, 0, 3, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 324 */     generateBox(level, chunkBB, 0, 5, 0, 0, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 325 */     generateBox(level, chunkBB, 4, 5, 0, 4, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 328 */     generateBox(level, chunkBB, 0, 2, 0, 4, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 329 */     generateBox(level, chunkBB, 0, 2, 13, 4, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 330 */     generateBox(level, chunkBB, 0, 0, 0, 4, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 331 */     generateBox(level, chunkBB, 0, 0, 15, 4, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 333 */     for (int x = 0; x <= 4; x++) {
/* 334 */       for (int z = 0; z <= 2; z++) {
/* 335 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/* 336 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, 18 - z, chunkBB);
/*     */       } 
/*     */     } 
/* 340 */     BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 341 */     BlockState nseFence = (BlockState)nsFence.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 342 */     BlockState nswFence = (BlockState)nsFence.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/* 343 */     generateBox(level, chunkBB, 0, 1, 1, 0, 4, 1, nseFence, nseFence, false);
/* 344 */     generateBox(level, chunkBB, 0, 3, 4, 0, 4, 4, nseFence, nseFence, false);
/* 345 */     generateBox(level, chunkBB, 0, 3, 14, 0, 4, 14, nseFence, nseFence, false);
/* 346 */     generateBox(level, chunkBB, 0, 1, 17, 0, 4, 17, nseFence, nseFence, false);
/* 347 */     generateBox(level, chunkBB, 4, 1, 1, 4, 4, 1, nswFence, nswFence, false);
/* 348 */     generateBox(level, chunkBB, 4, 3, 4, 4, 4, 4, nswFence, nswFence, false);
/* 349 */     generateBox(level, chunkBB, 4, 3, 14, 4, 4, 14, nswFence, nswFence, false);
/* 350 */     generateBox(level, chunkBB, 4, 1, 17, 4, 4, 17, nswFence, nswFence, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$BridgeStraight.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */