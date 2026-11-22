/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.item.BoneMealItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ class null extends OptionalDispenseItemBehavior {
/*     */   protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 201 */     setSuccess(true);
/* 202 */     ServerLevel serverLevel = source.level();
/* 204 */     BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 205 */     if (BoneMealItem.growCrop(dispensed, (Level)serverLevel, target) || BoneMealItem.growWaterPlant(dispensed, (Level)serverLevel, target, null)) {
/* 206 */       if (!serverLevel.isClientSide())
/* 207 */         serverLevel.levelEvent(1505, target, 15); 
/*     */     } else {
/* 210 */       setSuccess(false);
/*     */     } 
/* 213 */     return dispensed;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */