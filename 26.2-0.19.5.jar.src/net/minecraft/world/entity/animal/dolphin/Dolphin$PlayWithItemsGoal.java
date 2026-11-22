/*     */ package net.minecraft.world.entity.animal.dolphin;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class PlayWithItemsGoal extends Goal {
/*     */   private int cooldown;
/*     */   
/*     */   public PlayWithItemsGoal() {
/* 394 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 399 */     if (this.cooldown > Dolphin.this.tickCount)
/* 400 */       return false; 
/* 402 */     List<ItemEntity> items = Dolphin.this.level().getEntitiesOfClass(ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
/* 403 */     return (!items.isEmpty() || !Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty());
/*     */   }
/*     */   
/*     */   public void start() {
/* 408 */     List<ItemEntity> items = Dolphin.this.level().getEntitiesOfClass(ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
/* 409 */     if (!items.isEmpty()) {
/* 410 */       Dolphin.this.getNavigation().moveTo((Entity)items.get(0), 1.2000000476837158D);
/* 411 */       Dolphin.this.playSound(SoundEvents.DOLPHIN_PLAY, 1.0F, 1.0F);
/*     */     } 
/* 413 */     this.cooldown = 0;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 418 */     ItemStack itemStack = Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 419 */     if (!itemStack.isEmpty()) {
/* 420 */       drop(itemStack);
/* 421 */       Dolphin.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 422 */       this.cooldown = Dolphin.this.tickCount + Dolphin.access$000(Dolphin.this).nextInt(100);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 428 */     List<ItemEntity> items = Dolphin.this.level().getEntitiesOfClass(ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
/* 430 */     ItemStack itemStack = Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 431 */     if (!itemStack.isEmpty()) {
/* 432 */       drop(itemStack);
/* 433 */       Dolphin.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 434 */     } else if (!items.isEmpty()) {
/* 435 */       Dolphin.this.getNavigation().moveTo((Entity)items.get(0), 1.2000000476837158D);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drop(ItemStack itemStack) {
/* 440 */     if (itemStack.isEmpty())
/*     */       return; 
/* 444 */     double yHandPos = Dolphin.this.getEyeY() - 0.30000001192092896D;
/* 445 */     ItemEntity thrownItem = new ItemEntity(Dolphin.this.level(), Dolphin.this.getX(), yHandPos, Dolphin.this.getZ(), itemStack);
/* 446 */     thrownItem.setPickUpDelay(40);
/* 448 */     thrownItem.setThrower((Entity)Dolphin.this);
/* 450 */     float pow = 0.3F;
/* 451 */     float dir = Dolphin.access$100(Dolphin.this).nextFloat() * 6.2831855F;
/* 452 */     float pow2 = 0.02F * Dolphin.access$200(Dolphin.this).nextFloat();
/* 453 */     thrownItem.setDeltaMovement((0.3F * 
/* 454 */         -Mth.sin((Dolphin.this.getYRot() * 0.017453292F)) * Mth.cos((Dolphin.this.getXRot() * 0.017453292F)) + Mth.cos(dir) * pow2), (0.3F * 
/* 455 */         Mth.sin((Dolphin.this.getXRot() * 0.017453292F)) * 1.5F), (0.3F * 
/* 456 */         Mth.cos((Dolphin.this.getYRot() * 0.017453292F)) * Mth.cos((Dolphin.this.getXRot() * 0.017453292F)) + Mth.sin(dir) * pow2));
/* 459 */     Dolphin.this.level().addFreshEntity((Entity)thrownItem);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\dolphin\Dolphin$PlayWithItemsGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */