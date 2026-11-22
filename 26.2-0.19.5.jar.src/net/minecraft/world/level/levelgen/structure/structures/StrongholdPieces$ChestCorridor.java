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
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class ChestCorridor extends StrongholdPieces.StrongholdPiece {
/*     */   private static final int WIDTH = 5;
/*     */   
/*     */   private static final int HEIGHT = 5;
/*     */   
/*     */   private static final int DEPTH = 7;
/*     */   
/*     */   private boolean hasPlacedChest;
/*     */   
/*     */   public ChestCorridor(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 640 */     super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, genDepth, boundingBox);
/* 642 */     setOrientation(direction);
/* 643 */     this.entryDoor = randomSmallDoor(random);
/*     */   }
/*     */   
/*     */   public ChestCorridor(CompoundTag tag) {
/* 647 */     super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, tag);
/* 648 */     this.hasPlacedChest = tag.getBooleanOr("Chest", false);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 653 */     super.addAdditionalSaveData(context, tag);
/* 654 */     tag.putBoolean("Chest", this.hasPlacedChest);
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 659 */     generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*     */   }
/*     */   
/*     */   public static ChestCorridor createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 663 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 7, direction);
/* 665 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 666 */       return null; 
/* 669 */     return new ChestCorridor(genDepth, random, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 675 */     generateBox(level, chunkBB, 0, 0, 0, 4, 4, 6, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 677 */     generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/* 679 */     generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 6);
/* 682 */     generateBox(level, chunkBB, 3, 1, 2, 3, 1, 4, Blocks.STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICKS.defaultBlockState(), false);
/* 683 */     placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 1, chunkBB);
/* 684 */     placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 5, chunkBB);
/* 685 */     placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 2, chunkBB);
/* 686 */     placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 4, chunkBB);
/* 687 */     for (int z = 2; z <= 4; z++)
/* 688 */       placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 2, 1, z, chunkBB); 
/* 691 */     if (!this.hasPlacedChest && 
/* 692 */       chunkBB.isInside((Vec3i)getWorldPos(3, 2, 3))) {
/* 693 */       this.hasPlacedChest = true;
/* 694 */       createChest(level, chunkBB, random, 3, 2, 3, BuiltInLootTables.STRONGHOLD_CORRIDOR);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$ChestCorridor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */