/*     */ package net.minecraft.world.entity.animal.fox;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class FoxSearchForItemsGoal extends Goal {
/*     */   public FoxSearchForItemsGoal() {
/* 796 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 801 */     if (!Fox.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
/* 802 */       return false; 
/* 805 */     if (Fox.this.getTarget() != null || Fox.this.getLastHurtByMob() != null)
/* 806 */       return false; 
/* 809 */     if (!Fox.this.canMove())
/* 810 */       return false; 
/* 813 */     if (Fox.this.getRandom().nextInt(reducedTickDelay(10)) != 0)
/* 814 */       return false; 
/* 816 */     List<ItemEntity> items = Fox.this.level().getEntitiesOfClass(ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Fox.ALLOWED_ITEMS);
/* 817 */     return (!items.isEmpty() && Fox.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty());
/*     */   }
/*     */   
/*     */   public void tick() {
/* 822 */     List<ItemEntity> items = Fox.this.level().getEntitiesOfClass(ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Fox.ALLOWED_ITEMS);
/* 823 */     ItemStack itemStack = Fox.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 825 */     if (itemStack.isEmpty() && !items.isEmpty())
/* 826 */       Fox.this.getNavigation().moveTo((Entity)items.get(0), 1.2000000476837158D); 
/*     */   }
/*     */   
/*     */   public void start() {
/* 832 */     List<ItemEntity> items = Fox.this.level().getEntitiesOfClass(ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Fox.ALLOWED_ITEMS);
/* 833 */     if (!items.isEmpty())
/* 834 */       Fox.this.getNavigation().moveTo((Entity)items.get(0), 1.2000000476837158D); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxSearchForItemsGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */