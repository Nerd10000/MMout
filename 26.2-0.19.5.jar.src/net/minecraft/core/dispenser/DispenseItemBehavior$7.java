/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.SkullBlock;
/*     */ import net.minecraft.world.level.block.WitherSkullBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ class null extends OptionalDispenseItemBehavior {
/*     */   protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 246 */     ServerLevel serverLevel = source.level();
/* 247 */     Direction direction = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 248 */     BlockPos target = source.pos().relative(direction);
/* 250 */     if (serverLevel.isEmptyBlock(target) && WitherSkullBlock.canSpawnMob((Level)serverLevel, target, dispensed)) {
/* 251 */       serverLevel.setBlock(target, (BlockState)Blocks.WITHER_SKELETON_SKULL.defaultBlockState().setValue((Property)SkullBlock.ROTATION, Integer.valueOf(RotationSegment.convertToSegment(direction))), 3);
/* 252 */       serverLevel.gameEvent(null, (Holder)GameEvent.BLOCK_PLACE, target);
/* 253 */       BlockEntity skull = serverLevel.getBlockEntity(target);
/* 254 */       if (skull instanceof SkullBlockEntity) {
/* 254 */         SkullBlockEntity skullBlockEntity = (SkullBlockEntity)skull;
/* 255 */         WitherSkullBlock.checkSpawn((Level)serverLevel, target, skullBlockEntity);
/*     */       } 
/* 257 */       dispensed.shrink(1);
/* 258 */       setSuccess(true);
/*     */     } else {
/* 260 */       setSuccess(EquipmentDispenseItemBehavior.dispenseEquipment(source, dispensed));
/*     */     } 
/* 262 */     return dispensed;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$7.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */