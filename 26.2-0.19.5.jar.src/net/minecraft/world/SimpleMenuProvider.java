/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.MenuConstructor;
/*    */ 
/*    */ public final class SimpleMenuProvider implements MenuProvider {
/*    */   private final Component title;
/*    */   
/*    */   private final MenuConstructor menuConstructor;
/*    */   
/*    */   public SimpleMenuProvider(MenuConstructor menuConstructor, Component title) {
/* 14 */     this.menuConstructor = menuConstructor;
/* 15 */     this.title = title;
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 20 */     return this.title;
/*    */   }
/*    */   
/*    */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 25 */     return this.menuConstructor.createMenu(containerId, inventory, player);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\SimpleMenuProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */