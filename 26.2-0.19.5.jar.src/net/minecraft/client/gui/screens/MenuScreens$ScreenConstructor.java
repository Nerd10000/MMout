/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.screens.inventory.MenuAccess;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.MenuType;
/*    */ 
/*    */ interface ScreenConstructor<T extends net.minecraft.world.inventory.AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
/*    */   default void fromPacket(Component title, MenuType<T> type, Minecraft minecraft, int containerId) {
/* 57 */     U screen = create((T)type.create(containerId, minecraft.player.getInventory()), minecraft.player.getInventory(), title);
/* 59 */     minecraft.player.containerMenu = ((MenuAccess)screen).getMenu();
/* 60 */     minecraft.gui.setScreen((Screen)screen);
/*    */   }
/*    */   
/*    */   U create(T paramT, Inventory paramInventory, Component paramComponent);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\MenuScreens$ScreenConstructor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */