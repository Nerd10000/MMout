/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*     */ import net.minecraft.world.level.block.StemBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ enum null {
/*     */   public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 380 */     if (((Integer)state.getValue((Property)StemBlock.AGE)).intValue() == 7) {
/* 381 */       Block fruit = state.is(Blocks.PUMPKIN_STEM) ? Blocks.PUMPKIN : Blocks.MELON;
/* 382 */       if (neighbour.is(fruit))
/* 383 */         return (BlockState)(state.is(Blocks.PUMPKIN_STEM) ? Blocks.ATTACHED_PUMPKIN_STEM : Blocks.ATTACHED_MELON_STEM).defaultBlockState().setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)direction); 
/*     */     } 
/* 386 */     return state;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\UpgradeData$BlockFixers$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */