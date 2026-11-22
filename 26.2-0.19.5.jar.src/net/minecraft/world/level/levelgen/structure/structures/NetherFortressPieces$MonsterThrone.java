/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FenceBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public class MonsterThrone extends NetherFortressPieces.NetherBridgePiece {
/*     */   private static final int WIDTH = 7;
/*     */   
/*     */   private static final int HEIGHT = 8;
/*     */   
/*     */   private static final int DEPTH = 9;
/*     */   
/*     */   private boolean hasPlacedSpawner;
/*     */   
/*     */   public MonsterThrone(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 665 */     super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, genDepth, boundingBox);
/* 667 */     setOrientation(direction);
/*     */   }
/*     */   
/*     */   public MonsterThrone(CompoundTag tag) {
/* 671 */     super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, tag);
/* 672 */     this.hasPlacedSpawner = tag.getBooleanOr("Mob", false);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 677 */     super.addAdditionalSaveData(context, tag);
/* 679 */     tag.putBoolean("Mob", this.hasPlacedSpawner);
/*     */   }
/*     */   
/*     */   public static MonsterThrone createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, int genDepth, Direction direction) {
/* 683 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -2, 0, 0, 7, 8, 9, direction);
/* 685 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 686 */       return null; 
/* 689 */     return new MonsterThrone(genDepth, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 695 */     generateBox(level, chunkBB, 0, 2, 0, 6, 7, 7, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 698 */     generateBox(level, chunkBB, 1, 0, 0, 5, 1, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 699 */     generateBox(level, chunkBB, 1, 2, 1, 5, 2, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 700 */     generateBox(level, chunkBB, 1, 3, 2, 5, 3, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 701 */     generateBox(level, chunkBB, 1, 4, 3, 5, 4, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 704 */     generateBox(level, chunkBB, 1, 2, 0, 1, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 705 */     generateBox(level, chunkBB, 5, 2, 0, 5, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 706 */     generateBox(level, chunkBB, 1, 5, 2, 1, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 707 */     generateBox(level, chunkBB, 5, 5, 2, 5, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 708 */     generateBox(level, chunkBB, 0, 5, 3, 0, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 709 */     generateBox(level, chunkBB, 6, 5, 3, 6, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 710 */     generateBox(level, chunkBB, 1, 5, 8, 5, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 712 */     BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 713 */     BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 715 */     placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 1, 6, 3, chunkBB);
/* 716 */     placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 5, 6, 3, chunkBB);
/* 718 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true))).setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 0, 6, 3, chunkBB);
/* 719 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 6, 6, 3, chunkBB);
/* 721 */     generateBox(level, chunkBB, 0, 6, 4, 0, 6, 7, nsFence, nsFence, false);
/* 722 */     generateBox(level, chunkBB, 6, 6, 4, 6, 6, 7, nsFence, nsFence, false);
/* 724 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 0, 6, 8, chunkBB);
/* 725 */     placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 6, 6, 8, chunkBB);
/* 727 */     generateBox(level, chunkBB, 1, 6, 8, 5, 6, 8, weFence, weFence, false);
/* 729 */     placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 1, 7, 8, chunkBB);
/* 730 */     generateBox(level, chunkBB, 2, 7, 8, 4, 7, 8, weFence, weFence, false);
/* 731 */     placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 5, 7, 8, chunkBB);
/* 733 */     placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 2, 8, 8, chunkBB);
/* 734 */     placeBlock(level, weFence, 3, 8, 8, chunkBB);
/* 735 */     placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 4, 8, 8, chunkBB);
/* 737 */     if (!this.hasPlacedSpawner) {
/* 738 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(3, 5, 5);
/* 739 */       if (chunkBB.isInside((Vec3i)mutableBlockPos)) {
/* 740 */         this.hasPlacedSpawner = true;
/* 741 */         level.setBlock((BlockPos)mutableBlockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/* 743 */         BlockEntity blockEntity = level.getBlockEntity((BlockPos)mutableBlockPos);
/* 744 */         if (blockEntity instanceof SpawnerBlockEntity) {
/* 744 */           SpawnerBlockEntity spawner = (SpawnerBlockEntity)blockEntity;
/* 745 */           spawner.setEntityId(EntityTypes.BLAZE, random);
/*     */         } 
/*     */       } 
/*     */     } 
/* 751 */     for (int x = 0; x <= 6; x++) {
/* 752 */       for (int z = 0; z <= 6; z++)
/* 753 */         fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$MonsterThrone.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */