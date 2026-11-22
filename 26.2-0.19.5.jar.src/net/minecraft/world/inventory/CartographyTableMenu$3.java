/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null extends Slot {
/*    */   null(CartographyTableMenu this$0, Container container, int slot, int x, int y) {
/* 54 */     super(container, slot, x, y);
/*    */   }
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 57 */     return itemStack.has(DataComponents.MAP_ID);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\CartographyTableMenu$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */