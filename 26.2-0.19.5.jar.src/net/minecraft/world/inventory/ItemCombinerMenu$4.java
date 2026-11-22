/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ 
/*    */ class null extends SimpleContainer {
/*    */   null(int size) {
/* 84 */     super(size);
/*    */   }
/*    */   
/*    */   public void setChanged() {
/* 87 */     super.setChanged();
/* 88 */     ItemCombinerMenu.this.slotsChanged((Container)this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\ItemCombinerMenu$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */