/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ 
/*    */ class null extends SimpleContainer {
/*    */   null(int size) {
/* 29 */     super(size);
/*    */   }
/*    */   
/*    */   public void setChanged() {
/* 32 */     CartographyTableMenu.this.slotsChanged((Container)this);
/* 33 */     super.setChanged();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\CartographyTableMenu$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */