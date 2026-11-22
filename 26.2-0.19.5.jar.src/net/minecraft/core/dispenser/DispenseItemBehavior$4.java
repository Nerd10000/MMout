/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.BucketPickup;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ class null extends DefaultDispenseItemBehavior {
/*     */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/*     */     Item targetType;
/* 172 */     ServerLevel serverLevel = source.level();
/* 174 */     BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 176 */     BlockState blockState = serverLevel.getBlockState(target);
/* 177 */     Block block = blockState.getBlock();
/* 181 */     if (block instanceof BucketPickup) {
/* 181 */       BucketPickup bucket = (BucketPickup)block;
/* 182 */       ItemStack pickup = bucket.pickupBlock(null, (LevelAccessor)serverLevel, target, blockState);
/* 183 */       if (pickup.isEmpty())
/* 184 */         return super.execute(source, dispensed); 
/* 186 */       serverLevel.gameEvent(null, (Holder)GameEvent.FLUID_PICKUP, target);
/* 187 */       targetType = pickup.getItem();
/*     */     } else {
/* 189 */       return super.execute(source, dispensed);
/*     */     } 
/* 192 */     return consumeWithRemainder(source, dispensed, new ItemStack((ItemLike)targetType));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */