/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*    */ 
/*    */ class null extends ArmorSlot {
/*    */   null(HorseInventoryMenu this$0, Container inventory, LivingEntity owner, EquipmentSlot slot, int slotIndex, int x, int y, Identifier emptyIcon) {
/* 20 */     super(inventory, owner, slot, slotIndex, x, y, emptyIcon);
/*    */   }
/*    */   
/*    */   public boolean isActive() {
/* 23 */     return (horse.canUseSlot(EquipmentSlot.SADDLE) && horse.is(EntityTypeTags.CAN_EQUIP_SADDLE));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\HorseInventoryMenu$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */