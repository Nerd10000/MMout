/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.network.HashedStack;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null implements RemoteSlot {
/*    */   public void receive(HashedStack incoming) {}
/*    */   
/*    */   public void force(ItemStack outgoing) {}
/*    */   
/*    */   public boolean matches(ItemStack local) {
/* 29 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\RemoteSlot$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */