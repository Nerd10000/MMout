/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ enum null {
/*     */   public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 300 */     if (neighbour.is(state.getBlock()) && direction.getAxis().isHorizontal() && 
/* 301 */       state.getValue((Property)ChestBlock.TYPE) == ChestType.SINGLE && neighbour.getValue((Property)ChestBlock.TYPE) == ChestType.SINGLE) {
/* 302 */       Direction facing = (Direction)state.getValue((Property)ChestBlock.FACING);
/* 303 */       if (direction.getAxis() != facing.getAxis() && facing == neighbour.getValue((Property)ChestBlock.FACING)) {
/* 304 */         ChestType newType = (direction == facing.getClockWise()) ? ChestType.LEFT : ChestType.RIGHT;
/* 305 */         level.setBlock(neighbourPos, (BlockState)neighbour.setValue((Property)ChestBlock.TYPE, (Comparable)newType.getOpposite()), 18);
/* 308 */         if (facing == Direction.NORTH || facing == Direction.EAST) {
/* 309 */           BlockEntity one = level.getBlockEntity(pos);
/* 310 */           BlockEntity two = level.getBlockEntity(neighbourPos);
/* 311 */           if (one instanceof ChestBlockEntity) {
/* 311 */             ChestBlockEntity firstChestBlockEntity = (ChestBlockEntity)one;
/* 311 */             if (two instanceof ChestBlockEntity) {
/* 311 */               ChestBlockEntity secondChestBlockEntity = (ChestBlockEntity)two;
/* 312 */               ChestBlockEntity.swapContents(firstChestBlockEntity, secondChestBlockEntity);
/*     */             } 
/*     */           } 
/*     */         } 
/* 316 */         return (BlockState)state.setValue((Property)ChestBlock.TYPE, (Comparable)newType);
/*     */       } 
/*     */     } 
/* 320 */     return state;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\UpgradeData$BlockFixers$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */