/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ 
/*    */ public class BuriedTreasurePieces {
/*    */   public static class BuriedTreasurePiece extends StructurePiece {
/*    */     public BuriedTreasurePiece(BlockPos offset) {
/* 24 */       super(StructurePieceType.BURIED_TREASURE_PIECE, 0, new BoundingBox(offset));
/*    */     }
/*    */     
/*    */     public BuriedTreasurePiece(CompoundTag tag) {
/* 28 */       super(StructurePieceType.BURIED_TREASURE_PIECE, tag);
/*    */     }
/*    */     
/*    */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {}
/*    */     
/*    */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 37 */       int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.boundingBox.minX(), this.boundingBox.minZ());
/* 38 */       BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(this.boundingBox.minX(), y, this.boundingBox.minZ());
/* 40 */       while (pos.getY() > level.getMinY()) {
/* 41 */         BlockState currentState = level.getBlockState((BlockPos)pos);
/* 42 */         BlockState belowState = level.getBlockState(pos.below());
/* 44 */         if (belowState.is(Blocks.SANDSTONE) || belowState
/* 45 */           .is(Blocks.STONE) || belowState
/* 46 */           .is(Blocks.ANDESITE) || belowState
/* 47 */           .is(Blocks.GRANITE) || belowState
/* 48 */           .is(Blocks.DIORITE)) {
/* 50 */           BlockState softState = (currentState.isAir() || isLiquid(currentState)) ? Blocks.SAND.defaultBlockState() : currentState;
/* 52 */           for (Direction direction : Direction.values()) {
/* 53 */             BlockPos relativePos = pos.relative(direction);
/* 54 */             BlockState relativeState = level.getBlockState(relativePos);
/* 56 */             if (relativeState.isAir() || isLiquid(relativeState)) {
/* 57 */               BlockPos belowRelativePos = relativePos.below();
/* 58 */               BlockState belowRelativeState = level.getBlockState(belowRelativePos);
/* 60 */               if ((belowRelativeState.isAir() || isLiquid(belowRelativeState)) && direction != Direction.UP) {
/* 61 */                 level.setBlock(relativePos, belowState, 3);
/*    */               } else {
/* 63 */                 level.setBlock(relativePos, softState, 3);
/*    */               } 
/*    */             } 
/*    */           } 
/* 67 */           this.boundingBox = new BoundingBox((BlockPos)pos);
/* 68 */           createChest((ServerLevelAccessor)level, chunkBB, random, (BlockPos)pos, BuiltInLootTables.BURIED_TREASURE, null);
/*    */           return;
/*    */         } 
/* 72 */         pos.move(0, -1, 0);
/*    */       } 
/*    */     }
/*    */     
/*    */     private static boolean isLiquid(BlockState blockState) {
/* 77 */       return (blockState.is(Blocks.WATER) || blockState
/* 78 */         .is(Blocks.LAVA));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\BuriedTreasurePieces.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */