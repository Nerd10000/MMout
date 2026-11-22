/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.dialog.DialogConnectionAccess;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.ServerLinks;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ 
/*     */ public abstract class CommonDialogAccess implements DialogConnectionAccess {
/*     */   public void disconnect(Component message) {
/* 497 */     ClientCommonPacketListenerImpl.this.connection.disconnect(message);
/* 499 */     ClientCommonPacketListenerImpl.this.connection.handleDisconnection();
/*     */   }
/*     */   
/*     */   public void openDialog(Holder<Dialog> dialog, Screen activeScreen) {
/* 504 */     ClientCommonPacketListenerImpl.this.showDialog(dialog, this, activeScreen);
/*     */   }
/*     */   
/*     */   public void sendCustomAction(Identifier id, Optional<Tag> payload) {
/* 509 */     ClientCommonPacketListenerImpl.this.send((Packet<?>)new ServerboundCustomClickActionPacket(id, payload));
/*     */   }
/*     */   
/*     */   public ServerLinks serverLinks() {
/* 514 */     return ClientCommonPacketListenerImpl.this.serverLinks();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientCommonPacketListenerImpl$CommonDialogAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */