/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CarvedPumpkinBlock;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ class null extends OptionalDispenseItemBehavior {
/*     */   protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 269 */     ServerLevel serverLevel = source.level();
/* 270 */     BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 271 */     CarvedPumpkinBlock pumpkinBlock = (CarvedPumpkinBlock)Blocks.CARVED_PUMPKIN;
/* 273 */     if (serverLevel.isEmptyBlock(target) && pumpkinBlock.canSpawnGolem((LevelReader)serverLevel, target)) {
/* 274 */       if (!serverLevel.isClientSide()) {
/* 275 */         serverLevel.setBlock(target, pumpkinBlock.defaultBlockState(), 3);
/* 276 */         serverLevel.gameEvent(null, (Holder)GameEvent.BLOCK_PLACE, target);
/*     */       } 
/* 278 */       dispensed.shrink(1);
/* 279 */       setSuccess(true);
/*     */     } else {
/* 281 */       setSuccess(EquipmentDispenseItemBehavior.dispenseEquipment(source, dispensed));
/*     */     } 
/* 283 */     return dispensed;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$8.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */