/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ShulkerBoxSlot extends Slot {
/*    */   public ShulkerBoxSlot(Container container, int slot, int x, int y) {
/*  8 */     super(container, slot, x, y);
/*    */   }
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 13 */     return itemStack.getItem().canFitInsideContainerItems();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\ShulkerBoxSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */