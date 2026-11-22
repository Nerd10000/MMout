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
/*     */ import net.minecraft.world.level.block.WallTorchBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public class Straight extends StrongholdPieces.StrongholdPiece {
/*     */   private static final int WIDTH = 5;
/*     */   
/*     */   private static final int HEIGHT = 5;
/*     */   
/*     */   private static final int DEPTH = 7;
/*     */   
/*     */   private final boolean leftChild;
/*     */   
/*     */   private final boolean rightChild;
/*     */   
/*     */   public Straight(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 563 */     super(StructurePieceType.STRONGHOLD_STRAIGHT, genDepth, boundingBox);
/* 565 */     setOrientation(direction);
/* 566 */     this.entryDoor = randomSmallDoor(random);
/* 568 */     this.leftChild = (random.nextInt(2) == 0);
/* 569 */     this.rightChild = (random.nextInt(2) == 0);
/*     */   }
/*     */   
/*     */   public Straight(CompoundTag tag) {
/* 573 */     super(StructurePieceType.STRONGHOLD_STRAIGHT, tag);
/* 574 */     this.leftChild = tag.getBooleanOr("Left", false);
/* 575 */     this.rightChild = tag.getBooleanOr("Right", false);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 580 */     super.addAdditionalSaveData(context, tag);
/* 581 */     tag.putBoolean("Left", this.leftChild);
/* 582 */     tag.putBoolean("Right", this.rightChild);
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 587 */     generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/* 588 */     if (this.leftChild)
/* 589 */       generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 2); 
/* 591 */     if (this.rightChild)
/* 592 */       generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 2); 
/*     */   }
/*     */   
/*     */   public static Straight createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 597 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 7, direction);
/* 599 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 600 */       return null; 
/* 603 */     return new Straight(genDepth, random, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 609 */     generateBox(level, chunkBB, 0, 0, 0, 4, 4, 6, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 611 */     generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/* 613 */     generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 6);
/* 615 */     BlockState eastTorch = (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.EAST);
/* 616 */     BlockState westTorch = (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.WEST);
/* 618 */     maybeGenerateBlock(level, chunkBB, random, 0.1F, 1, 2, 1, eastTorch);
/* 619 */     maybeGenerateBlock(level, chunkBB, random, 0.1F, 3, 2, 1, westTorch);
/* 620 */     maybeGenerateBlock(level, chunkBB, random, 0.1F, 1, 2, 5, eastTorch);
/* 621 */     maybeGenerateBlock(level, chunkBB, random, 0.1F, 3, 2, 5, westTorch);
/* 623 */     if (this.leftChild)
/* 624 */       generateBox(level, chunkBB, 0, 1, 2, 0, 3, 4, CAVE_AIR, CAVE_AIR, false); 
/* 626 */     if (this.rightChild)
/* 627 */       generateBox(level, chunkBB, 4, 1, 2, 4, 3, 4, CAVE_AIR, CAVE_AIR, false); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$Straight.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */