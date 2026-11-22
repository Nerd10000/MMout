/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null extends Slot {
/*    */   null(Container container, int slot, int x, int y) {
/* 63 */     super(container, slot, x, y);
/*    */   }
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 66 */     return false;
/*    */   }
/*    */   
/*    */   public boolean mayPickup(Player player) {
/* 71 */     return ItemCombinerMenu.this.mayPickup(player, hasItem());
/*    */   }
/*    */   
/*    */   public void onTake(Player player, ItemStack carried) {
/* 76 */     ItemCombinerMenu.this.onTake(player, carried);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\ItemCombinerMenu$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */