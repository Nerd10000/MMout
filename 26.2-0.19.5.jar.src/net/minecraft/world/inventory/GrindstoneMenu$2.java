/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ 
/*    */ class null extends Slot {
/*    */   null(GrindstoneMenu this$0, Container container, int slot, int x, int y) {
/* 52 */     super(container, slot, x, y);
/*    */   }
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 55 */     return (itemStack.isDamageableItem() || EnchantmentHelper.hasAnyEnchantments(itemStack));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\GrindstoneMenu$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */