/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class DefaultSpreaderConfig implements MultifaceSpreader.SpreadConfig {
/*     */   protected final MultifaceBlock block;
/*     */   
/*     */   public DefaultSpreaderConfig(MultifaceBlock block) {
/* 143 */     this.block = block;
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockState oldState, BlockGetter level, BlockPos placementPos, Direction placementDirection) {
/* 148 */     return this.block.getStateForPlacement(oldState, level, placementPos, placementDirection);
/*     */   }
/*     */   
/*     */   protected boolean stateCanBeReplaced(BlockGetter level, BlockPos sourcePos, BlockPos placementPos, Direction placementDirection, BlockState existingState) {
/* 152 */     return (existingState.isAir() || existingState.is(this.block) || (existingState.is(Blocks.WATER) && existingState.getFluidState().isSource()));
/*     */   }
/*     */   
/*     */   public boolean canSpreadInto(BlockGetter level, BlockPos sourcePos, MultifaceSpreader.SpreadPos spreadPos) {
/* 157 */     BlockState existingState = level.getBlockState(spreadPos.pos());
/* 158 */     return (stateCanBeReplaced(level, sourcePos, spreadPos.pos(), spreadPos.face(), existingState) && this.block.isValidStateForPlacement(level, existingState, spreadPos.pos(), spreadPos.face()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\MultifaceSpreader$DefaultSpreaderConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */