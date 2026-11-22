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
/*     */ public class RightTurn extends StrongholdPieces.Turn {
/*     */   public RightTurn(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 819 */     super(StructurePieceType.STRONGHOLD_RIGHT_TURN, genDepth, boundingBox);
/* 821 */     setOrientation(direction);
/* 822 */     this.entryDoor = randomSmallDoor(random);
/*     */   }
/*     */   
/*     */   public RightTurn(CompoundTag tag) {
/* 826 */     super(StructurePieceType.STRONGHOLD_RIGHT_TURN, tag);
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 831 */     Direction orientation = getOrientation();
/* 832 */     if (orientation == Direction.NORTH || orientation == Direction.EAST) {
/* 833 */       generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*     */     } else {
/* 835 */       generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static RightTurn createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 840 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 5, direction);
/* 842 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 843 */       return null; 
/* 846 */     return new RightTurn(genDepth, random, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 852 */     generateBox(level, chunkBB, 0, 0, 0, 4, 4, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 854 */     generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/* 856 */     Direction orientation = getOrientation();
/* 857 */     if (orientation == Direction.NORTH || orientation == Direction.EAST) {
/* 858 */       generateBox(level, chunkBB, 4, 1, 1, 4, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*     */     } else {
/* 860 */       generateBox(level, chunkBB, 0, 1, 1, 0, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$RightTurn.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */