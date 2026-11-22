/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ 
/*    */ class null extends SimpleContainer {
/*    */   null(int size) {
/* 57 */     super(size);
/*    */   }
/*    */   
/*    */   public void setChanged() {
/* 60 */     super.setChanged();
/* 61 */     LoomMenu.this.slotUpdateListener.run();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\LoomMenu$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */