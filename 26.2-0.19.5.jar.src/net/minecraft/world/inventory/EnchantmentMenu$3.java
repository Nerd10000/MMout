/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ class null extends Slot {
/*    */   null(EnchantmentMenu this$0, Container container, int slot, int x, int y) {
/* 66 */     super(container, slot, x, y);
/*    */   }
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 69 */     return itemStack.is(Items.LAPIS_LAZULI);
/*    */   }
/*    */   
/*    */   public Identifier getNoItemIcon() {
/* 74 */     return EnchantmentMenu.EMPTY_SLOT_LAPIS_LAZULI;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\EnchantmentMenu$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */