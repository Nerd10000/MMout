/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ enum null {
/*    */   public void convert(Mob from, Mob to, ConversionParams params) {
/* 20 */     Entity rootPassenger = from.getFirstPassenger();
/* 22 */     to.copyPosition(from);
/* 23 */     to.setDeltaMovement(from.getDeltaMovement());
/* 25 */     if (rootPassenger != null) {
/* 26 */       rootPassenger.stopRiding();
/* 27 */       rootPassenger.boardingCooldown = 0;
/* 31 */       for (Entity passenger : to.getPassengers()) {
/* 32 */         passenger.stopRiding();
/* 33 */         passenger.remove(Entity.RemovalReason.DISCARDED);
/*    */       } 
/* 36 */       rootPassenger.startRiding(to);
/*    */     } 
/* 39 */     Entity vehicle = from.getVehicle();
/* 41 */     if (vehicle != null) {
/* 42 */       from.stopRiding();
/* 43 */       to.startRiding(vehicle, false, false);
/*    */     } 
/* 46 */     if (params.keepEquipment())
/* 47 */       for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 48 */         ItemStack itemStack = from.getItemBySlot(slot);
/* 49 */         if (!itemStack.isEmpty()) {
/* 50 */           to.setItemSlot(slot, itemStack.copyAndClear());
/* 51 */           to.setDropChance(slot, from.getDropChances().byEquipment(slot));
/*    */         } 
/*    */       }  
/* 56 */     to.fallDistance = from.fallDistance;
/* 57 */     to.setSharedFlag(7, from.isFallFlying());
/* 59 */     to.lastHurtByPlayerMemoryTime = from.lastHurtByPlayerMemoryTime;
/* 60 */     to.hurtTime = from.hurtTime;
/* 62 */     to.yBodyRot = from.yBodyRot;
/* 64 */     to.setOnGround(from.onGround());
/* 65 */     Objects.requireNonNull(to);
/* 65 */     from.getSleepingPos().ifPresent(to::setSleepingPos);
/* 67 */     Entity leashHolder = from.getLeashHolder();
/* 68 */     if (leashHolder != null)
/* 69 */       to.setLeashedTo(leashHolder, true); 
/* 72 */     ConversionType.convertCommon(from, to, params);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ConversionType$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */