/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ enum null {
/*     */   public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 294 */     return state.updateShape((LevelReader)level, (ScheduledTickAccess)level, pos, direction, neighbourPos, level.getBlockState(neighbourPos), level.getRandom());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\UpgradeData$BlockFixers$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */