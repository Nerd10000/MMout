/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CrafterSlot extends Slot {
/*    */   private final CrafterMenu menu;
/*    */   
/*    */   public CrafterSlot(Container container, int slot, int x, int y, CrafterMenu menu) {
/* 11 */     super(container, slot, x, y);
/* 12 */     this.menu = menu;
/*    */   }
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 17 */     return (!this.menu.isSlotDisabled(this.index) && super.mayPlace(itemStack));
/*    */   }
/*    */   
/*    */   public void setChanged() {
/* 22 */     super.setChanged();
/* 23 */     this.menu.slotsChanged(this.container);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\CrafterSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */