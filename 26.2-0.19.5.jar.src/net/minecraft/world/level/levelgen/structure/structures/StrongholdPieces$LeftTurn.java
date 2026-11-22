/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public class LeftTurn extends StrongholdPieces.Turn {
/*     */   public LeftTurn(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 771 */     super(StructurePieceType.STRONGHOLD_LEFT_TURN, genDepth, boundingBox);
/* 773 */     setOrientation(direction);
/* 774 */     this.entryDoor = randomSmallDoor(random);
/*     */   }
/*     */   
/*     */   public LeftTurn(CompoundTag tag) {
/* 778 */     super(StructurePieceType.STRONGHOLD_LEFT_TURN, tag);
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 783 */     Direction orientation = getOrientation();
/* 784 */     if (orientation == Direction.NORTH || orientation == Direction.EAST) {
/* 785 */       generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*     */     } else {
/* 787 */       generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static LeftTurn createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 792 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 5, direction);
/* 794 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 795 */       return null; 
/* 798 */     return new LeftTurn(genDepth, random, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 804 */     generateBox(level, chunkBB, 0, 0, 0, 4, 4, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 806 */     generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/* 808 */     Direction orientation = getOrientation();
/* 809 */     if (orientation == Direction.NORTH || orientation == Direction.EAST) {
/* 810 */       generateBox(level, chunkBB, 0, 1, 1, 0, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*     */     } else {
/* 812 */       generateBox(level, chunkBB, 4, 1, 1, 4, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$LeftTurn.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */