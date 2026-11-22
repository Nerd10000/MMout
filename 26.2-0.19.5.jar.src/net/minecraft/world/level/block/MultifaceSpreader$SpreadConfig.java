/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public interface SpreadConfig {
/*     */   BlockState getStateForPlacement(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection);
/*     */   
/*     */   boolean canSpreadInto(BlockGetter paramBlockGetter, BlockPos paramBlockPos, MultifaceSpreader.SpreadPos paramSpreadPos);
/*     */   
/*     */   default MultifaceSpreader.SpreadType[] getSpreadTypes() {
/* 111 */     return MultifaceSpreader.DEFAULT_SPREAD_ORDER;
/*     */   }
/*     */   
/*     */   default boolean hasFace(BlockState state, Direction face) {
/* 115 */     return MultifaceBlock.hasFace(state, face);
/*     */   }
/*     */   
/*     */   default boolean isOtherBlockValidAsSource(BlockState state) {
/* 119 */     return false;
/*     */   }
/*     */   
/*     */   default boolean canSpreadFrom(BlockState state, Direction face) {
/* 123 */     return (isOtherBlockValidAsSource(state) || hasFace(state, face));
/*     */   }
/*     */   
/*     */   default boolean placeBlock(LevelAccessor level, MultifaceSpreader.SpreadPos spreadPos, BlockState oldState, boolean postProcess) {
/* 127 */     BlockState spreadState = getStateForPlacement(oldState, (BlockGetter)level, spreadPos.pos(), spreadPos.face());
/* 128 */     if (spreadState != null) {
/* 130 */       if (postProcess)
/* 131 */         level.getChunk(spreadPos.pos()).markPosForPostProcessing(spreadPos.pos()); 
/* 133 */       return level.setBlock(spreadPos.pos(), spreadState, 2);
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\MultifaceSpreader$SpreadConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */