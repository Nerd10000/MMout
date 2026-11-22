/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BeehiveBlock;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ class null extends OptionalDispenseItemBehavior {
/*     */   private ItemStack takeLiquid(BlockSource source, ItemStack dispensed, ItemStack filledItemStack) {
/* 293 */     source.level().gameEvent(null, (Holder)GameEvent.FLUID_PICKUP, source.pos());
/* 294 */     return consumeWithRemainder(source, dispensed, filledItemStack);
/*     */   }
/*     */   
/*     */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 299 */     setSuccess(false);
/* 300 */     ServerLevel level = source.level();
/* 302 */     BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 304 */     BlockState state = level.getBlockState(target);
/* 306 */     if (state.is(BlockTags.BEEHIVES, s -> (s.hasProperty((Property)BeehiveBlock.HONEY_LEVEL) && s.getBlock() instanceof BeehiveBlock)) && ((Integer)state.getValue((Property)BeehiveBlock.HONEY_LEVEL)).intValue() >= 5) {
/* 307 */       ((BeehiveBlock)state.getBlock()).releaseBeesAndResetHoneyLevel((Level)level, state, target, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
/* 308 */       setSuccess(true);
/* 309 */       return takeLiquid(source, dispensed, new ItemStack((ItemLike)Items.HONEY_BOTTLE));
/*     */     } 
/* 310 */     if (level.getFluidState(target).is(FluidTags.WATER)) {
/* 311 */       setSuccess(true);
/* 312 */       return takeLiquid(source, dispensed, PotionContents.createItemStack(Items.POTION, Potions.WATER));
/*     */     } 
/* 314 */     return super.execute(source, dispensed);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$9.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */