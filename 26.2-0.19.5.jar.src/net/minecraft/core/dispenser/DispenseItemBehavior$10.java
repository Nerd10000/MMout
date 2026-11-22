/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.RespawnAnchorBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ class null extends OptionalDispenseItemBehavior {
/*     */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 322 */     Direction direction = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 323 */     BlockPos pos = source.pos().relative(direction);
/* 324 */     ServerLevel serverLevel = source.level();
/* 325 */     BlockState blockState = serverLevel.getBlockState(pos);
/* 326 */     setSuccess(true);
/* 327 */     if (blockState.is(Blocks.RESPAWN_ANCHOR)) {
/* 328 */       if (((Integer)blockState.getValue((Property)RespawnAnchorBlock.CHARGE)).intValue() != 4) {
/* 329 */         RespawnAnchorBlock.charge(null, (Level)serverLevel, pos, blockState);
/* 330 */         dispensed.shrink(1);
/*     */       } else {
/* 332 */         setSuccess(false);
/*     */       } 
/* 335 */       return dispensed;
/*     */     } 
/* 337 */     return super.execute(source, dispensed);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$10.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */