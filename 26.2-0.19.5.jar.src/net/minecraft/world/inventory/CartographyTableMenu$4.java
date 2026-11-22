/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ class null extends Slot {
/*    */   null(CartographyTableMenu this$0, Container container, int slot, int x, int y) {
/* 61 */     super(container, slot, x, y);
/*    */   }
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 64 */     return (itemStack.is(Items.PAPER) || itemStack.is(Items.MAP) || itemStack.is(Items.GLASS_PANE));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\CartographyTableMenu$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */