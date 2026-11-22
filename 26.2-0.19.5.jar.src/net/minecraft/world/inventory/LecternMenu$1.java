/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ 
/*    */ class null extends Slot {
/*    */   null(Container container, int slot, int x, int y) {
/* 31 */     super(container, slot, x, y);
/*    */   }
/*    */   
/*    */   public void setChanged() {
/* 34 */     super.setChanged();
/* 35 */     LecternMenu.this.slotsChanged(this.container);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\LecternMenu$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */