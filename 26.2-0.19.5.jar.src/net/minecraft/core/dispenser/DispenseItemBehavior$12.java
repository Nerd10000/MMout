/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ class null extends OptionalDispenseItemBehavior {
/*     */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 370 */     BlockPos pos = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 371 */     ServerLevel serverLevel = source.level();
/* 372 */     BlockState blockState = serverLevel.getBlockState(pos);
/* 374 */     Optional<BlockState> maybeWaxed = HoneycombItem.getWaxed(blockState);
/* 375 */     if (maybeWaxed.isPresent()) {
/* 376 */       serverLevel.setBlockAndUpdate(pos, maybeWaxed.get());
/* 377 */       serverLevel.levelEvent(3003, pos, 0);
/* 378 */       dispensed.shrink(1);
/* 379 */       setSuccess(true);
/* 381 */       return dispensed;
/*     */     } 
/* 383 */     return super.execute(source, dispensed);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$12.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */