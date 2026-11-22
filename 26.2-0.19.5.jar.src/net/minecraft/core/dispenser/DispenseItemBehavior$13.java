/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ class null extends DefaultDispenseItemBehavior {
/* 388 */   private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*     */   
/*     */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 392 */     PotionContents potion = (PotionContents)dispensed.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/* 393 */     if (!potion.is(Potions.WATER))
/* 394 */       return this.defaultDispenseItemBehavior.dispense(source, dispensed); 
/* 397 */     ServerLevel level = source.level();
/* 398 */     BlockPos pos = source.pos();
/* 400 */     BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 401 */     if (level.getBlockState(target).is(BlockTags.CONVERTABLE_TO_MUD)) {
/* 402 */       if (!level.isClientSide()) {
/* 403 */         RandomSource random = level.getRandom();
/* 404 */         for (int i = 0; i < 5; i++)
/* 405 */           level.sendParticles((ParticleOptions)ParticleTypes.SPLASH, pos.getX() + random.nextDouble(), (pos.getY() + 1), pos.getZ() + random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D); 
/*     */       } 
/* 409 */       level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 410 */       level.gameEvent(null, (Holder)GameEvent.FLUID_PLACE, pos);
/* 412 */       level.setBlockAndUpdate(target, Blocks.MUD.defaultBlockState());
/* 414 */       return consumeWithRemainder(source, dispensed, new ItemStack((ItemLike)Items.GLASS_BOTTLE));
/*     */     } 
/* 417 */     return this.defaultDispenseItemBehavior.dispense(source, dispensed);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$13.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */