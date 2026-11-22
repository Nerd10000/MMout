/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null extends Slot {
/*    */   null(InventoryMenu this$0, Container container, int slot, int x, int y) {
/* 75 */     super(container, slot, x, y);
/*    */   }
/*    */   
/*    */   public void setByPlayer(ItemStack itemStack, ItemStack previous) {
/* 78 */     owner.onEquipItem(EquipmentSlot.OFFHAND, previous, itemStack);
/* 79 */     super.setByPlayer(itemStack, previous);
/*    */   }
/*    */   
/*    */   public Identifier getNoItemIcon() {
/* 84 */     return InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\InventoryMenu$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */