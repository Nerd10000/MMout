/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.Hash;
/*    */ 
/*    */ class null implements Hash.Strategy<ItemStack> {
/*    */   public int hashCode(ItemStack item) {
/* 13 */     return ItemStack.hashItemAndComponents(item);
/*    */   }
/*    */   
/*    */   public boolean equals(ItemStack a, ItemStack b) {
/* 18 */     return (a == b || (a != null && b != null && a.isEmpty() == b.isEmpty() && ItemStack.isSameItemSameComponents(a, b)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ItemStackLinkedSet$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */