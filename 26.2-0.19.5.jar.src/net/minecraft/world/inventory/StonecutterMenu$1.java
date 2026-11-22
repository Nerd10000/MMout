/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ 
/*    */ class null extends SimpleContainer {
/*    */   null(int size) {
/* 42 */     super(size);
/*    */   }
/*    */   
/*    */   public void setChanged() {
/* 45 */     super.setChanged();
/* 46 */     StonecutterMenu.this.slotsChanged((Container)this);
/* 47 */     StonecutterMenu.this.slotUpdateListener.run();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\StonecutterMenu$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */