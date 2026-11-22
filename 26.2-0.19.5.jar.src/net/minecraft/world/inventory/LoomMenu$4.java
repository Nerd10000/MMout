/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null extends Slot {
/*    */   null(LoomMenu this$0, Container container, int slot, int x, int y) {
/* 80 */     super(container, slot, x, y);
/*    */   }
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 83 */     return LoomMenu.isDyeItem(itemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\LoomMenu$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */