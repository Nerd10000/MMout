/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ContainerListener;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CreativeInventoryListener implements ContainerListener {
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   public CreativeInventoryListener(Minecraft minecraft) {
/* 12 */     this.minecraft = minecraft;
/*    */   }
/*    */   
/*    */   public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {
/* 17 */     this.minecraft.gameMode.handleCreativeModeItemAdd(itemStack, slotIndex);
/*    */   }
/*    */   
/*    */   public void dataChanged(AbstractContainerMenu container, int id, int value) {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\CreativeInventoryListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */