/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null extends SimpleContainer {
/*    */   null(BeaconMenu this$0, int size) {
/* 30 */     super(size);
/*    */   }
/*    */   
/*    */   public boolean canPlaceItem(int slot, ItemStack itemStack) {
/* 33 */     return itemStack.is(ItemTags.BEACON_PAYMENT_ITEMS);
/*    */   }
/*    */   
/*    */   public int getMaxStackSize() {
/* 38 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\BeaconMenu$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */