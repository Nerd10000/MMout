/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.BeaconMenu;
/*    */ import net.minecraft.world.inventory.ContainerListener;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null implements ContainerListener {
/*    */   public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {}
/*    */   
/*    */   public void dataChanged(AbstractContainerMenu container, int id, int value) {
/* 58 */     BeaconScreen.this.primary = menu.getPrimaryEffect();
/* 59 */     BeaconScreen.this.secondary = menu.getSecondaryEffect();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\BeaconScreen$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */