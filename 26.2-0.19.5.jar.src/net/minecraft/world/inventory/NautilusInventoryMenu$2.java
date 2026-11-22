/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*    */ 
/*    */ class null extends ArmorSlot {
/*    */   null(NautilusInventoryMenu this$0, Container inventory, LivingEntity owner, EquipmentSlot slot, int slotIndex, int x, int y, Identifier emptyIcon) {
/* 25 */     super(inventory, owner, slot, slotIndex, x, y, emptyIcon);
/*    */   }
/*    */   
/*    */   public boolean isActive() {
/* 28 */     return nautilus.canUseSlot(EquipmentSlot.BODY);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\NautilusInventoryMenu$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */