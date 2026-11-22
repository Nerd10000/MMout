/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.BlockColumn;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ 
/*     */ class null implements BlockColumn {
/*     */   null(SurfaceSystem this$0) {}
/*     */   
/*     */   public BlockState getBlock(int blockY) {
/*  91 */     return protoChunk.getBlockState((BlockPos)columnPos.setY(blockY));
/*     */   }
/*     */   
/*     */   public void setBlock(int blockY, BlockState state) {
/*  96 */     LevelHeightAccessor heightAccessor = protoChunk.getHeightAccessorForGeneration();
/*  97 */     if (heightAccessor.isInsideBuildHeight(blockY)) {
/*  98 */       protoChunk.setBlockState((BlockPos)columnPos.setY(blockY), state);
/*  99 */       if (!state.getFluidState().isEmpty())
/* 100 */         protoChunk.markPosForPostProcessing((BlockPos)columnPos); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 107 */     return "ChunkBlockColumn " + String.valueOf(chunkPos);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceSystem$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */