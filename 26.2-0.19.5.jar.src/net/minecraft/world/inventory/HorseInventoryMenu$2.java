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
/* 30 */     super(inventory, owner, slot, slotIndex, x, y, emptyIcon);
/*    */   }
/*    */   
/*    */   public boolean isActive() {
/* 33 */     return (horse.canUseSlot(EquipmentSlot.BODY) && (horse.is(EntityTypeTags.CAN_WEAR_HORSE_ARMOR) || isLlama));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\HorseInventoryMenu$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */