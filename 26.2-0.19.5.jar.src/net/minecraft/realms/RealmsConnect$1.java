/*     */ package net.minecraft.realms;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.screens.DisconnectedScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
/*     */ import net.minecraft.client.multiplayer.LevelLoadTracker;
/*     */ import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
/*     */ import net.minecraft.client.quickplay.QuickPlayLog;
/*     */ import net.minecraft.client.resources.server.ServerPackManager;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.login.ClientLoginPacketListener;
/*     */ import net.minecraft.network.protocol.login.ServerboundHelloPacket;
/*     */ import net.minecraft.server.network.EventLoopGroupHolder;
/*     */ 
/*     */ class null extends Thread {
/*     */   null(String name) {
/*  43 */     super(name);
/*     */   }
/*     */   
/*     */   public void run() {
/*  46 */     InetSocketAddress address = null;
/*     */     try {
/*  48 */       address = new InetSocketAddress(hostname, port);
/*  50 */       if (RealmsConnect.this.aborted)
/*     */         return; 
/*  54 */       RealmsConnect.this.connection = Connection.connectToServer(address, EventLoopGroupHolder.remote(minecraft.options.useNativeTransport()), minecraft.getDebugOverlay().getBandwidthLogger());
/*  56 */       if (RealmsConnect.this.aborted)
/*     */         return; 
/*  60 */       ClientHandshakePacketListenerImpl clientHandshakePacketListener = new ClientHandshakePacketListenerImpl(RealmsConnect.this.connection, minecraft, server.toServerData(hostname), RealmsConnect.this.onlineScreen, false, null, status -> {
/*     */           
/*  60 */           }new LevelLoadTracker(), null);
/*  61 */       if (server.isMinigameActive())
/*  62 */         clientHandshakePacketListener.setMinigameName(server.minigameName); 
/*  65 */       if (RealmsConnect.this.aborted)
/*     */         return; 
/*  69 */       RealmsConnect.this.connection.initiateServerboundPlayConnection(hostname, port, (ClientLoginPacketListener)clientHandshakePacketListener);
/*  71 */       if (RealmsConnect.this.aborted)
/*     */         return; 
/*  75 */       RealmsConnect.this.connection.send((Packet)new ServerboundHelloPacket(minecraft.getUser().getName(), minecraft.getUser().getProfileId()));
/*  76 */       minecraft.updateReportEnvironment(ReportEnvironment.realm(server));
/*  77 */       minecraft.quickPlayLog().setWorldData(QuickPlayLog.Type.REALMS, String.valueOf(server.id), Objects.<String>requireNonNullElse(server.name, "unknown"));
/*  78 */       minecraft.getDownloadedPackSource().configureForServerControl(RealmsConnect.this.connection, ServerPackManager.PackPromptStatus.ALLOWED);
/*  79 */     } catch (Exception e) {
/*  80 */       minecraft.getDownloadedPackSource().cleanupAfterDisconnect();
/*  82 */       if (RealmsConnect.this.aborted)
/*     */         return; 
/*  86 */       RealmsConnect.LOGGER.error("Couldn't connect to world", e);
/*  87 */       String message = e.toString();
/*  89 */       if (address != null) {
/*  90 */         String filter = String.valueOf(address) + ":" + String.valueOf(address);
/*  91 */         message = message.replaceAll(filter, "");
/*     */       } 
/*  97 */       DisconnectedScreen screen = new DisconnectedScreen(RealmsConnect.this.onlineScreen, (Component)Component.translatable("mco.connect.failed"), (Component)Component.translatable("disconnect.genericReason", new Object[] { message }), CommonComponents.GUI_BACK);
/* 100 */       minecraft.execute(() -> minecraft.gui.setScreen((Screen)screen));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\realms\RealmsConnect$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */