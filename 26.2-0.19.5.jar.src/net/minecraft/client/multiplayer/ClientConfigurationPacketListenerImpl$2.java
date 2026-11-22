/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ 
/*     */ class null extends ClientCommonPacketListenerImpl.CommonDialogAccess {
/*     */   null(ClientConfigurationPacketListenerImpl this$0) {
/* 212 */     super(this$0);
/*     */   }
/*     */   
/*     */   public void runCommand(String command, Screen activeScreen) {
/* 215 */     ClientConfigurationPacketListenerImpl.LOGGER.warn("Commands are not supported in configuration phase, trying to run '{}'", command);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientConfigurationPacketListenerImpl$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */