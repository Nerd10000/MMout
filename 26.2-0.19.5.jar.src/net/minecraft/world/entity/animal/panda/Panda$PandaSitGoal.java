/*      */ package net.minecraft.world.entity.animal.panda;
/*      */ 
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ 
/*      */ class PandaSitGoal extends Goal {
/*      */   private int cooldown;
/*      */   
/*      */   public PandaSitGoal() {
/* 1040 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1045 */     if (this.cooldown > Panda.this.tickCount || Panda.this.isBaby() || Panda.this.isInWater() || !Panda.this.canPerformAction() || Panda.this.getUnhappyCounter() > 0)
/* 1046 */       return false; 
/* 1050 */     if (!Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
/* 1051 */       return true; 
/* 1054 */     return !Panda.this.level().getEntitiesOfClass(ItemEntity.class, Panda.this.getBoundingBox().inflate(6.0D, 6.0D, 6.0D), Panda::canPickUpAndEat).isEmpty();
/*      */   }
/*      */   
/*      */   public boolean canContinueToUse() {
/* 1059 */     if (Panda.this.isInWater() || (!Panda.this.isLazy() && Panda.access$400(Panda.this).nextInt(reducedTickDelay(600)) == 1))
/* 1060 */       return false; 
/* 1063 */     if (Panda.access$500(Panda.this).nextInt(reducedTickDelay(2000)) == 1)
/* 1064 */       return false; 
/* 1067 */     return true;
/*      */   }
/*      */   
/*      */   public void tick() {
/* 1072 */     if (!Panda.this.isSitting() && !Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
/* 1073 */       Panda.this.tryToSit(); 
/*      */   }
/*      */   
/*      */   public void start() {
/* 1079 */     if (Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/* 1080 */       List<ItemEntity> items = Panda.this.level().getEntitiesOfClass(ItemEntity.class, Panda.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Panda::canPickUpAndEat);
/* 1081 */       if (!items.isEmpty())
/* 1082 */         Panda.this.getNavigation().moveTo((Entity)items.getFirst(), 1.2000000476837158D); 
/*      */     } else {
/* 1085 */       Panda.this.tryToSit();
/*      */     } 
/* 1088 */     this.cooldown = 0;
/*      */   }
/*      */   
/*      */   public void stop() {
/* 1093 */     ItemStack itemStack = Panda.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 1094 */     if (!itemStack.isEmpty()) {
/* 1095 */       Panda.this.spawnAtLocation(getServerLevel(Panda.this.level()), itemStack);
/* 1096 */       Panda.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 1097 */       int waitSeconds = Panda.this.isLazy() ? (Panda.access$600(Panda.this).nextInt(50) + 10) : (Panda.access$700(Panda.this).nextInt(150) + 10);
/* 1098 */       this.cooldown = Panda.this.tickCount + waitSeconds * 20;
/*      */     } 
/* 1101 */     Panda.this.sit(false);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda$PandaSitGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */