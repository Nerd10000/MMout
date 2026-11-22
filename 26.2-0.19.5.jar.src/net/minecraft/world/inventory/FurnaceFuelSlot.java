/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class FurnaceFuelSlot extends Slot {
/*    */   private final AbstractFurnaceMenu menu;
/*    */   
/*    */   public FurnaceFuelSlot(AbstractFurnaceMenu menu, Container container, int slot, int x, int y) {
/* 11 */     super(container, slot, x, y);
/* 12 */     this.menu = menu;
/*    */   }
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 17 */     return (this.menu.isFuel(itemStack) || isBucket(itemStack));
/*    */   }
/*    */   
/*    */   public int getMaxStackSize(ItemStack itemStack) {
/* 22 */     return isBucket(itemStack) ? 1 : super.getMaxStackSize(itemStack);
/*    */   }
/*    */   
/*    */   public static boolean isBucket(ItemStack itemStack) {
/* 26 */     return itemStack.is(Items.BUCKET);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\FurnaceFuelSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */