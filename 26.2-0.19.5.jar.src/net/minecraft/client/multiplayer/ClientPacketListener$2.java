/*      */ package net.minecraft.client.multiplayer;
/*      */ 
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ 
/*      */ class null extends ClientCommonPacketListenerImpl.CommonDialogAccess {
/*      */   public void runCommand(String command, Screen activeScreen) {
/* 2814 */     ClientPacketListener.this.sendUnattendedCommand(command, activeScreen);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientPacketListener$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */