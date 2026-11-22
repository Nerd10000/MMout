/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ContainerListener;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null implements ContainerListener {
/*    */   public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {
/* 26 */     LecternScreen.this.bookChanged();
/*    */   }
/*    */   
/*    */   public void dataChanged(AbstractContainerMenu container, int id, int value) {
/* 31 */     if (id == 0)
/* 32 */       LecternScreen.this.pageChanged(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\LecternScreen$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */